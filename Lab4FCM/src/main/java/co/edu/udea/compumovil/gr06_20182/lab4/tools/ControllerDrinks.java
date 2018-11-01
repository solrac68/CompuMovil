package co.edu.udea.compumovil.gr06_20182.lab4.tools;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;
import co.edu.udea.compumovil.gr06_20182.lab4.model.Drink;
import co.edu.udea.compumovil.gr06_20182.lab4.model.DrinkDto;
import co.edu.udea.compumovil.gr06_20182.lab4.model.DrinksDto;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ControllerDrinks implements Callback<DrinksDto> {
    //List<Drink> drinks;
    OnMyResponse<DrinkDto> onMyResponse;

    static final String BASE_URL = "http://www.mocky.io/";

    public ControllerDrinks(OnMyResponse<DrinkDto> onMyResponse){
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

        Call<DrinksDto> call = apiAPI.getDrinks("5bb69ce32e00004d00683718");

        call.enqueue(this);
    }

    @Override
    public void onResponse(Call<DrinksDto> call, Response<DrinksDto> response) {
        if(response.isSuccessful()){
            //drinks = Mapper.MapDrinks(response.body().getDrinks());
            if(this.onMyResponse != null){
                this.onMyResponse.onResponse(response.body().getDrinks());
            }
        }
    }

    @Override
    public void onFailure(Call<DrinksDto> call, Throwable t) {
        Log.d("ControllerDrinks", "onFailure: " + t.getMessage());
        this.onMyResponse.onFailure(t.getMessage());
    }
}
