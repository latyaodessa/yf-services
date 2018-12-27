package yf.user;

import yf.publication.entities.MdProfile;
import yf.publication.entities.PhProfile;
import yf.publication.entities.Publication;
import yf.publication.entities.PublicationUser;
import yf.submission.dtos.PhotoshootingParticipantTypeEnum;
import yf.user.entities.User;
import yf.user.entities.VKUser;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Date;

public class UserProfileWorkflow {

    @PersistenceContext
    private EntityManager em;

    public PhProfile registerNewPhProfile(final VKUser vkUser) {
        PhProfile phProfile = new PhProfile();
        phProfile.setCountry(vkUser.getCountry());
        phProfile.setVk(vkUser.getId());
        phProfile.setCreatedOn(new Date());
        phProfile.setUser(vkUser.getUser());
        em.persist(phProfile);
        em.flush();
        return phProfile;
    }

    public MdProfile registerNewMdProfile(final VKUser vkUser) {
        MdProfile mdProfile = new MdProfile();
        mdProfile.setCountry(vkUser.getCountry());
        mdProfile.setVk(vkUser.getId());
        mdProfile.setCreatedOn(new Date());
        mdProfile.setUser(vkUser.getUser());
        em.persist(mdProfile);
        em.flush();
        return mdProfile;
    }

    public PublicationUser generatePublicationUserFromPhProfile(final Publication publication,
                                                                final PhProfile phProfile) {
        PublicationUser publicationUser = new PublicationUser();
        publicationUser.setPublication(em.merge(publication));
        User user = phProfile.getUser();
        publicationUser.setUser(em.merge(user));
        publicationUser.setType(PhotoshootingParticipantTypeEnum.PH);
        em.persist(publicationUser);

        // em.flush();
        // phProfile.getPublicationUsers().add(em.merge(publicationUser));
        // publication.getPublicationUsers().add(em.merge(publicationUser));

        em.flush();

        return publicationUser;

    }

    public PublicationUser generatePublicationUserFromMdProfile(final Publication publication,
                                                                final MdProfile mdProfile) {
        PublicationUser publicationUser = new PublicationUser();
        publicationUser.setPublication(em.merge(publication));
        User user = mdProfile.getUser();
        publicationUser.setUser(em.merge(user));
        publicationUser.setType(PhotoshootingParticipantTypeEnum.MD);
        em.persist(publicationUser);
        // em.flush();
        //
        // mdProfile.getPublicationUsers().add(publicationUser);
        // publication.getPublicationUsers().add(publicationUser);

        // em.merge(mdProfile);
        // em.merge(publication);
        em.flush();

        return publicationUser;

    }
}
