package raijin.common.datatypes;

public class Status {
  
  private boolean isSuccess = false;    //Indicates success of an operation
  private String result;                //To be displayed in Display View
  private String feedback;              //Message feedback to user 

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

  public boolean isSuccess() {
    return isSuccess;
  }

  public void setSuccess(boolean isSuccess) {
    this.isSuccess = isSuccess;
  }

}
