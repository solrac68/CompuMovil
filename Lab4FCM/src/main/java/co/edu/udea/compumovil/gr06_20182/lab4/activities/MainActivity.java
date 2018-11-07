package co.edu.udea.compumovil.gr06_20182.lab4.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import co.edu.udea.compumovil.gr06_20182.lab4.R;
import co.edu.udea.compumovil.gr06_20182.lab4.fragment.About;
import co.edu.udea.compumovil.gr06_20182.lab4.fragment.DishAddEdit;
import co.edu.udea.compumovil.gr06_20182.lab4.fragment.Dishfrag;
import co.edu.udea.compumovil.gr06_20182.lab4.fragment.DrinkAddEdit;
import co.edu.udea.compumovil.gr06_20182.lab4.fragment.Drinks;
import co.edu.udea.compumovil.gr06_20182.lab4.fragment.Profile;
import co.edu.udea.compumovil.gr06_20182.lab4.fragment.Settings;
import co.edu.udea.compumovil.gr06_20182.lab4.tools.SessionManager;

public class MainActivity extends AppCompatActivity implements
        Settings.OnFragmentInteractionListener,
        Dishfrag.OnFragmentListenerDish,
        DishAddEdit.OnFragmentListenerDishAddEdit,
        Drinks.OnFragmentListenerDrink,
        DrinkAddEdit.OnFragmentListenerDrinkAddEdit,
        GoogleApiClient.OnConnectionFailedListener{

    private static final String TAG = "MainActivity";
    private DrawerLayout dl;
    private ActionBarDrawerToggle t;
    private NavigationView nv;
    private TextView txtViewHeader;
    private TextView txtViewHeaderEmail;
    private View navHeader;
    private SessionManager session;
    private String userName;
    private String userEmail;
    private String userPassword;
    private String guidUser;
    private String urlImage;
    //private byte[] image;
    private SearchView searchView;
    private String fragment_current;

    // index to identify current nav menu item
    public static int navItemIndex = 0;


    public static int CURRENT_POSITION = 0;
    public static String FRAGMENTDISH = "DishFrag";
    public static String FRAGMENTDISHADDEDIT = "DishAddEdit";
    public static String FRAGMENTDDRINK = "Drinks";
    public static String FRAGMENTPROFILE = "Profile";
    public static String FRAGMENTSETTINGS = "Settings";
    public static String FRAGMENTDRINKADDEDIT = "DrinkAddEdit";
    public static String FRAGMENTABOUT = "About";
    Dishfrag dishfrag;
    Drinks drinks;
    Menu menu;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private GoogleApiClient googleApiClient;





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

        initialize();

        //getSession();


        if (savedInstanceState == null) {
            changeFragment(new Dishfrag(),FRAGMENTDISH);
            nv.getMenu().getItem(0).setActionView(R.layout.menu_dot);
        }

        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id){
                    case R.id.dishes:
                        changeFragment(Dishfrag.newInstance(true),FRAGMENTDISH);
                        CURRENT_POSITION = 0;
                        break;
                    case R.id.drinks:
                        changeFragment(Drinks.newInstance(true),FRAGMENTDDRINK);
                        CURRENT_POSITION = 1;
                        break;
                    case R.id.profile:
                        changeFragment(Profile.newInstance(urlImage,guidUser,userEmail),FRAGMENTPROFILE);
                        CURRENT_POSITION = 2;
                        break;
                    case R.id.settings:
                        changeFragment(Settings.newInstance(guidUser,userEmail,userPassword),FRAGMENTSETTINGS);
                        CURRENT_POSITION = 3;
                        break;
                    case R.id.close_session:
                        signOut();
                        break;
                    case R.id.about:
                        changeFragment(new About(),FRAGMENTABOUT);
                        CURRENT_POSITION = 5;
                        break;

                    default:
                }
                removeActionView(CURRENT_POSITION);

                return true;
            }
        });


    }

    private void initialize(){

        firebaseAuth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {

            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if (firebaseUser !=  null){
                    //Picasso.with(WelcomeActivity.this).load(firebaseUser.getPhotoUrl()).into(imvPhoto);

                    //txtViewHeaderEmail.setText(" Email: " + firebaseUser.getEmail());
                    userEmail = firebaseUser.getEmail();
                    urlImage = firebaseUser.getPhotoUrl()!= null ? firebaseUser.getPhotoUrl().toString():null;
                    guidUser = firebaseUser.getUid();

                    txtViewHeaderEmail.setText(userEmail);
                    txtViewHeader.setText(guidUser);



                }else {
                    Log.w(TAG, "onAuthStateChanged - signed_out");
                }
            }
        };

        //Inicialización de Google Account
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }





    @Override
    protected void onResume() {
        super.onResume();
    }

    public void removeActionView(int position){
        for(int i=0;i<=5;i++){
            nv.getMenu().getItem(i).setActionView(null);
        }
        nv.getMenu().getItem(position).setActionView(R.layout.menu_dot);
    }
    public void changeFragment(Fragment fragment,String name){
        fragment_current = name;
        // Create transaction
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();

        // Create new fragment
        //Fragment fragmentA = new FragmentA();
        // Replace whatever is in the fragment_container view with this fragment
        transaction.replace(R.id.frame, fragment,fragment_current);

        // add the transaction to the back stack (optional)
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();

        //Closing drawer on item click
        dl.closeDrawers();


        //invalidateOptionsMenu();

    }

