package fullstack.vttpfullstackproj.services;

import java.io.Reader;
import java.io.StringReader;
import java.util.LinkedList;
import java.util.List;

import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import fullstack.vttpfullstackproj.models.Cocktail;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

@Service
public class ApiService {

    private ResponseEntity<String> fetch(String url) {

        RestTemplate template = new RestTemplate();
        RequestEntity<Void> req = RequestEntity.get(url).build();

        try {
            ResponseEntity<String> res = template.exchange(req, String.class);
            return res;
        } catch (Exception e) {
            System.err.print(e);
            return null;
        }
    }

    private JsonObject readApiResponse(ResponseEntity<String> apiResponse) {
        String s = apiResponse.getBody();
        Reader reader = new StringReader(s);
        JsonReader jr = Json.createReader(reader);
        return jr.readObject();
    }

    public List<Cocktail> fetchDrinksByIngredients(String ingredient) {
        // Build API call URL
        String uri = "https://www.thecocktaildb.com/api/json/v1/1/filter.php";
        String url = UriComponentsBuilder.fromUriString(uri)
                .queryParam("i", ingredient.toLowerCase())
                .toUriString();

        ResponseEntity<String> apiResponse = fetch(url);
        JsonObject jo = readApiResponse(apiResponse);
        JsonArray jsonArray = jo.getJsonArray("drinks");

        // Manipulating output
        List<Cocktail> listOfCocktails = new LinkedList<>();
        for (int i = 0; i < jsonArray.size(); i++) {
            Cocktail n = new Cocktail();
            JsonObject jsonDrinkElement = jsonArray.getJsonObject(i);
            listOfCocktails.add(n.createSimpleCocktail(jsonDrinkElement));
        }

        return listOfCocktails;
    }

    public Cocktail fetchDrinkById(String id) {
        // Build API Call URL:
        String uri = "https://www.thecocktaildb.com/api/json/v1/1/lookup.php";
        String url = UriComponentsBuilder.fromUriString(uri)
                .queryParam("i", id)
                .toUriString();

        // Manipulate Response:
        Cocktail n = new Cocktail();
        ResponseEntity<String> apiResponse = fetch(url);
        JsonObject jo = readApiResponse(apiResponse);
        JsonArray jsonArray = jo.getJsonArray("drinks");
        JsonObject jsonDrinkElement = jsonArray.getJsonObject(0);
        Cocktail newDetailedCocktail = n.createDetailedCocktail(jsonDrinkElement);
        return newDetailedCocktail;
    }
}