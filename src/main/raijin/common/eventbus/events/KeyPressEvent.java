package raijin.common.eventbus.events;

import javafx.scene.input.KeyEvent;

public class KeyPressEvent {

  public String currentUserInput;
  public KeyEvent keyEvent;
  
  public KeyPressEvent(KeyEvent keyEvent, String currentUserInput) {
    this.keyEvent = keyEvent;
    this.currentUserInput = currentUserInput;
  }
  
}
