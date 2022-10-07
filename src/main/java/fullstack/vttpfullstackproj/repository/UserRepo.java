package fullstack.vttpfullstackproj.repository;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import fullstack.vttpfullstackproj.models.ExistingUser;

@Repository
public class UserRepo {

    @Autowired
    @Qualifier("repository")
    private RedisTemplate<String, String> repo;

    public String repoFormat(String s) {
        return s.trim().toLowerCase().replaceAll(" ", "");
    }

    private String toCaps(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    /* Register */
    public void registerEmail(String rawEmail) {
        ListOperations<String, String> listOps = repo.opsForList();
        String email = repoFormat(rawEmail);
        if (!isRegisteredEmail(email)) {
            listOps.leftPush("registeredprofiles", email);
        }
    }

    public void registerUUID(String uuid) {
        ListOperations<String, String> listOps = repo.opsForList();
        if (!isRegisteredUUID(uuid)) {
            listOps.leftPush("registerednames", uuid);
        }
    }

    public void updateProfileMapping(String uuid, String email, String name) {
        HashOperations<String, String, String> hashOps = repo.opsForHash();
        hashOps.put("uuidmap", uuid, repoFormat(email));
        hashOps.put("profilemap", name, repoFormat(email));
    }

    public void createProfile(String email, Map<String, String> m) {
        HashOperations<String, String, String> hashOps = repo.opsForHash();
        hashOps.putAll(repoFormat(email), m);
    }

    /* Checks */
    public Boolean isRegisteredEmail(String email) {
        ListOperations<String, String> listOps = repo.opsForList();
        if (listOps.indexOf("registeredprofiles", repoFormat(email)) != null) {
            return true;
        } else {
            return false;
        }
    }

    public Boolean isRegisteredUUID(String uuid) {
        ListOperations<String, String> listOps = repo.opsForList();
        if (listOps.indexOf("registerednames", uuid) != null) {
            return true;
        } else {
            return false;
        }
    }

    public Boolean isMapped(String name) {
        HashOperations<String, String, String> hashOps = repo.opsForHash();
        return hashOps.hasKey("profilemap", repoFormat(name));
    }

    /* De-register/Delete */
    public void deregisterEmail(String email) {
        ListOperations<String, String> listOps = repo.opsForList();
        listOps.remove("registeredprofiles", 0, repoFormat(email));
    }

    public void deregisterName(String name) {
        ListOperations<String, String> listOps = repo.opsForList();
        listOps.remove("registerednames", 0, repoFormat(name));
    }

    public void deleteEmail(String email) {
        repo.delete(repoFormat(email));
    }

    public void deleteProfileMapping(String name) {
        HashOperations<String, String, String> hashOps = repo.opsForHash();
        hashOps.delete("profilemap", repoFormat(name));
    }

    /* Queries */
    public String getEmailFromUUID(String uuid) {
        HashOperations<String, String, String> hashOps = repo.opsForHash();
        return hashOps.get("uuidmap", uuid);
    }

    public String getUUIDFromEmail(String email) {
        HashOperations<String, String, String> hashOps = repo.opsForHash();
        return hashOps.get(repoFormat(email), "uuid");
    }

    public String getUUIDFromName(String name) {
        HashOperations<String, String, String> hashOps = repo.opsForHash();
        String email = getEmailFromName(name);
        return hashOps.get(email, "uuid");
    }

    public String getEmailFromName(String name) {
        HashOperations<String, String, String> hashOps = repo.opsForHash();
        return hashOps.get("profilemap", name);
    }

    public String getNameFromEmail(String email) {
        HashOperations<String, String, String> hashOps = repo.opsForHash();
        return hashOps.get(repoFormat(email), "name");
    }

    public ExistingUser getUser(String email) {
        HashOperations<String, String, String> hashOps = repo.opsForHash();
        ExistingUser user = new ExistingUser(getUUIDFromEmail(email));
        user.setEmail(email);
        user.setName(hashOps.get(email, "name"));
        user.setCountry(toCaps(hashOps.get(email, "country")));
        user.setProfilePic(hashOps.get(email, "profilePic"));
        return user;
    }
}
