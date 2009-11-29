/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.eti.kask.sova.utils;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 *
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
        public void loadProperties(String file){
            
                InputStream is;
                try {
                        is = new FileInputStream(new File(file));
                        //ładujemy nasze ustawienia
                        properties.load(is);
                } catch (FileNotFoundException e) {
                        e.printStackTrace();
                } catch (IOException e) {
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
            }else{
              retColor =  new Color(  Integer.decode(color).intValue());
            }

            return retColor;
        }

    
}
