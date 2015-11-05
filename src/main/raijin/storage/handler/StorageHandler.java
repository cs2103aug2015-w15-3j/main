//@@author A0112213E

package raijin.storage.handler;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URLDecoder;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.slf4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.google.gson.stream.JsonReader;

import raijin.common.utils.RaijinLogger;

/**
 * Utility static class to handle low level I/O operations
 * @author papa
 *
 */
public class StorageHandler {

  private static Logger logger = RaijinLogger.getLogger();
  private StorageHandler() {}   

  //===========================================================================
  //Regular File Operations
  //===========================================================================

  /*make paths os independent*/
  public static String sanitizePath(String path) {
    return path.replaceFirst("^/(.:/)", "$1");
  }

  /*Retrieves parent directory where the program is being executed by user*/
  public static String getJarPath() throws UnsupportedEncodingException {
    String path = StorageHandler.class.getProtectionDomain().
        getCodeSource().getLocation().getPath();
    String decodedPath = URLDecoder.decode(path, "UTF-8");
    String sanitizedPath = sanitizePath(decodedPath.substring(0, decodedPath.length()-1));    
    return Paths.get(sanitizedPath).getParent().toString();         
  }

  public static boolean createDirectory(String dirPath){
    File directory = new File(dirPath);
    return directory.mkdir();  //If directory does not exist, create one
  }
  
  public static boolean createFile(String filePath) throws IOException {
    File file = new File(filePath);
    boolean isCreated = false;
    isCreated = file.createNewFile();
    return isCreated;
  }

  public static String createTempFile(String fileName) {
    File tempFile = null;
    try {
      tempFile = File.createTempFile(fileName, ".tmp");
      /*delete temporary file after JVM exited*/
      tempFile.deleteOnExit();                                                
    } catch (IOException e) {
      logger.error("Temp file is not created");
    }
    return tempFile.getAbsolutePath();
  }
  /*Will replace file if it exists in target*/
  public static void copyFiles(Path src, Path target) throws IOException {
    /*Copy attribute such as last-modified-time from source*/
    CopyOption[] options = new CopyOption[] {
        StandardCopyOption.COPY_ATTRIBUTES,
        StandardCopyOption.REPLACE_EXISTING
    };
    Files.copy(src, target, options);
  }

  public static boolean isDirectory(String dirPath) {
    return new File(dirPath).isDirectory();
  }


  public static void writeToFile(String output, String filePath) throws IOException {
    try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
        new FileOutputStream(filePath)))) {
      bw.append(output);
    } 
  }

  public static boolean deleteFile(String path) {
    return new File(path).delete();
  }

  /**
   * Obtains storage path from base config file
   * @param absolutePath
   * @return path of storage directory
   * @throws IOException 
   */
  public static String getStorageDirectory(String absolutePath) throws IOException {
    try (BufferedReader br = new BufferedReader(new FileReader(absolutePath))) {
      return br.readLine();
    } 
  }

  //===========================================================================
  //JSON Functions
  //===========================================================================

  public static JsonReader getJsonReaderFromFile(String filePath) {
    JsonReader jsonReader = null;
    try {
      jsonReader = new JsonReader(new FileReader(filePath));
    } catch (IOException e1) {
      e1.printStackTrace();
    }
    return jsonReader;
  }

  /*Deserializes object from JSON*/
  public static <T> T readFromJson(JsonReader jsonReader, Type typeOfSrc) 
    throws JsonParseException, IOException {
    T deserializedObject = new Gson().fromJson(jsonReader, typeOfSrc);
    jsonReader.close();
    return deserializedObject;
  }


  /*Returns JSON String given an object*/
  public static <T> String convertToJson(T targetObject) {
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    return gson.toJson(targetObject);
  }
  

}
