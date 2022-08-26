package fullstack.vttpfullstackproj.controllers;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
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

    @GetMapping(path = "/menu")
    public String menu(
            @RequestParam(defaultValue = "whiskey", name = "drinkFilter") String ingredient,
            Model model) {
        List<Cocktail> listOfCocktails = apiSvc.fetchDrinksByIngredients(ingredient.toLowerCase());
        model.addAttribute("ingredient", toCaps(ingredient));
        model.addAttribute("listOfCocktails", listOfCocktails);
        return "menu";
    }

    @GetMapping(path = "/drink")
    public String getDrinkById(
            @RequestParam(name = "idDrink") String idDrink,
            Model model) {
        Cocktail cocktail = apiSvc.fetchDrinkById(idDrink);
        model.addAttribute("cocktailDetails", cocktail);
        return "drinkdetails";
    }

    // @GetMapping(path = "/profile")
    // public String profilePage() {
    // return "login";
    // }

    @GetMapping(path = "/profile")
    public String name(
            OAuth2AuthenticationToken token,
            Model model) {

        User currentUser = userSvc.currentUser(token);
        String given_name = currentUser.getGivenName();
        String email = currentUser.getEmail();

        // TO-DO: Check for existing profile. If not found, create.
        Boolean profileExists = userSvc.profileExists(currentUser);

        if (!profileExists)
            userSvc.createUser(currentUser);

        List<String> listOfidDrink = restSvc.getProfile(email);
        List<Cocktail> listOfCocktails = new LinkedList<>();
        for (String id : listOfidDrink) {
            Cocktail cocktail = apiSvc.fetchDrinkById(id);
            listOfCocktails.add(cocktail);
        }

        model.addAttribute("name", given_name);
        model.addAttribute("listOfCocktails", listOfCocktails);
        return "profiledetails";
    }
}
