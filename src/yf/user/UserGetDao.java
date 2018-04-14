package yf.user;

import yf.user.entities.FBUser;
import yf.user.entities.User;
import yf.user.entities.VKUser;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

public class UserGetDao {

    @PersistenceContext
    private EntityManager em;

    public User getUserById(final Long id) {
        return em.find(User.class, id);
    }

    public VKUser getVkUser(final Long socialId) {
        return em.find(VKUser.class, socialId);
    }

    public VKUser getVkUserByUserId(final Long userId) {
        TypedQuery<VKUser> query = em.createNamedQuery(VKUser.QUERY_GET_VK_USER_BY_USER_ID, VKUser.class)
                .setParameter("user_id", userId);
        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public FBUser getFbUser(final Long socialId) {
        return em.find(FBUser.class, socialId);
    }

    public FBUser getFbUserByUserId(final Long userId) {
        TypedQuery<FBUser> query = em.createNamedQuery(FBUser.QUERY_GET_VK_USER_BY_USER_ID, FBUser.class)
                .setParameter("user_id", userId);
        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }

    }
}
