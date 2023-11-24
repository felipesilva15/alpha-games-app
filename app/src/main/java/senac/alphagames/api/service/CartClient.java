package senac.alphagames.api.service;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.PATCH;
import retrofit2.http.Path;
import senac.alphagames.model.CartItem;

public interface CartClient {
    @PATCH("cart/{productId}")
    Call<Void> addItemToCart(@Body CartItem cartItem, @Path("productId") int productId);
}
