package raijin.common.datatypes;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class HelpMessage {

  private static final Color COMMAND_FORMAT_COLOR = Color.rgb(51, 153, 255);
  private static final Color USER_INPUT_COLOR = Color.rgb(224, 224, 224);
  private static final Color ERROR_COLOR = Color.rgb(255, 55, 55);
  private static final Color DESCRIPTION_COLOR = Color.WHITE;
  private static final String DEFAULT_FONT_SIZE = "15";
  public Text commandFormat;
  public Text description;
  public List<Text> helpMessage = new ArrayList<Text>();

  public HelpMessage(String commandFormat, String description) {
    this.commandFormat = createText(commandFormat, COMMAND_FORMAT_COLOR);
    this.description = createText("\n" + description, DESCRIPTION_COLOR);
    initializeContent(commandFormat);
    initializeDescription(description);
  }
  
  Text createText(String input, Color fillColor) {
    Text output = new Text(input);
    output.setFill(fillColor);
    output.setStyle("-fx-font-size: " + DEFAULT_FONT_SIZE + ";");
    return output;
  }
  
  /**
   * Creates individual text node
   * @param commandFormat
   * @return
   */
  void initializeContent(String commandFormat) {
    String[] tokens = commandFormat.split(" ");
    
    for (String token : tokens) {
      helpMessage.add(generateTextFromString(token));
    }
  }

  void initializeDescription(String description) {
    if (description.substring(0, 1).equals("?")) {
      helpMessage.add(createText("\n" + description.substring(1), ERROR_COLOR));
    } else {
      helpMessage.add(createText("\n" + description, DESCRIPTION_COLOR));
    }
  }

  private Text generateTextFromString(String token) {
    if (token.substring(0, 1).equals("?")) { //Checks for user input
      String trimmed = token.substring(1);
      return createText(trimmed + " ", USER_INPUT_COLOR);
    } else {
      return createText(token + " ", COMMAND_FORMAT_COLOR);
    }
  }
}
