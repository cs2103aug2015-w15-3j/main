package raijin.common.utils;

public class SpeechRecognizer {

  public static SpeechRecognizer recognizer = new SpeechRecognizer();
  
  private SpeechRecognizer() {}
  
  public static SpeechRecognizer getRecognizer() {
    return recognizer;
  }

}
