package org.pg.eti.kask.ont.editor.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import org.pg.eti.kask.ont.editor.consts.CommandsConstans;
import org.pg.eti.kask.ont.editor.util.EditorUtil;

public class HelpMenu extends JMenu implements ActionListener{

	private static final long serialVersionUID = 7740158768762329246L;

	private ResourceBundle messages;
	
	public HelpMenu() {
		this.messages = EditorUtil.getResourceBundle(CommandsMenu.class);
		
		initialize();
	}
	
	private void initialize() {
		JMenuItem infoMenuItem = new JMenuItem();
		infoMenuItem.setText(messages.getString("infoMenuItem.text"));
		infoMenuItem.setActionCommand(CommandsConstans.INFO_COMMAND);
		infoMenuItem.addActionListener(this);
		
		this.add(infoMenuItem);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals(CommandsConstans.INFO_COMMAND)) {
			
		}
	}

}
