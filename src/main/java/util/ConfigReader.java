package util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {

	public static Properties properties;
	static {

		properties = new Properties();
		try {
			FileInputStream fis = new FileInputStream("src\\main\\java\\util\\config.properties");
			properties.load(fis);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static String getProperty(String key) {
		return properties.getProperty(key);

	}

}
