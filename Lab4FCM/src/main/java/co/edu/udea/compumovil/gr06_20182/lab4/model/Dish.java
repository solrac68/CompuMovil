package co.edu.udea.compumovil.gr06_20182.lab4.model;

public class Dish {

    private int id;
    private String name;
    private Integer price;
    private Integer time_preparation;
    private String image;
    private boolean favorite;
    private  String type;

    public Dish(){

    }

    public Dish(int id,String name, Integer price, int time_preparation,boolean favorite, String image, String type) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.time_preparation = time_preparation;
        this.favorite = favorite;
        this.image = image;
        this.type = type;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPrice() {
        return price;
    }

    public String getStrPrice() {
        return price.toString() + " pesos";
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getTime_preparation() {
        return time_preparation;
    }

    public String getStrTime_preparation() {
        return time_preparation.toString() + " min";
    }

    public void setTime_preparation(int time_preparation) {
        this.time_preparation = time_preparation;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
