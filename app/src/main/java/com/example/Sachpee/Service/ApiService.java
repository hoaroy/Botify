package com.example.Sachpee.Service;

import com.example.Sachpee.Model.User;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiService {
    @GET("/user/phone/{phoneNumber}")
    Call<User> getUserByPhoneNumber(@Path("phoneNumber") String phoneNumber);
}

