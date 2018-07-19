package com.khalidelfaker.Utils;

import android.support.annotation.Nullable;

import com.khalidelfaker.models.GithubRepos;

import java.lang.ref.WeakReference;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GithubCalls {

    // 2 - Public method to start fetching Github Repos as described in the myUrl
    public static void fetchGithubRepos(Callbacks callbacks, String myUrl) {

        // 2.1 - Create a weak reference to callback (avoid memory leaks)
        final WeakReference<Callbacks> callbacksWeakReference = new WeakReference<>(callbacks);

        // 2.2 - Get a Retrofit instance and the related endpoints
        GithubService gitHubService = GithubService.retrofit.create(GithubService.class);

        // 2.3 - Create the call on Github API
        Call<GithubRepos> call = gitHubService.getRepos(myUrl);
        // 2.4 - Start the call
        call.enqueue(new Callback<GithubRepos>() {

            @Override
            public void onResponse(Call<GithubRepos> call, Response<GithubRepos> response) {
                // 2.5 - Call the proper callback used in controller (MainFragment)
                if (callbacksWeakReference.get() != null)
                    callbacksWeakReference.get().onResponse(response.body());
            }

            @Override
            public void onFailure(Call<GithubRepos> call, Throwable t) {
                // 2.5 - Call the proper callback used in controller (MainFragment)
                if (callbacksWeakReference.get() != null) callbacksWeakReference.get().onFailure();
            }
        });
    }

    // 1 - Creating a callback
    public interface Callbacks {
        void onResponse(@Nullable GithubRepos repos);

        void onFailure();
    }
}
