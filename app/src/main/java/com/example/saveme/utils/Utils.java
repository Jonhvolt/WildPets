package com.example.saveme.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Общий утильный класс для работы
 */
public class Utils
{

    //общие константы для работы с preferences
    public static final String KEY_FIRST_LAUNCH = "firstLaunch";
    public static final String KEY_LOGIN = "login";
    public static final String KEY_PASS = "pass";

    //константы для разрешений
    public static final int PERMISSION_INTERNET_RESULT = 0;
    public static final int PERMISSION_STORAGE_RESULT = 1;
    public static final int PERMISSION_CAMERA_RESULT = 2;
    public static final int PERMISSION_LOCATION_RESULT = 3;

    /**
     * Проверяет, что строка null или длина строки <= 0
     * @param s - строка для проверки
     * @return
     */
    public static boolean isBlankString( String s )
    {
        if( s == null )
            return true;
        if( s.length() <= 0 )
            return true;
        return false;
    }

    /**
     * Проверяет, что строка не null и длина строки > 0
     * @param s
     * @return
     */
    public static boolean isNotBlankString( String s )
    {
        if( s == null )
            return false;
        if( s.length() <= 0 )
            return false;
        return true;
    }

    /**
     * Проверяем, что нет цифр или иных символов в имени или фамилии
     * @param name
     * @return
     */
    public static boolean checkName( String name )
    {
        Pattern pattern = Pattern.compile ( "^[A-ЯЁ][а-яё]+\\s[A-ЯЁ][а-яё]+$" );
        Matcher matcher = pattern.matcher ( name );

        if ( matcher.find () )
        {
            return true;
        }
        return false;
    }

    /**
     * Проверка email на корректность
     * @return
     */
    public static boolean checkEmail( String email )
    {
        Pattern pattern = Pattern.compile ( "'/^((([0-9A-Za-z]{1}[-0-9A-z\\.]{1,}[0-9A-Za-z]{1})|([0-9А-Яа-я]{1}[-0-9А-я\\.]{1,}[0-9А-Яа-я]{1}))@([-A-Za-z]{1,}\\.){1,2}[-A-Za-z]{2,})$/u', $item" );
        Matcher matcher = pattern.matcher ( email );

        if ( matcher.find () )
        {
            return true;
        }
        return false;
    }

    /**
     * Проверка телефона
     */
    public static boolean checkTelephoneNumber( String number )
    {
        Pattern pattern = Pattern.compile ( "^((8|\\+7)[\\- ]?)?(\\(?\\d{3}\\)?[\\- ]?)?[\\d\\- ]{7,10}$" );
        Matcher matcher = pattern.matcher ( number );

        if ( matcher.find () )
        {
            return true;
        }
        return false;
    }
}
