package co.edu.udea.compumovil.gr06_20182.lab2.tools;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import co.edu.udea.compumovil.gr06_20182.lab2.model.Dish;
import co.edu.udea.compumovil.gr06_20182.lab2.model.Drink;
import co.edu.udea.compumovil.gr06_20182.lab2.model.User;

public class SqliteHelper extends SQLiteOpenHelper {

    private static final String DATABASE = "Restaurant.sqlite";


    public SqliteHelper(Context context){
        super(context, DATABASE, null, 1);
    }

    public void queryData(String sql){
        SQLiteDatabase database = getWritableDatabase();
        database.execSQL(sql);
    }

    public void insertData(User user){
        SQLiteDatabase database = getWritableDatabase();

        String sql = "INSERT INTO " + User.TABLE_NAME + " VALUES (NULL,?,?,?,?)";
        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();;
        statement.bindString(1,user.getName());
        statement.bindString(2,user.getPassword());
        statement.bindString(3,user.getEmail());
        statement.bindBlob(4,user.getImage());
        statement.executeInsert();
        statement.clearBindings();
    }

    public void insertData(Dish dish){
        SQLiteDatabase database = getWritableDatabase();

        String sql = "INSERT INTO " + Dish.TABLE_NAME + " VALUES (NULL,?,?,?,?,?)";
        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();;
        statement.bindString(1,dish.getName());
        statement.bindLong(2,dish.getPrice());
        statement.bindLong(3,dish.getTime_preparation());
        statement.bindLong(4,dish.isFavorite()?1:0);
        statement.bindBlob(5,dish.getImage());
        statement.executeInsert();
        statement.clearBindings();
    }

    public void insertData(Drink drink){
        SQLiteDatabase database = getWritableDatabase();

        String sql = "INSERT INTO " + Drink.TABLE_NAME + " VALUES (NULL,?,?,?,?)";
        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();;
        statement.bindString(1,drink.getName());
        statement.bindLong(2,drink.getPrice());
        statement.bindLong(3,drink.isFavorite()?1:0);
        statement.bindBlob(4,drink.getImage());
        statement.executeInsert();
        statement.clearBindings();
    }

//    public boolean insertDataSecure(User user){
//        SQLiteDatabase database = getWritableDatabase();
//        ContentValues contentValues = new ContentValues();
//
//        contentValues.put(User.COLUMN_NAME,user.getName());
//        contentValues.put(User.COLUMN_PASSWORD,user.getPassword());
//        contentValues.put(User.COLUMN_EMAIL,user.getEmail());
//        contentValues.put(User.COLUMN_IMAGE,user.getImage());
//        long ins = database.insert(User.TABLE_NAME,null,contentValues);
//
//        return !(ins == -1);
//    }

    public Cursor getData(String sql){
        SQLiteDatabase database = getReadableDatabase();
        return database.rawQuery(sql,null);
    }

    public User getUserByEmail(String email){
        User user = null;
        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor = database.query(User.TABLE_NAME,
                new String[]{User.COLUMN_ID,User.COLUMN_NAME,User.COLUMN_PASSWORD,User.COLUMN_EMAIL,User.COLUMN_IMAGE},
                User.COLUMN_EMAIL + "=?",
                new String[]{email},null,null,null,null);

        if(cursor != null){
            cursor.moveToFirst();
            user = new User(
                    cursor.getInt(cursor.getColumnIndex(User.COLUMN_ID)),
                    cursor.getString(cursor.getColumnIndex(User.COLUMN_NAME)),
                    cursor.getString(cursor.getColumnIndex(User.COLUMN_PASSWORD)),
                    cursor.getString(cursor.getColumnIndex(User.COLUMN_EMAIL)),
                    cursor.getBlob(cursor.getColumnIndex(User.COLUMN_IMAGE))
            );
        }

        return user;

    }

    public Dish getDishById(Integer id){
        Dish dish = null;
        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor = database.query(Dish.TABLE_NAME,
                new String[]{Dish.COLUMN_ID,Dish.COLUMN_NAME,Dish.COLUMN_PRICE,Dish.COLUMN_TIME_PREPARATION,Dish.COLUMN_FAVORITE,Dish.COLUMN_IMAGE},
                Dish.COLUMN_ID + "=?",
                new String[]{id.toString()},null,null,null,null);

        if(cursor != null){
            cursor.moveToFirst();
            dish = new Dish(
                    cursor.getInt(cursor.getColumnIndex(Dish.COLUMN_ID)),
                    cursor.getString(cursor.getColumnIndex(Dish.COLUMN_NAME)),
                    cursor.getInt(cursor.getColumnIndex(Dish.COLUMN_PRICE)),
                    cursor.getInt(cursor.getColumnIndex(Dish.COLUMN_TIME_PREPARATION)),
                    cursor.getInt(cursor.getColumnIndex(Dish.COLUMN_FAVORITE))==1,
                    cursor.getBlob(cursor.getColumnIndex(Dish.COLUMN_IMAGE))
            );
        }

        return dish;
    }

