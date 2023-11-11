package senac.alphagames.api.service;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import senac.alphagames.model.User;

public interface UserClient {
    @GET("user")
    Call<List<User>> getUsers();

    @GET("user/{id}")
    Call<User> getUserById(@Path("id") int id);

    @POST("user")
    Call<User> createUser(@Body User user);

    @GET("me")
    Call<User> getCurrentUser();
}
