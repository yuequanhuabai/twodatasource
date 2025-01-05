package com.he.multi.service.impl.db;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.factory.config.*;
import org.springframework.beans.factory.support.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.util.ClassUtils;
import org.springframework.util.PropertyPlaceholderHelper;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.*;

public class CacheDaoProxyConfiguration implements BeanDefinitionRegistryPostProcessor, ResourceLoaderAware, ApplicationContextAware {

    private static final Logger logger = LoggerFactory.getLogger(CacheDaoProxyConfiguration.class);

    private ApplicationContext applicationContext;

    private static final String CONFIG_LOCATION_DELIMITERS = ",; \t\n";

    private static final String DEFAULT_RESOURCE_PATTERN = "**/*Dao.class";

    public static final String BASE_PACKAGE_PROPERTIES = "dao.proxy.basePackage";

    private MetadataReaderFactory metadataReaderFactory;
    private ResourcePatternResolver resourcePatternResolver;


    private Set<Class<?>> scannerPackages(String basePackage, boolean isDb) {
        Set<Class<?>> set = new LinkedHashSet<>();

        String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + resolveBasePackage(basePackage) + "/" + DEFAULT_RESOURCE_PATTERN;
        try {
            Resource[] resources = this.resourcePatternResolver.getResources(packageSearchPath);
            Resource[] var6 = resources;
            int var7 = resources.length;
            for (int var8 = 0; var8 < var7; ++var8) {
                Resource resource = var6[var8];
                if (resources[var8].isReadable()) {
                    MetadataReader metadataReader = this.metadataReaderFactory.getMetadataReader(resource);
                    String className = metadataReader.getClassMetadata().getClassName();
                    String proxyName = "Dao";
                    if (isDb) {
                        proxyName = "DataSource";
                    }
                    logger.info("Cache " + proxyName + "Proxy className:{}", className);
                    try {
                        Class<?> clazz = Class.forName(className, true, this.applicationContext.getClassLoader());
                        if (clazz.isInterface()) {
                            logger.info("Cache " + proxyName + "Proxy Scan class:{}", clazz.getName());
                            set.add(clazz);
                        }
                    } catch (ClassNotFoundException e) {
//                        throw new RuntimeException(e);
                        logger.error("Cache " + proxyName + "Proxy exception", e);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return set;
    }


    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
            String basePackage=   this.getBasePackage(registry,"dao.proxy.basePackage");
        String baseDbPackage=   this.getBasePackage(registry,"datasource.proxy.basePackage");
        this.register(registry,basePackage,false);

        this.register(registry,baseDbPackage,true);

    }

    private void register(BeanDefinitionRegistry registry, String packageLocation, boolean isDb) {
        if(null !=packageLocation){
            String[] basePackages = StringUtils.tokenizeToStringArray(packageLocation, ",; \t\n");
            String[] var5 =basePackages;
            int var6 = basePackages.length;

            for(int var7 =0; var7<var6;++var7){
                String basePackage = var5[var7];
                Set<Class<?>> beanClasses = this.scannerPackages(basePackage, isDb);
                logger.info("Cache Dao or DataSource proxy beanClasses :{}",beanClasses);
                GenericBeanDefinition definition;
                String beanName;
                for(Iterator var10=beanClasses.iterator();var10.hasNext();registry.registerBeanDefinition(beanName,definition)){
                    Class cls = (Class) var10.next();
                    CacheDaoProxyConfig.setDaoMethodDefilesByScanDaoCls(cls,isDb);
                    BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(cls);
                  definition=  (GenericBeanDefinition)beanDefinitionBuilder.getRawBeanDefinition();
                  definition.getConstructorArgumentValues().addGenericArgumentValue(cls);
                  definition.setBeanClass(CacheDaoProxyFactory.class);
                  definition.setAutowireMode(1);
                    String simpleName = cls.getSimpleName();
                     beanName = simpleName.substring(0, 1).toLowerCase() + simpleName.substring(1);
                     if(isDb){
                         logger.info("Cache DataSource proxy class beanName : {}",beanName);
                     }else {
                         logger.info("Cache Dao proxy class beanName : {}",beanName);
                     }

                }
            }
        }
    }

    private String getBasePackage(BeanDefinitionRegistry registry, String basePackage) {
        // 资源文件支持系统点位符
        PropertyPlaceholderHelper helper = new PropertyPlaceholderHelper("${", "}", ":", false);
        Properties systemProperties = System.getProperties();
        systemProperties.putAll(System.getenv());


        // 因为是注册前去取属性文件，得手动去拿spring配置的属性文件
        DefaultListableBeanFactory defaultListableBeanFactory = (DefaultListableBeanFactory) registry;
        String[] beanDefinitionNames = defaultListableBeanFactory.getBeanDefinitionNames();
        if (null != beanDefinitionNames && beanDefinitionNames.length > 0) {
            // 属性文件的bean一般都不会配名字和id
            PathMatchingResourcePatternResolver pathMatchingResourcePatternResolver = new PathMatchingResourcePatternResolver();

            for (String beanDefinitionName : beanDefinitionNames) {
                BeanDefinition beanDefinition = defaultListableBeanFactory.getBeanDefinition(beanDefinitionName);
                String beanClassName = beanDefinition.getBeanClassName();
                if (beanClassName == null) {
                    continue;
                }
                try {
                    Class<?> beanClass = Class.forName(beanClassName, true, this.applicationContext.getClassLoader());
                    if (PropertyPlaceholderConfigurer.class.isAssignableFrom(beanClass)) {
                        MutablePropertyValues propertyValues = beanDefinition.getPropertyValues();
                        PropertyValue locations = propertyValues.getPropertyValue("locations");
                        ArrayList<TypedStringValue> valueList = (ArrayList) locations.getValue();
                        StringBuilder sb = new StringBuilder();
                        for (TypedStringValue classPath : valueList) {
                            String value = classPath.getValue();
                            String newValue = helper.replacePlaceholders(value, systemProperties);
                            Resource[] resources = pathMatchingResourcePatternResolver.getResources(newValue);

                            if (resources != null && resources.length > 0) {
                                for (Resource resource : resources) {
                                    // 支持*通配符
                                    Properties properties = PropertiesLoaderUtils.loadProperties(resource);
                                    // 只要是BASE_PACKAGE_PROPERTIES结尾的属性，统统去扫描；
                                    Enumeration<?> enumeration = properties.propertyNames();
                                    while (enumeration.hasMoreElements()) {
                                        String propertyKey = (String) enumeration.nextElement();
                                        if (StringUtils.hasText(propertyKey) && propertyKey.endsWith(BASE_PACKAGE_PROPERTIES)) {
                                            String propertyValue = properties.getProperty(propertyKey);
                                            sb.append(propertyValue).append(",");
                                        }
                                    }

                                }
                            }

                        }

                        if (sb.length() > 0) {
                            sb.deleteCharAt(sb.length() - 1);
                        }
                        String finalValue = sb.toString();
                        return finalValue;
                    }


                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return "";
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
              logger.info("Cache Dao proxy applicationContext.getClassLoader : {}",applicationContext.getClassLoader());
              this.applicationContext=applicationContext;
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourcePatternResolver =ResourcePatternUtils.getResourcePatternResolver(resourceLoader);
        this.metadataReaderFactory  = new CachingMetadataReaderFactory(resourceLoader);

//         重寫BeanDefinitionRegistryPostprocessor的postProcessBeanDefinitionRegistry方法
//        BeanDefinitionBuilder definition = BeanDefinitionBuilder.genericBeanDefinition();
//        definition.getConstructorArgumentValues().addGenericArgumentValue("com.a.EsDao");
//        definition.setBeanClass(CacheDaoProxyFactory.class);
//        definition.setAutowireMode(1);


    }


    private String resolveBasePackage(String basePackage) {
        return ClassUtils.convertClassNameToResourcePath(basePackage);
    }




    private void register1(BeanDefinitionRegistry registry, String packageLocation, boolean isDb) {
        if (packageLocation != null) {
            // 分割包路径字符串
            String[] basePackages = StringUtils.tokenizeToStringArray(packageLocation, ",; \t\n");

            // 遍历每个包路径
            for (String basePackage : basePackages) {
                // 扫描包中的类
                Set<Class<?>> beanClasses = this.scannerPackages(basePackage, isDb);
                logger.info("Cache Dao or DataSource proxy beanClasses : {}", beanClasses);

                // 遍历扫描到的类并注册Bean定义
                for (Class<?> beanClass : beanClasses) {
                    // 配置与当前类相关的DAO方法定义文件
                    CacheDaoProxyConfig.setDaoMethodDefilesByScanDaoCls(beanClass, isDb);

                    // 创建Bean定义构建器
                    BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(beanClass);
                    GenericBeanDefinition definition = (GenericBeanDefinition) beanDefinitionBuilder.getRawBeanDefinition();

                    // 设置构造参数
                    definition.getConstructorArgumentValues().addGenericArgumentValue(beanClass);

                    // 设置Bean类为代理工厂类
                    definition.setBeanClass(CacheDaoProxyFactory.class);

                    // 设置自动装配模式为按类型装配
                    definition.setAutowireMode(AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE);

                    // 生成标准的Bean名称（首字母小写）
                    String simpleName = beanClass.getSimpleName();
                    String beanName = Character.toLowerCase(simpleName.charAt(0)) + simpleName.substring(1);

                    // 记录注册信息到日志
                    if (isDb) {
                        logger.info("Cache DataSource proxy class beanName : {}", beanName);
                    } else {
                        logger.info("Cache Dao proxy class beanName : {}", beanName);
                    }

                    // 注册Bean定义到Spring容器
                    registry.registerBeanDefinition(beanName, definition);
                }
            }
        }
    }






    //            String[] var8 = beanDefinitionNames;
//            int var9 = beanDefinitionNames.length;
//
//            for (int var10 = 0; var10 < var9; ++var10) {
//                String beanName = var8[var10];
//                BeanDefinition beanDefinition = defaultListableBeanFactory.getBeanDefinition(beanName);
//                String beanClassName = beanDefinition.getBeanClassName();
//                if (beanClassName != null && !"".equals(beanClassName)) {
//                    try {
//                        Class<?> beanClass = Class.forName(beanClassName, true, this.applicationContext.getClassLoader());
//                        if (PropertyPlaceholderAutoConfiguration.class.isAssignableFrom(beanClass)) {
//                            MutablePropertyValues propertyValues = beanDefinition.getPropertyValues();
//                            PropertyValue locations = propertyValues.getPropertyValue("locations");
//                            ArrayList<TypedStringValue> valueList = (ArrayList) locations.getValue();
//                            StringBuilder sb = new StringBuilder();
//                            Iterator<TypedStringValue> var19 = valueList.iterator();
//
//                            while (true) {
//                                Resource[] resources;
//                                do {
//                                    if (!var19.hasNext()) {
//                                        if (sb.length() > 0) {
//                                            sb.deleteCharAt(sb.length() - 1);
//                                        }
//                                        String finalValue = sb.toString();
//                                        logger.info("get finalValue properties :[{}]", "dao.proxy.basePackage" + finalValue);
//                                        return finalValue;
//                                    }
//                                    TypedStringValue classPath = var19.next();
//                                    String value = classPath.getValue();
//                                    String newValue = helper.replacePlaceholders(value, systemProperties);
//                                    resources = pathMatchingResourcePatternResolver.getResources(newValue);
//                                } while (resources.length <= 0);
//
//                                Resource[] var24 = resources;
//                                int var25 = resources.length;
//                                for (int var26 = 0; var26 < var25; ++var26) {
//                                    Resource resource = var24[var26];
//                                    Properties properties = PropertiesLoaderUtils.loadProperties(resource);
//                                    Enumeration<?> enumeration = properties.propertyNames();
//                                    while (enumeration.hasMoreElements()) {
//                                        String propertyKey = (String) enumeration.nextElement();
//                                        if (StringUtils.hasText(propertyKey) && propertyKey.endsWith(basePackage)) {
//                                            String propertyValue = properties.getProperty(propertyKey);
//                                            if (StringUtils.hasText(propertyValue)) {
//                                                logger.info("get properties: [{}] from [{}]", properties + "=" + propertyValue, resource);
//                                                sb.append(propertyValue).append(",");
//                                            }
//                                        }
//                                    }
//                                }
//                            }
//
//
//                        }
//
//                    } catch (ClassNotFoundException e) {
//                        throw new RuntimeException(e);
//                    } catch (IOException e) {
//                        throw new RuntimeException(e);
//                    }
//                }
//            }


}
