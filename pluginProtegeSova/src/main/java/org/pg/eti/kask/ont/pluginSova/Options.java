/*
 *
 * Copyright (c) 2010 Kunowski Piotr
 *
 * This file is part of OCS.  OCS is free software: you can
 * redistribute it and/or modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation, version 3.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with
 * this program; If not, see <http://www.gnu.org/licenses/>.
 *
 */

package org.pg.eti.kask.ont.pluginSova;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.pg.eti.kask.sova.visualization.FilterOptions;
import org.pg.eti.kask.sova.visualization.OVDisplay;

import prefuse.util.ui.JValueSlider;

public class Options extends JFrame {

	private static final String CHECKBOX_SUBCLASS_COMMAND = "checkbox_subclass";
	private static final String CHECKBOX_CLASS_COMMAND = "checkbox_class";
	private static final String CHECKBOX_DISJOINT_CLASS_COMMAND = "checkbox_disjoint_class";
	private static final String CHECKBOX_EQUIVALENT_CLASS_COMMAND = "checkbox_equvalent_class";
	private static final String CHECKBOX_CARDINALITY_COMMAND = "checkbox_cardinality_class";
	private static final String CHECKBOX_UNIONOF_COMMAND = "checkbox_unionof_class";
	private static final String CHECKBOX_COMPLEMENT_COMMAND = "checkbox_complement_class";
	private static final String CHECKBOX_INTERSECTION_COMMAND = "checkbox_intersection_class";
	private static final String CHECKBOX_INDYVIDUAL_COMMAND = "checkbox_indywidual_node";
	private static final String CHECKBOX_INSTANCEOF_COMMAND = "checkbox_instanceof";
	private static final String CHECKBOX_DIFFERENT_COMMAND = "checkbox_different";
	private static final String CHECKBOX_SAMEAS_COMMAND = "chechbox_sameas";
	private static final String CHECKBOX_ONEOF_COMMAND = "checkbox_oneof";
	private static final String CHECKBOX_PROPERTY_COMMAND = "checkbox_property";
	private static final String CHECKBOX_SUBPROPERTY_COMMAND = "checkbox_subproperty_edge";
	private static final String CHECKBOX_EQUIVALENT_COMMAND = "checkbox_equivalent_property";
	private static final String CHECKBOX_INVERSEOFPROPERTY_COMMAND = "checkbox_inverseof_property";
	private static final String CHECKBOX_FUNCTIONALPROPERTY_COMMAND = "checkbox_functionalproperty";
	private static final String CHECKBOX_INVERSFUNCTIONALPROPERTY_COMMAND = "checkbox_inversfunctionalproperty";
	private static final String CHECKBOX_SYMMETRICPROPERTY_COMMAND = "checkbox_symetricproperty";
	private static final String CHECKBOX_TRANSITIVEPROPERTY_COMMAND = "checkbox_transitiveproperty";
	private static final String CHECKBOX_INSTANCEPROPERTY_COMMAND = "checkbox_instanceproperty";
	private static final String CHECKBOX_DOMAIN_COMMAND = "checkbox_domain";
	private static final String CHECKBOX_RANGE_COMMAND = "checkbox_drange";
	private static final int DEFAULT_WIDTH = 300;
	private static final int DEFAULT_HEIGHT = 600;
	private JButton exitButt;
	private OVDisplay display;

	private JButton options = null;
	private JCheckBox chClass = null, chSubClass = null, chDisjointEdge = null,
			chCardinalityNode = null, chUnionOf = null, chIntersecionOf = null,
			chComplementOf = null, chEquivalent = null;
	private JCheckBox chIndywidual = null, chInstanceOf = null,
			chDifferent = null, chsameas = null, choneof = null;
	private JCheckBox chproperty = null, chInstanceProperty = null,
			chInverseOfProperty = null, chDomain = null, chRange = null,
			chSubProperty = null, chEquivalentProperty = null,
			chFunctionalProperty = null, chInversFunctionalProperty = null,
			chSymmetricProperty = null, chTransitiveProperty = null;
	private JPanel visValues = null;
	private boolean isOptionFrameShow = false;

