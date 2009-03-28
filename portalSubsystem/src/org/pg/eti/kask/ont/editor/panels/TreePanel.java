package org.pg.eti.kask.ont.editor.panels;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.util.ResourceBundle;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JEditorPane;
import javax.swing.JPanel;

import org.pg.eti.kask.ont.editor.consts.Constants;
import org.pg.eti.kask.ont.editor.tree.TreeDisplay;
import org.pg.eti.kask.ont.editor.util.EditorUtil;

import prefuse.data.Tree;
import prefuse.util.ColorLib;
import prefuse.util.FontLib;
import prefuse.util.ui.JFastLabel;

public class TreePanel extends JPanel {

	private static final long serialVersionUID = -7679306843622251728L;

	private ResourceBundle messages;

	private TreeDisplay display;
	private JEditorPane legendPane;
	private JFastLabel uriLabel;

	public TreePanel(Tree tree) {
		this.messages = EditorUtil.getResourceBundle(TreePanel.class);
		this.display = new TreeDisplay(this, tree);

		initializeLegendPanel();
		initialize();
	}

	private void initialize() {
		JPanel leftPanel = new JPanel();
		JPanel rightPanel = new JPanel();
		
		//najpierw prawy panel
		rightPanel.add(legendPane);
		rightPanel.setPreferredSize(new Dimension(200, Integer.MAX_VALUE));
		rightPanel.setMaximumSize(new Dimension(200, Integer.MAX_VALUE));
		rightPanel.setMinimumSize(new Dimension(200, Integer.MAX_VALUE));
		rightPanel.setBackground(Constants.VIS_COLOR_BACKGROUND);
		rightPanel.setBorder(BorderFactory.createLineBorder(new Color(ColorLib.gray(200),true)));
		rightPanel.setAlignmentY(Component.TOP_ALIGNMENT);

		uriLabel = new JFastLabel("");
		uriLabel.setPreferredSize(new Dimension(1000, 20));
		uriLabel.setMaximumSize(new Dimension(1000, 20));
		uriLabel.setBorder(BorderFactory.createMatteBorder(1, 1, 0, 0, Constants.VIS_COLOR_BORDER));
		uriLabel.setFont(FontLib.getFont("Tahoma", Font.PLAIN, 12));
		uriLabel.setBackground(Constants.VIS_COLOR_BACKGROUND);
		uriLabel.setForeground(Constants.VIS_COLOR_FOREGROUND);
		
		//pozniej lewy panel
		leftPanel.add(display);
		leftPanel.add(uriLabel);
		leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
		leftPanel.setAlignmentY(Component.TOP_ALIGNMENT);		

		this.setBackground(Constants.VIS_COLOR_BACKGROUND);
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		this.add(leftPanel);
		this.add(rightPanel);
	}

	public void initializeLegendPanel() {
		String content = "<html><body>"
				+ "<table style=\"font-size: 8px; font-family: Tahoma \">"
				+ "<tr><td bgcolor=\""
				+ Integer.toHexString(Constants.NODE_COLOR_CLASS & 0x00ffffff)
				+ "\" width=\"20px\"></td><td> - "
				+ messages.getString("legendPanel.owlClassItem.text")
				+ "</td></tr>"
				+ "<tr><td bgcolor=\""
				+ Integer.toHexString(Constants.NODE_COLOR_INDIVIDUAL & 0x00ffffff)
				+ "\"></td><td> - "
				+ messages.getString("legendPanel.owlIndividualItem.text")
				+ "</td></tr>"
				+ "<tr><td bgcolor=\""
				+ Integer.toHexString(Constants.NODE_COLOR_SELECTED & 0x00ffffff)
				+ "\"></td><td> - "
				+ messages.getString("legendPanel.selectedNodeItem.text")
				+ "</td></tr>"
				+ "<tr><td bgcolor=\""
				+ Integer.toHexString(Constants.NODE_COLOR_HIGHLIGHTED & 0x00ffffff)
				+ "\"></td><td> - "
				+ messages.getString("legendPanel.pathToSelectedNodeItem.text")
				+ "</td></tr>" + "</table>" + "</body></html>";

		legendPane = new JEditorPane("text/html", content);
		legendPane.setEditable(false);
		legendPane.setBorder(BorderFactory.createTitledBorder(messages.getString("legendPanel.title.text")));
		legendPane.setMinimumSize(new Dimension(195, 105));
		legendPane.setPreferredSize(new Dimension(195, 105));
		legendPane.setMaximumSize(new Dimension(195, 105));
	}

	public void updateUriLabel(String text) {
		uriLabel.setText(text);
	}

}
