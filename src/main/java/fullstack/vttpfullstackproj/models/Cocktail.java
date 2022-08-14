package fullstack.vttpfullstackproj.models;

import jakarta.json.JsonObject;

public class Cocktail {
    private String idDrink;
    private String strDrink;
    private String strDrinkThumb;

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

    public Cocktail createCocktail(JsonObject jo) {
        Cocktail n = new Cocktail();
        n.setIdDrink(jo.getString("idDrink"));
        n.setStrDrink(jo.getString("strDrink"));
        n.setStrDrinkThumb(jo.getString("strDrinkThumb"));
        return n;
    }

}
