package yf.core;

import java.util.Enumeration;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;


public class PropertiesReslover {
    private static final Logger LOG = Logger.getLogger(PropertiesReslover.class.getName());

	private static ResourceBundle resourceBundle;
	private static  Enumeration<String> keys;
	
	public PropertiesReslover(){

	}
	@PostConstruct
	public void initData(){
		try {
			setResourceBundle(ResourceBundle.getBundle("config", Locale.getDefault()));
			keys = resourceBundle.getKeys();
 
		} catch (Exception e) {
            LOG.log(Level.SEVERE, "Could not find message bundle");
		}
	}
	
	
	public String get(String key){
		return ResourceBundle.getBundle("config", Locale.getDefault()).getString(key);
	}
	
	
	public static ResourceBundle getResourceBundle() {
		return resourceBundle;
	}
	public static void setResourceBundle(ResourceBundle resourceBundle) {
		PropertiesReslover.resourceBundle = resourceBundle;
	}
	public static Enumeration<String> getKeys() {
		return keys;
	}
	public static void setKeys(Enumeration<String> keys) {
		PropertiesReslover.keys = keys;
	}
	
	
}
