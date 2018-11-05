package co.edu.udea.compumovil.gr06_20182.lab4.tools;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


import co.edu.udea.compumovil.gr06_20182.lab4.model.Dish;
import co.edu.udea.compumovil.gr06_20182.lab4.model.Drink;
import co.edu.udea.compumovil.gr06_20182.lab4.model.User;

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



    public static byte[] getBitmapAsByteArray(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        return outputStream.toByteArray();
    }



    public static Bitmap getByteArrayAsBitmap(byte[] imgByte) {
        return BitmapFactory.decodeByteArray(imgByte, 0, imgByte.length);
    }

    public static byte[] getBitmapAsByteArrayFromUrl(String urlBmp) {
        byte[] bitimage = null;
        try{
            URL url = new URL(urlBmp);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream inputStream = connection.getInputStream();
            Bitmap image = BitmapFactory.decodeStream(inputStream);
            bitimage = getBitmapAsByteArray(image);

        }catch (IOException e){
            Log.d("getBitmapAsByteArrayFU", "onFailure: " + e.getMessage());
        }
        return bitimage;
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
