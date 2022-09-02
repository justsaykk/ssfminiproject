package fullstack.vttpfullstackproj.services;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fullstack.vttpfullstackproj.repository.Repo;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;

@Service
public class UserService {

    @Autowired
    private Repo repo;

    public JsonObject createProfile(String name, String email, String country, String profilePic) {
        Map<String, String> m = new HashMap<>();
        m.put("name", name);
        m.put("country", country);
        m.put("profilePic", profilePic);
        repo.registerProfile(email);
        repo.createProfile(email, m);

        // Creating return JsonObject
        JsonObjectBuilder innerJob = Json.createObjectBuilder()
                .add("name", name)
                .add("country", country)
                .add("profilePic", profilePic);

        return Json.createObjectBuilder()
                .add(email, innerJob)
                .build();
    }
}
