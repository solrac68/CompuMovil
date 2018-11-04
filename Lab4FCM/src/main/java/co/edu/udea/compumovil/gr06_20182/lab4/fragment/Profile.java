package co.edu.udea.compumovil.gr06_20182.lab4.fragment;


import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import co.edu.udea.compumovil.gr06_20182.lab4.R;
import co.edu.udea.compumovil.gr06_20182.lab4.tools.SessionManager;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Profile.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Profile#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Profile extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    private static final String ARG_IMAGE = "Imag";
    private static final String ARG_NAME = "Name";
    private static final String ARG_EMAIL = "Email";
    private SessionManager session;

    // TODO: Rename and change types of parameters
    private String image;
    private String name;
    private String email;
    ImageView imageView;
    TextView textViewUsuario;
    TextView textViewEmail;


    //private OnFragmentInteractionListener mListener;

    public Profile() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Profile.
     */
    // TODO: Rename and change types and number of parameters
    public static Profile newInstance(String image, String name, String email) {
        Profile fragment = new Profile();
        Bundle args = new Bundle();
        args.putString(ARG_IMAGE, image);
        args.putString(ARG_NAME, name);
        args.putString(ARG_EMAIL, email);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //image = Uri.parse(getArguments().getString(ARG_IMAGE));
            image = getArguments().getString(ARG_IMAGE);
            name = getArguments().getString(ARG_NAME);
            email = getArguments().getString(ARG_EMAIL);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
    }

    private void init(View view){
        imageView = view.findViewById(R.id.imageProfile);
        textViewUsuario = view.findViewById(R.id.txtViewName2);
        textViewEmail = view.findViewById(R.id.txtViewEmail2);

        if(image != null){
            Picasso.get()
                    .load(image)
                    .resize(250, 250)
                    .centerCrop()
                    .into(imageView);
        }


        textViewUsuario.setText(name);
        textViewEmail.setText(email);
    }

}
