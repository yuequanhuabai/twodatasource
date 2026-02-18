package com.he.multi.service.impl.db;

import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.FactoryBean;

import java.lang.reflect.Proxy;

public class CacheDaoProxyFactory implements FactoryBean {

    private Class<T> daoInterface;

    public CacheDaoProxyFactory(Class<T> daoInterface) {
        this.daoInterface = daoInterface;
    }

    @Override
    public Object getObject() throws Exception {
        CacheDaoProxyHandler<Object> tCacheDaoProxyHandler = new CacheDaoProxyHandler<>();
        return Proxy.newProxyInstance(this.daoInterface.getClassLoader(),new Class[]{daoInterface}, tCacheDaoProxyHandler);
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
