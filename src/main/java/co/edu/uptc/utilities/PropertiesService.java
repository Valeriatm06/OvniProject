package co.edu.uptc.utilities;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertiesService {
    private final String filePath;
    private Properties properties;

    public PropertiesService(String filePath) {
        this.filePath = filePath;
        loadProperties();
    }

    public PropertiesService() {
        this("src/main/java/co/edu/uptc/resources/app.properties");
        loadProperties();
    }

    private void loadProperties() {
        properties = new Properties();
        try (FileInputStream fileInputStream = new FileInputStream(filePath)) {
            properties.load(fileInputStream);
        } catch (IOException e) {
            System.out.println("Error al leer propiedades: " + e.getMessage());
        }
    }

    public String getKeyValue(String key) {
        return properties.getProperty(key);
    }

    public int getIntValue(String key) {
        String value = properties.getProperty(key);
        try {
            return value != null ? Integer.parseInt(value) : 0;
        } catch (NumberFormatException e) {
            System.out.println("Error al convertir el valor de la propiedad a entero: " + e.getMessage());
            return 0;
        }
    }

    public void reloadProperties() {
        loadProperties();
    }
}
