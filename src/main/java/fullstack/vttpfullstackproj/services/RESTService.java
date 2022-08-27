package fullstack.vttpfullstackproj.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fullstack.vttpfullstackproj.models.User;
import fullstack.vttpfullstackproj.repository.Repo;

@Service
public class RESTService {

    @Autowired
    private Repo repo;

    public Boolean addDrink(User user, String idDrink) {
        String email = user.getEmail();
        return repo.addDrink(email, idDrink);
    }

    public List<String> getProfile(String name) {
        return repo.getProfile(name);
    }
}
