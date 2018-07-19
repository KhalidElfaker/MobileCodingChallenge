package com.khalidelfaker.Utils;

import com.khalidelfaker.models.GithubRepos;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface GithubService {

    //instantiate and configure Retrofit
    Retrofit retrofit = new Retrofit.Builder()
            //Set the API base URL
            .baseUrl("https://api.github.com/")
            //Add converter factory for serialization and deserialization of objects for Retrofit,
            .addConverterFactory(GsonConverterFactory.create())  //GSON this time
            .build();

    //we will make a REST request of type GET
    @GET("")
    //the return type of the GET corresponding to deserialize JSON
    Call<GithubRepos> getRepos(@Url String url);


}
