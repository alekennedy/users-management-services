package com.alekennedy.usersmanagement.etc;

import py.com.lib.util.values.Values;

/**
 *
 * @author ciac-alejandro
 */
public abstract interface ValuesImpl extends Values{
    public static interface variables
    {
        public static final String IP_PGSQL = "ipDB";
        public static final String IP_REDIS = "ipRedis";
        public static final String DB_NAME = "dbName";
        public static final String DB_PORT = "dbPort";
        public static final String DB_USER = "dbUser";
        public static final String DB_PASSWORD = "dbPassword";
        public static final String MAX_LOG_FILE_SIZE = "maxLogFileSize";
        public static final String LOG_FILE_DIRECTORY = "logFileDirectory";        
        public static final String APP_VERSION = "APP_VERSION";
        
    }

    public static interface resources
    {
        public static final String CONFIG_PROPERTIES = "usermanagement.properties";
    }

    public static interface constants
    {
        public static final String VERSION = "1.0";
        public static final String APLICATION_NAME = "UserManagement-Services";
        public static final String APLICATION_CONTEXT = "AMR-ANDE";
        public static final String DEFAULT_IP_DB_PGSQL = "localhost";
        public static final String DEFAULT_IP_DB_REDIS = "localhost";
        public static final String DEFAULT_DB_NAME = "amr";
        public static final String DEFAULT_DB_USER = "postgres";
        public static final String DEFAULT_DB_PASSWORD = "admin";
        public static final Integer DEFAULT_MAX_LOG_SIZE_BYTES = Integer.valueOf(1048576);
        public static final Integer DEFAULT_DB_PORT = Integer.valueOf(5432);
    }
    
}
