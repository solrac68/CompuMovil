package co.edu.udea.compumovil.gr06_20182.lab4.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import co.edu.udea.compumovil.gr06_20182.lab4.R;
import co.edu.udea.compumovil.gr06_20182.lab4.model.Dish;
import co.edu.udea.compumovil.gr06_20182.lab4.model.Drink;
import co.edu.udea.compumovil.gr06_20182.lab4.model.User;

public class SplashScreen extends Activity {

    private static int SPLASH_TIME_OUT = 2000;
    List<Dish> dishes;
    List<Drink> drinks;
    User user;
    Bitmap bitmap;
    private FirebaseFirestore mFirestore;
    private String tag = "Cloud FireStore get SplashScreen";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        initFirestone();
        initialization();
        //initDrinks();
        //initDishes();


        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {

                Intent i = new Intent(SplashScreen.this, LoginActivity.class);
                startActivity(i);

                finish();
            }
        }, SPLASH_TIME_OUT);
    }

    private void initFirestone(){
        mFirestore = FirebaseFirestore.getInstance();
    }

    private void initDishes(){
        CollectionReference platos = mFirestore.collection("dishes");


        dishes = new ArrayList<>();
        dishes.add(new Dish(1,"Carne con Tomate",35000,25,false, "carne-tomate.jpg","E"));
        dishes.add(new Dish(2,"Chuleta de Cerdo",30000,35,true, "chuleta-de-cerdo-con-pure.jpg","E"));
        dishes.add(new Dish(3,"Mejillones al vapor",55000,45,false, "mejillones-al-vapor.jpg","E"));
        dishes.add(new Dish(4,"Roast Beef",35000,25,true, "roastbeef-patatas.jpg","E"));

        for(Dish d:dishes){
            platos.add(d)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Log.d(tag, "Dish add with ID: " + documentReference.getId());
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(tag, "Error agregando un plato", e);
                        }
                    });
        }
    }

    private void initDrinks(){
        drinks = new ArrayList<>();
        drinks.add(new Drink(1,"Jugo de Frutas",15000f,false, "Jugos.jpg"));
        drinks.add(new Drink(2,"Vino Chileno",45000f,true, "Vino.jpg"));

        CollectionReference bebidas = mFirestore.collection(getString(R.string.DRINKS));

        for(Drink d:drinks){
            bebidas.add(d)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Log.d("Cloud FireStore add", "Drink add with ID: " + documentReference.getId());
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(tag, "Error agregando una bebida", e);
                        }
                    });
        }
    }

    void initialization(){
        CollectionReference platos = mFirestore.collection(getString(R.string.DISHES));

        platos.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    if(task.getResult().size() == 0){
                        Log.d(tag, "No tiene platos: ");
                        initDishes();
                    }
                    else{
                        Log.d(tag, "Nro de Platos platos: " + task.getResult().size());
                    }
                }
                else{
                    Log.d(tag, "Error query dishes: ",task.getException());
                }
            }
        });

        CollectionReference bebidas = mFirestore.collection(getString(R.string.DRINKS));

        bebidas.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    if(task.getResult().size() == 0){
                        Log.d(tag, "No tiene bebidas: ");
                        initDrinks();
                    }
                    else{
                        Log.d(tag, "Nro de Bebidas: " + task.getResult().size());
                    }
                }
                else{
                    Log.d(tag, "Error query drinks: ",task.getException());
                }
            }
        });

    }


}
