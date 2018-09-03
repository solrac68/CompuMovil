package co.edu.udea.compumovil.gr06_20182.labscm20182;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.net.URI;

public class DrinkActivity extends AppCompatActivity {

    ImageView imageView;
    Uri imageUri;
    private EditText txtName;
    private EditText txtPrecio;
    private EditText txtIngredient;
    private TextView txtView;

    SharedPreferences sharedpreferences;
    public static final String mypreference = "mypref";
    public static final String Name = "nameKey";
    public static final String Precio = "precio";
    public static final String Ingredient = "ingredient";
    public static final String ImageUri = "imageUri";


    private static int RESULT_LOAD_IMG = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drink);
        imageView = (ImageView) findViewById(R.id.imgView);
        txtView = (TextView) findViewById(R.id.txtView);
        txtName = (EditText)findViewById(R.id.txtName);
        txtPrecio = (EditText)findViewById(R.id.txtPrecio);
        txtIngredient = (EditText)findViewById(R.id.txtIngredient);

        sharedpreferences = getSharedPreferences(mypreference,
                Context.MODE_PRIVATE);
        if (sharedpreferences.contains(Name)) {
            txtName.setText(sharedpreferences.getString(Name, ""));
        }
        if (sharedpreferences.contains(Precio)) {
            txtPrecio.setText(sharedpreferences.getString(Precio, ""));
        }
        if (sharedpreferences.contains(Ingredient)) {
            txtIngredient.setText(sharedpreferences.getString(Ingredient, ""));
        }

        if (sharedpreferences.contains(ImageUri)) {
            imageUri = Uri.fromFile(new File(sharedpreferences.getString(ImageUri, "")));
            imageView.setImageURI(imageUri);
        }

        loadView();

    }

    private void loadView() {

        StringBuilder sb = new StringBuilder();
        sb.append(getResources().getString(R.string.str_Name))
                .append(":")
                .append(sharedpreferences.getString(Name, ""))
                .append("\n")
                .append(getResources().getString(R.string.str_price))
                .append(":")
                .append(sharedpreferences.getString(Precio, ""))
                .append("\n")
                .append(getResources().getString(R.string.str_ingredients))
                .append(":")
                .append(sharedpreferences.getString(Ingredient, ""));

        txtView.setText(sb);
    }

    public void loadImagefromGallery(View view) {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, RESULT_LOAD_IMG);

        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(Name, txtName.getText().toString());
        editor.putString(Precio, txtPrecio.getText().toString());
        editor.putString(Ingredient, txtIngredient.getText().toString());
        editor.apply();
        loadView();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if (sharedpreferences.contains(ImageUri)) {
            imageUri = Uri.fromFile(new File(sharedpreferences.getString(ImageUri, "")));
            imageView.setImageURI(imageUri);
        }
        if(resultCode == RESULT_OK && requestCode == RESULT_LOAD_IMG){
            imageUri = data.getData();
            imageView.setImageURI(imageUri);

            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString(ImageUri,imageUri.toString());
            editor.apply();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }

    private void exitApplication() {
        System.exit(0);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.id_clean:
                cleanControls();
                return true;
            case R.id.id_exit:
                exitApplication();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void cleanControls() {
        txtName.setText("");
        txtPrecio.setText("");
        txtIngredient.setText("");
        txtView.setText("");
        imageView.setImageURI(Uri.EMPTY);
    }
}
