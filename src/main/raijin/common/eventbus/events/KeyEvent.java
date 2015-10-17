package raijin.common.eventbus.events;

import javafx.scene.input.KeyCode;

public class KeyEvent {

  public KeyCode keycode;
  
  public KeyEvent(KeyCode keycode) {
    this.keycode = keycode;
  }
}
