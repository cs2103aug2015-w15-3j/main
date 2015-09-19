package raijin.common.datatypes;

import java.lang.reflect.Type;
import java.util.HashMap;

import raijin.storage.api.TasksMap;

import com.google.gson.reflect.TypeToken;

public class Constants {
  
  public static enum Priority { LOW, MID, HIGH }
  public static enum Command { 
    ADD, EDIT, DISPLAY, DELETE, DONE, EXIT, UNDO
  }

  public static final String FORMAT_DATE = "dd/MM/yyyy";
  public static final String FORMAT_TIME = "HHmm";
  public static final String NAME_USER_DATA = "/data.json";      //Default name used to store data 
  public static final String NAME_USER_CONFIG = "/config.json";  //Default name used to store user config
  public static final String NAME_USER_FOLDER = "/data";         //Default folder name for storage
  public static final String NAME_BASE_CONFIG = "/base.cfg";     //Base config which stores location of storage

  public static final Type tasksType = new TypeToken<TasksMap>(){}.getType();
}
