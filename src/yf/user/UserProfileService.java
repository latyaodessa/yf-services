package yf.user;

import yf.publication.entities.MdProfile;
import yf.publication.entities.PhProfile;
import yf.publication.entities.Publication;
import yf.publication.entities.PublicationUser;
import yf.user.dto.external.VKUserDTO;
import yf.user.entities.User;
import yf.user.entities.VKUser;

import javax.inject.Inject;

public class UserProfileService {

    @Inject
    private UserDao userDao;
    @Inject
    private UserWorkflow userWorkflow;
    @Inject
    private ProfilesDao profilesDao;
    @Inject
    private UserProfileWorkflow userProfileWorkflow;

    public PhProfile getOrRegisterPhProfileUser(final VKUserDTO vkUserDTO) {
        final VKUser vkUser = getOrRegisterVkUser(vkUserDTO);
        return getPhProfile(vkUser);

    }


    public MdProfile getOrRegisterMdProfileUser(final VKUserDTO vkUserDTO) {
        final VKUser vkUser = getOrRegisterVkUser(vkUserDTO);
        return getMdProfile(vkUser);


    }

    private VKUser getOrRegisterVkUser(final VKUserDTO vkUserDTO) {
        final VKUser existingVkUser = userDao.getVkUser(vkUserDTO.getId());

        if (existingVkUser == null) {
            User user = userWorkflow.registerAnonymousUser();
            final VKUser vkUserEntity = userWorkflow.createVKUser(vkUserDTO, user);
            userWorkflow.handleEmptyUserFields(user, vkUserEntity);
            return vkUserEntity;
        }

        return existingVkUser;

    }

    private PhProfile getPhProfile(final VKUser vkUser) {
        final PhProfile phProfile = profilesDao.getPhProfileByUserId(vkUser.getUser().getId());
        if (phProfile == null) {
            return userProfileWorkflow.registerNewPhProfile(vkUser);
        }
        return phProfile;
    }

    private MdProfile getMdProfile(final VKUser vkUser) {
        final MdProfile mdProfile = profilesDao.getMdProfileByUserId(vkUser.getUser().getId());
        if (mdProfile == null) {
            return userProfileWorkflow.registerNewMdProfile(vkUser);
        }
        return mdProfile;
    }

    public PublicationUser generatePublicationUserFromPhProfile(final Publication publication, final PhProfile phProfile) {
        return userProfileWorkflow.generatePublicationUserFromPhProfile(publication, phProfile);
    }

    public PublicationUser generatePublicationUserFromMdProfile(final Publication publication, final MdProfile mdProfile) {
        return userProfileWorkflow.generatePublicationUserFromMdProfile(publication, mdProfile);
    }
}
