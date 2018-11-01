package co.edu.udea.compumovil.gr06_20182.lab4.tools;


import android.util.Log;

import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import co.edu.udea.compumovil.gr06_20182.lab4.model.Dish;
import co.edu.udea.compumovil.gr06_20182.lab4.model.DishDto;
import co.edu.udea.compumovil.gr06_20182.lab4.model.DishesDto;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ControllerDishes implements Callback<DishesDto> {
    //List<Dish> dishes = null;
    OnMyResponse<DishDto> onMyResponse;
    static final String BASE_URL = "http://www.mocky.io/";

    public ControllerDishes(OnMyResponse<DishDto> onMyResponse)
    {
        this.onMyResponse = onMyResponse;
    }

    public void start(){
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        ApiService apiAPI = retrofit.create(ApiService.class);

        Call<DishesDto> call = apiAPI.getDishes("5bb69bd22e00007b00683715");

        call.enqueue(this);
    }


    @Override
    public void onResponse(Call<DishesDto> call, Response<DishesDto> response) {
        if(response.isSuccessful()){
            Log.d("ControllerDishes", "TAMANO consulado: " + response.body().getFoods().size());
            if(this.onMyResponse != null){
                this.onMyResponse.onResponse(response.body().getFoods());
            }
        }
    }

    @Override
    public void onFailure(Call<DishesDto> call, Throwable t) {
        Log.d("ControllerDishes", "onFailure: " + t.getMessage());
        this.onMyResponse.onFailure(t.getMessage());
    }
}
