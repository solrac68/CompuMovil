package co.edu.udea.compumovil.gr06_20182.labscm20182;

import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.RadioButton;

import java.util.Calendar;

public class FoodActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {

    ImageView imageView;
    Uri imageUri;
    private TextView mTimeDisplay;
    private CheckBox checkBoxMorning;
    private CheckBox checkBoxAfternoon;
    private CheckBox checkBoxNight;
    private RadioButton radioButtonPrincipalFood;
    private RadioButton radioButtonEntry;
    private EditText txtPrecio;
    private EditText txtIngredient;
    private TextView txtView;
    //private TextView textViewTime;
    private EditText txtName;
    private int mHour;
    private int mMinute;

    SharedPreferences sharedpreferences;
    public static final String mypreference = "mypref2";
    public static final String CheckBoxMorning = "checkBoxMorning";
    public static final String CheckBoxAfternoon = "checkBoxAfternoon";
    public static final String CheckBoxNight = "checkBoxNight";
    public static final String RadioButtonPrincipalFood = "radioButtonPrincipalFood";
    public static final String RadioButtonEntry = "radioButtonEntry";
    public static final String Precio = "precio";
    public static final String Ingredient = "ingredient";
    public static final String ImageUri = "imageUri";
    public static final String TimeDisplay = "mTimeDisplay";
    public static final String Name = "nameKey";

