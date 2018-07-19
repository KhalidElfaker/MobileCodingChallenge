package com.khalidelfaker.mobilecodingchallenge;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.khalidelfaker.Utils.GithubCalls;
import com.khalidelfaker.models.GithubRepos;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements GithubCalls.Callbacks {

    ProgressBar progressBar;
    ListView listView;
    GithubRepos githubRepos;
    Button trendingButton;
    Button settingButton;
    boolean listHasReachedTheBottom;
    boolean firstLoadDone;
    int page;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        githubRepos = new GithubRepos();
        listHasReachedTheBottom = false;
        firstLoadDone = false;
        page = 1;

        listView = findViewById(R.id.my_list_view);
        progressBar = findViewById(R.id.activity_main_progress_bar);
        trendingButton = findViewById(R.id.trendingButton);
        settingButton = findViewById(R.id.settingButton);

        trendingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //changing the button text and drawable's color as it's clicked
                trendingButton.setTextColor(Color.parseColor("#FF2C77DF"));
                Drawable image = v.getContext().getResources().getDrawable(R.drawable.ic_baseline_star_rate_18px_blue);
                int h = image.getIntrinsicHeight();
                int w = image.getIntrinsicWidth();
                image.setBounds(0, 0, w, h);
                trendingButton.setCompoundDrawables(null, image, null, null);

                if (!firstLoadDone) {
                    executeHttpRequestWithRetrofit();
                } else {//preventing user's re-click on the trending Button
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
                    String message = "Would you like to re-load the GitHub repositories list from the beginning!?";
                    alertDialogBuilder.setMessage(message);
                    alertDialogBuilder.setPositiveButton("Cancel",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {
                                    //do nothing
                                }
                            });
                    alertDialogBuilder.setNegativeButton("Reload",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {
                                    githubRepos.reset();
                                    page = 1;
                                    MyListViewAdapter myListViewAdapter = (MyListViewAdapter) listView.getAdapter();
                                    myListViewAdapter.notifyDataSetChanged();
                                    firstLoadDone = false;
                                    executeHttpRequestWithRetrofit();
                                }
                            });
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }

            }
        });

        //TODO don't forget this
        settingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "Coming soon...", Toast.LENGTH_SHORT).show();
            }
        });

        //proceeding the pagination method when the user reach the bottom of the list view
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == SCROLL_STATE_FLING)
                    listHasReachedTheBottom = true;
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem + visibleItemCount == totalItemCount && listHasReachedTheBottom) {
                    if (githubRepos.getTotalCount() != githubRepos.getItems().size()) {
                        //load new reps when the list view scroll to the bottom and didn't get all repos
                        listHasReachedTheBottom = false;
                        page++;
                        executeHttpRequestWithRetrofit(page);
                        Log.e("page", page + " loaded!");
                        Log.e("githubReposTotalCount", "" + githubRepos.getTotalCount());
                        Log.e("githubReposItemsSize", "" + githubRepos.getItems().size());
                    } else {
                        //all repos were loaded
                        updateUIWhenStopingHTTPRequest();
                    }
                }
            }
        });
    }

    // ------------------------------
    //  HTTP REQUEST (Retrofit Way)
    // ------------------------------

    // execute HTTP request for the first time when trending button clicked and update UI
    void executeHttpRequestWithRetrofit() {
        updateUIWhenStartingHTTPRequest();
        String url = "search/repositories?q=created:>" + fetchingDateForLast(30) + "&sort=stars&order=desc";
        GithubCalls.fetchGithubRepos(this, url);
    }

    void executeHttpRequestWithRetrofit(int page) {
        updateUIWhenStartingHTTPRequest();
        String url = "search/repositories?q=created:>" + fetchingDateForLast(30) + "&sort=stars&order=desc&page=" + page;
        GithubCalls.fetchGithubRepos(this, url);
    }

    //override callback methods
    @Override
    public void onResponse(@Nullable GithubRepos repos) {
        //when getting response, we update UI
        if (repos != null) this.updateUIWithListOfRepos(repos);
    }

    @Override
    public void onFailure() {
        //when getting error, we update UI
        updateUIWhenStopingHTTPRequest("An error happened!\nplease check your network connection!");
    }

    private void updateUIWithListOfRepos(GithubRepos repos) {
        if (listView.getCount() == 0) {//fist load and no data shown in the listView
            githubRepos = repos;
            listView.setAdapter(new MyListViewAdapter(this.getApplicationContext(), githubRepos));
            firstLoadDone = true;
        } else {//when proceeding pagination we add new data to the bottom of the list
            githubRepos.getItems().addAll(repos.getItems());
            MyListViewAdapter myListViewAdapter = (MyListViewAdapter) listView.getAdapter();
            myListViewAdapter.notifyDataSetChanged();
        }

        updateUIWhenStopingHTTPRequest();
    }

    public void updateUIWhenStartingHTTPRequest() {
        progressBar.setVisibility(View.VISIBLE);
    }

    //if something went wrong with the HTTP request we show an alert to the user
    private void updateUIWhenStopingHTTPRequest(String messageError) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(messageError);
        alertDialogBuilder.setNegativeButton("Close",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        //just close the alert and do nothing
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
        progressBar.setVisibility(View.GONE);
    }

    private void updateUIWhenStopingHTTPRequest() {
        progressBar.setVisibility(View.GONE);
    }

    //i've made this method to get a formatted date as "yyyy-MM-dd" from where the fetch on the APA will be done
    String fetchingDateForLast(int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -days);
        Date date = calendar.getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.FRANCE);
        return dateFormat.format(date);
    }
}
