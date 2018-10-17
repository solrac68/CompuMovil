package co.edu.udea.compumovil.gr06_20182.lab3.model;

public class Dish {
    public static final String TABLE_NAME = "dishes";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_PRICE = "price";
    public static final String COLUMN_TIME_PREPARATION = "time_preparation";
    public static final String COLUMN_IMAGE = "image";
    public static final String COLUMN_FAVORITE = "favorite";
    public static final String COLUMN_TYPE = "type";

    private int id;
    private String name;
    private Integer price;
    private Integer time_preparation;
    private byte[] image;
    private boolean favorite;
    private  String type;

    public static final String CREATE_TABLE =
            "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_NAME + " TEXT,"
                    + COLUMN_PRICE + " INTEGER,"
                    + COLUMN_TIME_PREPARATION + " INTEGER,"
                    + COLUMN_FAVORITE + " INTEGER,"
                    + COLUMN_IMAGE + " BLOB,"
                    + COLUMN_TYPE + " TEXT"
                    + ")";

    public Dish(){

    }

    public Dish(int id,String name, Integer price, int time_preparation,boolean favorite, byte[] image, String type) {
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
