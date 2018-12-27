package yf.core;

import javax.annotation.PostConstruct;
import java.util.Enumeration;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PropertiesResolover {
    private static final Logger LOG = Logger.getLogger(PropertiesResolover.class.getName());

    private ResourceBundle resourceBundle;
    private Enumeration<String> keys;

    @PostConstruct
    public void initData() {
        try {
            setResourceBundle(ResourceBundle.getBundle("config",
                    Locale.getDefault()));
            keys = resourceBundle.getKeys();

        } catch (Exception e) {
            LOG.log(Level.SEVERE,
                    "Could not find message bundle");
        }
    }

    public String get(String key) {
        return ResourceBundle.getBundle("config",
                Locale.getDefault())
                .getString(key);
    }

    public ResourceBundle getResourceBundle() {
        return resourceBundle;
    }

    public void setResourceBundle(ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
    }

    public Enumeration<String> getKeys() {
        return keys;
    }

    public void setKeys(Enumeration<String> keys) {
        this.keys = keys;
    }

}
