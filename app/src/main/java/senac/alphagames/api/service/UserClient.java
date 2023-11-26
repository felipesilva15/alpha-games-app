package senac.alphagames.api.service;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import senac.alphagames.model.Address;
import senac.alphagames.model.CartItem;
import senac.alphagames.model.User;

public interface UserClient {
    @GET("user")
    Call<List<User>> getUsers();

    @GET("user/{id}")
    Call<User> getUserById(@Path("id") int id);

    @POST("user")
    Call<User> createUser(@Body User user);

    @PUT("user/{id}")
    Call<User> updateUser(@Body User user, @Path("id") int id);

    @GET("me")
    Call<User> getCurrentUser();

    @GET("user/adresses")
    Call<List<Address>> getAdresses();

    @GET("user/cart")
    Call<List<CartItem>> getCartItems();
}
