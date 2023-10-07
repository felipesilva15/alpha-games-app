package senac.alphagames.ui.register;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import senac.alphagames.api.service.UserClient;
import senac.alphagames.helper.ErrorUtils;
import senac.alphagames.helper.LoadingDialog;
import senac.alphagames.model.User;

public class RegisterActivity extends AppCompatActivity {
    private TextInputLayout inputUsername, inputCpf, inputEmail, inputPassword, inputPasswordConfirm;
    private boolean isFormatting;
    private LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Habilita o botão de retorno na ActionBar
        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        loadingDialog = new LoadingDialog(this);

        // Define os inputs e valores
        inputUsername = findViewById(R.id.textInputLayoutUsername);
        inputCpf = findViewById(R.id.textInputLayoutCpf);
        inputEmail = findViewById(R.id.textInputLayoutEmail);
        inputPassword = findViewById(R.id.textInputLayoutPassword);
        inputPasswordConfirm = findViewById(R.id.textInputLayoutPasswordConfirm);

        // Define a ação do botão de registro
        Button btnRegisterUser = findViewById(R.id.buttonRegisterUser);
        btnRegisterUser.setOnClickListener(view -> register());

        // Adiciona máscara ao campo de CPF
        maskCpfEditText();
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
        String name = Objects.requireNonNull(inputUsername.getEditText()).getText().toString();
        String cpf = Objects.requireNonNull(inputCpf.getEditText()).getText().toString().replaceAll("[^\\d]", "");
        String email = Objects.requireNonNull(inputEmail.getEditText()).getText().toString();
        String password = Objects.requireNonNull(inputPassword.getEditText()).getText().toString();
        String passwordConfirm = Objects.requireNonNull(inputPasswordConfirm.getEditText()).getText().toString();

        boolean isValid = true;

        // Validação nome
        if (name.isEmpty()) {
            inputUsername.setError("Preenchimento obrigatório!");
            isValid = false;
        } else {
            inputUsername.setError("");
        }

        // Validação CPF
        if (cpf.isEmpty()) {
            inputCpf.setError("Preenchimento obrigatório!");
            isValid = false;
        } else if (cpf.length() < 11) {
            inputCpf.setError("Insira um CPF válido!");
            isValid = false;
        } else {
            inputCpf.setError("");
        }

        // Validação e-mail
        if (email.isEmpty()) {
            inputEmail.setError("Preenchimento obrigatório!");
            isValid = false;
        } else if (!email.matches("^[a-z0-9.]+@[a-z0-9]+\\.[a-z]+(\\.[a-z]+)?$")) {
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
        } else if (!password.equals(passwordConfirm)) {
            inputPassword.setError("As senhas não coincidem!");
            inputPasswordConfirm.setError("As senhas não coincidem!");
            isValid = false;
        } else if (passwordConfirm.isEmpty()) {
            inputPasswordConfirm.setError("Preenchimento obrigatório!");
            isValid = false;
        } else {
            inputPassword.setError("");
            inputPasswordConfirm.setError("");
        }

        return(isValid);
    }
    
    private void register() {
        // Valida se o formulário é válido
        if(!isValidForm()) {
            return;
        }

        // Obtém os dados a serem enviados na requisição
        String name = Objects.requireNonNull(inputUsername.getEditText()).getText().toString();
        String cpf = Objects.requireNonNull(inputCpf.getEditText()).getText().toString().replaceAll("[^\\d]", "");
        String email = Objects.requireNonNull(inputEmail.getEditText()).getText().toString();
        String password = Objects.requireNonNull(inputPassword.getEditText()).getText().toString();

        // Exibe o dialog de loading
        loadingDialog.show();

        // Obtém o client e a chamada do objeto da requisição
        UserClient httpClient = HttpServiceGenerator.createHttpService(RegisterActivity.this, UserClient.class);
        Call<User> call = httpClient.createUser(new User(name, email, password, cpf));

        // Executa a requisição
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (!response.isSuccessful()) {
                    loadingDialog.cancel();
                    ErrorUtils.validateUnsuccessfulResponse(RegisterActivity.this, response);
                    return;
                }

                onBackPressed();
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                loadingDialog.cancel();
                ErrorUtils.showErrorMessage(RegisterActivity.this, getString(R.string.network_error_message));
            }
        });
    }

    private void maskCpfEditText() {
        Objects.requireNonNull(inputCpf.getEditText()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void afterTextChanged(Editable s) {
                if (!isFormatting) {
                    isFormatting = true;

                    // Remove todos os caracteres não numéricos
                    String text = s.toString().replaceAll("[^\\d]", "");

                    // Formata o texto no formato desejado (###.###.###-##)
                    if (text.length() >= 10) {
                        text = text.substring(0, 3) + "." + text.substring(3, 6) + "." + text.substring(6, 9) + "-" + text.substring(9);
                    } else if (text.length() >= 7) {
                        text = text.substring(0, 3) + "." + text.substring(3, 6) + "." + text.substring(6);
                    } else if (text.length() >= 4) {
                        text = text.substring(0, 3) + "." + text.substring(3);
                    }

                    // Define o texto formatado de volta ao EditText
                    inputCpf.getEditText().setText(text);

                    // Move o cursor para o final do texto formatado
                    inputCpf.getEditText().setSelection(text.length());

                    isFormatting = false;
                }
            }
        });
    }
}