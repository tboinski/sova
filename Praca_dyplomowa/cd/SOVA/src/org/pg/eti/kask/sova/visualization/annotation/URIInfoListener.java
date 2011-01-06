package org.pg.eti.kask.sova.visualization.annotation;

import java.awt.event.MouseEvent;
import java.net.URI;

import org.pg.eti.kask.sova.graph.OWLtoGraphConverter;
import prefuse.controls.ControlAdapter;
import prefuse.visual.VisualItem;
/**
 * Klasa listenera ustawia URI wskazanego elementu w zadanym komponencie
 * @author Piotr Kunowski
 *
 */
public class URIInfoListener extends ControlAdapter {
	private URIInfoComponent uriInof;
	public URIInfoListener(URIInfoComponent component){
		this.uriInof = component;
	}
	
	/**
	 * Metoda po najechaniu na elemnet ustawia jego uri w uriInfo
	 */
	@Override
	public void itemMoved(VisualItem arg0, MouseEvent arg1) {
		Object o = arg0.get(OWLtoGraphConverter.COLUMN_URI);
		if (o==null){
			uriInof.setURIInfo("");
		}else{
			uriInof.setURIInfo(((URI)o).toString());
		}
	}
	@Override
	public void itemExited(VisualItem arg0, MouseEvent arg1) {
		uriInof.setURIInfo("");
	}

}
