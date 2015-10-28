package raijin.helper;

import java.io.File;

import raijin.storage.api.Session;
import raijin.storage.api.TasksManager;

public class TestProperties {


  public final String testCaseName;                //Name of test script used
  public File actualJSON;
  public File expectedJSON;
  public String testScript;
  public static final String TEST_DATA = "/src/test/resources/data/";
  public static final String TEST_SCRIPTS = "src/test/resources/scripts/";
  public static final String TEST_OUTPUT = "src/test/resources/output/";
  public static final String TEST_EXPECTED = "src/test/resources/expected/";
  
  //Basic dependencies for Raijin
  public Session session;

  //Expected result
  public TasksManager expected;

  public TestProperties(String testCaseName) {
    this.testCaseName = testCaseName;
    setupEnvironment();                           //Setup paths for storage
  }

  void setupEnvironment() {
    session = Session.getSession();
    session.setDataPath(TEST_OUTPUT + testCaseName + ".json");
    actualJSON = new File(TEST_OUTPUT + testCaseName + ".json");
    expectedJSON = new File(TEST_EXPECTED + testCaseName + ".json");
    testScript = TEST_SCRIPTS + testCaseName + ".txt";
  }
}
