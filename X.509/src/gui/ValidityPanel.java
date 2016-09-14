package gui;

import java.awt.GridLayout;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;

import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import code.DataException;
import code.DateLabelFormatter;

@SuppressWarnings("serial")
public class ValidityPanel extends JPanel {
	
	public static final int FROM = 0, TO = 1;
	
	private JLabel labels[] = new JLabel [2];
	private DateLabelFormatter dateFormatter = new DateLabelFormatter();
	private Properties properties = new Properties();
	private UtilDateModel dateModel[] = new UtilDateModel [2];
	private JDatePanelImpl datePanel[] =  new JDatePanelImpl [2];
	private JDatePickerImpl datePicker[] = new JDatePickerImpl [2];

	ValidityPanel() {		
		setBounds(720, 140, 560, 60);
		setLayout(new GridLayout(1, 4, 5, 3));
		Border b = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
        setBorder(BorderFactory.createTitledBorder(b, "Certificate Validity"));
		
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
	
	void resetPanel() {
		dateModel[FROM].setValue(Calendar.getInstance().getTime());
		dateModel[TO].setDate(dateModel[FROM].getYear()+1, dateModel[FROM].getMonth(), dateModel[FROM].getDay());
	}
	
	void enablePanel(boolean flag) {
		//setEnabled(false);
		for (int i = 0; i < 2; i++) {
			//labels[i].setEnabled(flag);
			datePicker[i].getComponent(1).setEnabled(flag);
		}
	}
	
	void checkData() throws DataException {
		if (dateModel[TO].getValue() == null || dateModel[FROM].getValue() == null 
				|| dateModel[TO].getValue().before(dateModel[FROM].getValue()))
			throw new DataException("Invalid expiration date.");
	}
	
	// ********************************************************************************************************
	// 										GETTERS AND SETTERS
	// ********************************************************************************************************
	
	String getDateS(int i) throws ParseException {
		return dateFormatter.valueToString(dateModel[i].getValue());
	}
	
	Date getDate(int i) {
		return dateModel[i].getValue();
	}
	
	void setDate(int i, Date d) {
		dateModel[i].setValue(d);
	}
}
