package fullstack.vttpfullstackproj.models;

import java.util.Map;

import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;

import jakarta.json.Json;
import jakarta.json.JsonObject;

public class User {
    private String given_name;
    private String email;
    private String profilePic;

    public String getGivenName() {
        return given_name;
    }

    public void setGivenName(String given_name) {
        this.given_name = given_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public User toUserObj(JsonObject jo) {
        User user = new User();
        user.setGivenName(jo.getString("given_name"));
        user.setEmail(jo.getString("email"));
        user.setProfilePic(jo.getString("picture"));
        return user;
    }

    public User toUserObj(OAuth2AuthenticationToken token) {
        User user = new User();
        Map<String, Object> attributes = token.getPrincipal().getAttributes();
        user.setGivenName(attributes.get("given_name").toString());
        user.setEmail(attributes.get("email").toString());
        user.setProfilePic(attributes.get("picture").toString());
        return user;
    }

    public JsonObject userToJsonObj(User user) {
        return Json.createObjectBuilder()
                .add("given_name", user.getGivenName())
                .add("email", user.getEmail())
                .add("picture", user.getProfilePic())
                .build();
    }
}
