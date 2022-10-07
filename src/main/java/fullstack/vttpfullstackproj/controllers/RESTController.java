package fullstack.vttpfullstackproj.controllers;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import fullstack.vttpfullstackproj.models.ExistingUser;
import fullstack.vttpfullstackproj.models.User;
import fullstack.vttpfullstackproj.services.RESTService;
import fullstack.vttpfullstackproj.services.UserService;

@RestController
@RequestMapping(path = "/api")
public class RESTController {

    @Autowired
    private RESTService restSvc;

    @Autowired
    private UserService userSvc;

    public ExistingUser getDbUser(OAuth2User user) {
        User currentUser = new User(user);
        return userSvc.getUser(currentUser.getEmail());
    }

    @PostMapping(path = "/drink/new")
    public void addDrink(
            @AuthenticationPrincipal OAuth2User user,
            @RequestBody MultiValueMap<String, String> form,
            HttpServletResponse response) throws IOException {
        ExistingUser dbUser = getDbUser(user);
        String idDrink = form.getFirst("idDrink");
        Boolean add = restSvc.addDrink(dbUser, idDrink);
        String successful = (add) ? "true" : "false";
        response.sendRedirect("/drink?idDrink=%s&successful=%s".formatted(idDrink, successful));
    }

    @PostMapping(path = "/profile/edit")
    public void editProfile(
            @RequestBody MultiValueMap<String, String> form,
            HttpServletResponse response) throws IOException {
        User oldUser = new User();
        oldUser.setOldUser(form);
        User editedUser = new User(form);
        userSvc.editUserProfile(oldUser, editedUser);
        response.sendRedirect("/profile/%s".formatted(editedUser.getName()));
    }

    @GetMapping(path = "/profile")
    public void getProfile(
            @AuthenticationPrincipal OAuth2User user,
            HttpServletResponse response) throws IOException {
        User currentUser = new User(user);
        if (userSvc.userExists(currentUser)) {
            String name = userSvc.getNamefromEmail(currentUser.getEmail());
            response.sendRedirect("/profile/%s".formatted(name));
        } else {
            userSvc.create(currentUser);
            response.sendRedirect("/profile/%s".formatted(currentUser.getName()));
        }
    }

    @PostMapping(path = "/profile/{idDrink}/delete")
    public void deleteDrink(
            @PathVariable(value = "idDrink") String idDrink,
            @AuthenticationPrincipal OAuth2User user,
            HttpServletResponse response) throws IOException {
        User dbUser = getDbUser(user);
        restSvc.deleteDrink(dbUser, idDrink);
        response.sendRedirect("/profile/%s".formatted(dbUser.getName()));
    }

    @PostMapping(path = "/user/delete")
    public void deleteUser(
            @AuthenticationPrincipal OAuth2User user,
            HttpServletResponse response) throws IOException {
        ExistingUser dbUser = getDbUser(user);
        userSvc.deleteUser(dbUser);
        response.sendRedirect("/logout");
    }
}
