package yf.user;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import yf.publication.entities.MdProfile;
import yf.publication.entities.PhProfile;

public class ProfilesDao {

    @PersistenceContext
    private EntityManager em;

    public PhProfile getPhProfileById(final Long id) {
        return em.find(PhProfile.class,
                id);
    }

    public MdProfile getMdProfileById(final Long id) {
        return em.find(MdProfile.class,
                id);
    }

    public PhProfile getPhProfileByUserId(final Long userId) {

        TypedQuery<PhProfile> query = em.createNamedQuery(PhProfile.QUERY_GET_PG_PROFILE_BY_USER_ID,
                PhProfile.class)
                .setParameter("user_id",
                        userId);
        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public MdProfile getMdProfileByUserId(final Long userId) {
        TypedQuery<MdProfile> query = em.createNamedQuery(MdProfile.QUERY_GET_MD_PROFILE_BY_USER_ID,
                MdProfile.class)
                .setParameter("user_id",
                        userId);
        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

}
