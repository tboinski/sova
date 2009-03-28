package org.pg.eti.kask.ont.editor.dialogs;

import java.awt.Dimension;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import org.pg.eti.kask.ont.editor.util.EditorUtil;

/**
 * 
 * 
 * @author Andrzej Jakowski
 */
public class ProgressDialog extends JDialog {

	private static final long serialVersionUID = -3131195392832181654L;
	
	//
	private String text;
	
	//
	private int leftRange;
	
	//
	private int rightRange;
	
	//
	private int progress;
	
	//
	private JProgressBar progessBar;
	
	//
	private JLabel label;
	
	public ProgressDialog(String text, int leftRange, int rightRange){
		this.text = text;
		this.leftRange = leftRange;
		this.rightRange = rightRange;
		
		
	}
	
	public void open(){
		initialize();		
	}
	
	private void initialize() {
		JPanel mainPanel = new JPanel();
		
		
		
		this.progessBar = new JProgressBar(leftRange, rightRange);
		this.label = new JLabel(text);
		
		mainPanel.add(label);
		
		mainPanel.add(progessBar);
		
		this.getContentPane().add(mainPanel);
		this.setTitle(text);
		this.setSize(new Dimension(200,100));
		this.setLocation(EditorUtil.getStartingPosition(this.getSize()));
		this.setModalityType(ModalityType.APPLICATION_MODAL);
		this.setVisible(true);
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public int getLeftRange() {
		return leftRange;
	}

	public void setLeftRange(int leftRange) {
		this.leftRange = leftRange;
	}

	public int getRightRange() {
		return rightRange;
	}

	public void setRightRange(int rightRange) {
		this.rightRange = rightRange;
	}



	public int getProgress() {
		return progress;
	}



	public void setProgress(int progress) {
		
		this.progessBar.setValue(progress);
		this.progress = progress;
		if(progress >= rightRange) {
			this.setVisible(false);
		}
	}

}
