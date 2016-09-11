package gui;

import java.awt.GridLayout;
import java.text.ParseException;
import java.util.Properties;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

@SuppressWarnings("serial")
public class ValidityPanel extends JPanel {
	
	public static final int FROM = 0, TO = 1;
	
	JLabel labels[] = new JLabel [2];
	DateLabelFormatter dateFormatter = new DateLabelFormatter();
	Properties properties = new Properties();
	UtilDateModel dateModel[] = new UtilDateModel [2];
	JDatePanelImpl datePanel[] =  new JDatePanelImpl [2];
	JDatePickerImpl datePicker[] = new JDatePickerImpl [2];

	public ValidityPanel() {		
		setBounds(720, 140, 550, 60);
		setLayout(new GridLayout(1, 4, 5, 3));
		setBorder(BorderFactory.createTitledBorder("Certificate Validity"));
		
		labels[FROM] = new JLabel("Not before: ");
		labels[TO] = new JLabel("Not after: ");
		
		for (int i = 0; i < 2; i++) {
			dateModel[i] = new UtilDateModel();
			datePanel[i] = new JDatePanelImpl(dateModel[i], properties);
			datePicker[i] =  new JDatePickerImpl (datePanel[i], dateFormatter);
			labels[i].setHorizontalAlignment(SwingConstants.RIGHT);
			add(labels[i]);
			add(datePicker[i]);
			dateModel[i].setSelected(true);
		}
		
		dateModel[TO].setDate(dateModel[FROM].getYear()+1, dateModel[FROM].getMonth(), dateModel[FROM].getDay());
		
		properties.put("text.today", "Today");
		properties.put("text.month", "Month");
		properties.put("text.year", "Year");
	}
	
	// ********************************************************************************************************
	// 												GETTERS
	// ********************************************************************************************************
	
	public String getDate(int i) throws ParseException {
		return dateFormatter.valueToString(dateModel[i].getValue());
	}
}
