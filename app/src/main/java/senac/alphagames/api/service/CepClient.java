package senac.alphagames.api.service;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import senac.alphagames.model.SearchCepDTO;

public interface CepClient {
    @GET("/cep/{cep}")
    Call<SearchCepDTO> searchCep(@Path("cep") String cep);
}
