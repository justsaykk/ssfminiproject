package fullstack.vttpfullstackproj.services;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fullstack.vttpfullstackproj.repository.UpdateRepo;
import fullstack.vttpfullstackproj.repository.UserRepo;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private UpdateRepo updateRepo;

    public JsonObject createProfile(String name, String email, String country, String profilePic) {
        Map<String, String> m = new HashMap<>();
        m.put("name", name);
        m.put("country", country);
        m.put("profilePic", profilePic);
        userRepo.registerEmail(email);
        userRepo.registerName(name);
        userRepo.updateProfileMapping(name, email);
        userRepo.createProfile(email, m);

        // Creating return JsonObject
        JsonObjectBuilder innerJob = Json.createObjectBuilder()
                .add("name", name)
                .add("country", country)
                .add("profilePic", profilePic);

        return Json.createObjectBuilder()
                .add(email, innerJob)
                .build();
    }

    public void editUserProfile(String name, Map<String, String> m) {
        // Email changes
        String oldEmail = m.get("oldEmail");
        String formEmail = m.get("formEmail");

        if (!oldEmail.equals(formEmail))
            updateRepo.updateEmail(name, oldEmail, formEmail);

        // Country changes
        String oldCountry = m.get("oldCountry");
        String formCountry = m.get("formCountry");
        String currentEmail = userRepo.getEmailFromName(name);

        if (!oldCountry.equals(formCountry))
            updateRepo.updateCountry(currentEmail, formCountry);

        // ProfilePic changes
        String oldProfilePic = m.get("oldProfilePic");
        String formProfilePic = m.get("formProfilePic");

        if (!oldProfilePic.equals(formProfilePic))
            updateRepo.updateProfilePic(currentEmail, formProfilePic);
    }

    public Map<String, String> getProfileDetails(String email) {
        return userRepo.getProfileDetails(email);
    }

    public Boolean isRegistered(String name) {
        return userRepo.isRegisteredName(name);
    }

    public String getEmailfromName(String name) {
        return userRepo.getEmailFromName(name);
    }

}
