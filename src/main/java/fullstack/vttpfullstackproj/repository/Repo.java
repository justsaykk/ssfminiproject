package fullstack.vttpfullstackproj.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class Repo {

    @Autowired
    @Qualifier("repository")
    private RedisTemplate<String, String> repo;

    @Value("${spring.redis.cacheTime}")
    private Long cacheTime;

    public Boolean addDrink(String key, String value) {
        ListOperations<String, String> listOps = repo.opsForList();
        List<String> listOfValues = getDrink(key);

        if (!listOfValues.contains(value)) {
            listOps.rightPush(key, value);
            return true;
        } else {
            return false;
        }
    }

    public List<String> getDrink(String key) {
        ListOperations<String, String> listOps = repo.opsForList();
        return listOps.range(key, 0, listOps.size(key) + 1);
    }

    public void removeDrink(String key, String value) {
        ListOperations<String, String> listOps = repo.opsForList();
        listOps.remove(key, 0, value);
    }
}
