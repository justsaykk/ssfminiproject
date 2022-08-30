package fullstack.vttpfullstackproj.controllers;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
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

    private String toCaps(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    @GetMapping(path = "/menu")
    public String menu(
            @RequestParam(defaultValue = "Scotch", name = "drinkFilter") String ingredient,
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

    @GetMapping(path = "/profile")
    public String profilePage() {
        return "login";
    }

    @PostMapping(path = "/profile")
    public String name(
            @RequestBody MultiValueMap<String, String> form,
            Model model) {

        String name = form.getFirst("name");
        System.out.printf("Accessing %s's profile.\n", name);
        List<String> listOfidDrink = restSvc.getProfile(name);
        List<Cocktail> listOfCocktails = new LinkedList<>();
        for (String id : listOfidDrink) {
            Cocktail cocktail = apiSvc.fetchDrinkById(id);
            listOfCocktails.add(cocktail);
        }

        model.addAttribute("name", toCaps(name.toLowerCase()));
        model.addAttribute("listOfCocktails", listOfCocktails);
        return "profiledetails";
    }
}
