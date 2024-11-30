package com.he.multi.service.impl.db;



import org.springframework.beans.factory.FactoryBean;

import java.lang.reflect.Proxy;

public class CacheDaoProxyFactory<T> implements FactoryBean<T> {

    private Class<T> daoInterface;

    public CacheDaoProxyFactory(Class<T> daoInterface) {
        this.daoInterface = daoInterface;
    }

    @Override
    public T getObject() throws Exception {
        CacheDaoProxyHandler<Object> tCacheDaoProxyHandler = new CacheDaoProxyHandler<>();
        return (T)Proxy.newProxyInstance(this.daoInterface.getClassLoader(),new Class[]{daoInterface}, tCacheDaoProxyHandler);
    }

    @Override
    public Class<T> getObjectType() {
        return this.daoInterface;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
