package fullstack.vttpfullstackproj.models;

// import java.util.Map;

import jakarta.json.JsonObject;

public class Drink {
    private String idDrink;
    private String strDrink;
    private String strDrinkThumb;
    private String strDrinkImage;

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

    public Drink(JsonObject jo) {
        this.idDrink = jo.getString("idDrink");
        this.strDrink = jo.getString("strDrink");
        this.strDrinkThumb = jo.getString("strDrinkThumb") + "/preview";
        this.strDrinkImage = jo.getString("strDrinkThumb");
    }
}
