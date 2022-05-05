/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alekennedy.usersmanagement.etc;

import py.com.lib.util.utilities.Utilities;
import py.com.lib.util.values.Variables;



/**
 *
 * @author ciac-alejandro
 */
public class SystemVariables {

  private SystemVariables() {
      
  }
    
  public static String getVariableContext()
  {
    return ValuesImpl.constants.APLICATION_CONTEXT;
  }
  
  public static String getApplicationName()
  {
    return ValuesImpl.constants.APLICATION_NAME;
  }
  
  public static String getDataBaseIp()
  {
    return getStringVariable(ValuesImpl.variables.IP_PGSQL);
  }
  
  public static String getDataBaseRedisIp()
  {
    return getStringVariable(ValuesImpl.variables.IP_REDIS);
  }
  
  public static String getDataBaseName()
  {
    return getStringVariable(ValuesImpl.variables.DB_NAME);
  }
  
  public static String getDataBaseUser()
  {
    return getStringVariable(ValuesImpl.variables.DB_USER);
  }
  
  public static String getDataBasePassword()
  {
    return getStringVariable(ValuesImpl.variables.DB_PASSWORD);
  }
  
  public static Integer getDataBasePort()
  {
    return getIntegerVariable(ValuesImpl.variables.DB_PORT);
  }
  
  public static Double getDoubleVariable(String variableName)
  {
    if (Variables.contains(getVariableContext(), String.class, variableName))
    {
      String stringValue = (String)Variables.get(getVariableContext(), String.class, variableName);
      
      return Utilities.stringToDouble(stringValue);
    }
    return null;
  }
  
  public static Boolean getBooleanVariable(String variableName)
  {
    if (Variables.contains(getVariableContext(), String.class, variableName))
    {
      String stringValue = (String)Variables.get(getVariableContext(), String.class, variableName);
      
      return Utilities.stringToBoolean(stringValue);
    }
    return false;
  }
  
  public static String getStringVariable(String variableName)
  {
    if (Variables.contains(getVariableContext(), String.class, variableName))
    {
      String stringValue = (String)Variables.get(getVariableContext(), String.class, variableName);
      
      return stringValue;
    }
    return null;
  }
  
  public static Integer getIntegerVariable(String variableName)
  {
    if (Variables.contains(getVariableContext(), String.class, variableName))
    {
      String stringValue = (String)Variables.get(getVariableContext(), String.class, variableName);
      
      return Utilities.stringToInteger(stringValue);
    }
    return null;
  }
  
  public static void setVariable(String variableName, Object value)
  {
    if (value == null) {
      return;
    }
    Variables.set(getVariableContext(), String.class, variableName, value.toString());
  }
    
}
