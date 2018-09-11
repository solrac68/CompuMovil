package co.edu.udea.compumovil.gr06_20182.lab2.activities;

import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import java.util.HashMap;

import co.edu.udea.compumovil.gr06_20182.lab2.R;
import co.edu.udea.compumovil.gr06_20182.lab2.tools.SessionManager;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout dl;
    private ActionBarDrawerToggle t;
    private NavigationView nv;
    private TextView txtViewHeader;
    private TextView txtViewHeaderEmail;
    private View navHeader;
    private SessionManager session;
    private String userName;
    private String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dl = (DrawerLayout)findViewById(R.id.activity_main);
        t = new ActionBarDrawerToggle(this, dl,R.string.openDrawer, R.string.closeDrawer);
        dl.addDrawerListener(t);
        t.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        nv = (NavigationView)findViewById(R.id.nv);
        navHeader = nv.getHeaderView(0);
        txtViewHeader =  navHeader.findViewById(R.id.textViewHeader);
        txtViewHeaderEmail = navHeader.findViewById(R.id.textViewHeaderEmail);

        getSession();

        txtViewHeader.setText(userName);
        txtViewHeaderEmail.setText(userEmail);

        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id){
                    case R.id.dishes:
                        Toast.makeText(MainActivity.this,"My Account", Toast.LENGTH_SHORT).show();
                    case R.id.settings:
                        Toast.makeText(MainActivity.this,"Settings", Toast.LENGTH_SHORT).show();
                    case R.id.drinks:
                        Toast.makeText(MainActivity.this,"Settings", Toast.LENGTH_SHORT).show();
                    case R.id.profile:
                        Toast.makeText(MainActivity.this,"Settings", Toast.LENGTH_SHORT).show();
                    case R.id.close_session:
                        Toast.makeText(MainActivity.this,"Settings", Toast.LENGTH_SHORT).show();
                    default:
                        return true;
                }
            }
        });

    }

    private void getSession() {
        session = new SessionManager(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        userName = user.get(SessionManager.KEY_NAME);
        userEmail = user.get(SessionManager.KEY_EMAIL);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(t.onOptionsItemSelected(item))
            return true;
        return super.onOptionsItemSelected(item);
    }
}
