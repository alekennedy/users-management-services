package com.alekennedy.usersmanagement.etc;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import py.com.lib.util.log.Debug;
import py.com.lib.util.log.Errors;
import py.com.lib.util.log.LogToFile;
import py.com.lib.util.log.Messages;
import py.com.lib.util.utilities.ResourcesManager;
import py.com.lib.util.utilities.Utilities;
import py.com.lib.util.values.Variables;



/**
 *
 * @author alejandro
 */
public class Configuration {

    private Configuration() {
        
    }    
    
    
    static boolean initialized = false;
  
    public static void initialize()
    {
      if (!initialized)
      {
        initializeVariables();
        initialized = true;
      }
    }
    
    public static void restart()
    {
        Variables.clearAll();
        initializeVariables();
        initialized = true;
    }
    private static void initializeVariables()
    {
        LogToFile.setLog(true);
        Errors.setLog(true);
        Messages.setLog(true);
        Debug.setLog(false);
        Variables.setCurrentContext(SystemVariables.getVariableContext());
        Variables.set("UserManagement-SERVICES", String.class, "applicationName", 
        SystemVariables.getApplicationName());
      try
      {
        loadProperties();

        initializeLog();
        initializeProperties();
        setVersion();
        storeProperties();
      }
      catch (Exception e)
      {
        StringBuilder sb = new StringBuilder();
        sb.append("Error en la generacion del archivo properties\n");
        sb.append(e);
        Errors.append(sb.toString());
      }
    }
  
  private static void loadProperties()
  {
    File fileProperties = new File(getConfigurationDirectoryPath(), ValuesImpl.resources.CONFIG_PROPERTIES);
    if (fileProperties.exists()) {
      Variables.loadProperties(SystemVariables.getVariableContext(), fileProperties);
    }
  }
  
  private static void storeProperties()
  {
    File fileProperties = new File(getConfigurationDirectoryPath(), ValuesImpl.resources.CONFIG_PROPERTIES);
    
    Variables.storeToProperties(SystemVariables.getVariableContext(), fileProperties);
  }
  
  private static void initializeLog()
  {
    String defaultLogFilePath = getLogDirectoryPath() + SystemVariables.getApplicationName() + ".log";
    
    String logFilePath = initializeString("logFileDirectory", defaultLogFilePath);
    Integer maxLogSize = initializeInteger("maxLogFileSize", ValuesImpl.constants.DEFAULT_MAX_LOG_SIZE_BYTES);
    
    LogToFile.setFileName(logFilePath);
    LogToFile.setLimit(maxLogSize.intValue());
  }
  
  private static void initializeProperties()
  {
    initializeString(ValuesImpl.variables.DB_NAME, ValuesImpl.constants.DEFAULT_DB_NAME);
    initializeString(ValuesImpl.variables.DB_USER, ValuesImpl.constants.DEFAULT_DB_USER);
    initializeString(ValuesImpl.variables.DB_PASSWORD, ValuesImpl.constants.DEFAULT_DB_PASSWORD);
    initializeString(ValuesImpl.variables.IP_PGSQL, ValuesImpl.constants.DEFAULT_IP_DB_PGSQL);
    initializeString(ValuesImpl.variables.IP_REDIS, ValuesImpl.constants.DEFAULT_IP_DB_REDIS);
    initializeInteger(ValuesImpl.variables.DB_PORT, ValuesImpl.constants.DEFAULT_DB_PORT);    
    initializeInteger(ValuesImpl.variables.MAX_LOG_FILE_SIZE, ValuesImpl.constants.DEFAULT_MAX_LOG_SIZE_BYTES);
    
  }
  
