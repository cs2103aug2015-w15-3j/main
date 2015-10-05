package raijin.storage.handler;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import raijin.common.datatypes.Constants;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.gson.reflect.TypeToken;

public class StorageHandlerTest {

  @Rule public TemporaryFolder tmpFolder = new TemporaryFolder();

  @Test
  public void testCreateDirectory() throws IOException {
    String path = tmpFolder.getRoot().getAbsolutePath();
    tmpFolder.newFolder("data");
    assertFalse(StorageHandler.createDirectory(path+"/data"));
  }


  @Rule public TemporaryFolder tmpFolder2 = new TemporaryFolder();
  @Test
  public void testCopyFiles() throws IOException {
    String path2 = tmpFolder2.getRoot().getAbsolutePath();
    File targetFile = tmpFolder.newFile("test.lol");
    StorageHandler.copyFiles(targetFile.toPath(), tmpFolder2.getRoot().toPath());
    assertFalse(new File(path2, "test.lol").exists());
  }
  
  @Test
  public void testIsDirectory() throws IOException {
    assertTrue(StorageHandler.isDirectory(tmpFolder.getRoot().getAbsolutePath()));
    File targetFile = tmpFolder.newFile("test.lol");
    assertFalse(StorageHandler.isDirectory(targetFile.getAbsolutePath()));
  }

  @Test
  public void testReadWriteJson() throws IOException {
    File testFile = tmpFolder.newFile("test.json");
    HashMap<String, String> config = new HashMap<String, String>();
    config.put("status", "running");
    config.put("undo", "Ctrl+Z");
    config.put("exit", "Alt+F4");
    StorageHandler.writeToFile(
        StorageHandler.convertToJson(config), testFile.getAbsolutePath());
    HashMap<String, String> config2 = StorageHandler.readFromJson(
        StorageHandler.getJsonReaderFromFile(testFile.getAbsolutePath()), 
        new TypeToken<HashMap<String, String>>(){}.getType());
    assertEquals(config, config2);
  }
  
  @Test
  public void testCreateFile() throws IOException {
    String expectedPath = tmpFolder.getRoot().getAbsolutePath() + Constants.NAME_BASE_CONFIG;
    StorageHandler.createFile(expectedPath);
    assertTrue(new File(expectedPath).exists());
  }

  @Test
  public void testGetStorageDirectory() throws IOException {
    File baseConfig = tmpFolder.newFile("base.cfg");
    String expectedStorageLocation = tmpFolder2.getRoot().getAbsolutePath();
    StorageHandler.writeToFile(expectedStorageLocation, baseConfig.getAbsolutePath());
    String storageLocation = StorageHandler.getStorageDirectory(baseConfig.getAbsolutePath());
    assertEquals(expectedStorageLocation, storageLocation);
  }

  @Test
  public void sanitizePath_WindowPath_ReturnInvariantPath() {
    String input = "/D:/LingJie Work/Workspace/Repo/main/target/classes/data";
    String expected = "D:/LingJie Work/Workspace/Repo/main/target/classes/data";
    
    String sanitized = StorageHandler.sanitizePath(input);
    assertEquals(expected, sanitized);
  }
  
}
