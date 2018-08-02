package yf.user;

import yf.publication.ProfileUserTypeEnum;
import yf.publication.entities.MdProfile;
import yf.publication.entities.PhProfile;
import yf.publication.entities.Publication;
import yf.publication.entities.PublicationUser;
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
        return phProfile;
    }

    public MdProfile registerNewMdProfile(final VKUser vkUser) {
        MdProfile mdProfile = new MdProfile();
        mdProfile.setCountry(vkUser.getCountry());
        mdProfile.setVk(vkUser.getId());
        mdProfile.setCreatedOn(new Date());
        mdProfile.setUser(vkUser.getUser());
        em.persist(mdProfile);
        return mdProfile;
    }

    public void addPublicationToPhProfile(final Publication publication, final PhProfile phProfile) {
        PublicationUser publicationUser = new PublicationUser();
        publicationUser.setPublication(publication);
        publicationUser.setUser(phProfile.getUser());
        publicationUser.setType(ProfileUserTypeEnum.PH);
        em.persist(publicationUser);

//        phProfile.getPublicationUsers().add(publicationUser);
//        publication.getPublicationUsers().add(publicationUser);
//
//        em.merge(phProfile);
//        em.merge(publication);

    }

    public void addPublicationToMdProfile(final Publication publication, final MdProfile mdProfile) {
        PublicationUser publicationUser = new PublicationUser();
        publicationUser.setPublication(publication);
        publicationUser.setUser(mdProfile.getUser());
        publicationUser.setType(ProfileUserTypeEnum.MD);
        em.persist(publicationUser);

//        mdProfile.getPublicationUsers().add(publicationUser);
//        publication.getPublicationUsers().add(publicationUser);
//
//        em.merge(mdProfile);
//        em.merge(publication);

    }
}
