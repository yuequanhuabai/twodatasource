package com.he.multi.service.impl.db;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.File;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CacheDaoProxyConfig implements InitializingBean    {

    private static final Logger logger= LoggerFactory.getLogger(CacheDaoProxyConfig.class);


          private static final Map<String,String> cacheDaoMap=  new ConcurrentHashMap<String,String>();

    private static final Map<String,String> cacheDataSourceMap=  new ConcurrentHashMap<String,String>();



    public static final String DAO_SWITCH="dao.switch";

    private static final String GEMFIRE_SUFFIX="RedisCloud";

    private static final String REDIS_SUFFIX="Redis";
    private static final String  ALL_SUFFIX="Redis,RedisCloud";

    @Value("${dao.switch:Redis}")
    private  String cacheSuffix;

    @Value("${dao.switch.file:}")
    private String daoSwitchFile;

    @Value("${datasource.switch:Ob}")
    private  String cacheDatasourceSuffix;

    @Value("${datasource.switch.file:}")
    private String datasourceSwitchFile;

    public static final String  OB_SUFFIX="Ob";
    public static final String  DB2_SUFFIX="Db2";
    public static final String  ALL_DATASOURCE_SUFFIX="Ob,Db2.syn";
    public static final String  SWITCH_TYPE_DAO="0";
    public static final String  SWITCH_TYPE_DATASOURCE="1";
    public static final String  EMPTY_JSON_STR="{}";


    public String getCacheSuffix() {
        return cacheSuffix;
    }

    public void setCacheSuffix(String cacheSuffix) {
        this.cacheSuffix = cacheSuffix;
    }

    public String getDaoSwitchFile() {
        return daoSwitchFile;
    }

    public void setDaoSwitchFile(String daoSwitchFile) {
        this.daoSwitchFile = daoSwitchFile;
    }

    public String[] getCacheSuffixs(String methodName){
        String methodNameKey=DAO_SWITCH+"."+methodName;
        if(cacheDaoMap.containsKey(methodNameKey)){
            throw new IllegalArgumentException("Cache Dao proxy suffix is not exists!  methodName is ["+methodNameKey+"]");
        }else{
            String methodDaoSwitch = cacheDaoMap.get(methodNameKey);
            if(!this.checkConfig(methodDaoSwitch)){
                throw new IllegalArgumentException("Cache Dao proxy suffix config err!");
            }else {
                return methodDaoSwitch.split(",");
            }
        }
    }

    private boolean checkConfig(String daoSwitch) {
        if(!StringUtils.hasText(daoSwitch)){
            return false;
        }

        String[] daoArr = daoSwitch.split(",");

        for(String dao:daoArr){
            if(!dao.contains(GEMFIRE_SUFFIX) && !dao.contains(REDIS_SUFFIX)){
                return false;
            }
        }
        return true;
    }


    public static void setDaoMethodDefilesByScanDaoCls(Class<?> cls,Boolean flag){
        String simpleName = cls.getSimpleName();
         simpleName = simpleName.substring(0, 1).toLowerCase() + simpleName.substring(1);
        Method[] methods = cls.getMethods();
        Method[] var4 =methods;

        int var5 = methods.length;
        for(int var6 = 0; var6 < var5; ++var6) {
            Method method = var4[var6];
            String methodName = method.getName();
             methodName = simpleName + "." + methodName;
             String methodNameKey;
             if(flag){
                 methodNameKey="datasource.switch."+methodName;
                 cacheDataSourceMap.put(methodNameKey,methodName);
             }else {
                 methodNameKey="dao.switch."+methodName;
                 cacheDaoMap.put(methodNameKey,"");
             }
        }


    }


    public Boolean updateDaoSwitch(Map<String,Object> daoSwitchMap,String switchFile){
        if(CollectionUtils.isEmpty(daoSwitchMap)){
            return false;
        }
        else {
            String globalMethodDatasourceSwitch;
            Iterator var4;
            Map.Entry entry;
            String methodNameKey;
            String methodDatasourceSwitch;
            if(!StringUtils.isEmpty(switchFile) && switchFile.equals(this.datasourceSwitchFile)){
                if(CollectionUtils.isEmpty(daoSwitchMap)){
                    return false;
                }

             globalMethodDatasourceSwitch=   this.checkOrSetMethodDatasourceSwitch(daoSwitchMap,"datasource.switch","");


            }
        }

        return false;
    }

    private String checkOrSetMethodDatasourceSwitch(Map<String, Object> datasourceSwitchMap, String methodNameKey, String defaultMethodDatasourceSwitch) {
        String methodDatasourceSwitch="";
        if(datasourceSwitchMap.containsKey(methodNameKey)){
            methodDatasourceSwitch=(String) datasourceSwitchMap.get(methodNameKey);
        }else {
            methodDatasourceSwitch=StringUtils.hasText(defaultMethodDatasourceSwitch)?defaultMethodDatasourceSwitch:this.cacheDatasourceSuffix;
        }

        if(this.checkDatasourceConfig(methodDatasourceSwitch)){
            datasourceSwitchMap.put(methodNameKey,methodDatasourceSwitch);
        }else {
            methodDatasourceSwitch = StringUtils.hasText(defaultMethodDatasourceSwitch) ? defaultMethodDatasourceSwitch : "Ob,Db2.syn";
            datasourceSwitchMap.put(methodNameKey,methodDatasourceSwitch);
        }
        return methodDatasourceSwitch;


    }

    private boolean checkDatasourceConfig(String datasourceSwitch) {
        if(StringUtils.hasText(datasourceSwitch)){
            return false;
        }else {
            String[] daoArr = datasourceSwitch.split(",");
            String[] var3= daoArr;
            int length = daoArr.length;
            for(int var4 = 0; var4 < length; ++var4) {
                String dao=var3[var4];
                if(!dao.contains("Ob") && !dao.contains("Db2")){
                    return false;
                }
            }
            return true;
        }

    }


    @Override
    public void afterPropertiesSet() throws Exception {
            this.handleDaoSwitchFile(this.daoSwitchFile);
            this.handleDaoSwitchFile(this.datasourceSwitchFile);
    }

    private void handleDaoSwitchFile(String switchFile) {
        String fileName ="daoSwitchFile";
        String switchJson="daoSwitchJson";
        String daoSwitch ="dao.switch";
        String suffix = this.cacheSuffix;
        String switchName = "daoSwitch";
        String switchType ="0";
        if(!StringUtils.isEmpty(switchFile) && switchFile.equals(this.datasourceSwitchFile)){
            fileName="datasourceSwitchFile";
            switchJson="datasourceSwitchJson";
            daoSwitch="datasource.switch";
            suffix=this.cacheDatasourceSuffix;
            switchName="datasourceSwitch";
            switchType="1";
        }

        File file = new File(switchFile);
//        this.getDaoSwitchPersistenceService();


    }
}
