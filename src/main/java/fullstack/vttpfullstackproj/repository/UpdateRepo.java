package fullstack.vttpfullstackproj.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
// import org.springframework.data.redis.core.ListOperations;

@Repository
public class UpdateRepo {
    @Autowired
    @Qualifier("repository")
    private RedisTemplate<String, String> repo;

    @Autowired
    private UserRepo userRepo;

    // @Autowired
    // private UserRepo userRepo;

    // public void updateEmail(String name, String oldEmail, String newEmail) {
    // ListOperations<String, String> listOps = repo.opsForList();
    // HashOperations<String, String, String> hmOps = repo.opsForHash();

    // // Update registeredProfiles (List)
    // listOps.remove("registeredprofiles", 1, oldEmail);
    // userRepo.registerEmail(newEmail);

    // // Update profileMap (Map)
    // hmOps.put("profilemap", name, newEmail);

    // // Update userHashMap (Map)
    // repo.rename(oldEmail, newEmail);

    // }

    public void updateCountry(String email, String newCountry) {
        HashOperations<String, String, String> hmOps = repo.opsForHash();
        hmOps.put(email, "country", userRepo.repoFormat(newCountry));
    }

    public void updateProfilePic(String email, String newProfilePic) {
        HashOperations<String, String, String> hmOps = repo.opsForHash();
        hmOps.put(email, "profilePic", userRepo.repoFormat(newProfilePic));
    }
}
