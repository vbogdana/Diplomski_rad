package gui;

import exceptions.DataException;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import code.CodeInterface;

public class ToolbarListener implements ActionListener, ListSelectionListener {
	private MainFrame mainFrame;
	private CodeInterface code;	// TODO

	public ToolbarListener(MainFrame mainFrame, CodeInterface code) {
		// TODO Auto-generated constructor stub
		this.code = code;
		this.mainFrame = mainFrame;
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		// TODO Auto-generated method stub
		switch (event.getActionCommand()) {
		
		case "Reset Local KeyStore":
			// TODO
			// empty keystore
			mainFrame.resetPanel();
			code.resetLocalKeyStore();
			break;
		
		case "New Keypair":
			mainFrame.resetPanel();
			break;
		
		case "Save":
			try {
				((SubjectPanel) mainFrame.subject_panel).checkData();
				mainFrame.serial_number_panel.checkData();
				mainFrame.validity_panel.checkData();
				// TODO
				code.saveKey();
			} catch (DataException e) {
				// TODO Auto-generated catch block
				GuiInterface.reportError(e);
			}
			break;
		
		case "Import (.p12)":
			// TODO
			code.importKeypair();
			break;
		
		case "Export (.p12)":
			// TODO
			code.exportKeypair();
			break;
		case "Sign certificate":
			// TODO
			code.signCertificate();
			break;
		case "Import certificate":
			// TODO
			code.importCertificate();
			break;
		
		case "Export certificate":
			// TODO
			code.exportCertificate();
			break;
		}

	}

	@Override
	public void valueChanged(ListSelectionEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
