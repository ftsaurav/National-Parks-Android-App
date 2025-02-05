package com.example.nationalparks.Util;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class util {
    public static final String PARKS_URL="https://developer.nps.gov/api/v1/parks?parkCode=&api_key=vWyM657g6zMqti0ppjznm6ESgfwS7cXn8Sezuo4q";
    public static String getParksUrl(String stateCode)
    {
        return "https://developer.nps.gov/api/v1/parks?parkCode="+stateCode+"&api_key=vWyM657g6zMqti0ppjznm6ESgfwS7cXn8Sezuo4q";
    }
    public static void hideKeyboard(View view) {
        InputMethodManager imm= (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}
