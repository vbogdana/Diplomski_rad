package gui;

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
	private JButton reset = new JButton ("Reset Local KeyStore"),
			new_keypair = new JButton ("New Keypair"),
			save_keypair = new JButton("Save"),
			import_keypair = new JButton("Import (.p12)"),
			export_keypair = new JButton("Export (.p12)");
	
	KeyStorePanel(ToolbarListener listener) {
		setBounds(15, 15, 300, 400);
		setLayout(null);
		setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		
		JPanel p = new JPanel();
		p.setBounds(10, 10, 280, 165);
		p.setLayout(null);
		p.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
		add(p);
	
		reset.setBounds(55, 10, 170, 25);
		new_keypair.setBounds(55, 50, 170, 25);
		save_keypair.setBounds(55, 80, 170, 25);
		import_keypair.setBounds(23, 130, 115, 25);
		export_keypair.setBounds(142, 130, 115, 25);
		p.add(reset);
		p.add(new_keypair);
		p.add(save_keypair);
		p.add(import_keypair);
		p.add(export_keypair);
		
		scroll_pane.setBounds(10, 185, 280, 205);
		add(scroll_pane);
		
		keystore_list.setSelectedIndex(0);
		keystore_list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);		
		keystore_list.addListSelectionListener(listener);
		
		reset.addActionListener(listener);
		new_keypair.addActionListener(listener);
		save_keypair.addActionListener(listener);
		import_keypair.addActionListener(listener);
		export_keypair.addActionListener(listener);
		
		resetPanel();
		
	}
	
	void resetPanel() {
		// TODO
		// if keystore.size < 0
		reset.setEnabled(false);
		new_keypair.setEnabled(true);
		save_keypair.setEnabled(true);
		import_keypair.setEnabled(true);
		export_keypair.setEnabled(false);
	}

}
