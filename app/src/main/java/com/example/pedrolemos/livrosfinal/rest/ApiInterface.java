package com.example.pedrolemos.livrosfinal.rest;

import com.example.pedrolemos.livrosfinal.model.BookResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface ApiInterface {

    @GET("volumes")
    Call<BookResponse> getBookByISBN(@Query("q") String isbn, @Query("orderBy") String orderBy, @Query("maxResults") String maxResults, @Query("key") String key);

    @GET
    Call<BookResponse> getBooksByCategory(@Url String url);
    
}