//    private void getSession() {
//        session = new SessionManager(getApplicationContext());
//
//        HashMap<String, String> user = session.getUserDetails();
//        userName = user.get(SessionManager.KEY_NAME);
//        userEmail = user.get(SessionManager.KEY_EMAIL);
//        userPassword = user.get(SessionManager.KEY_PASSWORD);
//        image = sqliteHelper.getUserByEmail(userEmail).getImage();
//
//        txtViewHeader.setText(userName);
//        txtViewHeaderEmail.setText(userEmail);
//    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(t.onOptionsItemSelected(item))
            return true;

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onFragmentInteraction(String name,String email, String password) {
        if(name != null){
            userName = name;
            //Toast.makeText(userName)
            Toast.makeText(getApplicationContext(), userName,Toast.LENGTH_SHORT).show();
        }
        if(password != null){
            userPassword = password;
            Toast.makeText(getApplicationContext(), userPassword,Toast.LENGTH_SHORT).show();
        }
        if(email != null){
            userEmail = email;
            Toast.makeText(getApplicationContext(), userEmail,Toast.LENGTH_SHORT).show();
        }

        txtViewHeader.setText(userName);
        txtViewHeaderEmail.setText(userEmail);
        //getSession();
    }

    //Evento que viene de Dishfrag, con este evento se lanza la creción de la ventana de edición o inserción de registros
    @Override
    public void onFragmentInteraction(String id,Boolean isNew) {
        changeFragment(DishAddEdit.newInstance(id,isNew),FRAGMENTDISHADDEDIT);
    }

    //Evento que viene de DishAddEdit, con este evento se lanza la ventana de visualización de platos Dishfrag
    @Override
    public void onFragmentInteraction(Boolean isNew) {
        changeFragment(Dishfrag.newInstance(isNew),FRAGMENTDISH);
    }

    //Evento que viene de Drink, con este evento se lanza la ventana de visualizacion de edición o inserción de registro.
    @Override
    public void onFragmentDrinkInteraction(String id, Boolean isNew) {
        changeFragment(DrinkAddEdit.newInstance(id,isNew),FRAGMENTDRINKADDEDIT);
    }

    //Evento que viene de DrinkAddEdit, con este evento se lanza la ventana de visualización de bebidas Drinks
    @Override
    public void onFragmentDrinkAddEditInteraction(Boolean isNew) {
        changeFragment(Drinks.newInstance(isNew),FRAGMENTDDRINK);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        firebaseAuth.removeAuthStateListener(authStateListener);
    }

    private void signOut(){
        firebaseAuth.signOut();
        if (Auth.GoogleSignInApi != null){
              Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
                  @Override
                  public void onResult(@NonNull Status status) {
                      if (status.isSuccess()){
                          Intent i = new Intent(MainActivity.this, LoginActivity.class);
                          startActivity(i);
                          finish();
                      }else {
                          Toast.makeText(MainActivity.this, "Error in Google Sign Out", Toast.LENGTH_SHORT).show();
                      }
                  }
              });

        }

    }





}
