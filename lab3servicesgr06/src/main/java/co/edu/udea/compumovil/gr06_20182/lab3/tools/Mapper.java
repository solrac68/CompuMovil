package co.edu.udea.compumovil.gr06_20182.lab3.tools;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import co.edu.udea.compumovil.gr06_20182.lab3.R;
import co.edu.udea.compumovil.gr06_20182.lab3.model.Dish;
import co.edu.udea.compumovil.gr06_20182.lab3.model.DishDto;
import co.edu.udea.compumovil.gr06_20182.lab3.model.Drink;
import co.edu.udea.compumovil.gr06_20182.lab3.model.DrinkDto;


public class Mapper {
    public static List<Dish> MapDishes(List<DishDto> dishesdto) {

        List<Dish> dishes = new ArrayList<>();
        Dish dish;
        byte[] image;
        Integer id = 0;
        Integer cont = 0;
        for(DishDto dishdto : dishesdto) {
            dish = new Dish();
            //Log.d("MAPPER", dishdto.getImage()!=null?dishdto.getImage():"");
            image = dishdto.getImage() != null ? SqliteHelper.getBitmapAsByteArrayFromUrl(dishdto.getImage()): null;
            dish.setId(++id);
            dish.setImage(image);
            dish.setFavorite(false);
            dish.setName(dishdto.getName());
            dish.setPrice(dishdto.getPrice());
            dish.setTime_preparation(dishdto.getTime());
            dish.setType(dishdto.getType());
            dishes.add(dish);

            if(++cont == 10){
                break;
            }
        }
        return dishes;
    }

    public static List<Drink> MapDrinks(List<DrinkDto> drinksdto) {
        List<Drink> drinks = new ArrayList<>();
        Drink drink;
        byte[] image;
        Integer id = 0;
        Integer cont = 0;
        for(DrinkDto drinkDto : drinksdto) {
            drink = new Drink();
            image = drinkDto.getImage() != null ? SqliteHelper.getBitmapAsByteArrayFromUrl(drinkDto.getImage()):null;
            drink.setId(++id);
            drink.setImage(image);
            drink.setFavorite(false);
            drink.setName(drinkDto.getName());
            drink.setPrice(convertStringToFloat(drinkDto.getPrice()));
            drinks.add(drink);
            if(++cont == 10){
                break;
            }
        }
        return drinks;
    }

    public static Float convertStringToFloat(String str){
        Float f;
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator(',');
        DecimalFormat format = new DecimalFormat("0.#");
        format.setDecimalFormatSymbols(symbols);
        try{
            f = format.parse(str).floatValue();
        } catch(ParseException ex) {
            f = Float.parseFloat(str);
        }
        return f;
    }
}