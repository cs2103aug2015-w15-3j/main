package raijin.helper;

import raijin.storage.api.Session;

public class TestProperties {


  private final String testCaseName;                //Name of test script used
  private static final String TEST_DATA = "/src/test/resources/data/";
  private static final String TEST_SCRIPTS = "src/test/resources/scripts/";
  private static final String TEST_OUTPUT = "src/test/resources/output/";
  private static final String TEST_EXPECTED = "src/test/resources/expected/";
  
  //Basic dependencies for Raijin
  private Session session;

  public TestProperties(String testCaseName) {
    this.testCaseName = testCaseName;
    setupEnvironment();                           //Setup paths for storage
  }
  
  void setupEnvironment() {
    session = Session.getSession();
    session.setDataPath(TEST_OUTPUT + testCaseName + ".json");
  }
}
