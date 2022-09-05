package fullstack.vttpfullstackproj.controllers;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import fullstack.vttpfullstackproj.models.Cocktail;
import fullstack.vttpfullstackproj.models.User;
import fullstack.vttpfullstackproj.services.ApiService;
import fullstack.vttpfullstackproj.services.RESTService;
import fullstack.vttpfullstackproj.services.UserService;
import jakarta.json.*;

@RestController
@RequestMapping(path = "/api")
public class RESTController {

    @Autowired
    private RESTService restSvc;

    @Autowired
    private ApiService apiSvc;

    @Autowired
    private UserService userSvc;

    @PostMapping(path = "/adddrink", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> addDrink(
            @RequestBody MultiValueMap<String, String> form,
            HttpServletResponse response) throws IOException {

        String name = form.getFirst("name").toLowerCase();
        String idDrink = form.getFirst("idDrink");

        // Check if there is a name value
        if (name.length() < 1) {
            System.out.println("RESTController: Name field cannot be empty");
            response.sendRedirect("/drink?idDrink=%s".formatted(idDrink));
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Boolean add = restSvc.addDrink(name, idDrink);
        if (!add) {
            String body = Json.createObjectBuilder()
                    .add("successfullyAdded", false)
                    .add("reason", "Duplicated addition")
                    .add(name, idDrink)
                    .build().toString();
            response.sendRedirect("/menu");
            return new ResponseEntity<String>(body, HttpStatus.BAD_REQUEST);
        } else {
            // Check for registration
            if (userSvc.isRegistered(name)) {
                response.sendRedirect("/drink?idDrink=%s".formatted(idDrink));
            } else {
                response.sendRedirect("/createprofile2/%s".formatted(name));
            }
            return new ResponseEntity<String>(HttpStatus.OK);
        }
    }

    @GetMapping(path = "/profile/{name}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getProfile(
            @PathVariable(value = "name") String rawName) {

        String name = rawName.toLowerCase();
        List<String> listOfidDrink = restSvc.getProfile(name);
        List<JsonObject> listOfCocktails = new LinkedList<>();

        for (String id : listOfidDrink) {
            Cocktail cocktail = new Cocktail();
            cocktail = apiSvc.fetchDrinkById(id);
            JsonObject e = cocktail.toJsonObject(cocktail);
            listOfCocktails.add(e);
        }

        JsonArrayBuilder builder = Json.createArrayBuilder();
        for (JsonObject jsonObject : listOfCocktails) {
            builder.add(jsonObject);
        }

        JsonObject jo = Json.createObjectBuilder()
                .add(name, builder)
                .build();

        return new ResponseEntity<String>(jo.toString(), HttpStatus.OK);
    }

    @PostMapping(path = "/createprofile", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> createProfile(
            @RequestBody MultiValueMap<String, String> form,
            HttpServletResponse response) throws IOException {

        User user = new User();
        user.setUser(form);
        userSvc.createProfile(user);

        response.sendRedirect("/");
        return new ResponseEntity<String>(user.toJson(user).toString(), HttpStatus.OK);
    }

    @PostMapping(path = "/editprofile", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> editProfile(
            @RequestBody MultiValueMap<String, String> form,
            HttpServletResponse response) throws IOException {

        User oldUser = new User();
        oldUser.setName(form.getFirst("name").toLowerCase());
        oldUser.setEmail(form.getFirst("oldEmail").toLowerCase());
        oldUser.setCountry(form.getFirst("oldCountry").toLowerCase());
        oldUser.setProfilePic(form.getFirst("oldProfilePicUrl").toLowerCase());

        User editedUser = new User();
        editedUser.setUser(form);

        // Send info to userService
        userSvc.editUserProfile(oldUser, editedUser);

        response.sendRedirect("/");
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(path = "/profile")
    public void getProfile(
            @RequestBody MultiValueMap<String, String> form,
            HttpServletResponse response) throws IOException {

        String name = form.getFirst("name").toLowerCase();
        response.sendRedirect("/profile/%s".formatted(name));
    }

    @PostMapping(path = "/delete/{name}/{idDrink}")
    public void deleteDrink(
            @PathVariable(value = "name") String rawName,
            @PathVariable(value = "idDrink") String idDrink,
            HttpServletResponse response) throws IOException {
        String name = rawName.toLowerCase();
        restSvc.deleteDrink(name.toLowerCase(), idDrink);
        response.sendRedirect("/profile/%s".formatted(name.toLowerCase()));
    }
}
