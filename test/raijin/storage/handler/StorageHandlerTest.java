package raijin.storage.handler;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

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

}
