package raijin.storage.handler;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URLDecoder;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Vector;

import org.slf4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import raijin.common.datatypes.Constants;
import raijin.common.datatypes.Task;
import raijin.common.utils.RaijinLogger;

public class StorageHandler {

  private static Logger logger = RaijinLogger.getLogger();
  private StorageHandler() {}   //Prevent developer from instantiating the class

  //===========================================================================
  //Regular File Operations
  //===========================================================================

  /*Retrieves directory where the program is being executed by user*/
  public static String getJarPath() throws UnsupportedEncodingException {
    String path = StorageHandler.class.getProtectionDomain().
        getCodeSource().getLocation().getPath();
    String decodedPath = URLDecoder.decode(path, "UTF-8");
    return sanitizePath(decodedPath.substring(0, decodedPath.length()-1));    //Trim last backslash for standardisation
  }

  public static boolean createDirectory(String dirPath){
    File directory = new File(dirPath);
    return directory.mkdir();  //If directory does not exist, create one
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
      /*Mark file to be deleted when JVM exit*/
      tempFile.deleteOnExit();
    } catch (IOException e) {
      logger.error("Temp file is not created");
    }
    return tempFile.getAbsolutePath();
  }

  /*Writes content to file*/
  public static void writeToFile(String output, String filePath) throws IOException {
    try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
        new FileOutputStream(filePath)))) {
      bw.append(output);
    } 
  }

  /**
   * Cannot recover from such error and storageDirectory is too important 
   * @param absolutePath
   * @return path of user storage directory
   * @throws IOException 
   */
  public static String getStorageDirectory(String absolutePath) throws IOException {
    try (BufferedReader br = new BufferedReader(new FileReader(absolutePath))) {
      return br.readLine();
    } 
  }

  /*make path invariant to windows and linux*/
  public static String sanitizePath(String path) {
    return path.replaceFirst("^/(.:/)", "$1");
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
  public static <T> T readFromJson(JsonReader jsonReader, Type typeOfSrc) {
    T deserializedObject = new Gson().fromJson(jsonReader, typeOfSrc);
    try {
      jsonReader.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return deserializedObject;
  }


  /*Returns JSON String given an object*/
  public static <T> String convertToJson(T targetObject) {
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    return gson.toJson(targetObject);
  }

}
