package co.edu.udea.compumovil.gr06_20182.labscm20182;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void callFood(View view){

        Intent intent = new Intent(this,FoodActivity.class);
        //EditText editText = findViewById(R.id.editText);
        //String message = editText.getText().toString();
        //intent.putExtra(EXTRA_MESSAGE,message);
        startActivity(intent);
    }

    public void callDrink(View view){
        Intent intent = new Intent(this,DrinkActivity.class);
        //EditText editText = findViewById(R.id.editText);
        //String message = editText.getText().toString();
        //intent.putExtra(EXTRA_MESSAGE,message);
        startActivity(intent);
    }
}
