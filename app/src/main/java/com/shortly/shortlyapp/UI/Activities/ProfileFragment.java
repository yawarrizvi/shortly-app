package com.shortly.shortlyapp.UI.Activities;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shortly.shortlyapp.DataProvider.Prefs;
import com.shortly.shortlyapp.R;
import com.shortly.shortlyapp.model.LoginResponse;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProfileFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends BaseFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

//    @Bind(R.id.txt_username)
    TextView mTxtViewUsername;

//    @Bind(R.id.txt_login_name)
    TextView mTxtViewLoginName;

//    @Bind(R.id.txt_website)
    TextView mTxtViewWebsite;

//    @Bind(R.id.txt_city)
    TextView mTxtViewCity;

//    @Bind(R.id.txt_email)
    TextView mTxtViewEmail;

//    @Bind(R.id.txt_gender)
    TextView mTxtViewGender;

//    @Bind(R.id.txt_phone)
    TextView mTxtViewPhone;


    private OnFragmentInteractionListener mListener;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mTxtViewUsername = (TextView) view.findViewById(R.id.txt_username);
        mTxtViewLoginName = (TextView) view.findViewById(R.id.txt_login_name);
        mTxtViewWebsite = (TextView) view.findViewById(R.id.txt_website);
        mTxtViewCity = (TextView) view.findViewById(R.id.txt_city);
        mTxtViewEmail = (TextView) view.findViewById(R.id.txt_email);
        mTxtViewGender = (TextView) view.findViewById(R.id.txt_gender);
        mTxtViewPhone = (TextView) view.findViewById(R.id.txt_phone);
        LoginResponse user = Prefs.getInstance(getContext()).getCurrentUser();
//        LoginResponse userDetails = user.getResponse().get(0);
        Log.v("", "");
        setUserDetails(user);

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

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void setUserDetails(LoginResponse user) {

        //TODO: update field value
        if (user.getFullName() != null) {
            mTxtViewUsername.setText(user.getFullName());
        }
        if (user.getFullName() != null) {
            mTxtViewWebsite.setText(user.getFullName());
        }

        //End TODO
        if (user.getFullName() != null) {
            mTxtViewLoginName.setText(user.getFullName());
        }

        if (user.getCity() != null) {
            mTxtViewCity.setText(user.getCity());
        }

        if (user.getEmail() != null) {
            mTxtViewEmail.setText(user.getEmail());
        }

        //TODO: confirm gender values
        double gender = user.getGender();
        if (gender > 0) {
            mTxtViewGender.setText("Male");
        } else {
            mTxtViewGender.setText("Female");
        }

        if (user.getPhone() != null) {
            mTxtViewPhone.setText(user.getPhone());
        }
    }
}
