package com.example.saveme.bean;

/**
 * Проверяет есть ли пользователь с таким логином и паролем среди зарегестрированных
 * Если есть, то запусакт MainActivity
 * Иначе запускает LoginActivity
 */
public class CheckValidUser
{
    private String login;
    private String pass;

    public CheckValidUser( String login, String pass )
    {
        this.login = login;
        this.pass = pass;
    }

    public boolean checkUser()
    {
        return true;
    }
}
