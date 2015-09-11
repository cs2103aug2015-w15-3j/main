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

  private Vector<Task> memory;
  private static final String fileName = "/data.json"; // Default file name
  private File jsonFile;

  public StorageHandler(String filePath) throws IOException {
    jsonFile = getJSONFile(filePath);
    memory = getJSONFromFile(filePath);
  }

  private File getJSONFile(String filePath) throws IOException { // Retrieves json file
    File file = new File(filePath + fileName);
    file.createNewFile(); // Create data file if not exists
    return file;
  }

  public Vector<Task> getJSONFromFile(String filePath) {
    JsonReader jsonReader = null;
    try {
      jsonReader = new JsonReader((new FileReader(filePath + fileName)));
    } catch (JsonIOException e) {
      e.printStackTrace();
    } catch (JsonSyntaxException e) {
      e.printStackTrace();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    return readJSONToVector(jsonReader);
  }

  public Vector<Task> readJSONToVector(JsonReader jsonReader) {
    Type type = new TypeToken<Vector<Task>>(){}.getType();
    Vector<Task> memoryFromFile = new Gson().fromJson(jsonReader, type);
    return memoryFromFile;
  }

  public void writeTasksToFile(Vector<Task> memory) {
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    String jsonString = memoryToJSON(gson, memory);

    try {
      FileOutputStream os = new FileOutputStream(jsonFile); // Overwrite whole file
      BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os));
      bw.append(jsonString);
      bw.close();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private String memoryToJSON(Gson gson, Vector<Task> memory) {
    return gson.toJson(memory);
  }

}
