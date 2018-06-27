package yf.user;

import org.joda.time.DateTime;
import yf.user.entities.Verifications;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.Date;
import java.util.logging.Logger;

public class VerificationDao {

    private static final Logger LOG = Logger.getLogger(VerificationDao.class.getName());

    @PersistenceContext
    private EntityManager em;


    public Verifications getVerificationByVerification(final String verification) {

        final Date yersterday = new DateTime(new Date()).minusDays(1).toDate();

        TypedQuery<Verifications> query = em.createNamedQuery(Verifications.QUERY_GET_VER_BY_VERFICATION, Verifications.class)
                .setParameter("verification", verification)
                .setParameter("yersterday", yersterday);
        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            LOG.warning("VERIFICATION TEMPLATE: " + e);
            return null;
        }
    }
}
