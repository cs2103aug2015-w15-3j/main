package raijin.common.datatypes;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class HelpMessage {

  private static final Color COMMAND_FORMAT_COLOR = Color.rgb(0, 102, 204);
  private static final Color DESCRIPTION_COLOR = Color.WHITE;
  private static final String DEFAULT_FONT_SIZE = "18";
  public Text commandFormat;
  public Text description;
  public List<Text> helpMessage = new ArrayList<Text>();

  public HelpMessage(String commandFormat, String description) {
    this.commandFormat = createText(commandFormat, COMMAND_FORMAT_COLOR);
    this.description = createText("\n" + description, DESCRIPTION_COLOR);
    helpMessage.add(this.commandFormat);
    helpMessage.add(this.description);
  }
  
  Text createText(String input, Color fillColor) {
    Text output = new Text(input);
    output.setFill(fillColor);
    output.setStyle("-fx-font-size: " + DEFAULT_FONT_SIZE + ";");
    return output;
  }
}
