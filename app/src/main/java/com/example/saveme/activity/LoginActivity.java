package com.example.saveme.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.saveme.R;
import com.example.saveme.presenter.LoginPresenter;
import com.example.saveme.utils.PreferencesManager;
import com.example.saveme.utils.Utils;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import static com.example.saveme.utils.PreferencesManager.KEY_FIRST_LAUNCH;

/**
 * Главное активити приложения
 */
public class LoginActivity extends AppCompatActivity
{
    private final String TAG = LoginActivity.class.getName();

    private TextView email;
    private EditText password;
    private Button apply;

    private LoginPresenter loginPresenter;

    private SharedPreferences preferences;

    private FirebaseAuth mAuth;
    private GoogleSignInOptions googleSignInOptions;
    private GoogleSignInClient mGoogleSignInClient;

    private static final int RC_SIGN_IN = 9001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().getDecorView().setSystemUiVisibility( View.SYSTEM_UI_FLAG_IMMERSIVE
                                                          | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                                          | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                                          | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                                          | View.SYSTEM_UI_FLAG_FULLSCREEN);
        preferences = PreferencesManager.getPreferences( this );

        mAuth = FirebaseAuth.getInstance();

        googleSignInOptions = new GoogleSignInOptions.Builder( GoogleSignInOptions.DEFAULT_SIGN_IN )
        .requestIdToken( getString( R.string.default_web_client_id ) )
        .requestEmail()
        .build();

        mGoogleSignInClient = GoogleSignIn.getClient( LoginActivity.this, googleSignInOptions );

        init();
    }

    private void init()
    {
        email =  findViewById(R.id.email_field);
        password = findViewById(R.id.password_field);
        password.setVisibility( View.INVISIBLE );

        loginPresenter = new LoginPresenter(this );

        if( isFirstLaunch() )
        {
            Log.i( this.getClass().getName(), "First launch app. Request all permission" );
            requestAllPermissions();
        }

        findViewById(R.id.exit_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.exit(0);
            }
        });

        findViewById(R.id.sign_up_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 startActivity(new Intent(LoginActivity.this, RegistrationActivity.class));
            }
        });

        apply = findViewById(R.id.apply_button);
        apply.setEnabled( false );
        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String login = email.getText().toString().trim();
                String pass = password.getText().toString().trim();

                if( Utils.isBlankString( login ) )
                {
                    Toast toast = Toast.makeText( LoginActivity.this, "Введите логин", Toast.LENGTH_LONG);
                    toast.show();
                    return;
                }

                //TODO сделать корректную проверку email
//                if( !(Utils.checkEmail( login )) )
//                {
//                    Toast toast = Toast.makeText( LoginActivity.this,
//                                                  "Введённый Вами email не корректен", Toast.LENGTH_SHORT);
//                    toast.show();
//                    return;
//                }

                if( Utils.isBlankString( pass ) )
                {
                    Toast toast = Toast.makeText( LoginActivity.this, "Введите пароль", Toast.LENGTH_LONG);
                    toast.show();
                    return;
                }

                //если всё нормально, то отправляем данные для проверки
                loginPresenter.signIn( login, pass );
            }
        });

        email.addTextChangedListener( new TextWatcher()
        {
            @Override
            public void beforeTextChanged( CharSequence s, int start, int count, int after ) { }

            @Override
            public void onTextChanged( CharSequence s, int start, int before, int count ) {}

            @Override
            public void afterTextChanged( Editable s )
            {
                password.setVisibility( View.VISIBLE );
                apply.setEnabled( true );
            }
        } );

        findViewById( R.id.google_sign_in_button ).setOnClickListener( new View.OnClickListener()
        {
            @Override
            public void onClick( View v )
            {
                int i = v.getId();
                if (i == R.id.google_sign_in_button)
                {
                    signIn();
                }
             //   else if (i == R.id.signOutButton)
             //   {
             //       signOut();
             //   }
            }
        } );
    }

    private void signIn()
    {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult( signInIntent, RC_SIGN_IN );
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent( data );
            try
            {
                GoogleSignInAccount account = task.getResult( ApiException.class);
                firebaseAuthWithGoogle( account );
            }
            catch (ApiException e)
            {

            }
        }
    }

    private void signOut()
    {
        //TODO готовый метод для выхода из своего аккаунта. Затолкать в любое место и после разлогирования отправять в LoginActivity

        // Firebase sign out
        mAuth.signOut();

        // Google sign out
        mGoogleSignInClient.signOut().addOnCompleteListener(this,
                                                            new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task)
                                                                {
                                                                    startActivity( new Intent( LoginActivity.this, LoginActivity.class ) );
                                                                }
                                                            });
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential( acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
             .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                 @Override
                 public void onComplete(@NonNull Task<AuthResult> task) {
                     if ( task.isSuccessful() )
                     {
                         FirebaseUser user = mAuth.getCurrentUser();
                         if( user != null )
                         {
                             startActivity( new Intent( LoginActivity.this, MainActivity.class ) );
                         }
                     }
                 }
             });
    }

    private void requestAllPermissions()
    {
        ActivityCompat.requestPermissions( LoginActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                                                            Manifest.permission.READ_EXTERNAL_STORAGE,
                                                                            Manifest.permission.CAMERA,
                                                                            Manifest.permission.ACCESS_FINE_LOCATION,
                                                                            Manifest.permission.INTERNET}, 0 );
    }

    /**
     * Проверяем первый ли раз запущено приложение
     * @return
     */
    private boolean isFirstLaunch()
    {
        if( preferences.contains( KEY_FIRST_LAUNCH ) )
        {
            return preferences.getBoolean( KEY_FIRST_LAUNCH, false );
        }
        else
        {
            PreferencesManager.getEditor( this ).putBoolean( KEY_FIRST_LAUNCH, false ).commit();
            Log.i( TAG, "Первый запуск приложения. Обновление"  );
            return true;
        }
    }

    public void showFailAuth( String errorMessage)
    {
        Toast toast = Toast.makeText( LoginActivity.this,
                                      "Ошибка авторизации. " + errorMessage, Toast.LENGTH_LONG);
        toast.show();
    }
}
