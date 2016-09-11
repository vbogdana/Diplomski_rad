package gui;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

@SuppressWarnings("serial")
public class KeyStorePanel extends JPanel implements ListSelectionListener {
	
	DefaultListModel<String> model = new DefaultListModel<>();
	JList<String> list = new JList<>(model);
	JScrollPane scroll_pane = new JScrollPane(list);
	JButton reset = new JButton ("Reset Local KeyStore"),
			new_keypair = new JButton ("New Keypair");
	
	public KeyStorePanel() {
		setBounds(10, 10, 250, 610);
		setLayout(null);
		Border b = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
		setBorder(BorderFactory.createTitledBorder(b, "Local KeyStore"));
		
		
		
		reset.setBounds(40, 25, 170, 25);
		new_keypair.setBounds(40, 60, 170, 25);
		add(reset);
		add(new_keypair);
		
		scroll_pane.setBounds(10, 120, 230, 170);
		add(scroll_pane);
		list.setSelectedIndex(0);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		list.addListSelectionListener(this);
		
	}

	@Override
	public void valueChanged(ListSelectionEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
