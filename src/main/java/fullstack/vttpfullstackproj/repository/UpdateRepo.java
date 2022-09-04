package fullstack.vttpfullstackproj.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UpdateRepo {
    @Autowired
    @Qualifier("repository")
    private RedisTemplate<String, String> repo;

    @Autowired
    private UserRepo userRepo;

    public void updateEmail(String name, String oldEmail, String newEmail) {
        ListOperations<String, String> listOps = repo.opsForList();
        HashOperations<String, String, String> hmOps = repo.opsForHash();

        // Update registeredProfiles (List)
        listOps.remove("registeredprofiles", 1, oldEmail);
        System.out.printf("Removed %s from registeredprofiles.\n", oldEmail);
        userRepo.registerEmail(newEmail);
        System.out.printf("Added email from %s to %s in registeredProfiles.\n", oldEmail, newEmail);

        // Update profileMap (Map)
        hmOps.put("profilemap", name, newEmail);
        System.out.printf("Updated email from %s to %s in profileMap.\n", oldEmail, newEmail);

        // Update userHashMap (Map)
        repo.rename(oldEmail, newEmail);
        System.out.printf("Changed email from %s to %s in repo.\n", oldEmail, newEmail);

    }

    public void updateCountry(String email, String newCountry) {
        HashOperations<String, String, String> hmOps = repo.opsForHash();
        hmOps.put(email, "country", newCountry);
        System.out.printf("Updated country to %s in %s.\n", newCountry, email);

    }

    public void updateProfilePic(String email, String newProfilePic) {
        HashOperations<String, String, String> hmOps = repo.opsForHash();
        hmOps.put(email, "profilePic", newProfilePic);
        System.out.printf("Updated profilePic to %s in %s.\n", newProfilePic, email);
    }
}
