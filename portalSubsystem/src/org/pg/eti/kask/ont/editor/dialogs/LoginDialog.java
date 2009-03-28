package org.pg.eti.kask.ont.editor.dialogs;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import org.pg.eti.kask.ont.editor.Logic;
import org.pg.eti.kask.ont.editor.Main;
import org.pg.eti.kask.ont.editor.consts.CommandsConstans;
import org.pg.eti.kask.ont.editor.util.ApplicationConfiguration;
import org.pg.eti.kask.ont.editor.util.EditorUtil;

/**
 * 
 *
 * @author Andrzej Jakowski
 */
public class LoginDialog extends JDialog implements ActionListener {

	private static final long serialVersionUID = -46924271780678403L;

	private ResourceBundle messages;
	
	private ApplicationConfiguration config;
	
	private JTextField loginTextField;
	private JPasswordField passwordField;
	private JCheckBox saveMeCheckBox;
	
	
	public LoginDialog() {
		this.messages = EditorUtil.getResourceBundle(LoginDialog.class);
		this.config = Logic.getInstance().getConfig();
	}
	
	private void initialize() {
		JLabel loginLabel = new JLabel(messages.getString("loginLabel.text"));

		boolean temp = false;
		if(config.getUserName() != null && !config.getUserName().equals("")) {
			temp = true;
		}
			
		loginTextField = new JTextField();
		if(temp) {			
			loginTextField.setText(config.getUserName());
		}
		
		JLabel passwordLabel = new JLabel(messages.getString("passwordLabel.text"));
		
		passwordField = new JPasswordField();
		if(temp) {			
			try {
				passwordField.setText(config.getPassword());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		saveMeCheckBox = new JCheckBox(messages.getString("saveMeCheckBox.text"));
		saveMeCheckBox.setSelected(true);
		
		JButton okButton = new JButton(messages.getString("okButton.text"));
		okButton.setActionCommand(CommandsConstans.OK_BUTTON);
		okButton.addActionListener(this);
		
		JButton cancelButton = new JButton(messages.getString("cancelButton.text"));
		cancelButton.setActionCommand(CommandsConstans.CANCEL_BUTTON);
		cancelButton.addActionListener(this);
		
		
		//konetener na podstawowe elementy okienka dialogowego
		JPanel mainPanel = new JPanel();
		
		//utworzenie zarzadcy ukladu
		GroupLayout layout = new GroupLayout(mainPanel);
		mainPanel.setLayout(layout);
		
		layout.setAutoCreateContainerGaps(true);
		layout.setAutoCreateGaps(true);
		
		//zlozenie layoutu
		layout.setHorizontalGroup(layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(loginLabel)
						.addComponent(passwordLabel))
				.addGap(50)
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(loginTextField)
						.addComponent(passwordField)						
						.addComponent(saveMeCheckBox)
						.addGroup(layout.createSequentialGroup()
								.addComponent(okButton)
								.addGap(30)
								.addComponent(cancelButton))));
		
		layout.setVerticalGroup(layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(loginLabel)
						.addComponent(loginTextField))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(passwordLabel)
						.addComponent(passwordField))
				.addComponent(saveMeCheckBox)
				.addGap(15)
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(okButton)
						.addComponent(cancelButton)));
		
		//przyciski ok i cancel maja ten sam rozmiar
		layout.linkSize(SwingConstants.HORIZONTAL, okButton, cancelButton);
		
		this.add(mainPanel);
		this.setTitle(messages.getString("loginDialog.title"));
		this.setSize(new Dimension(300,180));
		this.setLocation(EditorUtil.getStartingPosition(this.getSize()));
		this.setModalityType(ModalityType.APPLICATION_MODAL);
		this.setVisible(true);
	}
	
	public void open(){
		initialize();		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals(CommandsConstans.OK_BUTTON)) {
			String userName = loginTextField.getText();
			String password = new String(passwordField.getPassword());
			
			//jesli zaznaczony jest chceckbox zapisanie konfiguracji do pliku 
			if(saveMeCheckBox.isSelected()) {
				config.setUserName(userName);
				config.setPassword(password);
				
				config.saveToFile(Main.CONFIGURATION_FILE_PATH);
			}
			
			try {
				Logic logic = Logic.getInstance();
				logic.loginUser(userName, password);
			} catch (Exception ex) {	
				JOptionPane.showMessageDialog(this, ex.getMessage(), messages.getString("error.title"), JOptionPane.ERROR_MESSAGE);
			}
			
		} 
		this.setVisible(false);
	}


}
