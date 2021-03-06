/*
 *
 * Copyright (c) 2010 Gdańsk University of Technology
 * Copyright (c) 2010 Kunowski Piotr
 * Copyright (c) 2010 Jaworska Anna
 * Copyright (c) 2010 Kleczkowski Radosław
 * Copyright (c) 2010 Orłowski Piotr
 * Copyright (c) 2016 Wojciech Zielonka
 *
 * This file is part of SOVA.  SOVA is free software: you can
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
package org.pg.eti.kask.sova.visualization.annotation;

import javax.swing.JComboBox;

/**
 * Interfejs zawiera metody, które musi posiadać formatka wyświetlająca
 * informacje o zaznaczonym wierzchołku
 *
 * @author Piotr Kunowski
 *
 */
public interface AnnotationComponent {

    /**
     * ustawienie nazwy elementu
     *
     * @param name nazwa elementu
     */
    public void setNameText(String name);

    /**
     * ustawienie etykiety
     *
     * @param label
     */
    public void setLabelText(String label);

    /**
     * ustawienie opisu elementu
     *
     * @param coment
     */
    public void setCommentText(String coment);

    /**
     * getter ComboBoxa dla komentarzy
     *
     * @return 
     */
    public JComboBox getCommentLang();

    /**
     * getter ComboBoxa dla labelów
     *
     * @return 
     */
    public JComboBox getLabelLang();
}
