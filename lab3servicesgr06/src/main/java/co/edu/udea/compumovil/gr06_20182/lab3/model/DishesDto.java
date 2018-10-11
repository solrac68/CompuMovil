package co.edu.udea.compumovil.gr06_20182.lab3.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DishesDto {

    @SerializedName("foods")
    @Expose
    private List<DishDto> foods = null;

    public List<DishDto> getFoods() {
        return foods;
    }

    public void setFoods(List<DishDto> foods) {
        this.foods = foods;
    }

}
