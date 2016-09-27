package gui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;

@SuppressWarnings("serial")
public class VersionPanel extends JPanel implements ActionListener {
	public static final int NUM_VERSIONS = 3;
	public static final int V1 = 0, V2 = 1, V3 = 2;
	
	private MainFrame parent;
	private int supported_version;
	private int selected = V1;
	private ButtonGroup buttonGroup = new ButtonGroup();
	private JRadioButton buttons[] = new JRadioButton [3];
	
	VersionPanel(MainFrame parent, int version_supported) {
		this.parent = parent;
		this.supported_version = version_supported;
		
		setBounds(720, 10, 560, 50);
		setLayout(new GridLayout(1, 3, 5, 3));
		Border b = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
        setBorder(BorderFactory.createTitledBorder(b, "Certificate Version"));
		
		for (int i = 0; i < NUM_VERSIONS; i++) {
			if (i > supported_version || i == V2)
				continue;
			
			buttons[i] = new JRadioButton("Version " + (i + 1));
			buttons[i].addActionListener(this);
			buttonGroup.add(buttons[i]);
			add(buttons[i]);
		}
		
		buttons[selected].setSelected(true);			
	}
	
	void resetPanel() {
		selected = V1;
		buttons[selected].setSelected(true);
		
		if (supported_version >= V2) {
			enableV2();
			if (supported_version >= V3)
				enableV3();
		}
	}
	
	void enablePanel(boolean flag) {
		for (int i = 0; i < NUM_VERSIONS; i++)
			if (i <= supported_version && i != V2)
				buttons[i].setEnabled(flag);
				
	}
	
	void enableV2() {
		if (selected < V2) {
			parent.subject_panel.enableV2(false);
			parent.issuer_panel.enableV2(false);
		} else {
			parent.subject_panel.enableV2(true);
			parent.issuer_panel.enableV2(true);
		}
	}
	
	void enableV3() {
		if (selected < V2)
			parent.extensions_panel.enablePanel(false);
		else
			parent.extensions_panel.enablePanel(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		for (int i = 0; i < NUM_VERSIONS; i++)
			if (((JRadioButton) e.getSource()) == buttons[i]) {
				selected = i;
				break;
			} 
		
		if (supported_version >= V2) {
			enableV2();	
			if (supported_version >= V3)
				enableV3();
		}
	}
	
	// ********************************************************************************************************
	// 										GETTERS AND SETTERS
	// ********************************************************************************************************
	
	int getSupportedVersion() {
		return supported_version;
	}
	
	int getVersion() {
		return selected;
	}
	
	void setVersion(int v) {
		selected = v;
		if (buttons[v] != null) buttons[v].setSelected(true);
	}

}
