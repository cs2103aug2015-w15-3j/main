package raijin.common.datatypes;

public class Session {

  private static Session session = new Session();
  private static String jarPath;
  
  private Session() {
    
  }
  
  public static Session getSession() {
    return session;
  }

}
