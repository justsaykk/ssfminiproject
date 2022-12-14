package fullstack.vttpfullstackproj.controllers;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import fullstack.vttpfullstackproj.models.*;
import fullstack.vttpfullstackproj.services.*;

@Controller
@RequestMapping
public class FrontEndController {

    @Autowired
    private ApiService apiSvc;

    @Autowired
    private RESTService restSvc;

    @Autowired
    private UserService userSvc;

    private String toCaps(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    @GetMapping(path = "/login")
    public String login() {
        return "login";
    }

    @GetMapping(path = "/")
    public String index(Model model, @AuthenticationPrincipal OAuth2User user) {
        if (null == user) {
            model.addAttribute("loggedin", false);
            return "index";
        }

        User curUser = new User(user);
        if (!userSvc.userExists(curUser)) {
            userSvc.create(curUser);
        }

        model.addAttribute("loggedin", true);
        model.addAttribute("name", userSvc.getNamefromEmail(curUser.getEmail()));
        return "index";
    }

    @GetMapping(path = "/menu")
    public String menu(
            @RequestParam(defaultValue = "Scotch", name = "drinkFilter") String ingredient,
            @AuthenticationPrincipal OAuth2User user,
            Model model) {
        if (null == user) {
            model.addAttribute("loggedin", false);
        } else {
            User curUser = new User(user);
            if (!userSvc.userExists(curUser)) {
                userSvc.create(curUser);
            }
            model.addAttribute("loggedin", true);
            model.addAttribute("name", userSvc.getNamefromEmail(curUser.getEmail()));
        }
        String searchTerm = ingredient.toLowerCase().replaceAll(" ", "+");
        List<Drink> listOfCocktails = apiSvc.fetchDrinksByIngredients(searchTerm);
        model.addAttribute("ingredient", toCaps(ingredient.toLowerCase()));
        model.addAttribute("listOfCocktails", listOfCocktails);
        model.addAttribute("emptyListOfCocktails", (listOfCocktails.isEmpty()) ? "true" : "false");
        return "menu";
    }

    @GetMapping(path = "/drinkname")
    public String search(
            @RequestParam(name = "drinkName") String drinkName,
            @AuthenticationPrincipal OAuth2User user,
            Model model) {

        if (null == user) {
            model.addAttribute("loggedin", false);
        } else {
            User curUser = new User(user);
            if (!userSvc.userExists(curUser)) {
                userSvc.create(curUser);
            }
            model.addAttribute("loggedin", true);
            model.addAttribute("name", userSvc.getNamefromEmail(curUser.getEmail()));
        }
        String searchTerm = drinkName.toLowerCase().replaceAll(" ", "+");
        List<Drink> listOfCocktails = apiSvc.fetchDrinksByName(searchTerm);
        model.addAttribute("ingredient", toCaps(drinkName.toLowerCase()));
        model.addAttribute("listOfCocktails", listOfCocktails);
        model.addAttribute("emptyListOfCocktails", (listOfCocktails.isEmpty()) ? "true" : "false");
        return "menu";
    }

    @GetMapping(path = "/drink")
    public String getDrinkById(
            @RequestParam(name = "idDrink") String idDrink,
            @RequestParam(name = "successful", required = false) String successful,
            @AuthenticationPrincipal OAuth2User user,
            Model model) {

        if (null == user) {
            model.addAttribute("loggedin", false);
        } else {
            User curUser = new User(user);
            if (!userSvc.userExists(curUser)) {
                userSvc.create(curUser);
            }
            model.addAttribute("loggedin", true);
            model.addAttribute("name", userSvc.getNamefromEmail(curUser.getEmail()));
        }

        Detaileddrink cocktail = apiSvc.fetchDrinkById(idDrink);
        String message;
        if (null != successful) {
            switch (successful) {
                case "true":
                    message = "Drink Added!";
                    model.addAttribute("successful", successful);
                    model.addAttribute("message", message);
                    break;
                case "false":
                    message = "Duplicated entry, please add another drink";
                    model.addAttribute("successful", successful);
                    model.addAttribute("message", message);
                    break;
                default:
                    break;
            }
        }
        model.addAttribute("cocktailDetails", cocktail);
        return "drinkdetails";
    }

    @GetMapping(path = "/profile/{name}")
    public String getProfileDetails(
            @PathVariable(value = "name") String rawName,
            Model model) {

        String name = rawName.toLowerCase();
        List<String> listOfidDrink = restSvc.getProfile(name);

        // Get list of drinks from name
        List<Drink> listOfCocktails = new LinkedList<>();
        for (String id : listOfidDrink) {
            Drink cocktail = apiSvc.fetchDrinkById(id);
            listOfCocktails.add(cocktail);
        }

        // Get user details
        String email = userSvc.getEmailfromName(name);
        Map<String, String> profileDetails = userSvc.getUser(email).toMap();

        // Add to model
        model.addAttribute("loggedin", true);
        model.addAttribute("email", email);
        model.addAttribute("profileDetails", profileDetails);
        model.addAttribute("name", toCaps(name.toLowerCase()));
        model.addAttribute("listOfCocktails", listOfCocktails);
        model.addAttribute("emptyListOfCocktails", (listOfCocktails.isEmpty()) ? "true" : "false");
        return "profiledetails";
    }

    @PostMapping(path = "/profile/{email}/edit")
    public String editProfile(
            @PathVariable(value = "email") String email,
            Model model) {
        Map<String, String> profileDetails = userSvc.getUser(email).toMap();
        Country country = new Country();
        String[] listOfCountries = country.getCountries();

        model.addAttribute("loggedin", true);
        model.addAttribute("listOfCountries", listOfCountries);
        model.addAttribute("profileDetails", profileDetails);
        return "editprofile";
    }
}
