package co.edu.udea.compumovil.gr06_20182.lab3.tools;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import co.edu.udea.compumovil.gr06_20182.lab3.model.Dish;
import co.edu.udea.compumovil.gr06_20182.lab3.model.DishDto;

public class MyDownloadService extends Service {
    private int counter = 0;
    private static final int UPDATE_INTERVAL = 10000;
    private Timer timer = new Timer();
    private String TAG = "MyDownloadService";

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

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
                Log.d(TAG, String.valueOf(++counter) + " segundos");

                try {
                    //new BackgroundTask().execute();
                    new ControllerDishes(new OnMyResponse<DishDto>() {
                        @Override
                        public void onResponse(List<DishDto> obj) {
                            if(obj.size() > 0){
                                //Toast.makeText(getBaseContext(), obj.size() + "Registros descargados con EXITO", Toast.LENGTH_SHORT).show();
                                Log.d(TAG, obj.size() + " Registros DishDto descargados con EXITO");

                                new BackgroundTask().execute(obj);
                            }
                        }

                        @Override
                        public void onFailure(String msgError) {
                            Log.d(TAG, " Falla descarga: " + msgError);
                            //Toast.makeText(getBaseContext(), " FALLA descarga: " + msgError, Toast.LENGTH_SHORT).show();
                        }
                    }).start();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

            }

        }, 0, UPDATE_INTERVAL);
    }

    private int DownloadFile() {
        try {
            // Simulamos la descarga de un fichero
            Thread.sleep(150);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }

        return 100;
    }


    //Clase de Android para hacer tareas en background
    private class BackgroundTask extends AsyncTask<List<DishDto>, Void, List<Dish>> {

        //private String TAG = "BackgroundTask";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d(TAG, "Iniciando descarga de imagenes");
        }

        @Override
        protected List<Dish> doInBackground(List<DishDto>... dishesDto) {
            //int count = urls.length;

            return Mapper.MapDishes(dishesDto[0]);

        }

//        @Override
//        protected void onProgressUpdate(Integer... values) {
//            super.onProgressUpdate(values);
//            Log.d(TAG, String.valueOf(values[0]) + "% descargado");
//            Toast.makeText(getBaseContext(), values[0] + "% descargado", Toast.LENGTH_SHORT).show();
//        }

        @Override
        protected void onPostExecute(List<Dish>  result) {
            super.onPostExecute(result);
            Log.d(TAG, "Images Descargadas Con Exito: "+ result.size());
//            Toast.makeText(getBaseContext(),
//                    "Descargado " + result + " KBytes", Toast.LENGTH_SHORT)
//                    .show();
            //stopSelf();
        }
    }

}
