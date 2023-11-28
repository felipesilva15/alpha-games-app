package senac.alphagames.api.service;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import senac.alphagames.model.Order;

public interface OrderClient {
    @GET("order/{id}")
    Call<Order> getOrderById(@Path("id") int id);

    @POST("order")
    Call<Void> createOrder(@Body Order order);
}
