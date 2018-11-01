package co.edu.udea.compumovil.gr06_20182.lab4.fragment;

import android.content.Context;
import android.os.Bundle;
//import android.preference.PreferenceFragment;
import com.takisoft.fix.support.v7.preference.EditTextPreference;
import com.takisoft.fix.support.v7.preference.PreferenceFragmentCompat;
import android.support.annotation.Nullable;
import android.support.v7.preference.CheckBoxPreference;
import android.support.v7.preference.Preference;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import co.edu.udea.compumovil.gr06_20182.lab4.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Settings#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Settings extends PreferenceFragmentCompat {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private EditTextPreference editTextFullName;
    private EditTextPreference editTextEmail;
    private EditTextPreference editTextPassword;
    private CheckBoxPreference checkNotification;
    private CheckBoxPreference checkSound;

    private static final String ARG_NAME = "Name";
    private static final String ARG_EMAIL = "Email";
    private static final String ARG_PASSWORD = "Pasword";

    // TODO: Rename and change types of parameters
    private String name;
    private String password;
    private String email;

    private OnFragmentInteractionListener mListener;

    public Settings() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Settings.
     */
    // TODO: Rename and change types and number of parameters
    public static Settings newInstance(String name, String email, String password) {
        Settings fragment = new Settings();
        Bundle args = new Bundle();
        args.putString(ARG_NAME, name);
        args.putString(ARG_EMAIL, email);
        args.putString(ARG_PASSWORD, password);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            name = getArguments().getString(ARG_NAME);
            email = getArguments().getString(ARG_EMAIL);
            password = getArguments().getString(ARG_PASSWORD);
        }
    }

    @Override
    public void onCreatePreferencesFix(@Nullable Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);
    }

    private void init(){
        editTextFullName = (EditTextPreference)getPreferenceManager().findPreference("key_full_name");
        editTextEmail = (EditTextPreference)getPreferenceManager().findPreference("key_email");
        editTextPassword = (EditTextPreference)getPreferenceManager().findPreference("key_password");
        checkNotification = (CheckBoxPreference)getPreferenceManager().findPreference("key_notification");
        checkSound = (CheckBoxPreference)getPreferenceManager().findPreference("key_sound");

        editTextFullName.setSummary(name);
        editTextEmail.setSummary(email);
        editTextPassword.setSummary(password);

        editTextFullName.setOnPreferenceChangeListener(new listenerOnPreference());
        editTextEmail.setOnPreferenceChangeListener(new listenerOnPreference());
        editTextPassword.setOnPreferenceChangeListener(new listenerOnPreference());
        checkNotification.setOnPreferenceClickListener(new listenerOnClickPreference());
        checkSound.setOnPreferenceClickListener(new listenerOnClickPreference());
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        init();
        return super.onCreateView(inflater, container, savedInstanceState);
    }


    class listenerOnPreference implements Preference.OnPreferenceChangeListener {
        @Override
        public boolean onPreferenceChange(Preference preference, Object o) {
            String strValue = o.toString();
            if(preference instanceof android.support.v7.preference.EditTextPreference){
                preference.setSummary(strValue);
                if(preference.getKey().equals("key_full_name")){
                    mListener.onFragmentInteraction(strValue,null,null);
                }else if(preference.getKey().equals("key_email")){
                    mListener.onFragmentInteraction(null,strValue,null);
                }else  if(preference.getKey().equals("key_password") ){
                    mListener.onFragmentInteraction(null,null,strValue);
                }
            }
            return true;
        }
    }

    class listenerOnClickPreference implements Preference.OnPreferenceClickListener{
        @Override
        public boolean onPreferenceClick(Preference preference) {
            preference.setSummary(((CheckBoxPreference)preference).isChecked()?"true":"false");
            return true;
        }
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(String name, String email, String password);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }
}
