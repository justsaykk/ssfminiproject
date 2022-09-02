package fullstack.vttpfullstackproj.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class Repo {

    @Autowired
    @Qualifier("repository")
    private RedisTemplate<String, String> repo;

    public void addUser(String name, String email, String profilePic) {
        HashOperations<String, Object, Object> hashOps = repo.opsForHash();
        Map<String, String> m = new HashMap<>();
        m.put("name", name);
        m.put("picture", profilePic);
        hashOps.putAll(email, m);
        System.out.printf("Created %s. Email: %s. Picture: %s\n", name, email, profilePic);
    }

    public Boolean checkEmail(String email) {
        System.out.printf("Checking email: %s\n", email);

        Boolean found = repo.hasKey(email);

        if (found) {
            System.out.println("Email found!");
            return true;
        } else {
            System.out.println("Email not found!");
            return false;
        }
    }

    public Boolean checkName(String name) {
        System.out.printf("Checking name: %s\n", name);
        Boolean found = repo.hasKey(name);

        if (found) {
            System.out.println("Name found!");
            return true;
        } else {
            System.out.println("Name not found!");
            return false;
        }
    }

    public Boolean addDrink(String name, String value) {
        ListOperations<String, String> listOps = repo.opsForList();
        List<String> listOfValues = getProfile(name);

        if (!listOfValues.contains(value)) {
            // If profile is not found, rightPush will automatically create new profile.
            listOps.rightPush(name, value);
            return true;
        } else {
            return false;
        }
    }

    public List<String> getProfile(String key) {
        ListOperations<String, String> listOps = repo.opsForList();
        List<String> profile = listOps.range(key, 0, listOps.size(key) + 1);

        if (profile.isEmpty()) {
            System.out.printf("Profile not found. Creating %s profile.\n", key);
        }

        return profile;
    }

    public Boolean isRegistered(String profile) {
        ListOperations<String, String> listOps = repo.opsForList();
        if (listOps.indexOf("registeredprofiles", profile) != null) {
            System.out.printf("%s profile is found in registeredprofiles.\n", profile);
            return true;
        } else {
            System.out.printf("%s profile not found in registeredprofiles.\n", profile);
            return false;
        }
    }

    public void registerProfile(String profile) {
        ListOperations<String, String> listOps = repo.opsForList();
        if (!isRegistered(profile)) {
            listOps.leftPush("registeredprofiles", profile);
            System.out.printf("%s profile added to registeredprofiles.\n", profile);
        }
    }

    public void createProfile(String key, Map<String, String> m) {
        HashOperations<String, String, String> hashOps = repo.opsForHash();
        hashOps.putAll(key, m);
    }

    public void removeDrink(String key, String value) {
        ListOperations<String, String> listOps = repo.opsForList();
        listOps.remove(key, 0, value);
    }
}
