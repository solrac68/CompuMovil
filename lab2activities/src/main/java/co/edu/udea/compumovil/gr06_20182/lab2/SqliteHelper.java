package co.edu.udea.compumovil.gr06_20182.lab2;

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

    public SqliteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public void queryData(String sql){
        SQLiteDatabase database = getWritableDatabase();
        database.execSQL(sql);
    }

    public void insertData(User usuario){
        SQLiteDatabase database = getWritableDatabase();
        String sql = "INSERT INTO " + User.TABLE_NAME + " VALUES (NULL,?,?,?,?)";
        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();;
        statement.bindString(1,usuario.getName());
        statement.bindString(1,usuario.getPassword());
        statement.bindString(1,usuario.getEmail());
        statement.bindBlob(1,getBitmapAsByteArray(usuario.getImage()));
        statement.executeInsert();
        statement.clearBindings();
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

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
