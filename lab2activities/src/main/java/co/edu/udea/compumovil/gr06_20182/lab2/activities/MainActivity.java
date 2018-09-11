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
import co.edu.udea.compumovil.gr06_20182.lab2.fragment.About;
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


    public static int CURRENT_POSITION = 0;

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

        if (savedInstanceState == null) {
            changeFragment(new Dishes());
            nv.getMenu().getItem(0).setActionView(R.layout.menu_dot);
        }

        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id){
                    case R.id.dishes:
                        changeFragment(new Dishes());
                        CURRENT_POSITION = 0;
                        break;
                    case R.id.drinks:
                        changeFragment(new Drinks());
                        CURRENT_POSITION = 1;
                        break;
                    case R.id.profile:
                        changeFragment(new Profile());
                        CURRENT_POSITION = 2;
                        break;
                    case R.id.settings:
                        changeFragment(new Settings());
                        CURRENT_POSITION = 3;
                        break;
                    case R.id.close_session:
                        session.logoutUser();
                        break;
                    case R.id.about:
                        changeFragment(new About());
                        CURRENT_POSITION = 5;
                        break;

                    default:
                }
                removeActionView(CURRENT_POSITION);
                return true;
            }
        });

    }

    public void removeActionView(int position){
        for(int i=0;i<=5;i++){
            nv.getMenu().getItem(i).setActionView(null);
        }
        nv.getMenu().getItem(position).setActionView(R.layout.menu_dot);
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
        invalidateOptionsMenu();

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
