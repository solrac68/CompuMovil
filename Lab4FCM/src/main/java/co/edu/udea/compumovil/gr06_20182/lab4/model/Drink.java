package co.edu.udea.compumovil.gr06_20182.lab4.model;

public class Drink {

    private int id;
    private String name;
    private Float price;
    private String image;
    private boolean favorite;

    public Drink(){

    }

    public Drink(int id, String name, Float price, boolean favorite, String image) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.favorite = favorite;
        this.image = image;
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

    public Float getPrice() {
        return price;
    }

    public String getStrPrice() {
        return price.toString() + " pesos";
    }

    public void setPrice(Float price) {
        this.price = price;
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
}
