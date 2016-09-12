package exceptions;

@SuppressWarnings("serial")
public class CustomException extends Exception {
	
	public CustomException(String msg) {
		super(msg);
	}
	
	public String toString() {
		return super.getMessage();
	}

}
