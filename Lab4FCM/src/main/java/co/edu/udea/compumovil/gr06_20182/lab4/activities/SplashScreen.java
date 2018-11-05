package co.edu.udea.compumovil.gr06_20182.lab4.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import co.edu.udea.compumovil.gr06_20182.lab4.R;
import co.edu.udea.compumovil.gr06_20182.lab4.model.Dish;
import co.edu.udea.compumovil.gr06_20182.lab4.model.Drink;
import co.edu.udea.compumovil.gr06_20182.lab4.model.User;
import co.edu.udea.compumovil.gr06_20182.lab4.tools.MyDownloadService;
import co.edu.udea.compumovil.gr06_20182.lab4.tools.SqliteHelper;

public class SplashScreen extends Activity {

    private static int SPLASH_TIME_OUT = 2000;
    List<Dish> dishes;
    List<Drink> drinks;
    User user;
    Bitmap bitmap;
    SqliteHelper sqliteHelper;
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
        dishes.add(new Dish(1,"Carne con Tomate",35000,25,false, "https://firebasestorage.googleapis.com/v0/b/laboratorio4-7304d.appspot.com/o/dishes%2Fcarne-tomate.jpg?alt=media&token=a1bd4895-0fea-409f-bb05-ecbbd9185dc5","E"));
        dishes.add(new Dish(2,"Chuleta de Cerdo",30000,35,true, "https://firebasestorage.googleapis.com/v0/b/laboratorio4-7304d.appspot.com/o/dishes%2Fchuleta-de-cerdo-con-pure.jpg?alt=media&token=e6f5f789-6e7b-4da2-8486-c430e147e285","E"));
        dishes.add(new Dish(3,"Mejillones al vapor",55000,45,false, "https://firebasestorage.googleapis.com/v0/b/laboratorio4-7304d.appspot.com/o/dishes%2Fmejillones-al-vapor.jpg?alt=media&token=f56c902b-5332-41f4-b69d-342fe6264788","E"));
        dishes.add(new Dish(4,"Roast Beef",35000,25,true, "https://firebasestorage.googleapis.com/v0/b/laboratorio4-7304d.appspot.com/o/dishes%2Froastbeef-patatas.jpg?alt=media&token=534d1cc6-d08d-4a09-95ec-cd914240e008","E"));

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
        drinks.add(new Drink(1,"Jugo de Frutas",15000f,false, "https://firebasestorage.googleapis.com/v0/b/laboratorio4-7304d.appspot.com/o/drinks%2FJugos.jpg?alt=media&token=3b9476ba-f127-4af7-8429-d8f40dde592c"));
        drinks.add(new Drink(2,"Vino Chileno",45000f,true, "https://firebasestorage.googleapis.com/v0/b/laboratorio4-7304d.appspot.com/o/drinks%2FVino.jpg?alt=media&token=d5c0671a-ae79-4fe8-b45e-2df690a45ec1"));

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
