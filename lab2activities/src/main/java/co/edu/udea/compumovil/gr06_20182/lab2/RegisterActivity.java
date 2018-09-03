package co.edu.udea.compumovil.gr06_20182.lab2;

import android.Manifest;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import co.edu.udea.compumovil.gr06_20182.lab2.model.User;

public class RegisterActivity extends AppCompatActivity {
    EditText edtName,edtPassword,edtEmail;
    Button btnImage,btnRegistry;
    ImageView imageView;

    final int REQUEST_CODE_GALLERY = 999;

    public static  SqliteHelper sqliteHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set View to register.xml
        setContentView(R.layout.register);

        TextView loginScreen = (TextView) findViewById(R.id.link_to_login);

        // Listening to Login Screen link
        loginScreen.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                // Closing registration screen
                // Switching to Login Screen/closing register screen
                finish();
            }
        });
        Init();
        sqliteHelper = new SqliteHelper(this,"Restaurant.sqlite",null,1);
        sqliteHelper.queryData(User.CREATE_TABLE);

        btnImage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                ActivityCompat.requestPermissions(
                        RegisterActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},REQUEST_CODE_GALLERY
                );
            }
        });

    }

    private void Init(){
        edtName = findViewById(R.id.edtName);
        edtPassword= findViewById(R.id.edtPassword);
        edtEmail = findViewById(R.id.edtEmail);
        btnImage = findViewById(R.id.btnImage);
        btnRegistry = findViewById(R.id.btnRegistry);
        imageView = findViewById(R.id.imageView);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {

    }
}