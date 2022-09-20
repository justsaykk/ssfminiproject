package fullstack.vttpfullstackproj.models;

import java.util.HashMap;
import java.util.Map;

import org.springframework.util.MultiValueMap;

import jakarta.json.Json;
import jakarta.json.JsonObject;

public class User {
    private String profilePic;
    private String name;
    private String email;
    private String country;

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public JsonObject toJsonObject() {
        return Json.createObjectBuilder()
                .add(this.email, Json.createObjectBuilder()
                        .add("name", this.name)
                        .add("country", this.country)
                        .add("profilePic", this.profilePic))
                .build();
    }

    public Map<String, String> toMap() {
        Map<String, String> m = new HashMap<>();
        m.put("email", this.email);
        m.put("name", this.name);
        m.put("country", this.country);
        m.put("profilePic", this.profilePic);
        return m;
    }

    public void setUser(MultiValueMap<String, String> form) {
        this.name = form.getFirst("name").toLowerCase();
        this.email = form.getFirst("email").toLowerCase();
        this.country = form.containsKey("country") ? form.getFirst("country").toLowerCase() : "unknown";
        this.profilePic = form.getFirst("profilePicUrl").toLowerCase();
    }

    public void setOldUser(MultiValueMap<String, String> form) {
        this.name = form.getFirst("name").toLowerCase();
        this.email = form.getFirst("oldEmail").toLowerCase();
        this.country = form.getFirst("oldCountry").toLowerCase();
        this.profilePic = form.getFirst("oldProfilePicUrl").toLowerCase();
    }
}