  private static String getConfigurationDirectoryPath()
  {
    String applicationName = SystemVariables.getApplicationName();
    String fileSeparator = ResourcesManager.getFileSeparator();
    String homeDir = System.getenv("TELEMEDICINA_HOME");
    if(homeDir==null){
        homeDir = ResourcesManager.getHomeDirectory();
    }
    String configurationDirectoryPath = homeDir+fileSeparator+ "." + applicationName + fileSeparator + "etc" + fileSeparator;
    Logger.getLogger(Configuration.class.getName()).log(Level.INFO, "CONFIGURATION PATH: "+configurationDirectoryPath);
    File configurationDirectory = new File(configurationDirectoryPath);
    if (!configurationDirectory.exists())
    {
      if (!configurationDirectory.mkdirs()) {
        return "";
      }
    }
    else if (!configurationDirectory.isDirectory()) {
      return "";
    }
    String path;
    try
    {
      path = configurationDirectory.getCanonicalPath();
      if (!path.endsWith(fileSeparator)) {
        path = path + fileSeparator;
      }
    }
    catch (IOException e)
    {
      path = configurationDirectory.getAbsolutePath();
      if (!path.endsWith(fileSeparator)) {
        path = path + fileSeparator;
      }
    }
    return path;
  }
  
  private static String getLogDirectoryPath()
  {
    String applicationName = SystemVariables.getApplicationName();
    String fileSeparator = ResourcesManager.getFileSeparator();
    String homeDir = System.getenv("TELEMEDICINA_HOME");
    if(homeDir==null){
        homeDir = ResourcesManager.getHomeDirectory();
    }
    String configurationDirectoryPath = homeDir+fileSeparator + "." + applicationName + fileSeparator + "log" + fileSeparator;
    Logger.getLogger(Configuration.class.getName()).log(Level.INFO, "CONFIGURATION PATH: "+configurationDirectoryPath);
    File configurationDirectory = new File(configurationDirectoryPath);
    if (!configurationDirectory.exists())
    {
      if (!configurationDirectory.mkdirs()) {
        return "";
      }
    }
    else if (!configurationDirectory.isDirectory()) {
      return "";
    }
    String path;
    try
    {
      path = configurationDirectory.getCanonicalPath();
      if (!path.endsWith(fileSeparator)) {
        path = path + fileSeparator;
      }
    }
    catch (IOException e)
    {
      path = configurationDirectory.getAbsolutePath();
      if (!path.endsWith(fileSeparator)) {
        path = path + fileSeparator;
      }
    }
    return path;
  }
  
  public static String initializeString(String variableName, String defaultValue)
  {
    String value = null;
    if (Variables.contains(SystemVariables.getVariableContext(), String.class, variableName))
    {
      value = (String) Variables.get(SystemVariables.getVariableContext(), String.class, variableName);
    }
    else
    {
      value = defaultValue;
      Variables.set(SystemVariables.getVariableContext(), String.class, variableName, value);
    }
    return value;
  }
  
  public static Double initializeDouble(String variableName, Double defaultValue)
  {
    Double value = null;
    if (Variables.contains(SystemVariables.getVariableContext(), String.class, variableName))
    {
      try
      {
        value = Utilities.stringToDouble((String)Variables.get(SystemVariables.getVariableContext(), String.class, variableName));
      }
      catch (NumberFormatException e)
      {
        value = defaultValue;
      }
    }
    else
    {
      value = defaultValue;
      Variables.set(SystemVariables.getVariableContext(), String.class, variableName, value.toString());
    }
    return value;
  }
  
  public static Boolean initializeBoolean(String variableName, Boolean defaultValue)
  {
    Boolean value = null;
    if (Variables.contains(SystemVariables.getVariableContext(), String.class, variableName))
    {
      try
      {
        value = Utilities.stringToBoolean((String)Variables.get(SystemVariables.getVariableContext(), String.class, variableName));
      }
      catch (NumberFormatException e)
      {
        value = defaultValue;
      }
    }
    else
    {
      value = defaultValue;
      Variables.set(SystemVariables.getVariableContext(), String.class, variableName, value.toString());
    }
    return value;
  }
  
  public static Integer initializeInteger(String variableName, Integer defaultValue)
  {
    Integer value = null;
    if (Variables.contains(SystemVariables.getVariableContext(), String.class, variableName))
    {
      try
      {
        value = Utilities.stringToInteger((String)Variables.get(SystemVariables.getVariableContext(), String.class, variableName));
      }
      catch (NumberFormatException e)
      {
        value = defaultValue;
      }
    }
    else
    {
      value = defaultValue;
      Variables.set(SystemVariables.getVariableContext(), String.class, variableName, value.toString());
    }
    return value;
  }
  
  private static void setVersion()
  {
    Variables.set(SystemVariables.getVariableContext(), String.class, "APP_VERSION", "1.0");
  }
    
}
