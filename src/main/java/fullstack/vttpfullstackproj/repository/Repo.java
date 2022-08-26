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

    public void addUser(String key, String email, String profilePic) { // name as key
        HashOperations<String, Object, Object> hashOps = repo.opsForHash();
        Map<String, String> m = new HashMap<>();
        m.put("email", email);
        m.put("picture", profilePic);
        hashOps.putAll(key, m);
        System.out.printf("Created %s. Email: %s. Picture: %s\n", key, email, profilePic);
    }

    public Boolean checkUser(String key) {
        System.out.printf("Checking name: %s\n", key);
        return repo.hasKey(key);
    }

    public Boolean addDrink(String key, String value) { // email as key
        ListOperations<String, String> listOps = repo.opsForList();
        List<String> listOfValues = getProfile(key);

        if (!listOfValues.contains(value)) {
            listOps.rightPush(key, value);
            return true;
        } else {
            return false;
        }
    }

    public List<String> getProfile(String key) { // email as key
        ListOperations<String, String> listOps = repo.opsForList();
        return listOps.range(key, 0, listOps.size(key) + 1);
    }

    public void removeDrink(String key, String value) { // email as key
        ListOperations<String, String> listOps = repo.opsForList();
        listOps.remove(key, 0, value);
    }
}
