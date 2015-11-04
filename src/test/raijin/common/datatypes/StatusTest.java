//@@author A0112213E

package raijin.common.datatypes;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class StatusTest {

  @Test
  public void testCreateStatusWithoutResult() {
    Status status = new Status("added successfully");
    assertNull(status.getResult());
  }

  @Test
  public void testCreateStatusWithResult() {
    Status status = new Status("added successfully", "1. Submit OP1 19/9 2359");
    assertEquals("1. Submit OP1 19/9 2359", status.getResult());
  }
}