    private static int RESULT_LOAD_IMG = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food);
        imageView = (ImageView) findViewById(R.id.imgView);

        mTimeDisplay = (TextView) findViewById(R.id.textViewTime);
        checkBoxMorning = (CheckBox)findViewById(R.id.checkBoxMorning);
        checkBoxAfternoon = (CheckBox)findViewById(R.id.checkBoxAfternoon);
        checkBoxNight = (CheckBox)findViewById(R.id.checkBoxNight);
        radioButtonPrincipalFood = (RadioButton) findViewById(R.id.radioButtonPrincipalFood);
        radioButtonEntry = (RadioButton) findViewById(R.id.radioButtonEntry);
        txtPrecio =  (EditText)findViewById(R.id.txtPrecio);
        txtIngredient = (EditText)findViewById(R.id.txtIngredient);
        txtView = (TextView) findViewById(R.id.txtView);
        txtName = (EditText)findViewById(R.id.txtName);

        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        sharedpreferences = getSharedPreferences(mypreference, Context.MODE_PRIVATE);
        if (sharedpreferences.contains(Name)) {
            txtName.setText(sharedpreferences.getString(Name, ""));
        }
        if (sharedpreferences.contains(Precio)) {
            txtPrecio.setText(sharedpreferences.getString(Precio, ""));
        }
        if (sharedpreferences.contains(CheckBoxMorning)) {
            Boolean valorCheckMorning = sharedpreferences.getBoolean(CheckBoxMorning, false);
            checkBoxMorning.setChecked(valorCheckMorning);
        }
        if (sharedpreferences.contains(CheckBoxAfternoon)) {
            Boolean valorCheckAfternoon = sharedpreferences.getBoolean(CheckBoxAfternoon, false);
            checkBoxAfternoon.setChecked(valorCheckAfternoon);
        }
        if (sharedpreferences.contains(CheckBoxNight)) {
            Boolean valorCheckNight = sharedpreferences.getBoolean(CheckBoxNight, false);
            checkBoxNight.setChecked(valorCheckNight);
        }
        if (sharedpreferences.contains(RadioButtonPrincipalFood)) {
            Boolean valorCheck = sharedpreferences.getBoolean(RadioButtonPrincipalFood, true);
            radioButtonPrincipalFood.setChecked(valorCheck);
        }
        if (sharedpreferences.contains(RadioButtonEntry)) {
            Boolean valorCheck = sharedpreferences.getBoolean(RadioButtonEntry, false);
            radioButtonEntry.setChecked(valorCheck);
        }
        if (sharedpreferences.contains(Ingredient)) {
            txtIngredient.setText(sharedpreferences.getString(Ingredient, ""));
        }
        if (sharedpreferences.contains(TimeDisplay)) {
            String hour = new StringBuilder().append(mHour).append(":").append(mMinute).toString();
            mTimeDisplay.setText(sharedpreferences.getString(TimeDisplay, hour));
        }

        updateDisplay();
        loadView();
    }

    private void loadView() {

        Boolean morning = sharedpreferences.getBoolean(CheckBoxMorning, false);
        Boolean night = sharedpreferences.getBoolean(CheckBoxNight, false);
        Boolean afternoon = sharedpreferences.getBoolean(CheckBoxAfternoon, false);
        StringBuilder sbHora = new StringBuilder();
        if(morning){
            sbHora.append(getResources().getString(R.string.str_morning));
            sbHora.append(",");
        }
        if(night){
            sbHora.append(getResources().getString(R.string.str_night));
            sbHora.append(",");
        }
        if(afternoon){
            sbHora.append(getResources().getString(R.string.str_afternoon));
            sbHora.append(",");
        }


        if(sbHora.length() == 0) {
            sbHora.append("");
        }


        Boolean principalfood = sharedpreferences.getBoolean(RadioButtonPrincipalFood, true);
        String food;
        if(principalfood){
            food = getResources().getString(R.string.str_platofuerte);
        }
        else{
            food = getResources().getString(R.string.str_entrada);
        }

        Log.d("loadView",food);

        StringBuilder sb = new StringBuilder();
        sb.append(getResources().getString(R.string.str_Name))
                .append(":")
                .append(sharedpreferences.getString(Name, ""))
                .append("\n")
                .append(getResources().getString(R.string.str_date))
                .append(":")
                .append(sbHora)
                .append("\n")
                .append(getResources().getString(R.string.str_type))
                .append(":")
                .append(food)
                .append("\n")
                .append(getResources().getString(R.string.str_hour))
                .append(":")
                .append(sharedpreferences.getString(TimeDisplay, ""))
                .append("\n")
                .append(getResources().getString(R.string.str_price))
                .append(":")
                .append(sharedpreferences.getString(Precio, ""))
                .append("\n")
                .append(getResources().getString(R.string.str_ingredients))
                .append(":")
                .append(sharedpreferences.getString(Ingredient, ""));


        txtView.setText(sb);
    }

    private void updateDisplay() {
        mTimeDisplay.setText(new StringBuilder()
                .append(mHour).append(":").append(mMinute));
    }

    public void loadImagefromGallery(View view) {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, RESULT_LOAD_IMG);

        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(Name, txtName.getText().toString());
        editor.putString(Precio, txtPrecio.getText().toString());
        editor.putString(Ingredient, txtIngredient.getText().toString());
        editor.putString(TimeDisplay, mTimeDisplay.getText().toString());
        editor.putBoolean(CheckBoxMorning, checkBoxMorning.isChecked());
        editor.putBoolean(CheckBoxAfternoon, checkBoxAfternoon.isChecked());
        editor.putBoolean(CheckBoxNight, checkBoxNight.isChecked());
        editor.putBoolean(RadioButtonEntry, radioButtonEntry.isChecked());
        editor.putBoolean(RadioButtonPrincipalFood, radioButtonPrincipalFood.isChecked());

        editor.apply();

        Log.d("loadImagefromGallery",radioButtonPrincipalFood.toString());

        loadView();
    }

    public void onClickPicker(View view) {
        DialogFragment timePickerFragment = new TimePickerFragment();
        timePickerFragment.show(getFragmentManager(), "timePicker");
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(resultCode == RESULT_OK && requestCode == RESULT_LOAD_IMG){
            imageUri = data.getData();
            imageView.setImageURI(imageUri);
        }
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        mHour = hourOfDay;
        mMinute = minute;
        updateDisplay();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.id_clean:
                cleanControls();
                return true;
            case R.id.id_exit:
                exitApplication();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void cleanControls() {
        mTimeDisplay.setText("");
        txtName.setText("");
        checkBoxMorning.setChecked(false);
        checkBoxAfternoon.setChecked(false);
        checkBoxNight.setChecked(false);
        txtPrecio.setText("");
        txtIngredient.setText("");
        txtView.setText("");
        imageView.setImageURI(Uri.EMPTY);
    }

    private void exitApplication() {
        System.exit(0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }

}
