package co.edu.udea.compumovil.gr06_20182.lab4.activities;

//import android.app.Activity;
import android.content.Intent;
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
import co.edu.udea.compumovil.gr06_20182.lab4.tools.Helper;
import co.edu.udea.compumovil.gr06_20182.lab4.tools.SessionManager;
import co.edu.udea.compumovil.gr06_20182.lab4.tools.SqliteHelper;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";

    // Controls in this form
    EditText email,password;
    Button btnLogin;
    TextView registerScreen;

    static SqliteHelper sqliteHelper;

    // Session Manager Class
    SessionManager session;

    FirebaseAuth firebasAuth;
    FirebaseAuth.AuthStateListener authStateListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setting default screen to login.xml
        setContentView(R.layout.login);


        init();
        initEvents();

    }

    private void init(){
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        btnLogin = findViewById(R.id.btnLogin);
        registerScreen = findViewById(R.id.link_to_register);
        sqliteHelper = new SqliteHelper(this);
        session = new SessionManager(getApplicationContext());

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

    private void initEvents(){
        // Listening to register new account link
        registerScreen.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // Switching to Register screen
                Intent i = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(i);
            }
        });

        // Listening to login
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String s1 = email.getText().toString().trim();
                String s2 = password.getText().toString().trim();
                Validation val = validate();
                if (!val.isEstate()){
                    Toast.makeText(LoginActivity.this,val.getMessage(), Toast.LENGTH_SHORT).show();
                    return;
                }
                else{
                    signIn(s1, s2);
                }
            }
        });
    }

    private void signIn(final String email, final String password){
        firebasAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this,new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(LoginActivity.this,"El logeo de la cuenta fue exitosa",Toast.LENGTH_SHORT).show();
                    //String time = Helper.getMetaData(getApplicationContext(), "key_time");
                    //Toast.makeText(getApplicationContext(), time,Toast.LENGTH_SHORT).show();
                    session.createLoginSession(email,password);
//
                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(i);
                    finish();
                }
                else
                {
                    Toast.makeText(LoginActivity.this,getString(R.string.nok_user_authenticate),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private Validation validate(){
        Validation val = new Validation();
        val.setEstate(true);
        val.setMessage("");

        if(email.getText().toString().trim().length() < 6){
            val.setEstate(false);
            val.setMessage(getString(R.string.without_email));
        }else if(password.getText().toString().trim().length() == 0){
            val.setEstate(false);
            val.setMessage(getString(R.string.without_password));
        }

        return val;
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