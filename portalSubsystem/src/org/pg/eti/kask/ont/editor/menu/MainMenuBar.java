package org.pg.eti.kask.ont.editor.menu;

import java.util.ResourceBundle;

import javax.swing.JMenuBar;

import org.pg.eti.kask.ont.editor.MainFrame;
import org.pg.eti.kask.ont.editor.util.EditorUtil;

/**
 * Klasa reprezentujaca pasek z menu wystepujacy w ramce.
 * Sklada sie z reuzywalnych komponentow plik, polecenia, opcje 
 * i pomoc.
 * 
 * @author Andrzej Jakowski
 */
public class MainMenuBar extends JMenuBar {

	private static final long serialVersionUID = 50529612023973067L;

	private ResourceBundle messages ;

	private MainFrame parentFrame;
	
	private FileMenu fileMenu;
	private CommandsMenu commandsMenu;
	private OptionsMenu optionsMenu;
	private HelpMenu helpMenu;

	public MainMenuBar(MainFrame parentFrame) {
		this.parentFrame = parentFrame;

		initialize();
	}

	private void initialize() {
		this.messages = EditorUtil.getResourceBundle(MainMenuBar.class);
		
		//file menu
		this.fileMenu = new FileMenu(parentFrame);
		fileMenu.setText(messages.getString("fileMenu.text"));

		//commands menu
		this.commandsMenu = new CommandsMenu(parentFrame);
		commandsMenu.setText(messages.getString("commandsMenu.text"));

		//commands menu
		this.optionsMenu = new OptionsMenu(parentFrame);
		optionsMenu.setText(messages.getString("optionsMenu.text"));

		//help menu
		this.helpMenu = new HelpMenu();
		helpMenu.setText(messages.getString("helpMenu.text"));

		this.add(fileMenu);
		this.add(commandsMenu);
		this.add(optionsMenu);
		this.add(helpMenu);

	}
	
	public void reintializeMenus() {
		this.commandsMenu.reinitializeMenu();
		this.fileMenu.reinitializeMenu();
	}

	/**
	 * 
	 */
	public MainFrame getParentFrame() {
		return parentFrame;
	}

	/**
	 * 
	 * @param parent
	 */
	public void setParentFrame(MainFrame parentFrame) {
		this.parentFrame = parentFrame;
	}

}
