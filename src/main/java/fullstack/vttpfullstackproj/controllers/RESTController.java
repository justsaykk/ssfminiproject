package fullstack.vttpfullstackproj.controllers;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

    @PostMapping(path = "/adddrink")
    public void addDrink(
            @AuthenticationPrincipal OAuth2User user,
            @RequestBody MultiValueMap<String, String> form,
            RedirectAttributes redirectAttributes,
            HttpServletResponse response) throws IOException {

        User currentUser = new User();
        currentUser.setOAuthUser(user);
        String idDrink = form.getFirst("idDrink");
        Boolean add = restSvc.addDrink(currentUser, idDrink);

        if (!add) {
            String message = "Duplicated entry, please add another drink";
            redirectAttributes.addFlashAttribute("message", message);
        } else {
            String message = "Drink Added!";
            redirectAttributes.addFlashAttribute("message", message);
        }
        response.sendRedirect("/drink?idDrink=%s".formatted(idDrink));
    }

    @PostMapping(path = "/editprofile")
    public void editProfile(
            @RequestBody MultiValueMap<String, String> form,
            HttpServletResponse response) throws IOException {

        User oldUser = new User();
        oldUser.setOldUser(form);

        User editedUser = new User();
        editedUser.setUser(form);

        // Send info to userService
        userSvc.editUserProfile(oldUser, editedUser);
        response.sendRedirect("/profile/%s".formatted(editedUser.getName()));
    }

    @GetMapping(path = "/profile")
    public void getProfile(
            @AuthenticationPrincipal OAuth2User user,
            HttpServletResponse response) throws IOException {

        User currentUser = new User();
        currentUser.setOAuthUser(user);

        // Check if name is registered
        if (userSvc.userExists(currentUser)) {
            String name = userSvc.getNamefromEmail(currentUser.getEmail());
            response.sendRedirect("/profile/%s".formatted(name));
        } else {
            userSvc.createProfile(currentUser);
            response.sendRedirect("/profile/%s".formatted(currentUser.getName()));
        }
    }

    @PostMapping(path = "/delete/{name}/{idDrink}")
    public void deleteDrink(
            @PathVariable(value = "name") String rawName,
            @PathVariable(value = "idDrink") String idDrink,
            @AuthenticationPrincipal OAuth2User user,
            HttpServletResponse response) throws IOException {

        User currentUser = new User();
        currentUser.setOAuthUser(user);

        // Pull DB user
        User dbUser = new User();
        dbUser = userSvc.getUser(currentUser.getEmail());

        restSvc.deleteDrink(dbUser, idDrink);
        response.sendRedirect("/profile/%s".formatted(dbUser.getName()));
    }

    @PostMapping(path = "/deleteuser/{name}/{email}")
    public void deleteUser(
            @PathVariable(value = "name") String name,
            @PathVariable(value = "email") String email,
            @AuthenticationPrincipal OAuth2User user,
            HttpServletResponse response) throws IOException {

        User currentUser = new User();
        currentUser.setOAuthUser(user);

        // Pull DB user
        User dbUser = new User();
        dbUser = userSvc.getUser(currentUser.getEmail());

        userSvc.deleteUser(dbUser);
        response.sendRedirect("/");
    }
}
