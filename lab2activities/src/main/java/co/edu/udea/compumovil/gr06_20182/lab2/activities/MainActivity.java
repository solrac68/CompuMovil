package co.edu.udea.compumovil.gr06_20182.lab2.activities;

import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
import co.edu.udea.compumovil.gr06_20182.lab2.fragment.Dishes;
import co.edu.udea.compumovil.gr06_20182.lab2.fragment.Drinks;
import co.edu.udea.compumovil.gr06_20182.lab2.fragment.Profile;
import co.edu.udea.compumovil.gr06_20182.lab2.fragment.Settings;
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

    // index to identify current nav menu item
    public static int navItemIndex = 0;

    // tags used to attach the fragments
    private static final String TAG_HOME = "home";
    private static final String TAG_PHOTOS = "photos";
    private static final String TAG_MOVIES = "movies";
    private static final String TAG_NOTIFICATIONS = "notifications";
    private static final String TAG_SETTINGS = "settings";
    public static String CURRENT_TAG = TAG_HOME;

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
                        changeFragment(new Dishes());
                        //Toast.makeText(MainActivity.this,"My Account", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.settings:
                        changeFragment(new Settings());
                        break;
                        //Toast.makeText(MainActivity.this,"Settings", Toast.LENGTH_SHORT).show();
                    case R.id.drinks:
                        changeFragment(new Drinks());
                        break;
                        //Toast.makeText(MainActivity.this,"Settings", Toast.LENGTH_SHORT).show();
                    case R.id.profile:
                        changeFragment(new Profile());
                        break;
                        //Toast.makeText(MainActivity.this,"Settings", Toast.LENGTH_SHORT).show();
                    case R.id.close_session:
                        session.logoutUser();
                        //finish();
                        break;
                    default:
                        return true;
                }
                return true;
            }
        });

    }

    public void changeFragment(Fragment fragment){

        // Create transaction
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();

        // Create new fragment
        //Fragment fragmentA = new FragmentA();
        // Replace whatever is in the fragment_container view with this fragment
        transaction.replace(R.id.frame, fragment);

        // add the transaction to the back stack (optional)
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();

        //Closing drawer on item click
        dl.closeDrawers();

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
