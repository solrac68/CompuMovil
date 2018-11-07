package co.edu.udea.compumovil.gr06_20182.lab4.tools;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;

public class ImageHelper {
    public static byte[] imageViewToByte(ImageView image){
        Bitmap bitmap;

        image.setDrawingCacheEnabled(true);
        image.buildDrawingCache();

        bitmap = ((BitmapDrawable) image.getDrawable()).getBitmap();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);

        return outputStream.toByteArray();
    }

    public static byte[] getBitmapAsByteArray(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        return outputStream.toByteArray();
    }
}
