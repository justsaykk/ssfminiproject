package fullstack.vttpfullstackproj.services;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fullstack.vttpfullstackproj.models.ExistingUser;
import fullstack.vttpfullstackproj.models.User;
import fullstack.vttpfullstackproj.repository.ProfileRepo;
import fullstack.vttpfullstackproj.repository.UserRepo;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ProfileRepo profileRepo;

    public void create(User user) {
        String email = user.getEmail();
        String name = user.getName();
        String uuid = UUID.randomUUID().toString();
        // Check if there is existing uuid
        while (userRepo.isRegisteredUUID(uuid)) {
            uuid = UUID.randomUUID().toString();
        }
        Map<String, String> m = new HashMap<>();
        m.put("name", name);
        m.put("country", user.getCountry());
        m.put("profilePic", user.getProfilePic());
        m.put("uuid", uuid);
        
        // Do Stuff
        userRepo.registerEmail(email);
        userRepo.registerUUID(uuid);
        userRepo.updateProfileMapping(uuid, email, name);
        userRepo.createProfile(email, m);
    }

    public void editUserProfile(User oldUser, User editedUser) {
        String email = oldUser.getEmail();

        // Country changes
        String oldCountry = oldUser.getCountry();
        String formCountry = editedUser.getCountry();

        if (!oldCountry.equals(formCountry))
            profileRepo.updateCountry(email, formCountry);

        // ProfilePic changes
        String oldProfilePic = oldUser.getProfilePic();
        String formProfilePic = editedUser.getProfilePic();

        if (!oldProfilePic.equals(formProfilePic))
            profileRepo.updateProfilePic(email, formProfilePic);
    }

    public void deleteUser(ExistingUser user) {
        if (userExists(user)) {
            profileRepo.deleteUUID(user.getUuid());
            userRepo.deregisterEmail(user.getEmail());
            userRepo.deregisterName(user.getName());
            userRepo.deleteProfileMapping(user.getName());
            userRepo.deleteUUIDMapping(user.getUuid());
            userRepo.deleteEmail(user.getEmail());
        }
    }

    public Boolean userExists(User user) {
        String email = user.getEmail();
        Boolean hasEmail = profileRepo.hasEmail(email);
        Boolean isRegisteredEmail = userRepo.isRegisteredEmail(email);
        return (isRegisteredEmail && hasEmail) ? true : false;
    }

    public ExistingUser getUser(String email) {
        return userRepo.getUser(email);
    }

    public String getEmailfromName(String name) {
        return userRepo.getEmailFromName(name);
    }

    public String getNamefromEmail(String email) {
        return userRepo.getNameFromEmail(email);
    }
}
