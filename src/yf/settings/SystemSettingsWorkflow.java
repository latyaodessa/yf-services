package yf.settings;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

public class SystemSettingsWorkflow {

    @PersistenceContext
    private EntityManager em;

    public SystemSettings getSystemSettingByKey(final String key) {
        TypedQuery<SystemSettings> query = em.createNamedQuery(SystemSettings.QUERY_GET_SS_BY_KEY,
                SystemSettings.class)
                .setParameter("key",
                        key);
        try {
            return query.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }
}
