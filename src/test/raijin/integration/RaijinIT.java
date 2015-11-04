//@@author A0112213E

package raijin.integration;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import raijin.helper.TestProperties;
import raijin.helper.TestUtils;
import raijin.logic.api.Logic;

public class RaijinIT {

  private Logic logic;

  //===========================================================================
  // Helper 
  //==========================================================================
  
  void executeTest(String testCaseName) {
    //Given
    TestProperties properties = new TestProperties(testCaseName);
    TestUtils.runCommands(properties.testScript, properties.actualJSON.getAbsolutePath(),
        logic);
    
    //Then
    TestUtils.assertSimilarFiles(properties.expectedJSON, properties.actualJSON);
  }

  @Before
  public void setUp() throws Exception {
    logic = new Logic();
  }

  /**
   * Test C.R.U.D features of Raijin
   */
  @Test
  public void testBasicCRUD() {
    executeTest("basicCRUD");
  }

}
