package senac.alphagames.ui.login;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import senac.alphagames.R;
import senac.alphagames.api.HttpServiceGenerator;
import senac.alphagames.api.service.AuthenticationClient;
import senac.alphagames.api.service.UserClient;
import senac.alphagames.helper.ErrorUtils;
import senac.alphagames.model.TokenInfo;
import senac.alphagames.model.User;
import senac.alphagames.ui.main.MainActivity;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Habilita o botão de retorno na ActionBar
        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        // Trata o clique no botão de retorno (seta para trás)
        if (id == android.R.id.home) {
            onBackPressed(); // Fecha a Activity atual
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void login() {
        // Obtém o client e a chamada do objeto da requisição
        AuthenticationClient httpClient = HttpServiceGenerator.createHttpService(LoginActivity.this, AuthenticationClient.class);
        Call<TokenInfo> call = httpClient.login(new User("felipe.silvae@alphagames.com.br", "123"));

        // Executa a requisição
        call.enqueue(new Callback<TokenInfo>() {
            @Override
            public void onResponse(Call<TokenInfo> call, Response<TokenInfo> response) {
                if (!ErrorUtils.isValidResponse(LoginActivity.this, response)) {
                    return;
                }

                TokenInfo resp = response.body();

                getSharedPreferences("AlphaGamesPrefs", Context.MODE_PRIVATE)
                    .edit()
                    .putString("JwtToken", resp.getAccess_token())
                    .apply();

                Toast.makeText(LoginActivity.this, resp.getAccess_token(), Toast.LENGTH_SHORT);
            }

            @Override
            public void onFailure(Call<TokenInfo> call, Throwable t) {
                ErrorUtils.showErrorMessage(LoginActivity.this, getString(R.string.network_error_message));
            }
        });
    }
}