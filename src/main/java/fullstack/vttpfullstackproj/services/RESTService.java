package fullstack.vttpfullstackproj.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fullstack.vttpfullstackproj.models.User;
import fullstack.vttpfullstackproj.repository.ProfileRepo;

@Service
public class RESTService {

    @Autowired
    private ProfileRepo profileRepo;

    @Autowired
    private UserService userSvc;

    public Boolean addDrink(User currentUser, String idDrink) {

        if (!userSvc.userExists(currentUser)) {
            userSvc.createProfile(currentUser);
        }
        String name = userSvc.getNamefromEmail(currentUser.getEmail());
        return profileRepo.addDrink(name, idDrink);
    }

    public void deleteDrink(String name, String idDrink) {
        profileRepo.deleteDrink(name, idDrink);
    }

    public List<String> getProfile(String name) {
        return profileRepo.getProfile(name);
    }
}
