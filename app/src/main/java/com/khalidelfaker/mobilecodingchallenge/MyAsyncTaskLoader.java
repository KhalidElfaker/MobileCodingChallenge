package com.khalidelfaker.mobilecodingchallenge;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import java.util.ArrayList;


public class MyAsyncTaskLoader extends AsyncTaskLoader< ArrayList<String> > {

    public static ArrayList<String> list,fullList;

    public MyAsyncTaskLoader(Context context) {
        super(context);
    }

    @Override
    public ArrayList<String> loadInBackground() {
        list = new ArrayList<>();
        if(this.getId()==100){



        }
        fullList=list;
        return list;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

}
