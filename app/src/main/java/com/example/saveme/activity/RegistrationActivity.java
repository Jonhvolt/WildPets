package com.example.saveme.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.saveme.R;
import com.example.saveme.presenter.RegistrationPresenter;
import com.example.saveme.utils.Utils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegistrationActivity
extends AppCompatActivity {

    private RegistrationPresenter registrationPresenter;

    EditText phone, email, name, pass, passDouble;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        getWindow().getDecorView().setSystemUiVisibility( View.SYSTEM_UI_FLAG_IMMERSIVE
                                                          | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                                          | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                                          | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                                          | View.SYSTEM_UI_FLAG_FULLSCREEN);

        phone = findViewById( R.id.phone_registr );
        email = findViewById( R.id.email_registr );
        name = findViewById( R.id.name_field );
        pass = findViewById( R.id.password_registr );
        passDouble = findViewById( R.id.password_registr_double );

        init();
    }

    private void init()
    {
        registrationPresenter = new RegistrationPresenter(this);

        findViewById(R.id.cancel_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        findViewById(R.id.apply_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = name.getText().toString();
                if( Utils.isBlankString( userName ) )
                {
                    Toast toast = Toast.makeText( RegistrationActivity.this,
                                                  "Не введено имя пользователя", Toast.LENGTH_LONG);
                    toast.show();
                    return;
                }

                //TODO проверить email на корректность
                String userEmail = email.getText().toString();
                if( Utils.isBlankString( userEmail ) )
                {
                    Toast toast = Toast.makeText( RegistrationActivity.this,
                                                  "Не введен email", Toast.LENGTH_LONG);
                    toast.show();
                    return;
                }

                String password = pass.getText().toString();
                if( Utils.isBlankString( password ) || password.length() < 5 )
                {
                    Toast toast = Toast.makeText( RegistrationActivity.this,
                                                  "Пароль не может быть менее 5 символов", Toast.LENGTH_LONG);
                    toast.show();
                    return;
                }

                String passwordDouble = passDouble.getText().toString();
                if( !password.equals( passwordDouble ) )
                {
                    Toast toast = Toast.makeText( RegistrationActivity.this,
                                                  "Пароли не совпадают", Toast.LENGTH_LONG);
                    toast.show();
                    return;
                }

                registrationPresenter.signUp( phone.getText().toString(), userEmail, userName, password );
            }
        });
    }

    public void showRegistrationSuccess()
    {
        Toast toast = Toast.makeText( RegistrationActivity.this,
                                      "Вы успешно зарегестрированы!", Toast.LENGTH_LONG);
        toast.show();
    }

    public void showFailureRegistration( String errorMessage)
    {
        Toast toast = Toast.makeText( RegistrationActivity.this,
                                      "Ошибка при регистрации. " + errorMessage, Toast.LENGTH_LONG);
        toast.show();
    }
}
