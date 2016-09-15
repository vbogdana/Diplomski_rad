package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;

import code.DataException;

@SuppressWarnings("serial")
public class ExtensionsPanel extends JPanel {
	
	final JPanel panel = new JPanel();
	MainFrame parent;
	ExtensionPanel [] extension_panels = new ExtensionPanel [Constants.NUM_OF_EXTENSIONS];
	BasicConstraintsPanel basic_constraints_panel;
	
	boolean [] isCritical = new boolean [Constants.NUM_OF_EXTENSIONS];
	
	ExtensionsPanel(MainFrame parent) {
		this.parent = parent;
		setBounds(720, 420, 560, 200);
		setLayout(new BorderLayout());
		Border b = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
        setBorder(BorderFactory.createTitledBorder(b, "Certificate Version 3 Extensions"));
				
		panel.setLayout(null);
        panel.setPreferredSize(new Dimension(530, 1520));
        
        // TODO
        extension_panels[Constants.BC] = (basic_constraints_panel = new BasicConstraintsPanel(parent));
        /*
        generateAuthorityKeyIdPanel();
        generateSubjectKeyIdPanel();
        generateKeyUsagePanel();
        generateCertificatePoliciesPanel();
        generatePolicyMappingsPanel();
        generateSubjectAlternativeNamePanel();
        generateIssuerAlternativeNamePanel();
        generateSubjectDirectoryAttrPanel();
        generateNameConstraintsPanel();
        generatePolicyConstraintsPanel();
        generateExtendedKeyUsagePanel();
        generateCRLDistributionPointsPanel();
        generateInhibitAnyPolicyPanel();
        generateFreshestCRLPanel();
        */
        
        for (int i = 0; i < Constants.NUM_OF_EXTENSIONS; i++) {
        	isCritical[i] = false;
        	if (extension_panels[i] != null) 
        		panel.add(extension_panels[i]);
        }

        final JScrollPane scroll = new JScrollPane(panel);
        scroll.setBorder(null);
     
        add(scroll, BorderLayout.CENTER);

	}

	void resetPanel() {
		for (int i = 0; i < Constants.NUM_OF_EXTENSIONS; i++) {
			isCritical[i] = false;
			if (extension_panels[i] != null) 
				extension_panels[i].resetPanel();
		}
	}
	
	void enablePanel(boolean flag) {
		if (parent.version_panel.getVersion() < Constants.V2)
			setEnabled(false);
		else
			setEnabled(true);

		// TODO	
		for (int i = 0; i < Constants.NUM_OF_EXTENSIONS; i++)
			if (extension_panels[i] != null) 
				extension_panels[i].enablePanel(flag);
	}
	
	void checkData() throws DataException {
		// TODO
		basic_constraints_panel.checkData();
	}
	
	// ********************************************************************************************************
	// 											GETTERS AND SETTERS
	// ********************************************************************************************************
	
	void setIsCritical(int i, boolean v) {
		isCritical[i] = v;
	}
	
	boolean getCritical(int i) {
		return isCritical[i];
	}
	
	void setCritical(int i, boolean v) {
		isCritical[i] = v;
		if (extension_panels[i] != null)
			extension_panels[i].setCritical(v);
	}
}
