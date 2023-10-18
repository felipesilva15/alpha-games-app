package senac.alphagames.api.service;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import senac.alphagames.model.TokenInfo;
import senac.alphagames.model.User;

public interface AuthClient {
    @POST("login")
    Call<TokenInfo> login(@Body User user);

    @POST("logout")
    Call<Void> logout();
}
