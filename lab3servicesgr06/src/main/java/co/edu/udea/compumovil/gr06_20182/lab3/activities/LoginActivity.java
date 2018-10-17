package co.edu.udea.compumovil.gr06_20182.lab3.activities;

//import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import co.edu.udea.compumovil.gr06_20182.lab3.R;
import co.edu.udea.compumovil.gr06_20182.lab3.model.User;
import co.edu.udea.compumovil.gr06_20182.lab3.tools.Helper;
import co.edu.udea.compumovil.gr06_20182.lab3.tools.SessionManager;
import co.edu.udea.compumovil.gr06_20182.lab3.tools.SqliteHelper;

public class LoginActivity extends AppCompatActivity {  //Activity {

    // Controls in this form
    EditText email,password;
    Button btnLogin;
    TextView registerScreen;

    public static SqliteHelper sqliteHelper;

    // Session Manager Class
    SessionManager session;

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
                if(s1.equals("") || s2.equals("")){
                    Toast.makeText(getApplicationContext(), getString(R.string.validate_empty),Toast.LENGTH_SHORT).show();
                }
                else{
                    if(sqliteHelper.chkemailpassword(s1,s2)){
                        User user = sqliteHelper.getUserByEmail(s1);
                        //String time = Helper.getMetaData(getApplicationContext(), "key_time");
                        //Toast.makeText(getApplicationContext(), time,Toast.LENGTH_SHORT).show();
                        session.createLoginSession(user.getName(),s1,s2);
//
                        Intent i = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(i);
                        finish();
                    }
                    else{

                        Toast.makeText(getApplicationContext(), getString(R.string.nok_user_authenticate),Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }


}