package co.edu.udea.compumovil.gr06_20182.lab2.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;

import java.util.ArrayList;
import java.util.List;

import co.edu.udea.compumovil.gr06_20182.lab2.R;
import co.edu.udea.compumovil.gr06_20182.lab2.model.Dish;
import co.edu.udea.compumovil.gr06_20182.lab2.model.Drink;
import co.edu.udea.compumovil.gr06_20182.lab2.tools.SqliteHelper;

public class SplashScreen extends Activity {

    private static int SPLASH_TIME_OUT = 100;
    List<Dish> dishes;
    List<Drink> drinks;
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
        drinks = new ArrayList<>();
    }
}
