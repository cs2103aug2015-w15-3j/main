package raijin.common.utils;

import java.io.IOException;

import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.LiveSpeechRecognizer;
import edu.cmu.sphinx.api.Microphone;
import edu.cmu.sphinx.api.SpeechResult;
import edu.cmu.sphinx.recognizer.Recognizer;
import edu.cmu.sphinx.result.Result;
import edu.cmu.sphinx.util.props.ConfigurationManager;

public class SpeechRecognizer {

  public static SpeechRecognizer recognizer = null;
  private static final String LM_PATH = "resource/raijin.lm";
  private static final String DICT_PATH = "resource/raijin.6d";
  LiveSpeechRecognizer listener;
  Recognizer recog;
  edu.cmu.sphinx.frontend.util.Microphone microphone;

  private SpeechRecognizer() throws IOException {
    Configuration speechConfig = manualConfig();
    listener = new LiveSpeechRecognizer(speechConfig);
  }

  public Configuration manualConfig() {
    Configuration speechConfig = new Configuration();
    speechConfig.setAcousticModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us");
    speechConfig.setDictionaryPath("resource:/edu/cmu/sphinx/models/en-us/cmudict-en-us.dict");
    speechConfig.setLanguageModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us.lm.bin");
    return speechConfig;
  }

  public static SpeechRecognizer getRecognizer() throws IOException {
    if (recognizer == null) {
      recognizer = new SpeechRecognizer();
    }
    return recognizer;
  }

  /**
   * 
   * @param clear determines whether cached data will be cleared
   */
  public void startRecording(boolean clear) {
    listener.startRecognition(clear);
  }

  public void pauseRecording() {
    listener.stopRecognition();
  }

  public SpeechResult getResult() {
    SpeechResult result = listener.getResult();
    pauseRecording();
    return result;
  }
}
