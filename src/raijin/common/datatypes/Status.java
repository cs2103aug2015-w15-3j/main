package raijin.common.datatypes;

public class Status {
  
  private String result;
  private String feedback;

  /*Feedback is compulsory for any commands*/
  public Status(String feedback){
    this.feedback = feedback;
  }

  /*Alternative for those commands that require display*/
  public Status(String feedback, String result){
    this.feedback = feedback;
    this.result = result;
  }

  public String getResult() {
    return result;
  }

  public String getFeedback() {
    return feedback;
  }

}
