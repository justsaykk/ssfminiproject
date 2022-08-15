package fullstack.vttpfullstackproj.controllers;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import fullstack.vttpfullstackproj.models.*;
import fullstack.vttpfullstackproj.services.*;

@Controller
@RequestMapping
public class FrontEndController {

    @Autowired
    private ApiService apiSvc;

    @GetMapping(path = "/menu")
    public String menu(
            @RequestParam(defaultValue = "whiskey", name = "drinkFilter") String ingredient,
            Model model) {
        List<Cocktail> listOfCocktails = apiSvc.fetchDrinksByIngredients(ingredient.toLowerCase());
        model.addAttribute("listOfCocktails", listOfCocktails);
        return "menu";
    }

    @GetMapping(path = "/drink")
    public String getDrinkById(
            @RequestParam(name = "idDrink") String idDrink,
            Model model) {
        Cocktail cocktail = apiSvc.fetchDrinkById(idDrink);
        System.out.println("Adding this id to model " + cocktail.getIdDrink());
        model.addAttribute("cocktailDetails", cocktail);
        return "drinkdetails";
    }

}
