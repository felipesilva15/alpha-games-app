package senac.alphagames.api.service;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import senac.alphagames.model.Address;

public interface AddressClient {
    @GET("address/{id}")
    Call<Address> getAddressById(@Path("id") int id);

    @POST("address")
    Call<Address> createAddress(@Body Address address);

    @PUT("address/{id}")
    Call<Address> updateAddress(@Body Address address, @Path("id") int id);
}
