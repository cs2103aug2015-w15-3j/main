package raijin.common.utils;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;

public class RaijinLoggerTest {

  private static Logger logger;

  @BeforeClass
  public static void setUp() throws Exception {
    logger = LoggerFactory.getLogger(RaijinLoggerTest.class);
  }

  @Test
  public void testLogDebug() {
    ArrayList<String> test = new ArrayList<String>();
    logger.trace("Size of array list is {}", test.size());
  }

}
