package raijin.ui;

public class SimpleViewer extends BaseViewer{
  private static SimpleViewer instance = new SimpleViewer();
  
  private SimpleViewer() {}

  @Override
  public void showUser(String status) {
    System.out.print(status);
  }

  public static SimpleViewer getViewer() {
    return instance;
  }

}
