package com.monresto.acidlabs.monresto;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.monresto.acidlabs.monresto.Model.User;

public class SharedPreferenceManager {

    public static void saveUser(Context context, User user) {
        User.setInstance(user);
        SharedPreferences sharedPreferences = context.getSharedPreferences("session", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("id_user", user.getId());
        editor.remove(Config.USER);
        editor.putString(Config.USER, new Gson().toJson(user));
        editor.apply();
    }

    public static User getCurrentUser(Context context) {
        try {
            if (context != null) {
                SharedPreferences preferences = context.getSharedPreferences("session", Context.MODE_PRIVATE);
                if (preferences.contains("id_user")) {
                    User currentUser = new Gson().fromJson(preferences.getString(Config.USER, null), User.class);
                    User.setInstance(currentUser);

                    return currentUser;
                }
            }
        } catch (Exception e) {

        }
        return null;
    }

    public static void logout(Context context) {
        try {
            SharedPreferences sharedPreferences = context.getSharedPreferences("login_data", Context.MODE_PRIVATE);
            if (sharedPreferences != null) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.remove("passwordLogin");
                editor.remove("fbLogin");
                editor.apply();
            }

            User.setInstance(null);
            if (context != null) {
                SharedPreferences preferences = context.getSharedPreferences("session", Context.MODE_PRIVATE);
                preferences.edit().remove("id_user").apply();
                preferences.edit().remove(Config.USER).apply();
            }
        } catch (Exception e) {

        }
    }
}
