package gui;

import java.util.Enumeration;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.border.EtchedBorder;

@SuppressWarnings("serial")
public class KeyStorePanel extends JPanel {
	
	private DefaultListModel<String> keystore_model = new DefaultListModel<>();
	private JList<String> keystore_list = new JList<>(keystore_model);
	private JScrollPane scroll_pane = new JScrollPane(keystore_list);
	JButton reset = new JButton ("Reset Local KeyStore"),
			new_keypair = new JButton ("New Keypair"),
			save_keypair = new JButton("Save"),
			remove_keypair = new JButton("Remove Keypair"),
			import_keypair = new JButton("Import (.p12)"),
			export_keypair = new JButton("Export (.p12)");
	
	KeyStorePanel(ToolbarListener listener) {
		setBounds(15, 15, 300, 400);
		setLayout(null);
		setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		
		JPanel p = new JPanel();
		p.setBounds(10, 10, 280, 195);
		p.setLayout(null);
		p.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
		add(p);
	
		reset.setBounds(55, 10, 170, 25);
		new_keypair.setBounds(55, 50, 170, 25);
		save_keypair.setBounds(55, 80, 170, 25);
		remove_keypair.setBounds(55, 110, 170, 25);
		import_keypair.setBounds(23, 160, 115, 25);
		export_keypair.setBounds(142, 160, 115, 25);
		p.add(reset);
		p.add(new_keypair);
		p.add(save_keypair);
		p.add(remove_keypair);
		p.add(import_keypair);
		p.add(export_keypair);
		
		scroll_pane.setBounds(10, 215, 280, 175);
		add(scroll_pane);
		
		reset.addActionListener(listener);
		new_keypair.addActionListener(listener);
		save_keypair.addActionListener(listener);
		remove_keypair.addActionListener(listener);
		import_keypair.addActionListener(listener);
		export_keypair.addActionListener(listener);
		
		// resetPanel();
		
		keystore_list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);		
		keystore_list.addListSelectionListener(listener);
	}
	
	void loadKeystore(Enumeration<String> certificates) {
		keystore_model.removeAllElements();
		keystore_model.addElement("Local Keystore");

		if (certificates != null) {
			while (certificates.hasMoreElements())
				keystore_model.addElement(certificates.nextElement());
		}
		
		resetPanel();

	}
	
	void resetPanel() {
		if (keystore_model.isEmpty()) {
			reset.setEnabled(false);
			keystore_model.addElement("Local Keystore");
		} else {
			reset.setEnabled(true);
		}
		new_keypair.setEnabled(true);
		save_keypair.setEnabled(true);
		remove_keypair.setEnabled(false);
		import_keypair.setEnabled(true);
		export_keypair.setEnabled(false);
		
		keystore_list.setSelectedIndex(0);		
	}
	
	void enablePanel(boolean flag) {
		save_keypair.setEnabled(!flag);
		export_keypair.setEnabled(flag);
		remove_keypair.setEnabled(flag);
	}

	void addKeypair(String name) {
		keystore_model.addElement(name);		
	}
	
	void removeKeypair(String name) {
		for (int i = 0; i < keystore_model.size(); i++)
			if (keystore_model.getElementAt(i).equals(name)) {
				keystore_model.remove(i);
				break;
			}
	}

	int getSelectedIndex() {
		return keystore_list.getSelectedIndex();
	}
	
	String getSelectedValue() {
		return keystore_list.getSelectedValue();
	}

}
