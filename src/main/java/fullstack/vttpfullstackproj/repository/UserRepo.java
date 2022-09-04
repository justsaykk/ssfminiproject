package fullstack.vttpfullstackproj.repository;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepo {

    @Autowired
    @Qualifier("repository")
    private RedisTemplate<String, String> repo;

    public Boolean isRegisteredEmail(String profile) {
        ListOperations<String, String> listOps = repo.opsForList();
        if (listOps.indexOf("registeredprofiles", profile) != null) {
            System.out.printf("%s profile is found in registeredprofiles.\n", profile);
            return true;
        } else {
            System.out.printf("%s profile not found in registeredprofiles.\n", profile);
            return false;
        }
    }

    public Boolean isRegisteredName(String name) {
        ListOperations<String, String> listOps = repo.opsForList();
        if (listOps.indexOf("registerednames", name) != null) {
            System.out.printf("%s profile is found in registerednames.\n", name);
            return true;
        } else {
            System.out.printf("%s profile not found in registerednames.\n", name);
            return false;
        }
    }

    public void registerEmail(String email) {
        ListOperations<String, String> listOps = repo.opsForList();
        if (!isRegisteredEmail(email)) {
            listOps.leftPush("registeredprofiles", email);
            System.out.printf("%s profile added to registeredprofiles.\n", email);
        }
    }

    public void registerName(String name) {
        ListOperations<String, String> listOps = repo.opsForList();
        if (!isRegisteredName(name)) {
            listOps.leftPush("registerednames", name);
            System.out.printf("%s profile added to registerednames.\n", name);
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

    public Map<String, String> getProfileDetails(String email) {
        HashOperations<String, String, String> hashOps = repo.opsForHash();

        Map<String, String> m = new HashMap<>();
        m.put("email", email);
        m.put("name", hashOps.get(email, "name"));
        m.put("profilePic", hashOps.get(email, "profilePic"));
        m.put("country", hashOps.get(email, "country"));
        return m;
    }
}
