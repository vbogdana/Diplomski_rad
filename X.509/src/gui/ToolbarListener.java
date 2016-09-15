package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import code.CodeInterface;
import code.DataException;

public class ToolbarListener implements ActionListener, ListSelectionListener {
	private MainFrame mainFrame;
	private CodeInterface code;

	ToolbarListener(MainFrame mainFrame, CodeInterface code) {
		this.code = code;
		this.mainFrame = mainFrame;
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		switch (event.getActionCommand()) {	
			case "Reset Local KeyStore": resetLocalKeystorePerformed(); break;
			case "New Keypair": newKeypairPerformed(); break;
			case "Save": saveKeypairPerformed(); break;
			case "Import (.p12)": importKeypairPerformed(); break;
			case "Export (.p12)": exportKeypairPerformed(); break;
			case "Sign certificate": signCertificatePerformed(); break;
			case "Import certificate": importCertificatePerformed(); break;
			case "Export certificate": exportCertificatePerformed(); break;
		}

	}

	@Override
	public void valueChanged(ListSelectionEvent ev) {
		// TODO Auto-generated method stub
		if (!ev.getValueIsAdjusting()) {
			if (mainFrame.toolbar_panel.keystore_panel.getSelectedIndex() == 0) {	
				// reset
				mainFrame.toolbar_panel.resetPanel();
				mainFrame.resetPanel();				
				// enable
				mainFrame.enablePanel(true);
				mainFrame.toolbar_panel.keystore_panel.enablePanel(false);
				mainFrame.enableSignButton(false);
				mainFrame.enableExportCertificateButton(false);
			} else {
				String keypair_name = (String) mainFrame.toolbar_panel.keystore_panel.getSelectedValue();
				if (keypair_name == null)
					return;
				// reset
				mainFrame.resetPanel();
				if (code.loadKey(keypair_name)) {
					// enable
					mainFrame.enablePanel(false);
					mainFrame.toolbar_panel.keystore_panel.enablePanel(true);
					// ako je potpisan mora sign i export da se enableuje
					// npr da load key vraca int vrednost koja nam to govori
				} else {
					// reset
					mainFrame.toolbar_panel.resetPanel();
					GuiInterface.reportError("Error while loading keypair from local KeyStore.");
				}
			}
			
		}	
	}
	
	private void resetLocalKeystorePerformed() {	
		code.resetLocalKeyStore();
		mainFrame.loadKeystore(null);
	}
	
	private void newKeypairPerformed() {
		// reset
		mainFrame.toolbar_panel.resetPanel();
		mainFrame.resetPanel();
		// enable
		mainFrame.enablePanel(true);
	}
	
	private void saveKeypairPerformed() {
		try {
			((SubjectPanel) mainFrame.subject_panel).checkData();
			mainFrame.serial_number_panel.checkData();
			mainFrame.validity_panel.checkData();
			if (mainFrame.version_panel.getSupportedVersion() >= Constants.V3)
				mainFrame.extensions_panel.checkData();
			
			String keypair_name = JOptionPane.showInputDialog(mainFrame, "Name: ", null);
			
			if (keypair_name == null)
				return;		// in case of cancel
			
			if (keypair_name.isEmpty())
				throw new DataException("Invalid name.");
			
			if (code.saveKey(keypair_name)) {
				mainFrame.addKeypair(keypair_name);
				// reset
				mainFrame.toolbar_panel.resetPanel();
				mainFrame.resetPanel();
			}
		} catch (DataException e) {
			GuiInterface.reportError(e);
		}
	}
	
	private void importKeypairPerformed() {
		JFileChooser fileChooser = new JFileChooser(); // change default path "E:\\SI4ZP\\projekat\\test"
		fileChooser.setDialogType(JFileChooser.OPEN_DIALOG);
		fileChooser.setFileFilter(new FileNameExtensionFilter(".p12", "p12"));
		fileChooser.setAcceptAllFileFilterUsed(false);
		 
		int userSelection = fileChooser.showOpenDialog(null);
		 
		if (userSelection == JFileChooser.APPROVE_OPTION) {
		    File file = fileChooser.getSelectedFile();	    
		    try {
		    	String password = JOptionPane.showInputDialog(mainFrame, "Password:", null);		    
			    String keypair_name = JOptionPane.showInputDialog(mainFrame, "Name:", null);
			    
			    if (password == null || keypair_name == null)
			    	return;
			    
			    if (password.equals("") || keypair_name.equals(""))
			    	throw new DataException("Invalid password or name.");
			    
			    if (code.importKeypair(keypair_name, file.getAbsolutePath(), password)) {
			    	mainFrame.addKeypair(keypair_name);
			    	JOptionPane.showMessageDialog(mainFrame, "Keypair successfully imported!");
			    }			    
		    } catch (DataException e) {
		    	GuiInterface.reportError(e);
		    }
		}
	}
	
	private void exportKeypairPerformed() {
		JFileChooser fileChooser = new JFileChooser(); // change default path "E:\\SI4ZP\\projekat\\test"
		fileChooser.setDialogType(JFileChooser.SAVE_DIALOG);
		fileChooser.setFileFilter(new FileNameExtensionFilter(".p12", "p12"));
		fileChooser.setAcceptAllFileFilterUsed(false);
		 
		int userSelection = fileChooser.showSaveDialog(null);
		 
		if (userSelection == JFileChooser.APPROVE_OPTION) {
		    File file = fileChooser.getSelectedFile();	    
		    try {
			    String password = JOptionPane.showInputDialog(mainFrame, "Password:", null);
			    if (password == null)
			    	return;
			    
			    if (password.equals(""))
			    	throw new DataException("Invalid password.");
			    
			    String keypair_name = (String) mainFrame.toolbar_panel.keystore_panel.getSelectedValue();
			    if (code.exportKeypair(keypair_name, file.getAbsolutePath(), password)) {		    	
			    	JOptionPane.showMessageDialog(mainFrame, "Keypair successfully exported!");
			    }
		    } catch (DataException e) {
		    	GuiInterface.reportError(e);
		    }
		}	
	}
	
	private void signCertificatePerformed() {
		// TODO
		code.signCertificate();
	}
	
	private void importCertificatePerformed() {
		// TODO
		code.importCertificate();		
	}
	
	private void exportCertificatePerformed() {
		// TODO
		code.exportCertificate();
	}

}
