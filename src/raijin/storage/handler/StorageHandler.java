package raijin.storage.handler;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.util.Vector;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import raijin.common.datatypes.Task;

public class StorageHandler {


  private StorageHandler() {}

  public static JsonReader getJsonReaderFromFile(String filePath) {
    JsonReader jsonReader = null;
    try {
      jsonReader = new JsonReader((new FileReader(filePath)));
    } catch (JsonIOException e) {
      e.printStackTrace();
    } catch (JsonSyntaxException e) {
      e.printStackTrace();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    return jsonReader;
  }

  /*Deserializes object from JSON*/
  public static <T> T readFromJson(JsonReader jsonReader, Type typeOfSrc) {
    T deserializedObject = new Gson().fromJson(jsonReader, typeOfSrc);
    return deserializedObject;
  }

  /*Writes JSON to file*/
  public static void writeJsonToFile(String jsonString, File file) {
    try {
      FileOutputStream os = new FileOutputStream(file); // Overwrite whole file
      BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os));
      bw.append(jsonString);
      bw.close();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /*Returns JSON String given an object*/
  private static <T> String convertToJson(T targetObject) {
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    return gson.toJson(targetObject);
  }

}
