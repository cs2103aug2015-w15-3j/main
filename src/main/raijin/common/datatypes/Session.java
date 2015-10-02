package raijin.common.datatypes;

import java.io.UnsupportedEncodingException;

import raijin.storage.handler.StorageHandler;

public class Session {

  private static Session session = new Session();

  public String programDirectory;      
  public String storageDirectory; 
  public String baseConfigPath;
  public String userConfigPath;
  public String dataPath;
  
  private Session() {} 
  
  public static Session getSession() {
    return session;
  }
  
  public void setupPath() throws UnsupportedEncodingException {
    programDirectory = StorageHandler.getJarPath();
    storageDirectory = programDirectory + Constants.NAME_USER_FOLDER;
  }

}
