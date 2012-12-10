package com.d2fn.jester.bdb;

import com.d2fn.jester.config.BdbConfiguration;
import com.sleepycat.bind.EntryBinding;
import com.sleepycat.bind.serial.SerialBinding;
import com.sleepycat.bind.serial.StoredClassCatalog;
import com.sleepycat.collections.StoredMap;
import com.sleepycat.je.Database;
import com.sleepycat.je.DatabaseConfig;
import com.sleepycat.je.Environment;
import com.sleepycat.je.EnvironmentConfig;
import com.yammer.dropwizard.lifecycle.Managed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * BdbEnvironment
 */
public class BdbEnvironment implements Managed {
    private static final Logger log = LoggerFactory.getLogger(BdbEnvironment.class);

    private BdbConfiguration config;
    private Environment environment;

    private static final String classCatalogName = "jester_class_catalog";
    private StoredClassCatalog classCatalog;

    private DatabaseConfig dbConfig;

    private final Map<String, Database> databases = new HashMap<String, Database>();
    private final Map<String, StoredMap> maps = new HashMap<String, StoredMap>();

    public BdbEnvironment(BdbConfiguration config) {
        this.config = config;
    }

    @Override
    public synchronized void start() throws Exception {

        // establish the database itself
        log.info("starting BDB environment in {}", config.getPath());
        EnvironmentConfig envConfig = new EnvironmentConfig();
        envConfig.setTransactional(true);
        envConfig.setAllowCreate(true);
        environment = new Environment(new File(config.getPath()), envConfig);

        // set up the class class catalog (schema)
        dbConfig = new DatabaseConfig();
        dbConfig.setTransactional(true);
        dbConfig.setAllowCreate(true);
        Database catalogDb = environment.openDatabase(null, classCatalogName, dbConfig);
        classCatalog = new StoredClassCatalog(catalogDb);

        databases.clear();
        maps.clear();
    }

    @Override
    public synchronized void stop() throws Exception {
        
        // close all open databases
        synchronized(databases) {
            for(Database db : databases.values()) {
                db.close();
            }
        }
        
        classCatalog.close();
        environment.close();
    }
    
    public synchronized Environment getEnvironment() {
        return environment;
    }

    public synchronized StoredClassCatalog getClassCatalog() {
        return classCatalog;
    }
    
    public synchronized StoredMap open(String name, Class keyClass, Class valueClass) {

        if(maps.containsKey(name)) {
            return maps.get(name);
        }

        Database db = openDatabase(name);
        EntryBinding keyBinding = new SerialBinding(classCatalog, keyClass);
        EntryBinding valueBinding = new SerialBinding(classCatalog, valueClass);

        StoredMap map = new StoredMap(db, keyBinding, valueBinding, true); 
        maps.put(name, new StoredMap(db, keyBinding, valueBinding, true));
        return map;
    }
    
    public synchronized Database openDatabase(String name) {
        try {
            if(databases.containsKey(name)) {
                return databases.get(name);
            }
            Database db = environment.openDatabase(null, name, dbConfig);
            databases.put(name, db);
            return db;

        }
        catch(Exception e) {
            log.error(String.format("error opening database %s", name), e);
        }
        return null;
    }
    
    
    
}
