package gui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Properties;

import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JTextField;

import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import code.DateLabelFormatter;

@SuppressWarnings("serial")
public class SubjectDirectoryAttributesPanel extends ExtensionPanel {
	JLabel date;
	DateLabelFormatter dateFormatter = new DateLabelFormatter();
	Properties properties = new Properties();
	UtilDateModel dateModel;
	JDatePanelImpl datePanel;
	JDatePickerImpl datePicker;
	String [] texts = { "Place of birth:", "Country of citizenship:" };
	JLabel [] labels;
	JTextField [] values;
	JLabel gender;
	JCheckBox [] genders;
	ButtonGroup group;
	int oldSelection = 0;

	SubjectDirectoryAttributesPanel(MainFrame mainFrame) {
		super(mainFrame, "Subject directory attributes", Constants.SDA);

		panel.setBounds(10, 50, 490, 150);
		
		date = new JLabel("Date of birth:");
		date.setBounds(10, 10, 190, 25);
		panel.add(date);
		
		dateModel = new UtilDateModel();
		datePanel = new JDatePanelImpl(dateModel, properties);
		
		datePicker =  new JDatePickerImpl (datePanel, dateFormatter);
		datePicker.setBounds(210, 10, 270, 30);
		dateModel.setSelected(false);
		panel.add(datePicker);
		
		
		properties.put("text.today", "Today");
		properties.put("text.month", "Month");
		properties.put("text.year", "Year");
		
		labels = new JLabel [2];
		values = new JTextField [2];
		for (int i = 0; i < 2; i++) {
			labels[i] = new JLabel(texts[i]);
			labels[i].setBounds(10, 45 + i*35, 190, 25);
			values[i] = new JTextField(50);
			values[i].setBounds(210, 45 + i*35, 270, 25);
			panel.add(labels[i]);
			panel.add(values[i]);
		}
		
		gender = new JLabel("Gender:");
		gender.setBounds(10, 115, 190, 25);
		panel.add(gender);
			
		genders = new JCheckBox [2];
		genders[Constants.M] = new JCheckBox("male");
		genders[Constants.M].setBounds(210, 115, 100, 25);
		genders[Constants.F] = new JCheckBox("female");
		genders[Constants.F].setBounds(320, 115, 100, 25);
		group = new ButtonGroup();
		for (int i = 0; i < 2; i++) {
			// to enable deselecting checkboxes
			genders[i].addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent ev) {
					
					if (genders[Constants.M].isSelected()) {
						if (oldSelection == Constants.M) {
							group.clearSelection();
							oldSelection = -1;
						} else
							oldSelection = Constants.M;
					}
					
					if (genders[Constants.F].isSelected()) {
						if (oldSelection == Constants.F) {
							group.clearSelection();
							oldSelection = -1;
						} else
							oldSelection = Constants.F;
					}
				}				
			});
			group.add(genders[i]);
			panel.add(genders[i]);
		}
		
	}

	@Override
	void resetPanel() {
		resetExtensionPanel();
		dateModel.setSelected(false);
		for (int i = 0; i < 2; i++) {
			values[i].setText("");
			values[i].setDisabledTextColor(Color.LIGHT_GRAY);
		}
		oldSelection = -1;
		group.clearSelection();

	}

	@Override
	void enablePanel(boolean flag) {
		enableExtensionPanel(flag);
		if (mainFrame.version_panel.getVersion() < Constants.V2) {			
			date.setEnabled(false);
			datePicker.getComponent(1).setEnabled(false);
			for (int i = 0; i < 2; i++) {				
				labels[i].setEnabled(false);
				values[i].setEnabled(false);
				genders[i].setEnabled(false);
			}
			gender.setEnabled(false);
			resetPanel();
		} else {
			date.setEnabled(true);
			datePicker.getComponent(1).setEnabled(flag);
			for (int i = 0; i < 2; i++) {
				labels[i].setEnabled(true);
				values[i].setEnabled(flag);
				values[i].setDisabledTextColor(Color.BLACK);
				genders[i].setEnabled(flag);
			}
			gender.setEnabled(true);
		}

	}
	
	String getDateOfBirth() { 
		String date = "";
		if (dateModel.isSelected()) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			date = sdf.format(dateModel.getValue());
		}
		return date;
	}
	void setDateOfBirth(String v) {
		if (!v.isEmpty()) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			try {
				dateModel.setValue(sdf.parse(v));
			} catch (ParseException e) {
				GuiInterface.reportError("Invalid date of birth format.");
			}
		}
	}
	
	String getValue(int i) { return values[i].getText(); }
	void setValue(int i, String v) { values[i].setText(v); }
	String getGender() { 
		if (genders[Constants.M].isSelected()) return "M";
		else if (genders[Constants.F].isSelected()) return "F";
		else return "";
	}
	void setGender(String v) {
		switch (v) {
		case "M": genders[Constants.M].setSelected(true); break;
		case "F": genders[Constants.F].setSelected(true); break;
		}
	}

	@Override
	int getH() { return 210; }

	@Override
	void setY(int y) { setBounds(10, y, 510, 210); }

}
