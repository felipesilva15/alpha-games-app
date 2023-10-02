package senac.alphagames.api;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import senac.alphagames.model.User;

public interface UserClient {
    @GET("/user")
    Call<List<User>> index();

    @GET("/user/{id}")
    Call<User> show(@Path("id") int id);


}