    public Drink getDrinkById(Integer id){
        Drink drink = null;
        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor = database.query(Drink.TABLE_NAME,
                new String[]{Drink.COLUMN_ID,Drink.COLUMN_NAME,Drink.COLUMN_PRICE,Drink.COLUMN_FAVORITE,Drink.COLUMN_IMAGE},
                Drink.COLUMN_ID + "=?",
                new String[]{id.toString()},null,null,null,null);

        if(cursor != null){
            cursor.moveToFirst();
            drink = new Drink(
                    cursor.getInt(cursor.getColumnIndex(Drink.COLUMN_ID)),
                    cursor.getString(cursor.getColumnIndex(Drink.COLUMN_NAME)),
                    cursor.getInt(cursor.getColumnIndex(Drink.COLUMN_PRICE)),
                    cursor.getInt(cursor.getColumnIndex(Drink.COLUMN_FAVORITE))==1,
                    cursor.getBlob(cursor.getColumnIndex(Drink.COLUMN_IMAGE))
            );
        }

        return drink;
    }

    public List<Dish> getDishes(){
        List<Dish> dishes = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + Dish.TABLE_NAME + " ORDER BY " + Dish.COLUMN_ID + " DESC";

        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if(cursor.moveToFirst()){
            do{
                Dish dish = new Dish(
                        cursor.getInt(cursor.getColumnIndex(Dish.COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndex(Dish.COLUMN_NAME)),
                        cursor.getInt(cursor.getColumnIndex(Dish.COLUMN_PRICE)),
                        cursor.getInt(cursor.getColumnIndex(Dish.COLUMN_TIME_PREPARATION)),
                        cursor.getInt(cursor.getColumnIndex(Dish.COLUMN_FAVORITE))==1,
                        cursor.getBlob(cursor.getColumnIndex(Dish.COLUMN_IMAGE))
                );
                dishes.add(dish);
            } while(cursor.moveToNext());
        }

        return dishes;
    }

    public List<Drink> getDrinks(){
        List<Drink> drinks = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + Drink.TABLE_NAME + " ORDER BY " + Drink.COLUMN_ID + " DESC";

        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if(cursor.moveToFirst()){
            do{
                Drink dish = new Drink(
                        cursor.getInt(cursor.getColumnIndex(Dish.COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndex(Dish.COLUMN_NAME)),
                        cursor.getInt(cursor.getColumnIndex(Dish.COLUMN_PRICE)),
                        cursor.getInt(cursor.getColumnIndex(Dish.COLUMN_FAVORITE))==1,
                        cursor.getBlob(cursor.getColumnIndex(Dish.COLUMN_IMAGE))
                );
                drinks.add(dish);
            } while(cursor.moveToNext());
        }

        return drinks;
    }

    public int updateDish(Dish dish){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Dish.COLUMN_FAVORITE, String.valueOf(dish.isFavorite()?1:0));
        values.put(Dish.COLUMN_NAME, dish.getName());
        values.put(Dish.COLUMN_IMAGE, dish.getImage());
        values.put(Dish.COLUMN_TIME_PREPARATION, dish.getTime_preparation().toString());
        values.put(Dish.COLUMN_PRICE, dish.getPrice().toString());

        return db.update(Dish.TABLE_NAME, values, Dish.COLUMN_ID + " = ?",
                new String[]{String.valueOf(dish.getId())});
    }

    public int updateDrink(Drink dish){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Drink.COLUMN_FAVORITE, String.valueOf(dish.isFavorite()?1:0));
        values.put(Drink.COLUMN_NAME, dish.getName());
        values.put(Drink.COLUMN_IMAGE, dish.getImage());
        values.put(Drink.COLUMN_PRICE, dish.getPrice().toString());

        return db.update(Drink.TABLE_NAME, values, Drink.COLUMN_ID + " = ?",
                new String[]{String.valueOf(dish.getId())});
    }

    public static byte[] getBitmapAsByteArray(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        return outputStream.toByteArray();
    }

    // Checking if email exists
    public Boolean chkemail(String email){
        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor = database.rawQuery(User.SQL_CHECK_EMAIL,new String[]{email});
        return cursor.getCount() > 0;
    }

    // Checking if email exists
    public Boolean chkemailpassword(String email,String password){
        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor = database.rawQuery(User.SQL_CHECK_EMAIL_PASSWORD,new String[]{email,password});
        return cursor.getCount() > 0;
    }

    public static Bitmap getByteArrayAsBitmap(byte[] imgByte) {
        return BitmapFactory.decodeByteArray(imgByte, 0, imgByte.length);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(User.CREATE_TABLE);
        db.execSQL(Dish.CREATE_TABLE);
        db.execSQL(Drink.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + User.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Dish.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Drink.TABLE_NAME);
    }

    public void deleteTable(String name){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + name);
    }
}
