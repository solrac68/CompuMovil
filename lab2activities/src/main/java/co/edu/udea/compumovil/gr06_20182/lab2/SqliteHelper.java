package co.edu.udea.compumovil.gr06_20182.lab2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;

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

    public Bitmap getImage(int i){
        SQLiteDatabase db = getReadableDatabase();
        String qu = "select img  from table where feedid=" + i ;
        Cursor cur = db.rawQuery(qu, null);

        if (cur.moveToFirst()){
            byte[] imgByte = cur.getBlob(0);
            cur.close();
            return BitmapFactory.decodeByteArray(imgByte, 0, imgByte.length);
        }
        if (cur != null && !cur.isClosed()) {
            cur.close();
        }

        return null ;
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
