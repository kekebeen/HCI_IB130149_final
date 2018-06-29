package com.pretzel.ben.ib130149_flashcard.helper;

import android.app.Application;
import android.content.Context;

import com.pretzel.ben.ib130149_flashcard.LoginActivity;
import com.pretzel.ben.ib130149_flashcard.MainActivity;
import com.pretzel.ben.ib130149_flashcard.ProfileActivity;
import com.pretzel.ben.ib130149_flashcard.RegisterActivity;

public class MyApplication extends Application
{

    public static Context getContext()
    {
        return context;
    }

    // login activity
    public static LoginActivity getLoginActivity() {
        return loginActivity;
    }

    public static void setLoginActivity(LoginActivity loginActivity) {
        MyApplication.loginActivity = loginActivity;
    }

    public static LoginActivity loginActivity;

    // profile activity
    public static ProfileActivity profileActivity;
    public static ProfileActivity getProfileActivity() { return profileActivity; }
    public static void setProfileActivity(ProfileActivity profileActivity) {
        MyApplication.profileActivity = profileActivity;
    }

    // main activity

    public static MainActivity getMainActivity() {
        return mainActivity;
    }

    public static void setMainActivity(MainActivity mainActivity) {
        MyApplication.mainActivity = mainActivity;
    }

    // register activity
    public static RegisterActivity registerActivity;

    public static RegisterActivity getRegisterActivity() {return registerActivity;}

    public static void setRegisterActivity(RegisterActivity registerActivity) {
        MyApplication.registerActivity = registerActivity;
    }

    public  static MainActivity mainActivity;
    private static Context context;

    @Override
    public void onCreate()
    {
        super.onCreate();

        context = getApplicationContext();
        //custom code
    }
}


