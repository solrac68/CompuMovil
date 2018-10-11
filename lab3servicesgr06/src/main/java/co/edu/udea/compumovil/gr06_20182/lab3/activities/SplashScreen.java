package co.edu.udea.compumovil.gr06_20182.lab3.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;

import java.util.ArrayList;
import java.util.List;

import co.edu.udea.compumovil.gr06_20182.lab3.R;
import co.edu.udea.compumovil.gr06_20182.lab3.model.Dish;
import co.edu.udea.compumovil.gr06_20182.lab3.model.Drink;
import co.edu.udea.compumovil.gr06_20182.lab3.model.User;
import co.edu.udea.compumovil.gr06_20182.lab3.tools.SqliteHelper;

public class SplashScreen extends Activity {

    private static int SPLASH_TIME_OUT = 1000;
    List<Dish> dishes;
    List<Drink> drinks;
    User user;
    Bitmap bitmap;
    SqliteHelper sqliteHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);



        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                InitializationDishes();
                InitializationDrinks();
                InitializationUser();
                Intent i = new Intent(SplashScreen.this, LoginActivity.class);
                startActivity(i);

                finish();
            }
        }, SPLASH_TIME_OUT);
    }

    void InitializationDishes(){
        //Drink drink = new Drink()
        sqliteHelper = new SqliteHelper(getApplicationContext());

        sqliteHelper.deleteTable(Dish.TABLE_NAME);

        dishes = new ArrayList<>();
        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.fish);
        dishes.add(new Dish(1,"Lebranch",35000,25,false, SqliteHelper.getBitmapAsByteArray(bitmap)));
        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.crab);
        dishes.add(new Dish(1,"Candrejo",45000,35,true, SqliteHelper.getBitmapAsByteArray(bitmap)));
        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.lobster);
        dishes.add(new Dish(1,"Langosta",65000,45,false, SqliteHelper.getBitmapAsByteArray(bitmap)));
        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.sushi);
        dishes.add(new Dish(1,"Sushi",35000,25,true, SqliteHelper.getBitmapAsByteArray(bitmap)));
        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.octopus);
        dishes.add(new Dish(1,"Pulpo",20000,15,true, SqliteHelper.getBitmapAsByteArray(bitmap)));
        for(Dish d : dishes){
            sqliteHelper.insertData(d);
        }

    }

    void InitializationDrinks(){
        sqliteHelper = new SqliteHelper(getApplicationContext());

        sqliteHelper.deleteTable(Drink.TABLE_NAME);

        drinks = new ArrayList<>();
        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.fruit);
        drinks.add(new Drink(1,"Jugo de Frutas",15000,false, SqliteHelper.getBitmapAsByteArray(bitmap)));
        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.beer);
        drinks.add(new Drink(1,"Cerveza",45000,true, SqliteHelper.getBitmapAsByteArray(bitmap)));
        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.wine);
        drinks.add(new Drink(1,"Vino",25000,false, SqliteHelper.getBitmapAsByteArray(bitmap)));
        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.orangejuice);
        drinks.add(new Drink(1,"Jugo de Naranja",15000,true, SqliteHelper.getBitmapAsByteArray(bitmap)));
        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.soda);
        drinks.add(new Drink(1,"Soda",10000,true, SqliteHelper.getBitmapAsByteArray(bitmap)));
        for(Drink d : drinks){
            sqliteHelper.insertData(d);
        }
    }

    void InitializationUser(){
        sqliteHelper = new SqliteHelper(getApplicationContext());

        sqliteHelper.deleteTable(User.TABLE_NAME);
        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.user2);
        user = new User(1,"Carlos Augusto Hincapié R","alien","solrac.hincapie@gmail.com",SqliteHelper.getBitmapAsByteArray(bitmap));
        sqliteHelper.insertData(user);
    }
}
