package com.example.saveme.presenter;

import android.content.Intent;
import android.location.LocationManager;

import androidx.annotation.NonNull;

import com.example.saveme.activity.MainActivity;
import com.example.saveme.activity.RegistrationActivity;
import com.example.saveme.models.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Класс-посредник для работы с активити регистрацией
 */
public class RegistrationPresenter {

    private RegistrationActivity registrationActivityView;

    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private DatabaseReference users;

    public RegistrationPresenter( RegistrationActivity registrationActivityView )
    {
        this.registrationActivityView = registrationActivityView;

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        users = database.getReference( "users" );
    }

    public void signUp( final String phone, final String email, final String name, final String pass )
    {
        auth.createUserWithEmailAndPassword( email, pass )
            .addOnSuccessListener( new OnSuccessListener<AuthResult>()
        {
            @Override
            public void onSuccess( AuthResult authResult )
            {
                User user = new User(  );
                user.setName( name );
                if( phone != null )
                {
                    user.setPhone( phone );
                }
                user.setEmail( email );
                user.setPassword( pass );

                users.child( FirebaseAuth.getInstance().getCurrentUser().getUid() )
                     .setValue( user )
                     .addOnSuccessListener( new OnSuccessListener<Void>()
                {
                    @Override
                    public void onSuccess( Void aVoid )
                    {
                        registrationActivityView.showRegistrationSuccess();
                    }
                } );

                registrationActivityView.startActivity( new Intent( registrationActivityView, MainActivity.class) );
                registrationActivityView.finish();
            }
        } )
        .addOnFailureListener( new OnFailureListener()
        {
            @Override
            public void onFailure( @NonNull Exception e )
            {
                registrationActivityView.showFailureRegistration( e.getMessage() );
            }
        } );
    }
}
