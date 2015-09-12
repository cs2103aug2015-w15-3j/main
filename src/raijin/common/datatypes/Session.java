package raijin.common.datatypes;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;

import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import raijin.storage.handler.StorageHandler;

//@TODO add ability to change storage location
public class Session {

  private static Session session = null;
  private static Path dataPath;         //Location to store user's information
  
  public HashMap<String, String> config;
  

  private Session(Path dataPath) throws IOException {
    this.dataPath = dataPath;
    StorageHandler.setupDataFolder(dataPath);
    setupUserConfig();
  }
  
  //@TODO abstract out jsonreader just retrieve object will do
  private void setupUserConfig() {
    String userPath = dataPath.toString()+
        Constants.NAME_USER_FOLDER+Constants.NAME_USER_CONFIG;

     config = StorageHandler.readFromJson(StorageHandler.getJsonReaderFromFile(userPath),
         new TypeToken<HashMap<String, String>>(){}.getType());
  }

  public static Session createSession(Path dataPath) throws IOException {
    if (session == null) {
      session = new Session(dataPath);
    }
    return session;
  }

}
