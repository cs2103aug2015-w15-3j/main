package raijin.common.datatypes;

import static org.junit.Assert.*;

import java.util.TreeSet;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class IDManagerTest {

  private IDManager idManager;

  @Before
  public void setUp() throws Exception {
    this.idManager = IDManager.getIdManager();
  }

  @Test
  @Ignore
  public void testUniqueIds() {
    assertNotEquals(idManager.getId(), idManager.getId());
  }

  @Test
  @Ignore
  public void testReturnId() {
    int id = idManager.getId();
    TreeSet<Integer> idPool = idManager.getIdPool();
    assertFalse(idPool.contains(id));
    idManager.returnId(id);
    assertTrue(idPool.contains(id));
  }

  @Test
  public void testIdSequence() {
    int a = idManager.getId();
    System.out.println(a);
  }
}
