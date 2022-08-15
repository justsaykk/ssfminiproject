package fullstack.vttpfullstackproj.models;

import java.util.HashMap;
import java.util.Map;

import jakarta.json.JsonObject;
import jakarta.json.JsonValue;

public class Cocktail {
    private String idDrink;
    private String strDrink;
    private String strDrinkThumb;
    private String strDrinkImage;
    private String strInstructions;
    private Map<String, String> ingredients;

    public String getIdDrink() {
        return idDrink;
    }

    public void setIdDrink(String idDrink) {
        this.idDrink = idDrink;
    }

    public String getStrDrink() {
        return strDrink;
    }

    public void setStrDrink(String strDrink) {
        this.strDrink = strDrink;
    }

    public String getStrDrinkThumb() {
        return strDrinkThumb;
    }

    public void setStrDrinkThumb(String strDrinkThumb) {
        this.strDrinkThumb = strDrinkThumb;
    }

    public String getStrDrinkImage() {
        return strDrinkImage;
    }

    public void setStrDrinkImage(String strDrinkImage) {
        this.strDrinkImage = strDrinkImage;
    }

    public String getStrInstructions() {
        return strInstructions;
    }

    public void setStrInstructions(String strInstructions) {
        this.strInstructions = strInstructions;
    }

    public Map<String, String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(Map<String, String> ingredients) {
        this.ingredients = ingredients;
    }

    public Cocktail createSimpleCocktail(JsonObject jo) {
        Cocktail n = new Cocktail();
        n.setIdDrink(jo.getString("idDrink"));
        n.setStrDrink(jo.getString("strDrink"));
        n.setStrDrinkThumb(jo.getString("strDrinkThumb") + "/preview");
        n.setStrDrinkImage(jo.getString("strDrinkThumb"));
        return n;
    }

    public Cocktail createDetailedCocktail(JsonObject jo) {
        Cocktail n = new Cocktail();
        Map<String, String> map = new HashMap<>();
        n.setIdDrink(jo.getString("idDrink"));
        n.setStrDrink(jo.getString("strDrink"));
        n.setStrDrinkThumb(jo.getString("strDrinkThumb") + "/preview");
        n.setStrDrinkImage(jo.getString("strDrinkThumb"));
        n.setStrInstructions(jo.getString("strInstructions"));

        System.out.printf("just set id: %s\n", n.getIdDrink());

        for (int i = 1; i < 16; i++) {
            String strKey = "strIngredient" + Integer.toString(i);
            String strValue = "strMeasure" + Integer.toString(i);
            JsonValue key = jo.get(strKey);
            JsonValue value = jo.get(strValue);
            map.put(key.toString(), value.toString());
        }
        n.setIngredients(map);
        return n;
    }
}
