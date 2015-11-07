//@@author A0112213E

package raijin.ui;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.paint.Color;
import javafx.scene.text.Text;

/**
 * Represents help message that will pop out depending on typed command
 * @author papa
 *
 */
public class HelpMessage {

  private static final Color HELP_FORMAT_COLOR = Color.rgb(142, 0, 214);
  private static final Color COMMAND_FORMAT_COLOR = Color.rgb(51, 153, 255);
  private static final Color USER_INPUT_COLOR = Color.rgb(224, 224, 224);
  private static final Color ERROR_COLOR = Color.rgb(255, 55, 55);
  private static final Color DESCRIPTION_COLOR = Color.WHITE;
  private static final String DEFAULT_FONT_SIZE = "15";
  private static final String HELP_HEADER_FONT_SIZE = "25";

  /*acceptable format for a command*/
  public Text commandFormat;    
  /*function of a command or error message when there is an error with parsing*/
  public Text description;
  /*Text for help header in help Menu*/
  public Text help;
  
  public List<Text> helpMessage = new ArrayList<Text>();
  
  public HelpMessage(String help) {
	  this.help = createText(help, HELP_FORMAT_COLOR);
	  this.help.setStyle("-fx-font-size: " + HELP_HEADER_FONT_SIZE + ";");
	  helpMessage.add(this.help);
  }
  
  public HelpMessage(String commandFormat, String description) {
    this.commandFormat = createText(commandFormat, COMMAND_FORMAT_COLOR);
    this.description = createText("\n" + description, DESCRIPTION_COLOR);
    initializeCommandFormat(commandFormat);
    initializeDescription(description);
  }
  
  /**
   * Generates Text object with customized color 
   * @param input
   * @param fillColor
   */
  Text createText(String input, Color fillColor) {
    Text output = new Text(input);
    output.setFill(fillColor);
    output.setStyle("-fx-font-size: " + DEFAULT_FONT_SIZE + ";");
    return output;
  }
  
  private Text generateTextFromString(String token) {
    if (token.substring(0, 1).equals("?")) {                                    //Checks delimiter for user input 
      String trimmed = token.substring(1);
      return createText(trimmed + " ", USER_INPUT_COLOR);
    } else {
      return createText(token + " ", COMMAND_FORMAT_COLOR);
    }
  }

  /**
   * Creates individual text node
   * @param commandFormat
   * @return
   */
  void initializeCommandFormat(String commandFormat) {
    String[] tokens = commandFormat.split(" ");
    
    for (String token : tokens) {
      helpMessage.add(generateTextFromString(token));
    }
  }

  void initializeDescription(String description) {
    if (description.substring(0, 1).equals("?")) {                              //Checks for delimiter for error message
      helpMessage.add(createText("\n" + description.substring(1), ERROR_COLOR));
    } else {
      helpMessage.add(createText("\n" + description, DESCRIPTION_COLOR));
    }
  }

}
