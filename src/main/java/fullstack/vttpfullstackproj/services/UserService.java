package fullstack.vttpfullstackproj.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Service;

import fullstack.vttpfullstackproj.models.User;
import fullstack.vttpfullstackproj.repository.Repo;

@Service
public class UserService {

    @Autowired
    private Repo repo;

    public User currentUser(OAuth2AuthenticationToken token) {
        User currUser = new User();
        return currUser.toUserObj(token);
    }

    public Boolean profileExists(User user) {
        return repo.checkEmail(user.getEmail());
    }

    public void createUser(User user) {
        String given_name = user.getGivenName();
        String email = user.getEmail();
        String profilePic = user.getProfilePic();

        repo.addUser(given_name, email, profilePic);
    }
}
