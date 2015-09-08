package raijin.common.utils;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import raijin.common.utils.IDManager;

public class IDManagerTest {

  private IDManager idManager;

  @Before
  public void setUp()throws Exception{
    this.idManager = IDManager.getInstance();
  }

  @Test
  public void uniqueIDTest() {
    assertNotEquals(idManager.getId(), idManager.getId());
  }


}
