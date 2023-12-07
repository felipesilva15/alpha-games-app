package senac.alphagames.ui.personalData;

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

public class PersonalDataActivity extends AppCompatActivity {
    private TextInputLayout nameInput, cpfInput, emailInput, passwordInput, passwordConfirmInput;
    private Button saveButton;
    private boolean isFormatting;
    private LoadingDialog loadingDialog;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_data);

        // Habilita o botão de retorno na ActionBar
        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        loadingDialog = new LoadingDialog(this);

        // Define os inputs e valores
        nameInput = findViewById(R.id.TextInputLayoutPersonalDataName);
        cpfInput = findViewById(R.id.TextInputLayoutPersonalDataCpf);
        emailInput = findViewById(R.id.TextInputLayoutPersonalDataEmail);
        passwordInput = findViewById(R.id.TextInputLayoutPersonalDataPassword);
        passwordConfirmInput = findViewById(R.id.TextInputLayoutPersonalDataPasswordConfirm);

        // Define a ação do botão de registro
        saveButton = findViewById(R.id.ButtonPersonalDataSave);
        saveButton.setOnClickListener(view -> updateUser());

        // Adiciona máscara ao campo de CPF
        maskCpfEditText();

        Bundle bundle = getIntent().getExtras();

        if (bundle != null && bundle.containsKey("id")) {
            String idString = bundle.getString("id");

            if (idString != null && !idString.isEmpty()) {
                id = Integer.parseInt(idString);
                loadUser();
            }
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

    private boolean validateName() {
        String name = Objects.requireNonNull(nameInput.getEditText()).getText().toString();

        if (name.isEmpty()) {
            nameInput.setError("Preenchimento obrigatório!");
            return false;
        }

        nameInput.setError("");
        return true;
    }

    private boolean validateCpf() {
        String cpf = Objects.requireNonNull(cpfInput.getEditText()).getText().toString().replaceAll("[^\\d]", "");

        if (cpf.isEmpty()) {
            cpfInput.setError("Preenchimento obrigatório!");
            return false;
        } else if (cpf.length() < 11) {
            cpfInput.setError("Insira um CPF válido!");
            return false;
        }

        cpfInput.setError("");
        return true;
    }

    private boolean validateEmail() {
        String email = Objects.requireNonNull(emailInput.getEditText()).getText().toString();

        if (email.isEmpty()) {
            emailInput.setError("Preenchimento obrigatório!");
            return false;
        } else if (!email.matches("^[a-z0-9.]+@[a-z0-9]+\\.[a-z]+(\\.[a-z]+)?$")) {
            emailInput.setError("Insira um e-mail válido!");
            return false;
        }

        emailInput.setError("");
        return true;
    }

    private boolean validatePassword() {
        String password = Objects.requireNonNull(passwordInput.getEditText()).getText().toString();
        String passwordConfirm = Objects.requireNonNull(passwordConfirmInput.getEditText()).getText().toString();

        if (password.isEmpty()) {
            passwordInput.setError("Preenchimento obrigatório!");
            return false;
        } else if (password.length() < 3) {
            passwordInput.setError("A senha precisa ter pelo menos 3 caracteres!");
            return false;
        } else if (!password.equals(passwordConfirm)) {
            passwordInput.setError("As senhas não coincidem!");
            passwordConfirmInput.setError("As senhas não coincidem!");
            return false;
        } else if (passwordConfirm.isEmpty()) {
            passwordConfirmInput.setError("Preenchimento obrigatório!");
            return false;
        }

        passwordInput.setError("");
        passwordConfirmInput.setError("");
        return true;
    }

    private boolean isValidForm() {
        boolean isValid = true;

        // Validação nome
        if (!validateName()) {
            isValid = false;
        }

        // Validação CPF
        if (!validateCpf()) {
            isValid = false;
        }

        // Validação e-mail
        if (!validateEmail()) {
            isValid = false;
        }

        // Validação senha
        if (!validatePassword()) {
            isValid = false;
        }

        return(isValid);
    }

    private void loadUser() {
        loadingDialog.show();

        UserClient client = HttpServiceGenerator.createHttpService(this, UserClient.class);
        Call<User> call = client.getUserById(id);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (!response.isSuccessful()) {
                    ErrorUtils.validateUnsuccessfulResponse(PersonalDataActivity.this, response);
                    loadingDialog.cancel();

                    return;
                }

                User user = response.body();

                nameInput.getEditText().setText(user.getUSUARIO_NOME());
                cpfInput.getEditText().setText(user.getUSUARIO_CPF());
                emailInput.getEditText().setText(user.getUSUARIO_EMAIL());

                loadingDialog.cancel();
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                loadingDialog.cancel();
                ErrorUtils.showErrorMessage(PersonalDataActivity.this, getString(R.string.network_error_message));
            }
        });
    }

    private void updateUser() {
        // Valida se o formulário é válido
        if(!isValidForm()) {
            return;
        }

        // Obtém os dados a serem enviados na requisição
        String name = Objects.requireNonNull(nameInput.getEditText()).getText().toString();
        String cpf = Objects.requireNonNull(cpfInput.getEditText()).getText().toString().replaceAll("[^\\d]", "");
        String email = Objects.requireNonNull(emailInput.getEditText()).getText().toString();
        String password = Objects.requireNonNull(passwordInput.getEditText()).getText().toString();

        // Exibe o dialog de loading
        loadingDialog.show();

        // Obtém o client e a chamada do objeto da requisição
        UserClient httpClient = HttpServiceGenerator.createHttpService(PersonalDataActivity.this, UserClient.class);
        Call<User> call = httpClient.updateUser(new User(name, email, password, cpf), 0);

        // Executa a requisição
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (!response.isSuccessful()) {
                    loadingDialog.cancel();
                    ErrorUtils.validateUnsuccessfulResponse(PersonalDataActivity.this, response);
                    return;
                }

                onBackPressed();
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                loadingDialog.cancel();
                ErrorUtils.showErrorMessage(PersonalDataActivity.this, getString(R.string.network_error_message));
            }
        });
    }

    private void maskCpfEditText() {
        Objects.requireNonNull(cpfInput.getEditText()).addTextChangedListener(new TextWatcher() {
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
                    cpfInput.getEditText().setText(text);

                    // Move o cursor para o final do texto formatado
                    cpfInput.getEditText().setSelection(text.length());

                    isFormatting = false;
                }
            }
        });
    }
}