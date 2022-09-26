package fullstack.vttpfullstackproj.repository;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import fullstack.vttpfullstackproj.models.User;

@Repository
public class UserRepo {

    @Autowired
    @Qualifier("repository")
    private RedisTemplate<String, String> repo;

    private String repoFormat(String s) {
        return s.trim().toLowerCase().replaceAll(" ", "");
    }

    private String toCaps(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    public Boolean isRegisteredEmail(String email) {
        ListOperations<String, String> listOps = repo.opsForList();
        if (listOps.indexOf("registeredprofiles", repoFormat(email)) != null) {
            return true;
        } else {
            return false;
        }
    }

    public Boolean isRegisteredName(String name) {
        ListOperations<String, String> listOps = repo.opsForList();
        if (listOps.indexOf("registerednames", repoFormat(name)) != null) {
            return true;
        } else {
            return false;
        }
    }

    public Boolean isMapped(String name) {
        HashOperations<String, String, String> hashOps = repo.opsForHash();
        return hashOps.hasKey("profilemap", repoFormat(name));
    }

    public void registerEmail(String rawEmail) {
        ListOperations<String, String> listOps = repo.opsForList();
        String email = repoFormat(rawEmail);
        if (!isRegisteredEmail(email)) {
            listOps.leftPush("registeredprofiles", email);
        }
    }

    public void deregisterEmail(String email) {
        ListOperations<String, String> listOps = repo.opsForList();
        listOps.remove("registeredprofiles", 0, repoFormat(email));
    }

    public void registerName(String rawName) {
        ListOperations<String, String> listOps = repo.opsForList();
        String name = repoFormat(rawName);
        if (!isRegisteredName(name)) {
            listOps.leftPush("registerednames", name);
        }
    }

    public void deregisterName(String name) {
        ListOperations<String, String> listOps = repo.opsForList();
        listOps.remove("registerednames", 0, repoFormat(name));
    }

    public void createProfile(String email, Map<String, String> m) {
        HashOperations<String, String, String> hashOps = repo.opsForHash();
        hashOps.putAll(repoFormat(email), m);
    }

    public void deleteEmail(String email) {
        repo.delete(repoFormat(email));
    }

    public void updateProfileMapping(String name, String email) {
        HashOperations<String, String, String> hashOps = repo.opsForHash();
        hashOps.put("profilemap", repoFormat(name), repoFormat(email));
    }

    public void deleteProfileMapping(String name) {
        HashOperations<String, String, String> hashOps = repo.opsForHash();
        hashOps.delete("profilemap", repoFormat(name));
    }

    public String getEmailFromName(String name) {
        HashOperations<String, String, String> hashOps = repo.opsForHash();
        return hashOps.get("profilemap", repoFormat(name));
    }

    public String getNameFromEmail(String email) {
        HashOperations<String, String, String> hashOps = repo.opsForHash();
        return hashOps.get(repoFormat(email), "name");
    }

    public User getUser(String email) {
        HashOperations<String, String, String> hashOps = repo.opsForHash();
        User user = new User();
        user.setEmail(email);
        user.setName(hashOps.get(email, "name"));
        user.setCountry(toCaps(hashOps.get(email, "country")));
        user.setProfilePic(hashOps.get(email, "profilePic"));

        return user;
    }
}
