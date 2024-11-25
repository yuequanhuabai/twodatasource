package com.he.multi.service.impl.db;

import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.FactoryBean;

public class CacheDaoProxyFactory implements FactoryBean {

    private Class<T> daoInterface;


    public CacheDaoProxyFactory(Class<T> daoInterface) {
        this.daoInterface = daoInterface;
    }



    @Override
    public Object getObject() throws Exception {
        return null;
    }

    @Override
    public Class<?> getObjectType() {
        return null;
    }

    @Override
    public boolean isSingleton() {
        return FactoryBean.super.isSingleton();
    }
}
