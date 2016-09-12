package x509;

import code.CodeInterface;
import code.MyCode;
import exceptions.GuiException;
import gui.GuiInterface;

public class X509 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			// potrebno je konfigurisati koje verzije dozvoljavamo
			// ako je verzija 3 potrebno je konfigurisati koje ekstenzije podrzavamo
			// potrebno je konfigurisati koje algoritme podrzavamo
					
			CodeInterface my_code = null; 
			GuiV3 gui_access = null;
			boolean conf[] = { true, true, true, true };
			
			my_code = new MyCode(gui_access);			// ovo je implementacija studenta, i bice zavisna od gui_acces-a
			gui_access = new GuiV3(conf, my_code);
		} catch (GuiException e) {
			// Handles frame construction exceptions
			GuiInterface.reportError(e);
		} // TODO
		/*
		 catch (CustomException e) {
		  // catches programming exceptions
			MainFrame.reportError(e.toString());
		}
		*/

    
	}

}
