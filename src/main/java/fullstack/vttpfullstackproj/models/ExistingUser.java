package fullstack.vttpfullstackproj.models;

public class ExistingUser extends User{
    private String uuid;

    public String getUuid() {
        return uuid;
    }
    
    public ExistingUser(String uuid) {
        super();
        this.uuid = uuid;
    }
}
