package co.edu.udea.compumovil.gr06_20182.lab3.fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.InputStream;

import co.edu.udea.compumovil.gr06_20182.lab3.R;
import co.edu.udea.compumovil.gr06_20182.lab3.model.Dish;
import co.edu.udea.compumovil.gr06_20182.lab3.model.Drink;
import co.edu.udea.compumovil.gr06_20182.lab3.tools.ImageHelper;
import co.edu.udea.compumovil.gr06_20182.lab3.tools.SqliteHelper;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DrinkAddEdit.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DrinkAddEdit#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DrinkAddEdit extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_ID = "id";
    private static final String ARG_NEW = "estado";
    private Integer id;
    private Boolean isNew;
    private EditText txtNameDrink,txtPrice;
    private CheckBox checkFavorite;
    private ImageView imgDrink;
    private Button btnOk;
    private Drink drink;
    public static SqliteHelper sqliteHelper;
    final int REQUEST_CODE_GALLERY = 999;


    private OnFragmentListenerDrinkAddEdit mListener;

    public DrinkAddEdit() {
        // Required empty public constructor
    }


    public static DrinkAddEdit newInstance(Integer id, Boolean isNew) {
        DrinkAddEdit fragment = new DrinkAddEdit();
        Bundle args = new Bundle();
        args.putInt(ARG_ID, id);
        args.putBoolean(ARG_NEW, isNew);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            id = getArguments().getInt(ARG_ID);
            isNew = getArguments().getBoolean(ARG_NEW);
        }
        sqliteHelper = new SqliteHelper(getContext());
    }

    private void init(View view){
        txtNameDrink = view.findViewById(R.id.txtNameDrink);
        txtPrice = view.findViewById(R.id.txtPriceDrink);
        checkFavorite =  view.findViewById(R.id.checkFavoriteDrink);
        imgDrink = view.findViewById(R.id.imgDrink);
        btnOk = view.findViewById(R.id.btnOkDrink);
    }

    private void viewDataOnScreenToUpdate(){
        drink = sqliteHelper.getDrinkById(id);
        //Toast.makeText(getContext(), dish.getName(), Toast.LENGTH_SHORT).show();
        imgDrink.setImageBitmap(SqliteHelper.getByteArrayAsBitmap(drink.getImage()));
        txtNameDrink.setText(drink.getName());
        txtPrice.setText(drink.getPrice().toString());
        checkFavorite.setChecked(drink.isFavorite());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_drink_add_edit, container, false);

        init(view);

        // If data exists, this is to show on Screen.
        if(!isNew){
            viewDataOnScreenToUpdate();
        }

        imgDrink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},REQUEST_CODE_GALLERY);
            }
        });

        btnOk.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {



                if(isNew){
                    drink = new Drink();
                }

                drink.setName(txtNameDrink.getText().toString().trim());
                drink.setFavorite(checkFavorite.isChecked());
                drink.setImage(ImageHelper.imageViewToByte(imgDrink));
                drink.setPrice(Integer.parseInt(txtPrice.getText().toString().trim()));

                try {
                    if(isNew){
                        sqliteHelper.insertData(drink);
                        Toast.makeText(getContext(), "Hola Insert", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        sqliteHelper.updateDrink(drink);
                    }
                    Toast.makeText(getContext(), isNew?getString(R.string.ok_insert):getString(R.string.ok_update), Toast.LENGTH_SHORT).show();
                    onButtonPressed(isNew);
                }catch (Exception ex){
                    ex.printStackTrace();
                }
            }
        });


        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Boolean isNew) {
        if (mListener != null) {
            mListener.onFragmentDrinkAddEditInteraction(isNew);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentListenerDrinkAddEdit) {
            mListener = (OnFragmentListenerDrinkAddEdit) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentListenerDrinkAddEdit {
        void onFragmentDrinkAddEditInteraction(Boolean isNew);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        if(requestCode == REQUEST_CODE_GALLERY){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,REQUEST_CODE_GALLERY);
            }
            else{
                Toast.makeText(getContext(),getString(R.string.without_access_security),Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode,permissions,grantResults);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == REQUEST_CODE_GALLERY && resultCode == getActivity().RESULT_OK && data != null){
            Uri uri = data.getData();
            try {
                InputStream inputStream = getContext().getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                imgDrink.setImageBitmap(bitmap);
            }
            catch (FileNotFoundException e){
                e.printStackTrace();
            }

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}
