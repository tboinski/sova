/*
 *
 * Copyright (c) 2010 Gdańsk University of Technology
 * Copyright (c) 2010 Kunowski Piotr
 * Copyright (c) 2010 Jaworska Anna
 * Copyright (c) 2010 Kleczkowski Radosław
 * Copyright (c) 2010 Orłowski Piotr
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


package org.pg.eti.kask.sova.utils;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Klasa odpowiada za wczytywanie ustawień kolorów dla węzłów oraz krawędzi
 * z wybranego lub domyślnego.
 * @author Kunos
 */
public class VisualizationProperties {
        private Properties properties = new Properties();
        private static  VisualizationProperties instance = null;
        private static final String DEFAULT_PROPERTIES_PATH = "";

        private VisualizationProperties() {		
        }
        
        public static VisualizationProperties instanceOf(){
          if(instance == null) instance = new VisualizationProperties();
          return instance;
        }
	
        public void loadProperties(){
           this.loadProperties(DEFAULT_PROPERTIES_PATH);
        }

	/**
	 * Ładuje plik z ustawieniami kolorów z podanej ścieżki.
	 * @param file ścieżka do pliku ze schematem kolorów
	 */
        public void loadProperties(String file){
            
                InputStream is;
                try {
                        is = new FileInputStream(new File(file));
                        //ładujemy nasze ustawienia
                        properties.load(is);
                } catch (FileNotFoundException e) {
			Debug.sendMessage("Nie odnaleziono pliku z ustawieniami kolorów: "
				+ file);
                        e.printStackTrace();
                } catch (IOException e) {
			Debug.sendMessage("Błąd podczas wczytywania pliku: "
				+ file);
                        e.printStackTrace();
                }
                
        }
        
        /**
         * Pobranie wartości dla zadanego klucza
         * @param key wartość szukanego klucza
         * @param defaultValue domyślna wartość, ustawiana w przypadku nie znalezienia klucza w pliku
         * @return
         */
        public String getProperty(String key, String defaultValue)
        {
            return properties.getProperty(key, defaultValue);
        }
        
                /**
         * Pobranie koloru dla zadanego klucza z pliku konfiguracyjnego. Wartości koloru powinny
         * być zapisane w postaci #XXXXXX, gdzie X - liczba hex
         * @param key wartość szukanego klucza
         * @param defaultValue domyślna wartość, ustawiana w przypadku nie znalezienia klucza w pliku
         * @return
         */
        public Color getPropertyColor(String key, Color defaultColor)
        {
            Color retColor=null;
            String color = properties.getProperty(key);
            if (null==color){
               retColor = defaultColor;
	       Debug.sendMessage("No color found => using default color for " + key);
            }else{
		Debug.sendMessage("color found for " + key);
              retColor =  new Color(  Integer.decode(color).intValue());
            }

            return retColor;
        }

    
}
