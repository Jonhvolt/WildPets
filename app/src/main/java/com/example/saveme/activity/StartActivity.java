package com.example.saveme.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.example.saveme.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class StartActivity
extends AppCompatActivity {

    private SharedPreferences preferences;

    private FirebaseUser mFirebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        getWindow().getDecorView().setSystemUiVisibility( View.SYSTEM_UI_FLAG_IMMERSIVE
                                                          | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                                          | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                                          | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                                          | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                                          | View.SYSTEM_UI_FLAG_FULLSCREEN);

        preferences = getPreferences( MODE_PRIVATE );

        mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if ( mFirebaseUser == null )
        {
            startActivity( new Intent(this, LoginActivity.class) );
            finish();
        } else {
            startActivity( new Intent( this, MainActivity.class ) );
            finish();
        }
    }
}
