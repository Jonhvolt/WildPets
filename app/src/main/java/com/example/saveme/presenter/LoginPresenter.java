package com.example.saveme.presenter;

import android.content.Intent;

import androidx.annotation.NonNull;

import com.example.saveme.R;
import com.example.saveme.activity.LoginActivity;
import com.example.saveme.activity.MainActivity;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Класс обработчик для активити авторизации
 */
public class LoginPresenter {

    private LoginActivity loginView;

    private FirebaseAuth auth;

    public LoginPresenter( LoginActivity loginView )
    {
        this.loginView = loginView;

        auth = FirebaseAuth.getInstance();
    }

    public void signIn( String login, String password )
    {
        auth.signInWithEmailAndPassword( login, password )
            .addOnSuccessListener( new OnSuccessListener<AuthResult>()
        {
            @Override
            public void onSuccess( AuthResult authResult )
            {
                loginView.startActivity( new Intent( loginView, MainActivity.class ) );
                loginView.finish();
            }
        } ).addOnFailureListener( new OnFailureListener()
        {
            @Override
            public void onFailure( @NonNull Exception e )
            {
                loginView.showFailAuth( e.getMessage() );
            }
        } );
    }
}
