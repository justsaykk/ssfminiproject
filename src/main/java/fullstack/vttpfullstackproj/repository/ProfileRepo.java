package fullstack.vttpfullstackproj.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ProfileRepo {
    @Autowired
    @Qualifier("repository")
    private RedisTemplate<String, String> repo;

    // public void addUser(String name, String email, String profilePic) {
    // HashOperations<String, Object, Object> hashOps = repo.opsForHash();
    // Map<String, String> m = new HashMap<>();
    // m.put("name", name);
    // m.put("picture", profilePic);
    // hashOps.putAll(email, m);
    // }

    public Boolean hasEmail(String email) {
        return repo.hasKey(email);
    }

    public Boolean hasName(String name) {
        return repo.hasKey(name);
    }

    public Boolean addDrink(String name, String value) {
        ListOperations<String, String> listOps = repo.opsForList();
        List<String> listOfValues = getProfile(name);

        if (!listOfValues.contains(value)) {
            // If name is not found, rightPush will automatically create new profile.
            listOps.rightPush(name, value);
            return true;
        } else {
            return false;
        }
    }

    public List<String> getProfile(String name) {
        ListOperations<String, String> listOps = repo.opsForList();
        return listOps.range(name, 0, listOps.size(name) + 1);
    }

    public void deleteName(String name) {
        repo.delete(name);
    }

    public void deleteDrink(String name, String value) {
        ListOperations<String, String> listOps = repo.opsForList();
        listOps.remove(name, 0, value);
    }
}
