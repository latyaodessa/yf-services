package yf.elastic.core;

import javax.naming.NamingException;
import java.util.logging.Level;
import java.util.logging.Logger;

import static javax.naming.InitialContext.doLookup;

public final class PropertyLookupHelper {
    private static final Logger LOG = Logger.getLogger(PropertyLookupHelper.class.getName());

    public PropertyLookupHelper() {
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
