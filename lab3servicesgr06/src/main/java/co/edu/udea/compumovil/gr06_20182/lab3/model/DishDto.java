package co.edu.udea.compumovil.gr06_20182.lab3.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DishDto {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("schedule")
    @Expose
    private List<Boolean> schedule = null;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("time")
    @Expose
    private Integer time;
    @SerializedName("price")
    @Expose
    private Integer price;
    @SerializedName("currency")
    @Expose
    private String currency;
    @SerializedName("Ingredients")
    @Expose
    private List<String> ingredients = null;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<Boolean> getSchedule() {
        return schedule;
    }

    public void setSchedule(List<Boolean> schedule) {
        this.schedule = schedule;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }

}
