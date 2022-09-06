package fullstack.vttpfullstackproj.services;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fullstack.vttpfullstackproj.models.User;
import fullstack.vttpfullstackproj.repository.UpdateRepo;
import fullstack.vttpfullstackproj.repository.UserRepo;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private UpdateRepo updateRepo;

    public Boolean createProfile(User user) {
        String name = user.getName();
        String email = user.getEmail();
        String country = user.getCountry();
        String profilePic = user.getProfilePic();

        if (isRegisteredName(name)) {
            return false;
        } else {
            Map<String, String> m = new HashMap<>();
            m.put("name", name);
            m.put("country", country);
            m.put("profilePic", profilePic);
            userRepo.registerEmail(email);
            userRepo.registerName(name);
            userRepo.updateProfileMapping(name, email);
            userRepo.createProfile(email, m);
            return true;
        }

    }

    public void editUserProfile(User oldUser, User editedUser) {
        // Email changes
        String name = editedUser.getName();
        String oldEmail = oldUser.getEmail();
        String formEmail = editedUser.getEmail();

        if (!oldEmail.equals(formEmail))
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

    public User getUserDetails(String email) {
        return userRepo.getUserDetails(email);
    }

    public Boolean isRegisteredName(String name) {
        return userRepo.isRegisteredName(name);
    }

    public String getEmailfromName(String name) {
        return userRepo.getEmailFromName(name);
    }

}
