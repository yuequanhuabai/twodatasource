package com.he.multi.service.impl.db;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class CacheDaoProxyHandler<T> implements InvocationHandler, Serializable {

    private static final Logger logger = LoggerFactory.getLogger(CacheDaoProxyHandler.class);

    public CacheDaoProxyHandler() {
    }

    private final ExecutorService executorService = Executors.newFixedThreadPool(10);

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (Object.class.equals(method.getDeclaringClass())) {
            return method.invoke(this, args);
        }
        Object invokeResult = null;
        String simpleName = method.getDeclaringClass().getSimpleName();
        simpleName = simpleName.substring(0, 1).toLowerCase() + simpleName.substring(1);
        String methodName = method.getName();
        CacheDaoProxyConfig cacheDaoProxyConfig = SpringBeanUtils.getCtx().getBean(CacheDaoProxyConfig.class);

        String[] cacheSuffixs = cacheDaoProxyConfig.getCacheSuffixs(simpleName + "." + methodName);

        ArrayList<String> synSuffixList = new ArrayList<>();
        ArrayList<String> asynSuffixList = new ArrayList<>();

        String firstCacheSuffix = cacheSuffixs[0];
        addSynOrAsynSuffix(firstCacheSuffix, synSuffixList, asynSuffixList, true);

        // 第一个之后的Dao层开关识别配置文件的配置
        if (cacheSuffixs.length > 1) {
            for (int i = 1; i < cacheSuffixs.length; i++) {
                String cacheSuffix = cacheSuffixs[i];
                addSynOrAsynSuffix(cacheSuffix, synSuffixList, asynSuffixList, false);
            }
        }

        // 执行同步后缀
        for (int i = 1; i < synSuffixList.size(); i++) {
            String suffix = synSuffixList.get(i);
            String beanName = simpleName + suffix;
            if (i == 0) {
                // 第一个同步方法为主，接受返回值；
                invokeResult = invokeByBeanName(method, args, beanName);

            } else {
                Object invoke = invokeByBeanName(method, args, beanName);
            }
        }

        // 执行异步后缀

        for (String suffix : synSuffixList) {
            String beanName = simpleName + suffix;
            try {
                runInvokeByBeanName(method, args, beanName);
            } catch (Exception e) {
                logger.error("asyc err: " + beanName + "." + method.getName(), e);
            }
        }

        return invokeResult;
    }

    /**
     * 添加同步或异步的执行后缀
     * 第一个后缀永远同步执行
     * 非第一个后缀，默认异步执行；
     *
     * @param cacheSuffix
     * @param synSuffixList
     * @param asynSuffixList
     * @param isFirstMethod
     */
    private void addSynOrAsynSuffix(String cacheSuffix, List<String> synSuffixList, List<String> asynSuffixList, boolean isFirstMethod) {

        String[] split = cacheSuffix.split(COMMONCONSTANT.DOT);
        String suffix = split[0];
        String mode = (split.length > 1) ? split[1] : COMMONCONSTANT.ASYN;
        if (isFirstMethod) {
            // 第一个后缀永远同步执行
            mode = COMMONCONSTANT.SYN;
        }

        if (COMMONCONSTANT.SYN.equals(mode)) {
            synSuffixList.add(suffix);
        } else {
            asynSuffixList.add(suffix);
        }

    }

    private T getBean(String beanName) throws ClassNotFoundException {
        if (SpringBeanUtils.getCtx().containsBean(beanName)) {
            return (T) SpringBeanUtils.getCtx().getBean(beanName);
        }
        String errMsg = String.format("proxy bean Impl is not exist! bean name is: %s", (beanName.substring(0, 1).toUpperCase() + beanName));

        throw new ClassNotFoundException(errMsg);

    }

    private Object invokeByBeanName(Method method, Object[] args, String beanName) throws Throwable {
        Object invokeResult = null;
        T bean = getBean(beanName);
        if (bean != null) {
            try {
                invokeResult = method.invoke(bean, args);
            } catch (InvocationTargetException e) {
                throw e.getCause();
            }
        }

        return invokeResult;
    }
//    final Map<String, Object> contextMap,
    private void runInvokeByBeanName( Method method, Object[] args, String beanName) throws Throwable {

        Future<?> submit = executorService.submit(
                () -> {
                    try {
                        CacheDaoProxyHandler.this.invokeByBeanName(method, args, beanName);
                    } catch (Throwable e) {
                        throw new RuntimeException(e);
                    }
                }
        );


    }


}
