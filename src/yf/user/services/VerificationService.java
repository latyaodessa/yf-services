package yf.user.services;

import yf.user.VerificationDao;
import yf.user.dto.VerificationTypesEnum;
import yf.user.entities.User;
import yf.user.entities.Verifications;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Date;
import java.util.UUID;

public class VerificationService {

    @PersistenceContext
    private EntityManager em;

    @Inject
    private VerificationDao verificationDao;


    public Verifications createEmailVerification(final User userToRegister, final VerificationTypesEnum typesEnum) {
        Verifications verifications = new Verifications();
        verifications.setType(typesEnum);
        verifications.setUserId(userToRegister.getId());
        verifications.setVerification(UUID.randomUUID().toString());
        verifications.setVerified(false);
        verifications.setCreatedOn(new Date());
        em.persist(verifications);

        return verifications;
    }

    public Verifications validateVerification(final String verification) {
        final Verifications entity = verificationDao.getVerificationByVerification(verification);

        if (entity == null) {
            return null;
        }

        if (!entity.getVerified()) {
            entity.setVerified(true);
            em.persist(entity);
            return entity;
        }

        return null;
    }

}