	public synchronized boolean isOptionFrameShow() {
		return isOptionFrameShow;
	}

	public synchronized void setOptionFrameShow(boolean isOptionFrameShow) {
		this.isOptionFrameShow = isOptionFrameShow;
	}

	public Options(OVDisplay display) {
		setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
		setResizable(false);
		setUndecorated(true);
		this.display = display;
		if (visValues == null) {
			initVisValuesPanel();
		}
		this.add(visValues);
		exitButt = new JButton("Exit");
		exitButt.setSize(80, 40);
		exitButt.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				setVisible(false);
				isOptionFrameShow = false;
			}
		});
		this.add(exitButt, BorderLayout.SOUTH);
	}

	private void initVisValuesPanel() {
		visValues = new JPanel();
		visValues.setLayout(new BoxLayout(visValues, BoxLayout.Y_AXIS));

		final JValueSlider slider = new JValueSlider("Distance", 1, 15,
				FilterOptions.getDistance());
		slider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				display.getVisualization().setDistance(
						slider.getValue().intValue());
				display.getVisualization().refreshFilter();
			}
		});
		slider.setBackground(Color.WHITE);
		Box cf = new Box(BoxLayout.Y_AXIS);
		cf.setSize(new Dimension(300, 70));
		cf.add(slider);
		cf.setBorder(BorderFactory.createTitledBorder("Connectivity Filter"));
		JPanel panelDist = new JPanel();
		panelDist.add(cf);

		visValues.add(panelDist);

		JPanel buttonPanel = new JPanel(new GridLayout(2, 1));
		buttonPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory
				.createEtchedBorder(), "Visualization types"));
		// buttonPanel.setSize(new Dimension(300, 80));
		JRadioButton forceDirectedRadial = new JRadioButton(
				"ForceDirectedLayout", true);
		forceDirectedRadial.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				display
						.changeVisualizationLayout(OVDisplay.FORCE_DIRECTED_LAYOUT);

			}
		});
		JRadioButton radialTreeRadial = new JRadioButton("RadialTreeLayout",
				false);
		radialTreeRadial.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				display.changeVisualizationLayout(OVDisplay.RADIAL_TREE_LAYOUT);

			}
		});
		ButtonGroup radialGroup = new ButtonGroup();
		radialGroup.add(forceDirectedRadial);
		radialGroup.add(radialTreeRadial);
		buttonPanel.add(forceDirectedRadial);

		buttonPanel.add(radialTreeRadial);
		visValues.add(buttonPanel);

		CheckBoxListener checkboxListener = new CheckBoxListener();
		JPanel checkboxPanel = new JPanel(new GridLayout(15, 2));
		checkboxPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory
				.createEtchedBorder(), "Filter"));

		chClass = new JCheckBox("Class");
		chClass.setActionCommand(CHECKBOX_CLASS_COMMAND);
		chClass.setSelected(true);
		chClass.addActionListener(checkboxListener);
		checkboxPanel.add(chClass);

		chSubClass = new JCheckBox("SubClass edge");
		chSubClass.setActionCommand(CHECKBOX_SUBCLASS_COMMAND);
		chSubClass.setSelected(true);
		chSubClass.addActionListener(checkboxListener);
		checkboxPanel.add(chSubClass);

		chDisjointEdge = new JCheckBox("DisjointWith edge");
		chDisjointEdge.setActionCommand(CHECKBOX_DISJOINT_CLASS_COMMAND);
		chDisjointEdge.addActionListener(checkboxListener);
		chDisjointEdge.setSelected(true);
		checkboxPanel.add(chDisjointEdge);

		chEquivalent = new JCheckBox("EquivalentClass edge");
		chEquivalent.setActionCommand(CHECKBOX_EQUIVALENT_CLASS_COMMAND);
		chEquivalent.addActionListener(checkboxListener);
		chEquivalent.setSelected(true);
		checkboxPanel.add(chEquivalent);

		chCardinalityNode = new JCheckBox("Cardinality");
		chCardinalityNode.setActionCommand(CHECKBOX_CARDINALITY_COMMAND);
		chCardinalityNode.addActionListener(checkboxListener);
		chCardinalityNode.setSelected(true);
		checkboxPanel.add(chCardinalityNode);

		chUnionOf = new JCheckBox("UnionOf");
		chUnionOf.setActionCommand(CHECKBOX_UNIONOF_COMMAND);
		chUnionOf.addActionListener(checkboxListener);
		chUnionOf.setSelected(true);
		checkboxPanel.add(chUnionOf);

		chIntersecionOf = new JCheckBox("IntersecionOf");
		chIntersecionOf.setActionCommand(CHECKBOX_INTERSECTION_COMMAND);
		chIntersecionOf.addActionListener(checkboxListener);
		chIntersecionOf.setSelected(true);
		checkboxPanel.add(chIntersecionOf);

		chComplementOf = new JCheckBox("ComplementOf");
		chComplementOf.setActionCommand(CHECKBOX_COMPLEMENT_COMMAND);
		chComplementOf.addActionListener(checkboxListener);
		chComplementOf.setSelected(true);
		checkboxPanel.add(chComplementOf);

		chIndywidual = new JCheckBox("Individual");
		chIndywidual.setActionCommand(CHECKBOX_INDYVIDUAL_COMMAND);
		chIndywidual.addActionListener(checkboxListener);
		chIndywidual.setSelected(true);
		checkboxPanel.add(chIndywidual);

		chInstanceOf = new JCheckBox("InstanceOf edge");
		chInstanceOf.setActionCommand(CHECKBOX_INSTANCEOF_COMMAND);
		chInstanceOf.addActionListener(checkboxListener);
		chInstanceOf.setSelected(true);
		checkboxPanel.add(chInstanceOf);

		chDifferent = new JCheckBox("Different");
		chDifferent.setActionCommand(CHECKBOX_DIFFERENT_COMMAND);
		chDifferent.addActionListener(checkboxListener);
		chDifferent.setSelected(true);
		checkboxPanel.add(chDifferent);

		chsameas = new JCheckBox("SameAs");
		chsameas.setActionCommand(CHECKBOX_SAMEAS_COMMAND);
		chsameas.addActionListener(checkboxListener);
		chsameas.setSelected(true);
		checkboxPanel.add(chsameas);

		choneof = new JCheckBox("OneOf");
		choneof.setActionCommand(CHECKBOX_ONEOF_COMMAND);
		choneof.addActionListener(checkboxListener);
		choneof.setSelected(true);
		checkboxPanel.add(choneof);
		visValues.add(checkboxPanel);

		// property

		chproperty = new JCheckBox("Property");
		chproperty.setActionCommand(CHECKBOX_PROPERTY_COMMAND);
		chproperty.addActionListener(checkboxListener);
		chproperty.setSelected(true);
		checkboxPanel.add(chproperty);
		visValues.add(checkboxPanel);

		chSubProperty = new JCheckBox("SubProperty edge");
		chSubProperty.setActionCommand(CHECKBOX_SUBPROPERTY_COMMAND);
		chSubProperty.addActionListener(checkboxListener);
		chSubProperty.setSelected(true);
		checkboxPanel.add(chSubProperty);
		visValues.add(checkboxPanel);

		chEquivalentProperty = new JCheckBox("EquivalentProperty edge");
		chEquivalentProperty.setActionCommand(CHECKBOX_EQUIVALENT_COMMAND);
		chEquivalentProperty.addActionListener(checkboxListener);
		chEquivalentProperty.setSelected(true);
		checkboxPanel.add(chEquivalentProperty);
		visValues.add(checkboxPanel);

		chFunctionalProperty = new JCheckBox("FunctionalProperty");
		chFunctionalProperty
				.setActionCommand(CHECKBOX_FUNCTIONALPROPERTY_COMMAND);
		chFunctionalProperty.addActionListener(checkboxListener);
		chFunctionalProperty.setSelected(true);
		checkboxPanel.add(chFunctionalProperty);
		visValues.add(checkboxPanel);

		chInversFunctionalProperty = new JCheckBox("InversFunctionalProperty");
		chInversFunctionalProperty
				.setActionCommand(CHECKBOX_INVERSFUNCTIONALPROPERTY_COMMAND);
		chInversFunctionalProperty.addActionListener(checkboxListener);
		chInversFunctionalProperty.setSelected(true);
		checkboxPanel.add(chInversFunctionalProperty);
		visValues.add(checkboxPanel);

		chSymmetricProperty = new JCheckBox("SymmetricProperty");
		chSymmetricProperty
				.setActionCommand(CHECKBOX_SYMMETRICPROPERTY_COMMAND);
		chSymmetricProperty.addActionListener(checkboxListener);
		chSymmetricProperty.setSelected(true);
		checkboxPanel.add(chSymmetricProperty);
		visValues.add(checkboxPanel);

		chTransitiveProperty = new JCheckBox("TransitiveProperty");
		chTransitiveProperty
				.setActionCommand(CHECKBOX_TRANSITIVEPROPERTY_COMMAND);
		chTransitiveProperty.addActionListener(checkboxListener);
		chTransitiveProperty.setSelected(true);
		checkboxPanel.add(chTransitiveProperty);
		visValues.add(checkboxPanel);

		chInverseOfProperty = new JCheckBox("InversOf");
		chInverseOfProperty
				.setActionCommand(CHECKBOX_INVERSEOFPROPERTY_COMMAND);
		chInverseOfProperty.addActionListener(checkboxListener);
		chInverseOfProperty.setSelected(true);
		checkboxPanel.add(chInverseOfProperty);
		visValues.add(checkboxPanel);

		chInstanceProperty = new JCheckBox("InstanceProperty Edge");
		chInstanceProperty.setActionCommand(CHECKBOX_INSTANCEPROPERTY_COMMAND);
		chInstanceProperty.addActionListener(checkboxListener);
		chInstanceProperty.setSelected(true);
		checkboxPanel.add(chInstanceProperty);
		visValues.add(checkboxPanel);

		chDomain = new JCheckBox("Domain");
		chDomain.setActionCommand(CHECKBOX_DOMAIN_COMMAND);
		chDomain.addActionListener(checkboxListener);
		chDomain.setSelected(true);
		checkboxPanel.add(chDomain);
		visValues.add(checkboxPanel);

		chRange = new JCheckBox("Range");
		chRange.setActionCommand(CHECKBOX_RANGE_COMMAND);
		chRange.addActionListener(checkboxListener);
		chRange.setSelected(true);
		checkboxPanel.add(chRange);
		visValues.add(checkboxPanel);

	}

	private class CheckBoxListener implements ActionListener {

	
		public void actionPerformed(ActionEvent e) {

			if (e.getActionCommand().equals(CHECKBOX_CLASS_COMMAND)) {
				if (chClass.isSelected()) {
					FilterOptions.setClassFilter(true);
					chSubClass.setEnabled(true);
					chDisjointEdge.setEnabled(true);
					chUnionOf.setEnabled(true);
					chIntersecionOf.setEnabled(true);
					chCardinalityNode.setEnabled(true);
					chComplementOf.setEnabled(true);
					chEquivalent.setEnabled(true);

				} else {
					FilterOptions.setClassFilter(false);
					chSubClass.setEnabled(false);
					chDisjointEdge.setEnabled(false);
					chUnionOf.setEnabled(false);
					chIntersecionOf.setEnabled(false);
					chCardinalityNode.setEnabled(false);
					chComplementOf.setEnabled(false);
					chEquivalent.setEnabled(false);
				}
			} else if (e.getActionCommand().equals(CHECKBOX_SUBCLASS_COMMAND)) {
				if (chSubClass.isSelected()) {
					FilterOptions.setSubClassEdge(true);
				} else {
					FilterOptions.setSubClassEdge(false);
				}
			} else if (e.getActionCommand().equals(
					CHECKBOX_DISJOINT_CLASS_COMMAND)) {
				if (chDisjointEdge.isSelected()) {
					FilterOptions.setDisjointClassEdge(true);
				} else {
					FilterOptions.setDisjointClassEdge(false);
				}
			} else if (e.getActionCommand().equals(CHECKBOX_UNIONOF_COMMAND)) {
				if (chUnionOf.isSelected()) {
					FilterOptions.setUnionOf(true);
				} else {
					FilterOptions.setUnionOf(false);
				}
			} else if (e.getActionCommand().equals(
					CHECKBOX_INTERSECTION_COMMAND)) {
				if (chIntersecionOf.isSelected()) {
					FilterOptions.setIntersectionOf(true);
				} else {
					FilterOptions.setIntersectionOf(false);
				}
			} else if (e.getActionCommand().equals(
					CHECKBOX_EQUIVALENT_CLASS_COMMAND)) {
				if (chEquivalent.isSelected()) {
					FilterOptions.setEquivalentClassEdge(true);
				} else {
					FilterOptions.setEquivalentClassEdge(false);
				}
			} else if (e.getActionCommand().equals(CHECKBOX_COMPLEMENT_COMMAND)) {
				if (chComplementOf.isSelected()) {
					FilterOptions.setComplementOf(true);
				} else {
					FilterOptions.setComplementOf(false);
				}
			} else if (e.getActionCommand()
					.equals(CHECKBOX_CARDINALITY_COMMAND)) {
				if (chCardinalityNode.isSelected()) {
					FilterOptions.setCardinality(true);
				} else {
					FilterOptions.setCardinality(false);
				}
			} else if (e.getActionCommand().equals(CHECKBOX_INDYVIDUAL_COMMAND)) {
				if (chIndywidual.isSelected()) {
					FilterOptions.setIndividual(true);
					chInstanceOf.setEnabled(true);
					chDifferent.setEnabled(true);
					chsameas.setEnabled(true);
					choneof.setEnabled(true);
				} else {
					FilterOptions.setIndividual(false);
					chInstanceOf.setEnabled(false);
					chDifferent.setEnabled(false);
					chsameas.setEnabled(false);
					choneof.setEnabled(false);
				}
			} else if (e.getActionCommand().equals(CHECKBOX_INSTANCEOF_COMMAND)) {
				if (chInstanceOf.isSelected()) {
					FilterOptions.setInstanceOfEdge(true);
				} else {
					FilterOptions.setInstanceOfEdge(false);
				}
			} else if (e.getActionCommand().equals(CHECKBOX_DIFFERENT_COMMAND)) {
				if (chDifferent.isSelected()) {
					FilterOptions.setDifferent(true);
				} else {
					FilterOptions.setDifferent(false);
				}
			} else if (e.getActionCommand().equals(CHECKBOX_SAMEAS_COMMAND)) {
				if (chsameas.isSelected()) {
					FilterOptions.setSameAs(true);
				} else {
					FilterOptions.setSameAs(false);
				}
			} else if (e.getActionCommand().equals(CHECKBOX_ONEOF_COMMAND)) {
				if (choneof.isSelected()) {
					FilterOptions.setOneOf(true);
				} else {
					FilterOptions.setOneOf(false);

				}
			} else // property
			if (e.getActionCommand().equals(CHECKBOX_PROPERTY_COMMAND)) {
				if (chproperty.isSelected()) {
					FilterOptions.setProperty(true);
					chSubProperty.setEnabled(true);
					chEquivalentProperty.setEnabled(true);
					chFunctionalProperty.setEnabled(true);
					chInversFunctionalProperty.setEnabled(true);
					chSymmetricProperty.setEnabled(true);
					chTransitiveProperty.setEnabled(true);
					chInstanceProperty.setEnabled(true);
					chDomain.setEnabled(true);
					chRange.setEnabled(true);
					chInverseOfProperty.setEnabled(true);

				} else {
					FilterOptions.setProperty(false);
					chSubProperty.setEnabled(false);
					chEquivalentProperty.setEnabled(false);
					chFunctionalProperty.setEnabled(false);
					chInversFunctionalProperty.setEnabled(false);
					chSymmetricProperty.setEnabled(false);
					chTransitiveProperty.setEnabled(false);
					chInstanceProperty.setEnabled(false);
					chDomain.setEnabled(false);
					chRange.setEnabled(false);
					chInverseOfProperty.setEnabled(false);
				}
			} else if (e.getActionCommand()
					.equals(CHECKBOX_SUBPROPERTY_COMMAND)) {
				if (chSubProperty.isSelected()) {
					FilterOptions.setSubPropertyEdge(true);
				} else {
					FilterOptions.setSubPropertyEdge(false);
				}
			} else if (e.getActionCommand().equals(CHECKBOX_EQUIVALENT_COMMAND)) {
				if (chEquivalentProperty.isSelected()) {
					FilterOptions.setEquivalentPropertyEdge(true);
				} else {
					FilterOptions.setEquivalentPropertyEdge(false);
				}
			} else if (e.getActionCommand().equals(
					CHECKBOX_INVERSEOFPROPERTY_COMMAND)) {
				if (chInverseOfProperty.isSelected()) {
					FilterOptions.setInverseOfProperty(true);
				} else {
					FilterOptions.setInverseOfProperty(false);
				}
			} else if (e.getActionCommand().equals(
					CHECKBOX_FUNCTIONALPROPERTY_COMMAND)) {
				if (chFunctionalProperty.isSelected()) {
					FilterOptions.setFunctionalProperty(true);
				} else {
					FilterOptions.setFunctionalProperty(false);
				}

			} else if (e.getActionCommand().equals(
					CHECKBOX_INVERSFUNCTIONALPROPERTY_COMMAND)) {
				if (chInversFunctionalProperty.isSelected()) {
					FilterOptions.setInverseFunctionalProperty(true);
				} else {
					FilterOptions.setInverseFunctionalProperty(false);
				}
			} else if (e.getActionCommand().equals(
					CHECKBOX_SYMMETRICPROPERTY_COMMAND)) {
				if (chSymmetricProperty.isSelected()) {
					FilterOptions.setSymmetricProperty(true);
				} else {
					FilterOptions.setSymmetricProperty(false);
				}
			} else if (e.getActionCommand().equals(
					CHECKBOX_TRANSITIVEPROPERTY_COMMAND)) {
				if (chTransitiveProperty.isSelected()) {
					FilterOptions.setTransitiveProperty(true);
				} else {
					FilterOptions.setTransitiveProperty(false);
				}
			} else if (e.getActionCommand().equals(
					CHECKBOX_INSTANCEPROPERTY_COMMAND)) {
				if (chInstanceProperty.isSelected()) {
					FilterOptions.setInstanceProperty(true);
				} else {
					FilterOptions.setInstanceProperty(false);
				}
			} else if (e.getActionCommand().equals(CHECKBOX_DOMAIN_COMMAND)) {
				if (chDomain.isSelected()) {
					FilterOptions.setDomain(true);
				} else {
					FilterOptions.setDomain(false);
				}
			} else if (e.getActionCommand().equals(CHECKBOX_RANGE_COMMAND)) {
				if (chRange.isSelected()) {
					FilterOptions.setRange(true);
				} else {
					FilterOptions.setRange(false);
				}
			}
			display.getVisualization().refreshFilter();
		}
	}
}