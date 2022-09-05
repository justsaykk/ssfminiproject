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

    public Boolean isRegisteredEmail(String profile) {
        ListOperations<String, String> listOps = repo.opsForList();
        if (listOps.indexOf("registeredprofiles", profile) != null) {
            return true;
        } else {
            return false;
        }
    }

    public Boolean isRegisteredName(String name) {
        ListOperations<String, String> listOps = repo.opsForList();
        if (listOps.indexOf("registerednames", name) != null) {
            return true;
        } else {
            return false;
        }
    }

    public void registerEmail(String email) {
        ListOperations<String, String> listOps = repo.opsForList();
        if (!isRegisteredEmail(email)) {
            listOps.leftPush("registeredprofiles", email);
        }
    }

    public void registerName(String name) {
        ListOperations<String, String> listOps = repo.opsForList();
        if (!isRegisteredName(name)) {
            listOps.leftPush("registerednames", name);
        }
    }

    public void createProfile(String key, Map<String, String> m) {
        HashOperations<String, String, String> hashOps = repo.opsForHash();
        hashOps.putAll(key, m);
    }

    public void updateProfileMapping(String name, String email) {
        HashOperations<String, String, String> hashOps = repo.opsForHash();
        hashOps.put("profilemap", name, email);
    }

    public String getEmailFromName(String name) {
        HashOperations<String, String, String> hashOps = repo.opsForHash();
        return hashOps.get("profilemap", name);
    }

    public User getUserDetails(String email) {
        HashOperations<String, String, String> hashOps = repo.opsForHash();
        User user = new User();
        user.setEmail(email);
        user.setName(hashOps.get(email, "name"));
        user.setCountry(hashOps.get(email, "country"));
        user.setProfilePic(hashOps.get(email, "profilePic"));

        return user;
    }
}
