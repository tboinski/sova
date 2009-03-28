package org.pg.eti.kask.ont.editor.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;

import org.pg.eti.kask.ont.editor.Logic;
import org.pg.eti.kask.ont.editor.Main;
import org.pg.eti.kask.ont.editor.MainFrame;
import org.pg.eti.kask.ont.editor.consts.CommandsConstans;
import org.pg.eti.kask.ont.editor.panels.MainPanel;
import org.pg.eti.kask.ont.editor.tree.model.ClassesTreeModel;
import org.pg.eti.kask.ont.editor.util.ApplicationConfiguration;
import org.pg.eti.kask.ont.editor.util.EditorUtil;

public class OptionsMenu extends JMenu implements ActionListener{

	private static final long serialVersionUID = 3548998813359660061L;
	
	private ResourceBundle messages;
	
	private MainFrame parentFrame;
	
	private JCheckBoxMenuItem polishMenuItem;
	private JCheckBoxMenuItem englishMenuItem;
	private JCheckBoxMenuItem showInferredHierarchyItem;
	
	
	public OptionsMenu(MainFrame parentFrame) {
		this.messages = EditorUtil.getResourceBundle(OptionsMenu.class);
		this.parentFrame = parentFrame;
		
		initialize();	
	}
	
	private void initialize() {
		//pobranie jezyka z aktualnej konfiguracji
		ApplicationConfiguration config = Logic.getInstance().getConfig();
		String lang = config.getLocaleLanguage();
		boolean showInferredHierarchy = config.isShowInferredHierarchy();
		
		JMenu languageMenu = new JMenu();
		languageMenu.setText(messages.getString("languageMenu.text"));
		
		polishMenuItem = new JCheckBoxMenuItem();
		polishMenuItem.setText(messages.getString("polishMenuItem.text"));
		polishMenuItem.setActionCommand(CommandsConstans.SET_POLISH_COMMAND);
		polishMenuItem.addActionListener(this);
		if(lang.equals("pl")) {
			polishMenuItem.setSelected(true);
		} else {
			polishMenuItem.setSelected(false);
		}
		
		englishMenuItem = new JCheckBoxMenuItem();
		englishMenuItem.setText(messages.getString("englishMenuItem.text"));
		englishMenuItem.setActionCommand(CommandsConstans.SET_ENGLISH_COMMAND);
		englishMenuItem.addActionListener(this);
		if(lang.equals("en")) {
			englishMenuItem.setSelected(true);
		} else {
			englishMenuItem.setSelected(false);
		}
		
		showInferredHierarchyItem = new JCheckBoxMenuItem();
		showInferredHierarchyItem.setText(messages.getString("showInferredHierarchyItem.text"));
		showInferredHierarchyItem.setActionCommand(CommandsConstans.SHOW_INFERRED_HIERARCHY);
		showInferredHierarchyItem.addActionListener(this);
		if(showInferredHierarchy) {
			showInferredHierarchyItem.setSelected(true);
		}
		
		languageMenu.add(polishMenuItem);
		languageMenu.add(englishMenuItem);
		
		this.add(languageMenu);	
		this.add(showInferredHierarchyItem);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String language = new String();
		Logic logic = Logic.getInstance();
		ApplicationConfiguration config = logic.getConfig();
		
		if(e.getActionCommand().equals(CommandsConstans.SET_POLISH_COMMAND)) {
			language = "pl";
			polishMenuItem.setSelected(true);
			englishMenuItem.setSelected(false);
			restartApplication(language);
		} else if(e.getActionCommand().equals(CommandsConstans.SET_ENGLISH_COMMAND)) {
			language = "en";
			polishMenuItem.setSelected(false);
			englishMenuItem.setSelected(true);
			restartApplication(language);
		} else if(e.getActionCommand().equals(CommandsConstans.SHOW_INFERRED_HIERARCHY)) {
						
			if(showInferredHierarchyItem.isSelected()) {
				String ontologyURI = logic.getLoadedOntologyURI();
				if(ontologyURI != null && !ontologyURI.equals("")) {
					ClassesTreeModel tree = new ClassesTreeModel(logic.getOntology(ontologyURI));
					parentFrame.getMainPanel().displayTree(tree);
					config.setShowInferredHierarchy(true);
				}
			} else {
				config.setShowInferredHierarchy(false);
				//usuniecie odpowiedniej zakladki
				parentFrame.getMainPanel().remove(MainPanel.TREE_TAB_INDEX);
			}
			
			//zapisanie konfiguracji do pliku
			config.saveToFile(Main.CONFIGURATION_FILE_PATH);
			
			
		}
		
		
	}
	
	private void restartApplication(String language) {
		Locale.setDefault(new Locale(language));
		ApplicationConfiguration config = Logic.getInstance().getConfig();
		
		//zapisanie do pliku nowego jezyka
		config.setLocaleLanguage(language);
		config.saveToFile(Main.CONFIGURATION_FILE_PATH);

		//wystartowanie aplikacji z nowymi localami
		parentFrame.setVisible(false);
		parentFrame.dispose();
		Main.startUp();
	}

}
