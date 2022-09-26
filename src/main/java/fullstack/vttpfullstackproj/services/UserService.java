package fullstack.vttpfullstackproj.services;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fullstack.vttpfullstackproj.models.User;
import fullstack.vttpfullstackproj.repository.ProfileRepo;
import fullstack.vttpfullstackproj.repository.UpdateRepo;
import fullstack.vttpfullstackproj.repository.UserRepo;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private UpdateRepo updateRepo;

    @Autowired
    private ProfileRepo profileRepo;

    public void createProfile(User user) {
        String name = user.getName();
        String email = user.getEmail();
        String country = user.getCountry();
        String profilePic = user.getProfilePic();

        Map<String, String> m = new HashMap<>();
        m.put("name", name);
        m.put("country", country);
        m.put("profilePic", profilePic);
        userRepo.registerEmail(email);
        userRepo.registerName(name);
        userRepo.updateProfileMapping(name, email);
        userRepo.createProfile(email, m);
    }

    public void editUserProfile(User oldUser, User editedUser) {
        // Email changes
        String name = editedUser.getName();
        String oldEmail = oldUser.getEmail();
        String formEmail = editedUser.getEmail();

        if (!oldEmail.equals(formEmail) && !userRepo.isRegisteredEmail(formEmail))
            updateRepo.updateEmail(name, oldEmail, formEmail);

        // Country changes
        String oldCountry = oldUser.getCountry();
        String formCountry = editedUser.getCountry();
        String currentEmail = userRepo.getEmailFromName(name);

        if (!oldCountry.equals(formCountry))
            updateRepo.updateCountry(currentEmail, formCountry);

        // ProfilePic changes
        String oldProfilePic = oldUser.getProfilePic();
        String formProfilePic = editedUser.getProfilePic();

        if (!oldProfilePic.equals(formProfilePic))
            updateRepo.updateProfilePic(currentEmail, formProfilePic);
    }

    public void deleteUser(User user) {
        if (userExists(user)) {
            userRepo.deregisterEmail(user.getEmail());
            userRepo.deregisterName(user.getName());
            userRepo.deleteProfileMapping(user.getName());
            profileRepo.deleteName(user.getName());
            userRepo.deleteEmail(user.getEmail());
        }
    }

    public Boolean userNameExists(String name) {
        System.out.println("Checking is userNameExists >> " + name);
        Boolean isMapped = userRepo.isMapped(name);
        Boolean isRegisteredName = userRepo.isRegisteredName(name);
        return (isMapped && isRegisteredName) ? true : false;
    }

    public Boolean userEmailExists(String email) {
        Boolean hasEmail = profileRepo.hasEmail(email);
        Boolean isRegisteredEmail = userRepo.isRegisteredEmail(email);
        return (isRegisteredEmail && hasEmail) ? true : false;
    }

    public Boolean userExists(User user) {
        String email = user.getEmail();
        String name = user.getName();
        return (userEmailExists(email) && userNameExists(name)) ? true : false;
    }

    public User getUser(String email) {
        return userRepo.getUser(email);
    }

    public Boolean isRegisteredName(String name) {
        return userRepo.isRegisteredName(name);
    }

    public Boolean isRegisteredEmail(String email) {
        return userRepo.isRegisteredEmail(email);
    }

    public String getEmailfromName(String name) {
        return userRepo.getEmailFromName(name);
    }

    public String getNamefromEmail(String email) {
        return userRepo.getNameFromEmail(email);
    }
}
