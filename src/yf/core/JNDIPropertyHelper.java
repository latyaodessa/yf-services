package yf.core;

import static javax.naming.InitialContext.doLookup;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.NamingException;

public class JNDIPropertyHelper {

    private static final Logger LOG = Logger.getLogger(JNDIPropertyHelper.class.getName());

    public JNDIPropertyHelper() {
        super();
    }

    public <T> T lookup(final String jndiName) {
        try {
            return doLookup(jndiName);
        } catch (NamingException e) {
            LOG.log(Level.SEVERE,
                    "{0} JNDI resource is not configured",
                    jndiName);
            throw new RuntimeException(e);
        }
    }
}
