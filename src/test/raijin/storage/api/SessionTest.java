package raijin.storage.api;

import static org.junit.Assert.*;

import java.io.UnsupportedEncodingException;

import org.junit.Before;
import org.junit.Test;

import raijin.common.datatypes.Constants;
import raijin.storage.handler.StorageHandler;

public class SessionTest {

  private Session session;

  @Before
  public void setUp() throws Exception {
    session = Session.getSession();
  }

  @Test
  public void testProgramDirectory_MatchWithTargetDirectory() throws UnsupportedEncodingException {
    String expected = StorageHandler.getJarPath() + Constants.NAME_USER_FOLDER;
    assertEquals(expected, session.programDirectory);
  }

}
