package co.edu.udea.compumovil.gr06_20182.lab4.activities;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import co.edu.udea.compumovil.gr06_20182.lab4.R;
import co.edu.udea.compumovil.gr06_20182.lab4.model.User;
import co.edu.udea.compumovil.gr06_20182.lab4.model.Validation;

public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = "RegisterActivity";  //Activity {
    EditText edtPassword,edtEmail;
    Button btnImage,btnRegistry;
    //ImageView imageView;
    TextView loginScreen;
    User user;
    FirebaseAuth firebasAuth;
    FirebaseAuth.AuthStateListener authStateListener;


    final int REQUEST_CODE_GALLERY = 999;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set View to register.xml
        setContentView(R.layout.register);

        Init();

        // Listening to Login Screen link
        loginScreen.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                // Closing registration screen
                // Switching to Login Screen/closing register screen
                finish();
            }
        });



        btnRegistry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Validation val = validate();

                if (!val.isEstate()){
                    Toast.makeText(RegisterActivity.this,val.getMessage(), Toast.LENGTH_SHORT).show();
                    return;
                }

                createAccount(edtEmail.getText().toString(),edtPassword.getText().toString());


            }
        });

    }
    private Validation validate(){
        Validation val = new Validation();
        val.setEstate(true);
        val.setMessage("");

        if(edtEmail.getText().toString().trim().length() < 6){
            val.setEstate(false);
            val.setMessage(getString(R.string.without_email));
        }else if(edtPassword.getText().toString().trim().length() == 0){
            val.setEstate(false);
            val.setMessage(getString(R.string.without_password));
        }

        return val;
    }

    private void Init(){
        edtPassword= (EditText)findViewById(R.id.edtPassword);
        edtEmail = (EditText)findViewById(R.id.edtEmail);
        //btnImage = (Button) findViewById(R.id.btnImage);
        btnRegistry = (Button)findViewById(R.id.btnRegistry);
        //imageView = (ImageView) findViewById(R.id.imageView);
        loginScreen = (TextView) findViewById(R.id.link_to_login);

        //sqliteHelper = new SqliteHelper(this);
        firebasAuth = FirebaseAuth.getInstance();

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if(firebaseUser != null){
                    Log.w(TAG,"onAuthStateChanged - signed_in" + firebaseUser.getUid());
                    Log.w(TAG,"onAuthStateChanged - signed_in" + firebaseUser.getEmail());
                }
                else{
                    Log.w(TAG,"onAuthStateChanged - signed_out");
                }

            }
        };
    }


    private void createAccount(final String email,final String password){
        firebasAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(RegisterActivity.this,"La creación de la cuenta fue exitosa",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(RegisterActivity.this,"La creación de la cuenta no fue exitosa",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    @Override
    protected void onStart(){
        super.onStart();
        firebasAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop(){
        super.onStop();
        firebasAuth.removeAuthStateListener(authStateListener);
    }


}