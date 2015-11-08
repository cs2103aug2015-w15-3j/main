//@@author A0112213E

package raijin.integration;

import static org.junit.Assert.*;

import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

import raijin.common.datatypes.Task;
import raijin.helper.TestProperties;
import raijin.helper.TestUtils;
import raijin.logic.api.Logic;
import raijin.storage.api.TasksManager;

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
    TasksManager.getManager().setCompletedTasks(new HashMap<Integer, Task>());
    TasksManager.getManager().setPendingTasks(new HashMap<Integer, Task>());
  }

  /**
   * Test C.R.U.D features of Raijin
   */
  @Test
  public void testBasicCRUD() {
    executeTest("basicCRUD");
  }

}
