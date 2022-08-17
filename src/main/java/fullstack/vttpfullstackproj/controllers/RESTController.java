package fullstack.vttpfullstackproj.controllers;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fullstack.vttpfullstackproj.models.Cocktail;
import fullstack.vttpfullstackproj.services.ApiService;
import fullstack.vttpfullstackproj.services.RESTService;
import jakarta.json.Json;

@RestController
@RequestMapping(path = "/api")
public class RESTController {

    @Autowired
    private RESTService restSvc;

    @Autowired
    private ApiService apiSvc;

    @PostMapping(path = "/adddrink")
    public ResponseEntity<String> addDrink(
            @RequestBody MultiValueMap<String, String> form,
            HttpServletResponse response) throws IOException {
        String name = form.getFirst("name");
        String idDrink = form.getFirst("idDrink");
        Boolean add = restSvc.addDrink(name, idDrink);

        if (!add) {
            String body = Json.createObjectBuilder()
                    .add("successfullyAdded", false)
                    .add("reason", "Duplicated addition")
                    .add(name, idDrink)
                    .build().toString();
            return new ResponseEntity<String>(body, HttpStatus.BAD_REQUEST);
        } else {
            response.sendRedirect("/drink?idDrink=%s".formatted(idDrink));
            return new ResponseEntity<String>(HttpStatus.OK);
        }
    }

    @PostMapping(path = "/profile/{name}")
    public ResponseEntity<String> getProfile(
            @PathVariable(value = "name") String name) {

        List<String> listOfidDrink = restSvc.getProfile(name);
        List<Cocktail> listOfCocktails = new LinkedList<>();

        for (String id : listOfidDrink) {
            Cocktail cocktail = apiSvc.fetchDrinkById(id);
            listOfCocktails.add(cocktail);
        }
        // TO-DO: Build Json Object from this and return it.
        return null;
    }
}
