package fullstack.vttpfullstackproj.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fullstack.vttpfullstackproj.models.ExistingUser;
import fullstack.vttpfullstackproj.models.User;
import fullstack.vttpfullstackproj.repository.ProfileRepo;
import fullstack.vttpfullstackproj.repository.UserRepo;

@Service
public class RESTService {

    @Autowired
    private ProfileRepo profileRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private UserService userSvc;

    public Boolean addDrink(ExistingUser dbUser, String idDrink) {

        if (!userSvc.userExists(dbUser)) {
            userSvc.create(dbUser);
        }
        return profileRepo.addDrink(dbUser.getUuid(), idDrink);
    }

    public void deleteDrink(User user, String idDrink) {
        profileRepo.deleteDrink(user.getName(), idDrink);
    }

    public List<String> getProfile(String name) {
        return profileRepo.getProfile(userRepo.getUUIDFromName(name));
    }
}
