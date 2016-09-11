package gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;

@SuppressWarnings("serial")
public class PublicKeyPanel extends JPanel implements ActionListener {
	public static final int NUM_OF_ALGORITHMS = 4;
	public static final int DSA_MIN = 512, DSA_STEP = 64, DSA_MAX = 2048, NUM_OF_DSA_LENGTHS = ((DSA_MAX - DSA_MIN) / DSA_STEP + 1);
	public static final int NUM_OF_SETS = 3;
	public static final int DSA = 0, RSA = 1, GOST = 2, EC = 3;
	public static final int EC_CURVE_PARAMETER = 4;
	public static final int KEY_LENGTH = 0, TYPE = 0, SET = 0, NAMED_CURVE = 1;
	
	private MainFrame parent;
	private int algorithm = -1;
	public boolean enabled[] = { true, true, true, true };			// algorithm configuration
	
	// KEY GENERATOR ALGORITHMS
	private final String algorithms[] = { "DSA", "RSA", "GOST", "EC" };
	private ButtonGroup buttonGroup = new ButtonGroup();
	private JRadioButton alg_buttons[] = new JRadioButton [NUM_OF_ALGORITHMS];
		
	// ALGORITHM PARAMETERS
	private final String texts[] = { "Key length:", "Key length:", "Type:", "Set:", "Curve:" };	
	private String dsa_lengths[] = new String [NUM_OF_DSA_LENGTHS];
	private String rsa_lengths[] = { "512", "1024", "2048", "4096" };
	private String gost_types[] = { "GOST3410-2001-CryptoPro-A", "GOST3410-2001-CryptoPro-B", "GOST3410-2001-CryptoPro-C", "GOST3410-2001-CryptoPro-XchA", "GOST3410-2001-CryptoPro-XchB" };
	private String ec_sets[] = { "X9.62", "SEC", "NIST" };
	private String curves_by_set[][] = { 
								  { "prime192v1", "prime192v2", "prime192v3", "prime239v1", "prime239v2", "prime239v3", "prime256v1" }, 
								  { "secp192k1", "secp192r1", "secp224k1", "secp224r1", "secp256k1", "secp256r1", "secp384r1", "secp521r1" }, 
								  { "P-224", "P-256", "P-384", "P-521" } 
								};
	private String parameter_values[][] = { dsa_lengths, rsa_lengths, gost_types, ec_sets };
	private JLabel labels[] = new JLabel [NUM_OF_ALGORITHMS + 1];
	private DefaultComboBoxModel<String> model[] = new DefaultComboBoxModel [NUM_OF_SETS];
	private JComboBox parameters[] = new JComboBox [NUM_OF_ALGORITHMS + 1];

	// HASH ALGORITHMS
	private String hashes[][] = { 
							{ "SHA1withDSA" }, 
							{ "MD2withRSA", "MD5withRSA", "SHA1withRSA", "SHA224withRSA", "SHA256withRSA", "SHA384withRSA", "SHA512withRSA", "RIPEMD128withRSA", "RIPEMD160withRSA", "RIPEMD256withRSA" },
							{ "GOST3411withGOST3410", "GOST3411withECGOST3410" },
							{ "SHA1withECDSA", "SHA224withECDSA", "SHA256withECDSA", "SHA384withECDSA", "SHA512withECDSA" }
						};
	private JComboBox hash_algorithms[] = new JComboBox [NUM_OF_ALGORITHMS];
	
	public PublicKeyPanel(MainFrame parent) throws CustomException {
		this.parent = parent;
		setBounds(720, 210, 550, 200);
		setLayout(new GridBagLayout());
		setBorder(BorderFactory.createTitledBorder("Certificate Public Key"));
		
		initPanelData();
		
		for (int i = 0; i < NUM_OF_ALGORITHMS; i++)
			if (enabled[i]) {
				algorithm = i;
				break;
			}
		
		if (algorithm == -1)
			throw new CustomException("At least one public key algorithm must be enabled.");
		
		alg_buttons[algorithm].setSelected(true);
		selectAlgorithm();
	}
	
