package gui;

public class X509 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			new MainFrame();
		} catch (CustomException e) {
			// TODO Auto-generated catch block
			MainFrame.reportError(e.getMessage());
		}
    
	}

}
