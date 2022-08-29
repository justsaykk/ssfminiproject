package fullstack.vttpfullstackproj.models;

public class User {
    private String name;
    private String email;
    private String profilePic;

    public String getGivenName() {
        return name;
    }

    public void setGivenName(String name) {
        this.name = name;
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
}
