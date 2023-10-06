package senac.alphagames.ui.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import senac.alphagames.R;
import senac.alphagames.api.HttpServiceGenerator;
import senac.alphagames.api.service.AuthenticationClient;
import senac.alphagames.helper.ErrorUtils;
import senac.alphagames.helper.LoadingDialog;
import senac.alphagames.model.TokenInfo;
import senac.alphagames.model.User;
import senac.alphagames.ui.register.RegisterActivity;

public class LoginActivity extends AppCompatActivity {
    private TextInputLayout inputEmail;
    private TextInputLayout inputPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Habilita o botão de retorno na ActionBar
        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // Define os inputs e valores
        inputEmail = findViewById(R.id.textInputLayoutEmail);
        inputPassword = findViewById(R.id.textInputLayoutPassword);

        // Define a ação do botão de login
        Button btnLogin = findViewById(R.id.buttonLogin);
        btnLogin.setOnClickListener(view -> login());

        // Define a ação do botão de registro
        Button btnRegister = findViewById(R.id.buttonRegister);
        btnRegister.setOnClickListener(view -> {
            Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(registerIntent);
        });
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

    private boolean isValidForm() {
        String email = Objects.requireNonNull(inputEmail.getEditText()).getText().toString();
        String password = Objects.requireNonNull(inputPassword.getEditText()).getText().toString();

        boolean isValid = true;

        // Validação e-mail
        if (email.isEmpty()) {
            inputEmail.setError("Preenchimento obrigatório!");
            isValid = false;
        } else if (!email.contains("@")) {
            inputEmail.setError("Insira um e-mail válido!");
            isValid = false;
        } else {
            inputEmail.setError("");
        }

        // Validação senha
        if (password.isEmpty()) {
            inputPassword.setError("Preenchimento obrigatório!");
            isValid = false;
        } else if (password.length() < 3) {
            inputPassword.setError("A senha precisa ter pelo menos 3 caracteres!");
            isValid = false;
        } else {
            inputPassword.setError("");
        }

        return(isValid);
    }

    private void login() {
        // Valida se o formulário é válido
        if(!isValidForm()) {
            return;
        }

        // Obtém os dados a serem enviados na requisição
        String email = Objects.requireNonNull(inputEmail.getEditText()).getText().toString();
        String password = Objects.requireNonNull(inputPassword.getEditText()).getText().toString();

        // Exibe o dialog de loading
        final LoadingDialog loadingDialog = new LoadingDialog(LoginActivity.this);
        loadingDialog.startLoadingDialog();

        // Obtém o client e a chamada do objeto da requisição
        AuthenticationClient httpClient = HttpServiceGenerator.createHttpService(LoginActivity.this, AuthenticationClient.class);
        Call<TokenInfo> call = httpClient.login(new User(email, password));

        // Executa a requisição
        call.enqueue(new Callback<TokenInfo>() {
            @Override
            public void onResponse(Call<TokenInfo> call, Response<TokenInfo> response) {
                if (!response.isSuccessful()) {
                    ErrorUtils.validateUnsuccessfulResponse(LoginActivity.this, response);
                    return;
                }

                TokenInfo resp = response.body();
                Log.i("login", "Token de acesso: " + resp.getAccess_token());
                //Toast.makeText(LoginActivity.this, resp.getAccess_token(), Toast.LENGTH_SHORT);

                getSharedPreferences("AlphaGamesPrefs", Context.MODE_PRIVATE)
                    .edit()
                    .putString("JwtToken", resp.getAccess_token())
                    .apply();

                onBackPressed();
            }

            @Override
            public void onFailure(Call<TokenInfo> call, Throwable t) {
                loadingDialog.dismissDialog();
                ErrorUtils.showErrorMessage(LoginActivity.this, getString(R.string.network_error_message));
            }
        });
    }
}