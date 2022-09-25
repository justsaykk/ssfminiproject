package fullstack.vttpfullstackproj.models;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.oauth2.core.user.OAuth2User;
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

    public Boolean urlValidator(String url) {
        try {
            new URL(url).toURI();
            return true;
        } catch (Exception e) {
            return false;
        }
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
        String profilePicUrl = form.getFirst("profilePicUrl").toLowerCase();
        this.name = form.getFirst("name").toLowerCase();
        this.email = form.getFirst("email").toLowerCase();
        this.country = (form.getFirst("country").isEmpty()) ? "unknown" : form.getFirst("country").toLowerCase();
        this.profilePic = (urlValidator(profilePicUrl))
                ? profilePicUrl.toLowerCase()
                : "https://media.istockphoto.com/vectors/thumbnail-image-vector-graphic-vector-id1147544807?k=20&m=1147544807&s=612x612&w=0&h=pBhz1dkwsCMq37Udtp9sfxbjaMl27JUapoyYpQm0anc=";
    }

    public void setOAuthUser(OAuth2User user) {
        Boolean isGoogleAuthenticated = user.getAttributes().containsKey("at_hash");
        this.name = user.getAttribute("name");
        this.email = user.getAttribute("email");
        this.country = isGoogleAuthenticated ? "unknown" : user.getAttribute("location");
        this.profilePic = isGoogleAuthenticated ? user.getAttribute("picture") : user.getAttribute("avatar_url");
    }

    public void setOldUser(MultiValueMap<String, String> form) {
        this.name = form.getFirst("name").toLowerCase();
        this.email = form.getFirst("oldEmail").toLowerCase();
        this.country = form.getFirst("oldCountry").toLowerCase();
        this.profilePic = form.getFirst("oldProfilePicUrl").toLowerCase();
    }
}
