package senac.alphagames.api;

import android.content.Context;
import android.util.Log;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HttpServiceGenerator {
    private static final String BASE_URL = "http://192.168.1.14:8000/api/";

    private static OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();

    private static Retrofit.Builder builder = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create());

    public static Retrofit retrofit = builder.build();

    public static <S> S createHttpService(Context context, Class<S> clientClass) {
        // Intercepta todas as requisições e inclui o header Authorization
        httpClientBuilder.addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                Request.Builder newRequest = request.newBuilder();

                // Authorization - JWT Token
                String token = context.getSharedPreferences("AlphaGamesPrefs", Context.MODE_PRIVATE).getString("JwtToken", "");
                if (!token.isEmpty()) {
                    newRequest.header("Authorization", String.format("Bearer %s", token));
                }

                // Accept - Sempre deve ter retorno em JSON
                newRequest.header("Accept", "application/json");

                return chain.proceed(newRequest.build());
            }
        });

        builder = builder.client(httpClientBuilder.build());
        retrofit = builder.build();

        return retrofit.create(clientClass);
    }
}