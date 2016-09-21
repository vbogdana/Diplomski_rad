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
	KeyIdentifiersPanel key_identifiers_panel;
	KeyUsagePanel key_usage_panel;
	CertificatePoliciesPanel certificate_policies_panel;
	
	boolean [] isCritical = new boolean [Constants.NUM_OF_EXTENSIONS];
	
	ExtensionsPanel(MainFrame parent, boolean[] extensions_conf) {
		this.parent = parent;
		setBounds(720, 420, 560, 200);
		setLayout(new BorderLayout());
		Border b = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
        setBorder(BorderFactory.createTitledBorder(b, "Certificate Version 3 Extensions"));        
        
        if (extensions_conf[Constants.AKID])
        	extension_panels[Constants.AKID] = (key_identifiers_panel = new KeyIdentifiersPanel(parent));
        if (extensions_conf[Constants.KU])
        	extension_panels[Constants.KU] = (key_usage_panel = new KeyUsagePanel(parent));
        if (extensions_conf[Constants.CP])
        	extension_panels[Constants.CP] = (certificate_policies_panel = new CertificatePoliciesPanel(parent));
        // TODO
        if (extensions_conf[Constants.BC])
        	extension_panels[Constants.BC] = (basic_constraints_panel = new BasicConstraintsPanel(parent));
        /*
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
        int Y = 10;
        for (int i = 0; i < Constants.NUM_OF_EXTENSIONS; i++) {
        	if (extension_panels[i] != null) {
        		extension_panels[i].setY(Y);
        		Y += extension_panels[i].getH() + 10;
        	}
        }
        
        panel.setLayout(null);
        panel.setPreferredSize(new Dimension(530, Y));		// 1520
        
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

		for (int i = 0; i < Constants.NUM_OF_EXTENSIONS; i++)
			if (extension_panels[i] != null) 
				extension_panels[i].enablePanel(flag);
	}
	
	void checkData() throws DataException {
		// TODO
		if (basic_constraints_panel != null) basic_constraints_panel.checkData();
		if (certificate_policies_panel != null) certificate_policies_panel.checkData();
	}
	
	// ********************************************************************************************************
	// 											GETTERS AND SETTERS
	// ********************************************************************************************************
	
	boolean getCritical(int i) {
		if (extension_panels[i] != null) 
			return extension_panels[i].isCritical.isSelected();
		else if (i == Constants.SKID && extension_panels[Constants.AKID] != null)
			return extension_panels[Constants.AKID].isCritical.isSelected();
		else
			return false;
	}
	
	void setCritical(int i, boolean v) {
		if (extension_panels[i] != null)
			extension_panels[i].setCritical(v);
	}
}
