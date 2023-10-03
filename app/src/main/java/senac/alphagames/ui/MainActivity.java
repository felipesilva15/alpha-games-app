package senac.alphagames.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import senac.alphagames.R;
import senac.alphagames.api.UserClient;
import senac.alphagames.helper.ErrorUtils;
import senac.alphagames.model.ApiError;
import senac.alphagames.model.User;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnSend = findViewById(R.id.btnSendRequest);

        btnSend.setOnClickListener(view -> sendRequest());
    }

    private void sendRequest() {
        TextView txt = findViewById(R.id.txtView);

        // Cria o OkHttpClient
        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();

        // Intercepta todas as requisições e inclui o Authorization
        okHttpClientBuilder.addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                Request.Builder newRequest = request.newBuilder().header("Authorization", String.format("Bearer %s", "JWT token"));

                return chain.proceed(newRequest.build());
            }
        });

        // Logs das requisições
//        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
//        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
//        okHttpClientBuilder.addInterceptor(logging);

        // Cria a instância do Builder do Retrofit
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("http://192.168.1.14:8000/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClientBuilder.build());

        Retrofit retrofit = builder.build();

        // Obtém o client e a chamada do objeto da requisição
        UserClient client = retrofit.create(UserClient.class);
        Call<User> call = client.show(1);

        // Executa a requisição
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (!response.isSuccessful()) {
                    if (response.code() == 401 || response.code() == 403) {
                        // Redirecionar para tela de login
                        return;
                    }

                    //ApiError apiError = ErrorUtils.parseError(response);
                    Log.e(this.getClass().getName(), response.errorBody().toString());
                    //Toast.makeText(MainActivity.this, apiError.getMessage(), Toast.LENGTH_SHORT).show();
                    Toast.makeText(MainActivity.this, "Ocorreu um erro. Tente novamente mais tarde.", Toast.LENGTH_SHORT).show();
                    return;
                }

                User resp = response.body();

                Log.i(this.getClass().getName(), "Deu tudo Certo!");
                txt.setText(resp.getUSUARIO_NOME() + " - " + resp.getUSUARIO_EMAIL());
                //Toast.makeText(MainActivity.this, resp.getUSUARIO_NOME(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e(this.getClass().getName(), t.getMessage());
                Toast.makeText(MainActivity.this, "Ocorreu um erro. Tente novamente mais tarde.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}