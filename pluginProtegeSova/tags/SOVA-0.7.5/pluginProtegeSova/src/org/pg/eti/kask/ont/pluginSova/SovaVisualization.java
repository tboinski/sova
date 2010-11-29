package org.pg.eti.kask.ont.pluginSova;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

import org.pg.eti.kask.sova.visualization.OVDisplay;
import org.protege.editor.owl.ui.view.AbstractOWLViewComponent;



public class SovaVisualization extends AbstractOWLViewComponent {

	private static final long serialVersionUID = -4515710047558710080L;
    private  OVDisplay display;

    public  boolean doLayout = true;
    private JButton options = null;
    private Options optionFrame=null;
    private boolean isOptionFrameShow = false;
    private  JPanel leftPanel = null, rightPanel = null;
    private AnnotationPanel annotation;
	private URITextField uriInfo = null;
    @Override
    protected void disposeOWLView() {
    	display.removeDisplayVis();
    	
    }

    
    {
    	this.setLayout(new BorderLayout());
    }
    @Override
    protected void initialiseOWLView() throws Exception {
    	if (display == null){
    		display = new OVDisplay(getOWLModelManager().getActiveOntology());
    		annotation = new AnnotationPanel();
    		display.addAnnotationComponent(annotation);
    		uriInfo = new URITextField();
    		display.addURIInfoComponent(uriInfo);
    		display.setSize(800, 600);
        }
		display.setOntology(getOWLModelManager().getActiveOntology());
    	display.generateGraphFromOWl();
//        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        
        if (leftPanel == null){
        	initLeftPanel();
        	this.add(leftPanel,BorderLayout.CENTER);
        }
        if (rightPanel==null){
        	initRightPanel();
        	this.add(rightPanel, BorderLayout.EAST);
        }
    }

	private void initRightPanel() {
		rightPanel = new JPanel();
		rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
		ImagePanel icon = new ImagePanel(new ImageIcon(getClass().getResource(
				"/img/SOVA.png")).getImage());
		rightPanel.add(icon);
		JPanel buttonPanel = new JPanel(new GridLayout(4, 1));
		JButton but = new JButton("Play/Stop");
		but.setToolTipText("Play or stop animation");
		but.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (doLayout) {
					display.getVisualization().stopLayout();
					doLayout = false;
				} else {
					display.getVisualization().startLayout();
					doLayout = true;
				}
			}
		});
		but.setSize(100, 80);
		buttonPanel.add(but);
		JButton but2 = new JButton("Reset");
		but2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				display.removeDisplayVis();
				display.generateGraphFromOWl(getOWLModelManager()
						.getActiveOntology());
				doLayout = true;
			}
		});
		but2.setSize(100, 80);
		but2.setToolTipText("Reload ontology");
		buttonPanel.add(but2);

		options = new JButton("Options");
		options.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {			
				if (optionFrame == null) {
					initOptionFrame();
				}
				if (!optionFrame.isOptionFrameShow()) {
					optionFrame.setVisible(true);
					optionFrame.setOptionFrameShow(true);
				} else {
					optionFrame.setVisible(false);
					optionFrame.setOptionFrameShow(false);
				}
				
			}
		});
		options.setSize(100, 80);
		buttonPanel.add(options);
		JButton saveImage = new JButton("Save Image");
		saveImage.addActionListener(new ActionListener() {

		
			public void actionPerformed(ActionEvent e) {
				File f = new File("");
			    FileDialog fd = new FileDialog(new Frame(), "Save",    FileDialog.SAVE);
			    fd.setFilenameFilter(new FilenameFilter() {
				
					public boolean accept(File dir, String name) {
						if (name.toUpperCase().endsWith(".PNG") ||
								name.toUpperCase().endsWith(".JPG")){
							return true;
						}
						return false;
					}
				});
			    fd.setLocation(50, 50);
			    fd.setVisible(true);
			    if (fd.getDirectory()==null || fd.getFile()==null )
			    	return;
			    String sFile = fd.getDirectory()+fd.getFile();
			    
				String format = "png";
				
				if (sFile.toUpperCase().endsWith(".PNG") ||
						sFile.toUpperCase().endsWith(".JPG")){
					format = sFile.substring(sFile.length()-3, sFile.length());
				}else{
					sFile += '.'+format; 
				}
				
				File file = new File(sFile) ;
				
				FileOutputStream os;
				try {

					os = new FileOutputStream(file);
					display.saveImage(os, format.toUpperCase(), 5);
					os.close();
					//zapis do pliku
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				}

			}
		});
		buttonPanel.add(saveImage);
		rightPanel.add(buttonPanel);
		rightPanel.add(annotation);
		rightPanel.setPreferredSize(new Dimension(180, Integer.MAX_VALUE));
		rightPanel.setMaximumSize(new Dimension(190, Integer.MAX_VALUE));
		rightPanel.setMinimumSize(new Dimension(170, Integer.MAX_VALUE));
	}
    private void initLeftPanel(){
        leftPanel = new JPanel();
        leftPanel.setLayout(new BorderLayout());
		leftPanel.add(display,BorderLayout.CENTER);
//		display.setFocusable(true);
//		leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
		JPanel stopka = new JPanel();
		stopka.setLayout(new BoxLayout(stopka, BoxLayout.X_AXIS));
		stopka.setSize(Integer.MAX_VALUE, 20);
		stopka.setMaximumSize(new Dimension(Integer.MAX_VALUE, 20));
		stopka.setMinimumSize(new Dimension(Integer.MAX_VALUE, 20));
		stopka.setBackground(Color.WHITE);
		stopka.add(uriInfo);
		stopka.add(display.getSearchPanel());
		leftPanel.add(stopka, BorderLayout.SOUTH);
    }
    private void initOptionFrame(){
		optionFrame = new Options(display);
		Point location = options.getLocationOnScreen();
		location.x -= optionFrame.getSize().width;
		location.y -= 250;
		optionFrame.setLocation(location);
    }
  
}
