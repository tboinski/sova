package org.pg.eti.kask.ont.editor.dialogs;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

import org.pg.eti.kask.ont.editor.consts.Constants;
import org.pg.eti.kask.ont.editor.table.model.Annotation;
import org.pg.eti.kask.ont.editor.util.EditorUtil;

import prefuse.util.FontLib;

public class AnnotationsDialog extends JDialog implements ActionListener {
	
	private static final long serialVersionUID = -4489234582731180059L;
	
	private ResourceBundle messages;
	
	private JComboBox annotationsComboBox;
	private JTextArea annotationValue;
	private JComboBox languageComboBox;
	private JButton okButton;
	private JButton cancelButton;

	private Annotation annotation;
	
	public AnnotationsDialog() {
		this.messages = EditorUtil.getResourceBundle(AnnotationsDialog.class);
	}

	private void initialize() {
		JLabel annotationsLabel = new JLabel();
		annotationsLabel.setText(messages.getString("annotationsDialog.annotationsLabel.text"));
		
		annotationsComboBox = new JComboBox();
		annotationsComboBox.addItem(Constants.RDFS_COMMENT);
		annotationsComboBox.addItem(Constants.RDFS_LABEL);
		
		JLabel valueLabel = new JLabel();
		valueLabel.setText(messages.getString("annotationsDialog.valueLabel.text"));
		
		annotationValue = new JTextArea();
		annotationValue.setWrapStyleWord(true);
		annotationValue.setLineWrap(true);
		annotationValue.setFont(FontLib.getFont("Arial", 12));
		
		JScrollPane annotationValueScrollPane = new JScrollPane(annotationValue);
		
		JLabel languageLabel = new JLabel();
		languageLabel.setText(messages.getString("annotationsDialog.languageLabel.text"));
		
		languageComboBox = new JComboBox();
		languageComboBox.addItem("");
		languageComboBox.addItem(Constants.LANGUAGE_POLISH);
		languageComboBox.addItem(Constants.LANGUAGE_ENGLISH);
		
		this.okButton = new JButton();
		okButton.setText(messages.getString("annotationsDialog.okButton.text"));
		okButton.addActionListener(this);
		
		this.cancelButton = new JButton();
		cancelButton.setText(messages.getString("annotationsDialog.cancelButton.text"));
		cancelButton.addActionListener(this);
		
		
		JPanel mainPanel = new JPanel();
		
		GroupLayout layout = new GroupLayout(mainPanel);
		mainPanel.setLayout(layout);
		
		layout.setAutoCreateContainerGaps(true);
		layout.setAutoCreateGaps(true);
		
		
		layout.setHorizontalGroup(layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup()
						.addComponent(annotationsLabel)
						.addComponent(languageLabel)
						.addComponent(valueLabel))
				.addGap(10)
				.addGroup(layout.createParallelGroup()
						.addComponent(annotationsComboBox)
						.addComponent(languageComboBox)
						.addComponent(annotationValueScrollPane)						
						.addGroup(layout.createSequentialGroup()
								.addComponent(okButton)
								.addGap(15)
								.addComponent(cancelButton))));
		
		layout.setVerticalGroup(layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(annotationsLabel)
						.addComponent(annotationsComboBox))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(languageLabel)
						.addComponent(languageComboBox))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(valueLabel)
						.addComponent(annotationValueScrollPane))
				.addGap(10)
				.addGroup(layout.createParallelGroup()
						.addComponent(okButton)
						.addComponent(cancelButton)));
		
		layout.linkSize(SwingConstants.HORIZONTAL, okButton, cancelButton);
		
		//zainicjalizowania okienka dialogowego
		this.add(mainPanel);		
		this.setTitle(messages.getString("annotationsDialog.title"));
		this.setSize(new Dimension(400, 250));
		this.setLocation(EditorUtil.getStartingPosition(this.getSize()));
		this.setModalityType(ModalityType.APPLICATION_MODAL);
		this.setVisible(true);
	}
	
	public void open() {
		initialize();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == okButton) {
			String annotationType = (String)annotationsComboBox.getSelectedItem();
			String lang = (String)languageComboBox.getSelectedItem();
			
			annotation = new Annotation();
			annotation.setProperty(annotationType);
			annotation.setLanguage(lang);
			annotation.setValue(annotationValue.getText());
		} else if(e.getSource() == cancelButton) {
		}
		
		this.setVisible(false);
	}

	public Annotation getAnnotation() {
		return annotation;
	}

}
