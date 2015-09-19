package raijin.common.datatypes;

public class Constants {
  
  public static enum Priority { LOW, MID, HIGH }
  public static enum Command { 
    ADD, EDIT, DISPLAY, DELETE, DONE, EXIT, UNDO
  }

  public static final String FORMAT_DATE = "dd/M/yyyy";
  public static final String FORMAT_TIME = "HHmm";
  public static final String NAME_USER_DATA = "/data.json";      //Default name used to store data 
  public static final String NAME_USER_CONFIG = "/config.json";  //Default name used to store user config
  public static final String NAME_USER_FOLDER = "/data";         //Default folder name for storage
  public static final String NAME_BASE_CONFIG = "/base.cfg";     //Base config which stores location of storage

}
