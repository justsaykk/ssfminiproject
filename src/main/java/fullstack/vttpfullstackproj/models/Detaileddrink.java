package fullstack.vttpfullstackproj.models;

import java.util.HashMap;
import java.util.Map;

import jakarta.json.JsonObject;
import jakarta.json.JsonValue;

public class Detaileddrink extends Drink {
    private String strInstructions;
    private Map<String, String> ingredients;

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

    public Detaileddrink(JsonObject jo) {
        super(jo);
        Map<String, String> map = new HashMap<>();
        this.strInstructions = (jo.getString("strInstructions"));

        for (int i = 1; i < 16; i++) {
            String strKey = "strIngredient" + Integer.toString(i);
            String strValue = "strMeasure" + Integer.toString(i);
            JsonValue key = jo.get(strKey);
            JsonValue value = jo.get(strValue);
            if (value.toString().equals("null")) {
                break;
            }
            map.put(key.toString().replaceAll("\"", ""),
                    value.toString().replaceAll("\"", ""));
        }
        this.ingredients = map;
    }
}
