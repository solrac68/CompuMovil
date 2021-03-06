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
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.InputStream;

import co.edu.udea.compumovil.gr06_20182.lab3.R;
import co.edu.udea.compumovil.gr06_20182.lab3.activities.RegisterActivity;
import co.edu.udea.compumovil.gr06_20182.lab3.model.Dish;
import co.edu.udea.compumovil.gr06_20182.lab3.tools.ImageHelper;
import co.edu.udea.compumovil.gr06_20182.lab3.tools.Mapper;
import co.edu.udea.compumovil.gr06_20182.lab3.tools.SqliteHelper;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DishAddEdit.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DishAddEdit#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DishAddEdit extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_ID = "id";
    private static final String ARG_NEW = "estado";
    private Integer id;
    private Boolean isNew;
    private EditText txtNameDish,txtTimePreparation,txtPrice;
    private CheckBox checkFavorite;
    private ImageView imgDish;
    private Button btnOk;
    private Dish dish;
    public static SqliteHelper sqliteHelper;
    final int REQUEST_CODE_GALLERY = 999;
    final int REQUEST_CODE_PHONE = 998;
    final String TAG = "DishAddEdit";


    private OnFragmentListenerDishAddEdit mListener;

    public DishAddEdit() {
        // Required empty public constructor
    }


    public static DishAddEdit newInstance(Integer id, Boolean isNew) {
        DishAddEdit fragment = new DishAddEdit();
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
        txtNameDish = view.findViewById(R.id.txtNameDish);
        txtTimePreparation = view.findViewById(R.id.txtTimePreparation);
        txtPrice = view.findViewById(R.id.txtPrice);
        checkFavorite =  view.findViewById(R.id.checkFavorite);
        imgDish = view.findViewById(R.id.imgDish);
        btnOk = view.findViewById(R.id.btnOk);
    }

    private void viewDataOnScreenToUpdate(){
        dish = sqliteHelper.getDishById(id);
        //Toast.makeText(getContext(), dish.getName(), Toast.LENGTH_SHORT).show();
        imgDish.setImageBitmap(SqliteHelper.getByteArrayAsBitmap(dish.getImage()));
        txtNameDish.setText(dish.getName());
        txtTimePreparation.setText(dish.getTime_preparation().toString());
        txtPrice.setText(dish.getPrice().toString());
        checkFavorite.setChecked(dish.isFavorite());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dish_add_edit, container, false);

        init(view);

        // If data exists, this is to show on Screen.
        if(!isNew){
            viewDataOnScreenToUpdate();
        }

        imgDish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},REQUEST_CODE_GALLERY);
            }
        });

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE},REQUEST_CODE_PHONE);

                if(isNew){
                    dish = new Dish();
                    dish.setId(sqliteHelper.getNextIdDish());
                    dish.setType("H");
                }

                dish.setName(txtNameDish.getText().toString().trim());
                dish.setFavorite(checkFavorite.isChecked());

                try{
                    dish.setImage(ImageHelper.imageViewToByte(imgDish));
                }catch(Exception e)
                {
                    dish.setImage(SqliteHelper.getBitmapAsByteArray(BitmapFactory.decodeResource(getResources(),R.drawable.octopus)));
                }

                //byte[] bitmap = SqliteHelper.getBitmapAsByteArray(BitmapFactory.decodeResource(getResources(),R.drawable.fish));

                dish.setPrice(Integer.parseInt(txtPrice.getText().toString().trim()));
                dish.setTime_preparation(Integer.parseInt(txtTimePreparation.getText().toString().trim()));

                try {
                    if(isNew){
                        Log.d(TAG, " Antes: " + dish.getId());
                        sqliteHelper.insertData(dish);
                        Log.d(TAG, " Id: " + dish.getId());
                    }
                    else{
                        sqliteHelper.updateDish(dish);
                    }
                    Toast.makeText(getContext(), isNew?getString(R.string.ok_insert):getString(R.string.ok_update), Toast.LENGTH_SHORT).show();
                    onButtonPressed(isNew);
                }catch (Exception ex){
                    Log.d(TAG, " Error: " + ex.getMessage());
                    ex.printStackTrace();
                }
            }
        });


        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Boolean isNew) {
        if (mListener != null) {
            mListener.onFragmentInteraction(isNew);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentListenerDishAddEdit) {
            mListener = (OnFragmentListenerDishAddEdit) context;
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

    public interface OnFragmentListenerDishAddEdit {
        // TODO: Update argument type and name
        void onFragmentInteraction(Boolean isNew);
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
        else if (requestCode == REQUEST_CODE_PHONE){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                TelephonyManager tMgr = (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);
                String mPhoneNumber = tMgr.getLine1Number();
                Toast.makeText(getContext(), mPhoneNumber, Toast.LENGTH_SHORT).show();
            }
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
                imgDish.setImageBitmap(bitmap);
            }
            catch (FileNotFoundException e){
                e.printStackTrace();
            }

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}
