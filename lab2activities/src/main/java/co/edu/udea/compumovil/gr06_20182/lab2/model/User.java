package co.edu.udea.compumovil.gr06_20182.lab2.model;

import android.graphics.Bitmap;

public class User {
    public static final String TABLE_NAME = "usuarios";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_IMAGE = "image";
    public static final String COLUMN_TIMESTAMP = "timestamp";

    private String name;
    private String password;
    private String email;
    private byte[] image;
    // Create table SQL query

    public static final String CREATE_TABLE =
            "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_NAME + " TEXT,"
                    + COLUMN_PASSWORD + " TEXT,"
                    + COLUMN_EMAIL + " TEXT,"
                    + COLUMN_IMAGE + " BLOB"
                    + ")";
    public static final String SQL_CHECK_EMAIL =
            "SELECT * FROM "+User.TABLE_NAME+ " where " + User.COLUMN_EMAIL + "=?";

    public static final String SQL_CHECK_EMAIL_PASSWORD =
            "SELECT * FROM "+User.TABLE_NAME+ " where " + User.COLUMN_EMAIL + "=? AND " + User.COLUMN_PASSWORD + "=?";

    public User() {
    }

    public User(String name, String password, String email, byte[] image) {
        this.name = name;
        this.password = password;
        this.email = email;
        this.image = image;
    }

    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }







}
