package fullstack.vttpfullstackproj.models;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

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

    private String format(String s) {
        return s.toString().trim().replaceAll(" ", "").toLowerCase();
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
        Optional<String> profilePicUrl = Optional.ofNullable(form.getFirst("profilePicUrl"));
        Optional<String> country = Optional.ofNullable(form.getFirst("country"));
        this.name = format(form.getFirst("name"));
        this.email = format(form.getFirst("email"));
        this.country = (country.isEmpty()) ? "unknown" : format(country.get());
        this.profilePic = (urlValidator(profilePicUrl.get()))
                ? profilePicUrl.get()
                : "https://media.istockphoto.com/vectors/thumbnail-image-vector-graphic-vector-id1147544807?k=20&m=1147544807&s=612x612&w=0&h=pBhz1dkwsCMq37Udtp9sfxbjaMl27JUapoyYpQm0anc=";
    }

    public void setOAuthUser(OAuth2User user) {
        Boolean isGoogleAuthenticated = user.getAttributes().containsKey("at_hash");
        this.name = format(user.getAttribute("name"));
        this.email = format(user.getAttribute("email"));
        this.country = isGoogleAuthenticated
                ? "unknown"
                : format(user.getAttribute("location"));
        this.profilePic = isGoogleAuthenticated
                ? user.getAttribute("picture")
                : user.getAttribute("avatar_url");
        System.out.println("Profile Pic >> " + profilePic);
    }

    public void setOldUser(MultiValueMap<String, String> form) {
        this.name = format(form.getFirst("name"));
        this.email = format(form.getFirst("email"));
        this.country = format(form.getFirst("oldCountry"));
        this.profilePic = form.getFirst("oldProfilePicUrl");
    }
}
