package co.edu.udea.compumovil.gr06_20182.lab4.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DrinksDto {

    @SerializedName("drinks")
    @Expose
    private List<DrinkDto> drinks = null;

    public List<DrinkDto> getDrinks() {
        return drinks;
    }

    public void setDrinks(List<DrinkDto> drinks) {
        this.drinks = drinks;
    }

}
