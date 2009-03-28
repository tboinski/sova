package org.pg.eti.kask.ont.editor.wizard;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.WindowConstants;

import org.pg.eti.kask.ont.editor.util.EditorUtil;
import org.pg.eti.kask.ont.editor.wizard.page.AbstractPage;

/**
 * Klasa abstrakcyjna pozwalajaca na tworzenie wizardow.
 * Kazdy wizard sklada sie ze stron bedacych instanacjami klasy
 * <code>AbstractPage</code>.
 * 
 * @author Andrzej Jakowski
 */
public abstract class AbstractWizard extends JDialog {
	
	private ResourceBundle messages;
	
	//obecnie wyswietlana strona wizarda
	private AbstractPage currentPage;
	
	//panel zawierajacy przyciski 
	private JPanel buttonsPanel;
	
	//panel zaiwerajacy naglowek wizarda
	private JPanel headerPanel;
	
	//panel zawierajacy cialo wizarda
	private JPanel pagePanel;
	
	//przycisk pozwalajacy na anulowanie pracy wizarda
	private JButton cancelButton;
	
	//przycisk pozwalajacy na wykonanie wlasciwej akcji po zebraniu danych z wizarda 
	private JButton finishButton;
	
	//przycisk pozwalajacy na przejscie do poprzedniej storny wizarda
	private JButton prevButton;
	
	//przycisk pozwalajacy na przejscie do kolejnej strony wizarda
	private JButton nextButton;
	
	//okresla strona aktualnie zaznaczona
	private int actualSelectedPage;
	
	//przechowuje strony tego wizarda
	private AbstractPage[] pages;
	
	private int WIDTH = 500;
	
	private int HEIGHT = 500;
		
	/**
	 * Metoda otwierajaca okno wizarda.
	 */
	public void open() {
		this.messages = EditorUtil.getResourceBundle(AbstractWizard.class);
		currentPage = this.getStartPage();
		actualSelectedPage = 0;
		pages = this.getPages();
		this.doStart();
		initialize();
	}
	
