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

    @PostMapping(path = "/adddrink", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> addDrink(
            @AuthenticationPrincipal OAuth2User user,
            @RequestBody MultiValueMap<String, String> form,
            RedirectAttributes redirectAttributes,
            HttpServletResponse response) throws IOException {

        System.out.println(user);

        String name = user.getAttribute("name").toString().toLowerCase();
        String idDrink = form.getFirst("idDrink");

        // Check for registration
        if (!userSvc.isRegisteredName(name)) {
            User newUser = new User();
            newUser.setOAuthUser(user);
            userSvc.createProfile(newUser);
        }

        // Add drink to profile
        Boolean add = restSvc.addDrink(name, idDrink);
        if (!add) {
            String message = "Duplicated entry, please add another drink";
            redirectAttributes.addFlashAttribute("message", message);
        } else {
            String message = "Drink Added!";
            redirectAttributes.addFlashAttribute("message", message);
        }

        response.sendRedirect("/drink?idDrink=%s".formatted(idDrink));
        return new ResponseEntity<String>(HttpStatus.OK);
    }

    @PostMapping(path = "/editprofile", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> editProfile(
            @RequestBody MultiValueMap<String, String> form,
            HttpServletResponse response) throws IOException {

        User oldUser = new User();
        oldUser.setOldUser(form);

        User editedUser = new User();
        editedUser.setUser(form);

        // Send info to userService
        userSvc.editUserProfile(oldUser, editedUser);

        response.sendRedirect("/profile/%s".formatted(editedUser.getName()));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(path = "/profile")
    public void getProfile(
            @AuthenticationPrincipal OAuth2User user,
            HttpServletResponse response) throws IOException {

        String name = user.getAttribute("name").toString().toLowerCase();

        // Check if name is registered
        if (userSvc.isRegisteredName(name)) {
            response.sendRedirect("/profile/%s".formatted(name));
        } else {
            User newUser = new User();
            newUser.setOAuthUser(user);
            userSvc.createProfile(newUser);
            response.sendRedirect("/profile/%s".formatted(name));
        }
    }

    @PostMapping(path = "/delete/{name}/{idDrink}")
    public void deleteDrink(
            @PathVariable(value = "name") String rawName,
            @PathVariable(value = "idDrink") String idDrink,
            HttpServletResponse response) throws IOException {
        String name = rawName.toLowerCase();
        restSvc.deleteDrink(name, idDrink);
        response.sendRedirect("/profile/%s".formatted(name));
    }

    @PostMapping(path = "/deleteuser/{name}/{email}")
    public void deleteUser(
            @PathVariable(value = "name") String name,
            @PathVariable(value = "email") String email,
            HttpServletResponse response) throws IOException {
        userSvc.deleteUser(name.toLowerCase(), email.toLowerCase());
        response.sendRedirect("/");
    }
}
