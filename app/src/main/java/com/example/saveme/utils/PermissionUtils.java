package com.example.saveme.utils;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

/**
 * Утильный класс для работы с разрешениями приложения
 */
public class PermissionUtils
{

    /**
     * Проверка, что нужен диалог перед запросом разрешения и создание его
     * Если диалог не нужен, то просто запрашиваем нужное разрешение
     */
    public static void shouldShowRequestPermissionRationale( final Activity activity, final String permission, String message  )
    {
        if( ActivityCompat.shouldShowRequestPermissionRationale( activity, permission ) )
        {
            new AlertDialog.Builder( activity ).setTitle( "Разрешение" ).setMessage( message ).setPositiveButton( "Принять", new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick( DialogInterface dialog, int which )
                {
                    ActivityCompat.requestPermissions( activity, new String[]{permission}, 0 );
                }
            } )
                                                 .setNegativeButton( "Отмена", new DialogInterface.OnClickListener()
                                                 {
                                                     @Override
                                                     public void onClick(
                                                     DialogInterface dialog,
                                                     int which )
                                                     {
                                                         dialog.dismiss();
                                                     }
                                                 } )
                                                 .create()
                                                 .show();
        }
        else
        {
            ActivityCompat.requestPermissions( activity, new String[]{permission}, 0 );
        }
    }

    /**
     * Проверяка, что ранее было выдано разрешение на использование камеры
     * @return
     */
    public static int checkCameraPermission( Activity context )
    {
        return ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA);
    }

    /**
     * Проверка, что ранее было выдано разрешение на использование памяти устройства
     * @return
     */
    public static int checkStoragePermission( Activity context )
    {
        return ContextCompat.checkSelfPermission(context, Manifest.permission_group.STORAGE);
    }

    /**
     * Проверка, что ранее было выдано разрешение на определение местоположения устройства
     * @return
     */
    public static int checkLocationPermission( Activity context )
    {
       return ContextCompat.checkSelfPermission( context, Manifest.permission_group.LOCATION );
    }

    public static int checkInternetPermission( Activity context )
    {
        return ContextCompat.checkSelfPermission( context, Manifest.permission.INTERNET );
    }
}
