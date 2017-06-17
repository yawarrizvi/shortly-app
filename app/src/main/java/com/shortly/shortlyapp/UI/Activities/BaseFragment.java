package com.shortly.shortlyapp.UI.Activities;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.view.View;

import butterknife.ButterKnife;

/**
 * Created by yarizvi on 17/06/2017.
 */

public class BaseFragment extends android.support.v4.app.Fragment{
    @CallSuper
    @Override public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
    }

    @CallSuper
    @Override public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @CallSuper
    @Override public void onDestroy() {
        super.onDestroy();
    }
}
