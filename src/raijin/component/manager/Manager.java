package raijin.component.manager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import raijin.component.viewer.SimpleViewer;
import raijin.util.Status;

public class Manager {

  private static BufferedReader br;
  private static Status status;
  private static SimpleViewer viewer;

  public static void main(String[] args) throws IOException {
    setupEnvironment();             //Initiliase key components
    while (status.isRunning()) {
      viewer.showUser(Status.INFO_PROMPT);
      String userInput = br.readLine();
    }
  }

  private static void setupEnvironment() {
    br = new BufferedReader(new InputStreamReader(System.in));
    status = Status.getStatus();          //Generate universal program status`
    viewer = SimpleViewer.getViewer();    //Generate viewer that handles program output
    viewer.showUser(Status.INFO_WELCOME);
  }

}
