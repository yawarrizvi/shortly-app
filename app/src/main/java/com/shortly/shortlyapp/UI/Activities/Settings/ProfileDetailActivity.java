package com.shortly.shortlyapp.UI.Activities.Settings;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.shortly.shortlyapp.DataProvider.Prefs;
import com.shortly.shortlyapp.R;
import com.shortly.shortlyapp.UI.Activities.BaseActivity;
import com.shortly.shortlyapp.model.LoginResponse;

import butterknife.Bind;

public class ProfileDetailActivity extends BaseActivity {

    @Bind(R.id.txt_username)
    TextView mTxtViewUsername;

    @Bind(R.id.txt_email)
    TextView mTxtViewEmail;

    @Bind(R.id.txt_phone)
    TextView mTxtViewPhone;

    @Bind(R.id.txt_gender)
    TextView mTxtViewGender;

    @Bind(R.id.txt_country)
    TextView mTxtViewCountry;

    @Bind(R.id.txt_city)
    TextView mTxtViewCity;

    @Bind(R.id.txt_address)
    TextView mTxtViewAddress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_detail);

        LoginResponse user = Prefs.getInstance(this).getCurrentUser();
//        LoginResponse userDetails = user.getResponse().get(0);
        Log.v("", "");
//        setUserDetails(user);
    }

    
}
