package fullstack.vttpfullstackproj.controllers;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import fullstack.vttpfullstackproj.models.*;
import fullstack.vttpfullstackproj.services.*;

@Controller
@RequestMapping
public class FrontEndController {

    @Autowired
    private ApiService apiSvc;

    @GetMapping(path = "/menu")
    public String menu(Model model) {
        List<Cocktail> listOfCocktails = apiSvc.fetchDrinksByIngredients("Gin");
        model.addAttribute("listOfCocktails", listOfCocktails);
        return "menu";
    }
}
