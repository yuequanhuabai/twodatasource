package com.he.multi.service.impl.db;

import org.springframework.beans.BeansException;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.beans.factory.config.TypedStringValue;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.util.ClassUtils;
import org.springframework.util.PropertyPlaceholderHelper;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.*;

public class CacheDaoProxyConfiguration implements BeanDefinitionRegistryPostProcessor , ResourceLoaderAware , ApplicationContextAware {


    private ApplicationContext applicationContext;

    private static final String CONFIG_LOCATION_DELIMITERS = ",; \t\n";

    private static final String DEFAULT_RESOURCE_PATTERN = "**/*Dao.class";

    public static final String BASE_PACKAGE_PROPERTIES="dao.proxy.basePackage";

    private MetadataReaderFactory metadataReaderFactory;
    private ResourcePatternResolver resourcePatternResolver;

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {

    }

    private String getBasePackage(BeanDefinitionRegistry registry,String basePackage) {
        // 资源文件支持系统点位符
        PropertyPlaceholderHelper helper = new PropertyPlaceholderHelper("${", "}", ":", false);
        Properties systemProperties = System.getProperties();

        // 因为是注册前去取属性文件，得手动去拿spring配置的属性文件
        DefaultListableBeanFactory defaultListableBeanFactory = (DefaultListableBeanFactory) registry;
        String[] beanDefinitionNames = defaultListableBeanFactory.getBeanDefinitionNames();
        if(null != beanDefinitionNames && beanDefinitionNames.length > 0) {
            // 属性文件的bean一般都不会配名字和id
            PathMatchingResourcePatternResolver pathMatchingResourcePatternResolver = new PathMatchingResourcePatternResolver();
            for (String beanDefinitionName : beanDefinitionNames) {
                BeanDefinition beanDefinition = defaultListableBeanFactory.getBeanDefinition(beanDefinitionName);
                String beanClassName = beanDefinition.getBeanClassName();
                if(beanClassName==null){
                    continue;
                }
                try {
                    Class<?> beanClass = Class.forName(beanClassName, true, this.applicationContext.getClassLoader());
                    if(PropertyPlaceholderConfigurer.class.isAssignableFrom(beanClass)){
                        MutablePropertyValues propertyValues = beanDefinition.getPropertyValues();
                        PropertyValue locations = propertyValues.getPropertyValue("locations");
                        ArrayList<TypedStringValue> valueList = (ArrayList) locations.getValue();
                        StringBuilder sb = new StringBuilder();
                        for(TypedStringValue classPath : valueList){
                            String value = classPath.getValue();
                            String newValue = helper.replacePlaceholders(value, systemProperties);
                            Resource[] resources = pathMatchingResourcePatternResolver.getResources(newValue);

                            if(resources != null && resources.length > 0) {
                                for(Resource resource : resources){
                                    // 支持*通配符
                                    Properties properties = PropertiesLoaderUtils.loadProperties(resource);
                                    // 只要是BASE_PACKAGE_PROPERTIES结尾的属性，统统去扫描；
                                    Enumeration<?> enumeration = properties.propertyNames();
                                    while (enumeration.hasMoreElements()){
                                        String propertyKey = (String) enumeration.nextElement();
                                        if(StringUtils.hasText(propertyKey)&& propertyKey.endsWith(BASE_PACKAGE_PROPERTIES)){
                                            String propertyValue = properties.getProperty(propertyKey);
                                            sb.append(propertyValue).append(",");
                                        }
                                    }

                                }
                            }

                        }

                        if(sb.length() > 0){
                            sb.deleteCharAt(sb.length()-1);
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

    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
//         重寫BeanDefinitionRegistryPostprocessor的postProcessBeanDefinitionRegistry方法
//        BeanDefinitionBuilder definition = BeanDefinitionBuilder.genericBeanDefinition();
//        definition.getConstructorArgumentValues().addGenericArgumentValue("com.a.EsDao");
//        definition.setBeanClass(CacheDaoProxyFactory.class);
//        definition.setAutowireMode(1);


    }

    private Set<Class<?>> scannerPackages(String basePackage)  {
        Set<Class<?>> set = new LinkedHashSet<>();

        String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + resolveBasePackage(basePackage) + "/" + DEFAULT_RESOURCE_PATTERN;
        try {
            Resource[] resources = this.resourcePatternResolver.getResources(packageSearchPath);
            for(Resource resource : resources){
                if(resource.isReadable()){
                    MetadataReader metadataReader = metadataReaderFactory.getMetadataReader(resource);
                    String className = metadataReader.getClassMetadata().getClassName();
                    Class<?> clazz = null;
                    try {
                        // 使用spring容器的类加载器，目的是确保於应用中使用的类加载器相同，即可加载到应用的dao接口；
                        clazz= Class.forName(className,true,this.applicationContext.getClassLoader());
                        // 只加载扫描配置的dao下面，所有以Dao结尾的接口；
                        if(clazz.isInterface()){
                            set.add(clazz);
                        }

                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
//            throw new RuntimeException(e);
        }


        return set;
    }

    private String resolveBasePackage(String basePackage) {
        return ClassUtils.convertClassNameToResourcePath(basePackage);
    }


}
