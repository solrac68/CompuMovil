package co.edu.udea.compumovil.gr06_20182.lab2.tools;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.widget.ImageView;

public class ImageHelper {
    public static byte[] imageViewToByte(ImageView image){
        Bitmap bitmap = ((BitmapDrawable)image.getDrawable()).getBitmap();
        return SqliteHelper.getBitmapAsByteArray(bitmap);
    }
}
