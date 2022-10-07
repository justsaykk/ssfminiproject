package fullstack.vttpfullstackproj.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ProfileRepo {
    @Autowired
    @Qualifier("repository")
    private RedisTemplate<String, String> repo;

    @Autowired
    private UserRepo userRepo;

    public Boolean hasEmail(String email) {
        return repo.hasKey(email);
    }

    public Boolean addDrink(String uuid, String value) {
        ListOperations<String, String> listOps = repo.opsForList();
        List<String> listOfValues = getProfile(uuid);
        if (!listOfValues.contains(value)) {
            listOps.rightPush(uuid, value);
            return true;
        } else {
            return false;
        }
    }

    public List<String> getProfile(String uuid) {
        ListOperations<String, String> listOps = repo.opsForList();
        Optional<Long> listSize = Optional.ofNullable(listOps.size(uuid));
        return listOps.range(uuid, 0, listSize.get() + 1);
    }

    public void deleteUUID(String uuid) {
        repo.delete(uuid);
    }

    public void deleteDrink(String name, String value) {
        ListOperations<String, String> listOps = repo.opsForList();
        String uuid = userRepo.getUUIDFromName(name);
        listOps.remove(uuid, 0, value);
    }

    public void updateCountry(String email, String newCountry) {
        HashOperations<String, String, String> hmOps = repo.opsForHash();
        hmOps.put(email, "country", userRepo.repoFormat(newCountry));
    }

    public void updateProfilePic(String email, String newProfilePic) {
        HashOperations<String, String, String> hmOps = repo.opsForHash();
        hmOps.put(email, "profilePic", userRepo.repoFormat(newProfilePic));
    }
}
