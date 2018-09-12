package co.edu.udea.compumovil.gr06_20182.lab2.tools;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import java.io.ByteArrayOutputStream;
import java.security.PublicKey;

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

    public boolean insertDataSecure(User user){
        SQLiteDatabase database = getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(User.COLUMN_NAME,user.getName());
        contentValues.put(User.COLUMN_PASSWORD,user.getPassword());
        contentValues.put(User.COLUMN_EMAIL,user.getEmail());
        contentValues.put(User.COLUMN_IMAGE,user.getImage());
        long ins = database.insert(User.TABLE_NAME,null,contentValues);

        return !(ins == -1);
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
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + User.TABLE_NAME);
    }
}