	/**
	 * Metoda inicjalizujaca wszystkie komponenty wizarda.
	 */
	private void initialize() {
		//pobranie glownego kontenera dla dialoga i ustawienie managera layoutu
		Container dialogContainer = this.getContentPane();
		dialogContainer.setLayout(new BoxLayout(dialogContainer, BoxLayout.Y_AXIS));
		
		//page Panel
		pagePanel = new JPanel();		
		pagePanel.setLayout(new BorderLayout());
		pagePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		pagePanel.add(currentPage.getContainer(), BorderLayout.CENTER);
				
		//header panel
		headerPanel = new JPanel();
		headerPanel.setLayout(new BorderLayout());
		headerPanel.setBackground(Color.WHITE);
		headerPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		headerPanel.setMinimumSize(new Dimension(getWizardWidth(), 60));
		headerPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
		headerPanel.setPreferredSize(new Dimension(getWizardWidth(), 60));		
		JLabel titleLabel = new JLabel(this.getWizardTitle());		
		titleLabel.setFont(new Font("Times New Roman", Font.BOLD, 20));
		headerPanel.add(titleLabel, BorderLayout.PAGE_START);		
		JLabel descriptionLabel = new JLabel(this.getWizardDescription());		
		headerPanel.add(descriptionLabel, BorderLayout.CENTER);
		headerPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		//cancel button
		cancelButton = new JButton(messages.getString("cancelButtonLabel"));
		cancelButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				doCancel();
				setVisible(false);				
			}
			
		});
				
		//finish button
		finishButton = new JButton(messages.getString("finishButtonLabel"));
		finishButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {				
				for(AbstractPage page: pages) {
					if(!page.validate()) {
						return;
					}
				}
				doFinish();
				setVisible(false);
			}
			
		});
		
		//next button 
		nextButton = new JButton(messages.getString("nextButtonLabel"));
		nextButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if(currentPage.validate()) {
					if(canGoToNextPage()) {					
						currentPage = getNextPage();
						pagePanel.removeAll();
						pagePanel.add(currentPage.getContainer(), BorderLayout.CENTER);					
						pagePanel.validate();
						pagePanel.repaint();
	
						repaintButtonsPanel();
					}
				}
			}
			
		});
		
		//back button		
		prevButton = new  JButton(messages.getString("backButtonLabel"));
		prevButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				if(canGoToPreviousPage()) {
					currentPage = getPreviousPage();
					pagePanel.removeAll();
					pagePanel.add(currentPage.getContainer(), BorderLayout.CENTER);					
					pagePanel.validate();
					pagePanel.repaint();
					
					repaintButtonsPanel();
					
				}
			}
			
		});
		
		//buttons panel
		buttonsPanel = new JPanel();
		Box buttonsBox = new Box(BoxLayout.X_AXIS);		
		buttonsBox.add(prevButton);
		buttonsBox.add(nextButton);
		buttonsBox.add(Box.createHorizontalStrut(10));
		buttonsBox.add(finishButton);
		buttonsBox.add(Box.createHorizontalStrut(10));
		buttonsBox.add(cancelButton);
		buttonsPanel.add(buttonsBox);
		buttonsPanel.setPreferredSize(new Dimension(getWizardWidth(), 40));
		buttonsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
				
		//dodanie kolejnych elementow do glownego kontenera
		dialogContainer.add(Box.createVerticalStrut(5));
		dialogContainer.add(headerPanel);
		dialogContainer.add(Box.createVerticalStrut(10));
		dialogContainer.add(new JSeparator());
		dialogContainer.add(Box.createVerticalStrut(10));
		dialogContainer.add(pagePanel);
		dialogContainer.add(Box.createVerticalStrut(10));
		dialogContainer.add(new JSeparator());
		dialogContainer.add(Box.createVerticalStrut(10));
		dialogContainer.add(buttonsPanel);
			
		this.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
		this.setSize(getWizardWidth(), getWizardHeight());
		this.setTitle(this.getWizardTitle());
		this.setLocation(EditorUtil.getStartingPosition(this.getSize()));	
		this.setModalityType(ModalityType.APPLICATION_MODAL);
		repaintButtonsPanel();
		this.setVisible(true);
	}
	
	/**
	 * Metoda ustawiajaca (wlaczajaca/wylaczajaca) przyciski w panelu
	 * buttonsPanel.
	 * 
	 */
	private void repaintButtonsPanel() {
		if(this.canGoToNextPage()) {
			nextButton.setEnabled(true);
		} else {
			nextButton.setEnabled(false);
		}
		
		if(this.canGoToPreviousPage()) {
			prevButton.setEnabled(true);
		} else {
			prevButton.setEnabled(false);
		}
		
	}
	
	/**
	 * Metoda wywolywana w momencie startu wizarda.
	 */
	protected abstract void doStart();
	
	/**
	 * Metoda wywolywana w momencie zatwierdzenia wizarda.
	 */
	protected abstract void doFinish();
	
	/**
	 * Metoda wywolywana w momencie anulowania dzialania wizarda..
	 */
	protected abstract void doCancel();

	/**
	 * Metoda zwracajaca strone startowa wizarda.
	 * 
	 * @return
	 */
	protected abstract AbstractPage getStartPage();

	/**
	 * Zwraca pole currentPage - okreslajace, ktora strona wizarda jest obecenie przetwarzana.
	 * 
	 * @return instancja klasy AbstractPage.
	 */
	public AbstractPage getCurrentPage() {
		return currentPage;
	}

	/**
	 * Ustawia pole currentPage - okreslajace, ktora strona wizarda jest obecnie przetwarzana.
	 * 
	 * @param currentPage instancja klasy AbstractPage.
	 */
	public void setCurrentPage(AbstractPage currentPage) {
		this.currentPage = currentPage;
	}
	
	/**
	 * Metoda abstrakcyjna zwracajaca nazwe danego wizarda.
	 * 
	 * @return nazwa danego wizarda, zostanie umieszczona w naglowku wizarda.
	 */
	protected abstract String getWizardTitle();
	
	/**
	 * Metoda abstrakcyjna zwracajaca opis funkcjonalnosci wizarda. 
	 * Zostanie on umieszczony w naglowku wizarda. 
	 * 
	 * @return opis w postaci instancji klasy String
	 */	
	protected abstract String getWizardDescription();
	
	/**
	 * Metoda zwracajaca wszystkie strony wizarda. 
	 * 
	 * @return tablica z instancjami stron wizarda
	 */
	protected abstract AbstractPage[] getPages();
	
	/**
	 * Metoda wywolywana w momencie przejscia do kolejengo kroku w wizardzie.
	 * 
	 * @return instancja klasy AbstractPage jesli dana strona wizarda ma nastepna strone, 
	 * null w przeciwnym wypadku
	 */
	protected AbstractPage getNextPage() {
		if(actualSelectedPage >=0 && actualSelectedPage < pages.length-1) {
			actualSelectedPage++;
			return pages[actualSelectedPage];
		} else {
			return null;
		}
	}
	
	/**
	 * Metoda wywolywana w momencie przejscia wstecz w wizardzie.
	 * 
	 * @return instancja klasy AbstractPage jesli dana strona wizarda ma strone poprzedzajaca, 
	 * null w przeciwnym wypadku
	 */
	protected AbstractPage getPreviousPage() {
		if(actualSelectedPage >0 && actualSelectedPage <= pages.length) {
			actualSelectedPage--;
			return pages[actualSelectedPage];
		} else {
			return null;
		}
	}
	
	/**
	 * Metoda pozwalajaca okreslic czy istnieje przejscie z danej
	 * strony wizarda do poprzedniej.
	 * 
	 * @return true jesli poprzednia strona istnieje, false w przeciwnym wypadku
	 */
	protected boolean canGoToPreviousPage() {
		if(actualSelectedPage >0 && actualSelectedPage <= pages.length) 
			return true;
		else
			return false;
	}
	
	/**
	 * Metoda pozwalajaca okreslic czy istnieje przejscie z danej
	 * strony wizarda do nastepnej.
	 * 
	 * @return true jesli nastepna strona istnieje, false w przeciwnym wypadku
	 */
	protected boolean canGoToNextPage() {
		if(actualSelectedPage >=0 && actualSelectedPage < pages.length-1) 
			return true;
		else 
			return false;
	}
	
	public int getWizardWidth() {
		return WIDTH;
	}
	
	public int getWizardHeight() {
		return HEIGHT;
	}
}