	private void initPanelData() {
		for (int i = 0, j = DSA_MIN; j <= DSA_MAX; j += DSA_STEP, i++)
			dsa_lengths[i] = String.valueOf(j);
		
		for (int i = 0; i < NUM_OF_SETS; i++) {
			model[i] = new DefaultComboBoxModel<> ();
			for (int j = 0; j < curves_by_set[i].length; j++)
				model[i].addElement(curves_by_set[i][j]);
		}

		for (int i = 0, row = 0; i < NUM_OF_ALGORITHMS; i++) {
			GridBagConstraints c = new GridBagConstraints();
			
			if (!enabled[i])
				continue;
			
			// add algorithm choices
			alg_buttons[i] = new JRadioButton(algorithms[i]);
			alg_buttons[i].addActionListener(this);
			buttonGroup.add(alg_buttons[i]);
			c.gridx = 0; c.gridy = row;
			c.weightx = 1.0; c.weighty = 1.0;
			c.fill = GridBagConstraints.NONE;
			c.anchor = GridBagConstraints.LINE_START;			
			add(alg_buttons[i], c); 
			
			// add labels
			labels[i] = new JLabel(texts[i]);
			labels[i].setHorizontalAlignment(SwingConstants.LEFT);
			c.gridx = 1; c.gridy = row;
			c.fill = GridBagConstraints.NONE;
			c.anchor = GridBagConstraints.LINE_END;
			add(labels[i], c);
			if (i == EC) {
				labels[EC_CURVE_PARAMETER] = new JLabel(texts[EC_CURVE_PARAMETER]);
				labels[EC_CURVE_PARAMETER].setHorizontalAlignment(SwingConstants.LEFT);
				c.gridx = 1; c.gridy = row + 1;
				c.fill = GridBagConstraints.NONE;
				add(labels[EC_CURVE_PARAMETER], c);
			}
						
			// add combo boxes for algorithm parameters
			parameters[i] = new JComboBox(parameter_values[i]);
			c.gridx = 2; c.gridy = row;
			c.fill = GridBagConstraints.HORIZONTAL;
			c.insets = new Insets(2, 4, 2, 4);
			add(parameters[i], c);
			if (i == EC) {			
				parameters[EC_CURVE_PARAMETER] = new JComboBox(model[0]);
				parameters[EC_CURVE_PARAMETER].setPreferredSize(new Dimension(218, 25));
				c.gridx = 2; c.gridy = row + 1;
				c.fill = GridBagConstraints.HORIZONTAL;
				c.insets = new Insets(2, 4, 2, 4);
				add(parameters[EC_CURVE_PARAMETER], c);
				
				// add action listener for when choosing a set of curves
				parameters[EC].addActionListener(new ActionListener () {
					@Override
					public void actionPerformed(ActionEvent e) {
						int selected_curve_set = parameters[EC].getSelectedIndex();		
						parameters[EC_CURVE_PARAMETER].setModel(model[selected_curve_set]);
					}		
				});
			}
					
			// add combo boxes for hashes		
			c.gridx = 3; c.gridy = row;
			hash_algorithms[i] = new JComboBox(hashes[i]);
			hash_algorithms[i].setPreferredSize(new Dimension(189, 25));
			hash_algorithms[i].addActionListener(this);
			add(hash_algorithms[i], c);
			
			// increment rows
			row++;
			
		}
		
	}
	
	public void selectAlgorithm() {
		labels[algorithm].setEnabled(true);
		parameters[algorithm].setEnabled(true);
		hash_algorithms[algorithm].setEnabled(true);
		if (algorithm == EC) {
			labels[EC_CURVE_PARAMETER].setEnabled(true);
			parameters[EC_CURVE_PARAMETER].setEnabled(true);
		}
		for (int i = 0; i < NUM_OF_ALGORITHMS; i++) {
			if (!enabled[i])
				continue;
			
			if (i != algorithm) {
					labels[i].setEnabled(false);
					parameters[i].setEnabled(false);
					hash_algorithms[i].setEnabled(false);
					if (i == EC) {
						labels[EC_CURVE_PARAMETER].setEnabled(false);
						parameters[EC_CURVE_PARAMETER].setEnabled(false);
					}
				}
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() instanceof JRadioButton) {
			JRadioButton src = ((JRadioButton) e.getSource());
			if (src == alg_buttons[DSA]) {
				algorithm = DSA;			
			} else if (src == alg_buttons[RSA]) {
				algorithm = RSA;			
			} else if (src == alg_buttons[GOST]) {
				algorithm = GOST;
			} else {
				algorithm = EC;
			}	
			selectAlgorithm();
		}
		
		// if it's either radio button or hash combo box it needs to change text field value
		parent.subject_panel.setValue(InfoPanel.SA, getSignatureAlgorithm());				
	}
		
	// ********************************************************************************************************
	// 												GETTERS
	// ********************************************************************************************************
	
	public int getAlgorithmIndex() {
		return algorithm;
	}
	
	public String getAlgorithm() {
		return algorithms[algorithm];
	}
	
	public String getAlgorithmParameter(int i) throws CustomException {
		if (i == 0)
			return (String) parameters[algorithm].getSelectedItem();
		else 
			if (algorithm == EC)
				return (String) parameters[EC_CURVE_PARAMETER].getSelectedItem();
			else
				throw new CustomException("Invalid index while obtaining public key parameter.");

	}
	
	public String getSignatureAlgorithm() {
		return (String) hash_algorithms[algorithm].getSelectedItem();
	}

}

