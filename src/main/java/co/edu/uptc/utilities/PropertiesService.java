package co.edu.uptc.utilities;
import java.io.FileInputStream;
import java.io.IOException;

import java.util.Properties;

public class PropertiesService {
   private String filePath="src/main/java/co/edu/uptc/resources/app.properties";

    public String getKeyValue(String Key) {
        Properties properties = new Properties();
        String value = "";
        try (FileInputStream fileInputStream = new FileInputStream(filePath)) {
            properties.load(fileInputStream);
            value=properties.getProperty(Key);
        } catch (IOException e) {
            System.out.println("Error al leer propiedades: " + e.getMessage());
        }
        return value;
    }   
}