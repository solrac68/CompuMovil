package co.edu.udea.compumovil.gr06_20182.lab4.tools;

import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Consumer;

import co.edu.udea.compumovil.gr06_20182.lab4.R;
import co.edu.udea.compumovil.gr06_20182.lab4.model.Dish;
import co.edu.udea.compumovil.gr06_20182.lab4.model.DishDto;
import co.edu.udea.compumovil.gr06_20182.lab4.model.Drink;
import co.edu.udea.compumovil.gr06_20182.lab4.model.DrinkDto;

public class MyDownloadService extends Service {
    private int counter = 0;
    private  int UPDATE_INTERVAL;
    private Timer timer = new Timer();
    private String TAG = "MyDownloadService";
    SqliteHelper sqliteHelper;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        UPDATE_INTERVAL = Helper.getMetaData(getApplicationContext(),"key_time");

        doSomethingRepeatedly();

        return START_STICKY;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
        }
        Log.d(TAG, " Servicio Detenido");
        Toast.makeText(getBaseContext(), "Servicio Detenido",
                Toast.LENGTH_SHORT).show();
    }



    private void doSomethingRepeatedly() {
        timer.scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run() {
                Log.d(TAG, String.valueOf(++counter) + " veces");

                try {
                    new ControllerDishes(new OnMyResponse<DishDto>() {
                        @Override
                        public void onResponse(List<DishDto> obj) {

                            if(obj.size() > 0){
                                Log.d(TAG, obj.size() + " Registros DishDto descargados con EXITO");

                                new BackgroundTaskDish().execute(obj);
                            }
                        }

                        @Override
                        public void onFailure(String msgError) {
                            Log.d(TAG, " Falla descarga imagenes platos: " + msgError);
                            //Toast.makeText(getBaseContext(), " FALLA descarga: " + msgError, Toast.LENGTH_SHORT).show();
                        }
                    }).start();

                    new ControllerDrinks(new OnMyResponse<DrinkDto>() {
                        @Override
                        public void onResponse(List<DrinkDto> obj) {
                            if(obj.size() > 0){
                                //Toast.makeText(getBaseContext(), obj.size() + "Registros descargados con EXITO", Toast.LENGTH_SHORT).show();
                                //Log.d(TAG, obj.size() + " Registros DishDto descargados con EXITO");

                                new BackgroundTaskDrink().execute(obj);
                            }
                        }

                        @Override
                        public void onFailure(String msgError) {
                            Log.d(TAG, " Falla descarga imagenes bebidas: " + msgError);
                        }
                    }).start();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

            }

        }, 0, UPDATE_INTERVAL);
    }


    //Clase de Android para hacer tareas en background
    private class BackgroundTaskDish extends AsyncTask<List<DishDto>, Void, List<Dish>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Log.d(TAG, "Iniciando descarga de imagenes de platos");
        }

        @Override
        protected List<Dish> doInBackground(List<DishDto>... dishesDto) {
            //int count = urls.length;
            byte[] bitmap = SqliteHelper.getBitmapAsByteArray(BitmapFactory.decodeResource(getResources(),R.drawable.fish));
            sqliteHelper = new SqliteHelper(getApplicationContext());
            List<Dish>  dishes = Mapper.MapDishes(dishesDto[0]);

            // TODO: cambiar
//            for(Dish d:dishes){
//                if(d.getImage() == null){
//                    d.setImage(bitmap);
//                }
//            }

            // TODO: cambiar
            //sqliteHelper.initializationDishes(dishes);

            return dishes;

        }

        @Override
        protected void onPostExecute(List<Dish>  result) {
            super.onPostExecute(result);
            Log.d(TAG, "Images Platos descargadas Con Exito: "+ result.size());
        }
    }

    private class BackgroundTaskDrink extends AsyncTask<List<DrinkDto>, Void, List<Drink>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Log.d(TAG, "Iniciando descarga de imagenes de bebidas");
        }

        @Override
        protected List<Drink> doInBackground(List<DrinkDto>... drinksDto) {
            byte[] bitmap = SqliteHelper.getBitmapAsByteArray(BitmapFactory.decodeResource(getResources(),R.drawable.fruit));

            sqliteHelper = new SqliteHelper(getApplicationContext());
            List<Drink>  drinks = Mapper.MapDrinks(drinksDto[0]);
            for(Drink d:drinks){
                // TODO: cambiar
//                if(d.getImage() == null){
//                    d.setImage(bitmap);
//                }
            }
            // TODO: cambiar
            //sqliteHelper.initializationDrinks(drinks);

            return drinks;

        }

        @Override
        protected void onPostExecute(List<Drink>  result) {
            super.onPostExecute(result);
            Log.d(TAG, "Images Bebidas Descargadas Con Exito: "+ result.size());
        }
    }

}
