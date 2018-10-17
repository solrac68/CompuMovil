package co.edu.udea.compumovil.gr06_20182.lab3.model;

public class Drink {
    public static final String TABLE_NAME = "drinks";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_PRICE = "price";
    public static final String COLUMN_IMAGE = "image";
    public static final String COLUMN_FAVORITE = "favorite";

    private int id;
    private String name;
    private Float price;
    private byte[] image;
    private boolean favorite;

    public static final String CREATE_TABLE =
            "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_NAME + " TEXT,"
                    + COLUMN_PRICE + " REAL,"
                    + COLUMN_FAVORITE + " INTEGER,"
                    + COLUMN_IMAGE + " BLOB"
                    + ")";

    public Drink(){

    }

    public Drink(int id, String name, Float price, boolean favorite, byte[] image) {
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

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }
}
