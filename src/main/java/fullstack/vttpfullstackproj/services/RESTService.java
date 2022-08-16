package fullstack.vttpfullstackproj.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fullstack.vttpfullstackproj.repository.Repo;

@Service
public class RESTService {

    @Autowired
    private Repo repo;

    public Boolean addDrink(String name, String idDrink) {
        return repo.addDrink(name, idDrink);
    }
}
