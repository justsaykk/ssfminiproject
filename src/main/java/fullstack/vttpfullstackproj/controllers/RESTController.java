package fullstack.vttpfullstackproj.controllers;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import fullstack.vttpfullstackproj.models.Cocktail;
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

        String name = form.getFirst("name");
        String idDrink = form.getFirst("idDrink");

        // Check if there is a name value
        if (name.length() < 1) {
            System.out.println("Name field cannot be empty");
            response.sendRedirect("/drink?idDrink=%s".formatted(idDrink));
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        System.out.printf("user %s is trying to add drinkId %s... but is the user registered?\n", name, idDrink);
        Boolean add = restSvc.addDrink(name, idDrink);
        if (!add) {
            System.out.println("Duplicated drink added. Cancelling request");
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
                System.out.printf("drinkId %s successfully added to user %s's profile\n", idDrink, name);
                response.sendRedirect("/drink?idDrink=%s".formatted(idDrink));
            } else {
                System.out.printf(
                        "drinkId %s successfully added to user %s's profile. Redirecting to createprofile...\n",
                        idDrink, name);
                response.sendRedirect("/createprofile");
            }
            return new ResponseEntity<String>(HttpStatus.OK);
        }
    }

    @GetMapping(path = "/profile/{name}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getProfile(
            @PathVariable(value = "name") String name) {

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

        String name = form.getFirst("name");
        String email = form.getFirst("email");
        String profilePic = form.getFirst("profilePicUrl");
        String country = form.containsKey("country") ? form.getFirst("country") : "unknown";

        JsonObject jo = userSvc.createProfile(name, email, country, profilePic);
        response.sendRedirect("/");
        return new ResponseEntity<String>(jo.toString(), HttpStatus.OK);
    }

    @PostMapping(path = "/editprofile", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> editProfile(
            @RequestBody MultiValueMap<String, String> form,
            HttpServletResponse response) throws IOException {
        Map<String, String> m = new HashMap<>();
        String name = form.getFirst("name");

        // Email changes
        m.put("oldEmail", form.getFirst("oldEmail"));
        m.put("formEmail", form.getFirst("email"));

        // Country changes
        m.put("oldCountry", form.getFirst("oldCountry"));
        m.put("formCountry", form.getFirst("country"));

        // ProfilePic changes
        m.put("oldProfilePic", form.getFirst("oldProfilePicUrl"));
        m.put("formProfilePic", form.getFirst("profilePicUrl"));

        // Send info to userService
        userSvc.editUserProfile(name, m);

        response.sendRedirect("/");
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
