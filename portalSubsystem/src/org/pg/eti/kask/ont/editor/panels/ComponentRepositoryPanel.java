package org.pg.eti.kask.ont.editor.panels;

import java.awt.Dimension;
import java.util.ResourceBundle;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import org.pg.eti.kask.ont.editor.MainFrame;
import org.pg.eti.kask.ont.editor.tree.ClassesTree;
import org.pg.eti.kask.ont.editor.tree.IndividualsTree;
import org.pg.eti.kask.ont.editor.tree.PropertiesTree;
import org.pg.eti.kask.ont.editor.tree.model.OntologyClassesTreeModel;
import org.pg.eti.kask.ont.editor.tree.model.OntologyIndividualsTreeModel;
import org.pg.eti.kask.ont.editor.tree.model.OntologyPropertiesTreeModel;
import org.pg.eti.kask.ont.editor.util.EditorUtil;


/**
 * 
 * 
 * @author Andrzej Jakowski
 */
public class ComponentRepositoryPanel extends JTabbedPane {
	
	private static final long serialVersionUID = -1700766622497574219L;
	
	private ResourceBundle messages;
	
	//referencja do ramki glownej
	private MainFrame parentFrame;

	private ClassesTree classesTree;
	private IndividualsTree individualsTree;
	private PropertiesTree propertiesTree;
	private JPanel classesPanel;
	private JPanel propertiesPanel;
	private JPanel individualsPanel;
	
	public ComponentRepositoryPanel(MainFrame parentFrame) {
		this.messages = EditorUtil.getResourceBundle(ComponentRepositoryPanel.class);
		this.parentFrame = parentFrame;
		this.classesPanel = new JPanel();
		this.propertiesPanel = new JPanel();
		this.individualsPanel = new JPanel();		

		this.addTab(messages.getString("classesTab.title"), classesPanel);
		this.addTab(messages.getString("propertiesTab.title"), propertiesPanel);
		this.addTab(messages.getString("individualsTab.title"), individualsPanel);
	}

	/**
	 * 
	 * @param model
	 */
	public void displayOntologyClasses(OntologyClassesTreeModel model) {
		this.classesPanel.removeAll();
		this.classesTree = new ClassesTree(model, this);		
		this.classesPanel.setLayout(new BoxLayout(classesPanel, BoxLayout.Y_AXIS));
		
		JScrollPane scrollPane = new JScrollPane(this.classesTree);
		scrollPane.setPreferredSize(new Dimension(190, 200));
		scrollPane.setMinimumSize(new Dimension(190, 200));
		scrollPane.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));

		this.classesPanel.add(scrollPane);
	}
	
	

	/**
	 * 
	 * @param model
	 */
	public void displayOntologyProperties(OntologyPropertiesTreeModel model) {
		this.propertiesPanel.removeAll();
		this.propertiesTree = new PropertiesTree(model, this);	
		this.propertiesPanel.setLayout(new BoxLayout(propertiesPanel, BoxLayout.Y_AXIS));
		
		
		JScrollPane scrollPane = new JScrollPane(this.propertiesTree);
		scrollPane.setPreferredSize(new Dimension(190, 200));
		scrollPane.setMinimumSize(new Dimension(190, 200));
		scrollPane.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));

		this.propertiesPanel.add(scrollPane);

	}

	/**
	 * 
	 * @param model
	 */
	public void displayOntologyIndividuals(OntologyIndividualsTreeModel model) {
		
		this.individualsPanel.removeAll();
		this.individualsTree = new IndividualsTree(model, this);	
		this.individualsPanel.setLayout(new BoxLayout(individualsPanel, BoxLayout.Y_AXIS));
		
		JScrollPane scrollPane = new JScrollPane(this.individualsTree);
		scrollPane.setPreferredSize(new Dimension(190, 200));
		scrollPane.setMinimumSize(new Dimension(190, 200));
		scrollPane.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));

		this.individualsPanel.add(scrollPane);		
	}	

	public MainFrame getParentFrame() {
		return parentFrame;
	}

	public ClassesTree getClassesTree() {
		return classesTree;
	}

	public IndividualsTree getIndividualsTree() {
		return individualsTree;
	}
	
	public PropertiesTree getPropertiesTree() {
		return propertiesTree;
	}


}
