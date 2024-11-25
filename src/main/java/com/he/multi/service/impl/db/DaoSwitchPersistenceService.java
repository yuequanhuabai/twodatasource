package com.he.multi.service.impl.db;

import java.io.File;
import java.util.Map;

public interface DaoSwitchPersistenceService {
    String DEFAULT="daoSwitchPersistenceFileImpl";

    boolean write(String var1 , String var2, Map<String,Object> var3);

    String read(String var1, File var2);
}
