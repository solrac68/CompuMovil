package co.edu.udea.compumovil.gr06_20182.lab3.activities;

import android.Manifest;
//import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.InputStream;

import co.edu.udea.compumovil.gr06_20182.lab3.R;
import co.edu.udea.compumovil.gr06_20182.lab3.model.User;
import co.edu.udea.compumovil.gr06_20182.lab3.model.Validation;
import co.edu.udea.compumovil.gr06_20182.lab3.tools.ImageHelper;
import co.edu.udea.compumovil.gr06_20182.lab3.tools.SqliteHelper;

public class RegisterActivity extends AppCompatActivity {  //Activity {
    EditText edtName,edtPassword,edtEmail;
    Button btnImage,btnRegistry;
    ImageView imageView;
    User user;

    final int REQUEST_CODE_GALLERY = 999;

    public static SqliteHelper sqliteHelper;

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

        sqliteHelper = new SqliteHelper(this);

        btnImage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                ActivityCompat.requestPermissions(
                        RegisterActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},REQUEST_CODE_GALLERY
                );

            }
        });

        btnRegistry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user = new User();
                Validation val = validate();
                if (!val.isEstate()){
                    Toast.makeText(RegisterActivity.this,val.getMessage(), Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!sqliteHelper.chkemail(edtEmail.getText().toString().trim())){
                    user.setName(edtName.getText().toString().trim());
                    user.setEmail(edtEmail.getText().toString().trim());
                    user.setPassword(edtPassword.getText().toString().trim());
                    try{
                        user.setImage(ImageHelper.imageViewToByte(imageView));
                    }catch(Exception e){
                        user.setImage(SqliteHelper.getBitmapAsByteArray(BitmapFactory.decodeResource(getResources(),R.drawable.user2)));
                    }

                    try {
                        sqliteHelper.insertData(user);
                        Toast.makeText(RegisterActivity.this, getString(R.string.ok_insert), Toast.LENGTH_SHORT).show();
                        finish();
                    }catch (Exception ex){
                        ex.printStackTrace();
                    }
                }
                else{
                    Toast.makeText(RegisterActivity.this, getString(R.string.email_exist), Toast.LENGTH_SHORT).show();
                }


            }
        });

    }
    private Validation validate(){
        Validation val = new Validation();
        val.setEstate(true);
        val.setMessage("");

        if (edtName.getText().toString().trim().length() == 0){
            val.setEstate(false);
            val.setMessage(getString(R.string.without_name));
        }else if(edtEmail.getText().toString().trim().length() == 0){
            val.setEstate(false);
            val.setMessage(getString(R.string.without_email));
        }else if(edtPassword.getText().toString().trim().length() == 0){
            val.setEstate(false);
            val.setMessage(getString(R.string.without_password));
        }

        return val;
    }

    private void Init(){
        edtName = (EditText) findViewById(R.id.edtName);
        edtPassword= (EditText)findViewById(R.id.edtPassword);
        edtEmail = (EditText)findViewById(R.id.edtEmail);
        btnImage = (Button) findViewById(R.id.btnImage);
        btnRegistry = (Button)findViewById(R.id.btnRegistry);
        imageView = (ImageView) findViewById(R.id.imageView);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        if(requestCode == REQUEST_CODE_GALLERY){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,REQUEST_CODE_GALLERY);
            }
            else{
                Toast.makeText(this.getApplicationContext(),getString(R.string.without_access_security),Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode,permissions,grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == REQUEST_CODE_GALLERY && resultCode == RESULT_OK && data != null){
            Uri uri = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                imageView.setImageBitmap(bitmap);
            }
            catch (FileNotFoundException e){
                e.printStackTrace();
            }

        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    //https://www.youtube.com/watch?v=4bU9cZsJRLI
    //Minuto 8:35
}