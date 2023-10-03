package senac.alphagames.api;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import senac.alphagames.model.User;

public interface UserClient {
    @GET("user")
    Call<List<User>> index();

    @GET("user/{id}")
    Call<User> show(@Path("id") int id);

    @POST("user")
    Call<User> store (@Body User user);
}
