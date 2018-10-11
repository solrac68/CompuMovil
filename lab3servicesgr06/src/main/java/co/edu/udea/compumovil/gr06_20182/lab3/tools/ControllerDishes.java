package co.edu.udea.compumovil.gr06_20182.lab3.tools;


import android.util.Log;

import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import co.edu.udea.compumovil.gr06_20182.lab3.model.Dish;
import co.edu.udea.compumovil.gr06_20182.lab3.model.DishesDto;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ControllerDishes implements Callback<DishesDto> {
    List<Dish> dishes = null;
    OnMyDishesResponse onMyDishesResponse;
    static final String BASE_URL = "http://www.mocky.io/v2/5bb69bd22e00007b00683715";

    public ControllerDishes(OnMyDishesResponse onMyDishesResponse)
    {
        this.onMyDishesResponse = onMyDishesResponse;
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

        Call<DishesDto> call = apiAPI.getDishes();

        call.enqueue(this);
    }


    @Override
    public void onResponse(Call<DishesDto> call, Response<DishesDto> response) {
        if(response.isSuccessful()){
            dishes = Mapper.MapDishes(response.body().getFoods());
            if(this.onMyDishesResponse != null){
                this.onMyDishesResponse.onResponse(dishes);
            }
        }
    }

    @Override
    public void onFailure(Call<DishesDto> call, Throwable t) {
        Log.d("ControllerDishes", "onFailure: " + t.getMessage());
    }
}
