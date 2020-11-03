package com.example.saveme.presenter;

import android.app.Activity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainPresenter
{
    private Activity mainView;

    private FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

    public MainPresenter( Activity mainView )
    {
        this.mainView = mainView;
    }
}
