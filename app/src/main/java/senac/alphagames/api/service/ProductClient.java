package senac.alphagames.api.service;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import senac.alphagames.model.Product;

public interface ProductClient {
    @GET("product")
    Call<List<Product>> getProducts();

    @GET("product/{id}")
    Call<Product> getProductById(@Path("id") int id);
}
