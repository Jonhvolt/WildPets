package com.example.saveme.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.saveme.R;

import java.io.File;

public class MainActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById( R.id.do_post ).setOnClickListener( new View.OnClickListener()
        {
            @Override
            public void onClick( View v )
            {
                startActivity( new Intent( MainActivity.this, NewAnimalActivity.class ) );
            }
        } );
    }
}
