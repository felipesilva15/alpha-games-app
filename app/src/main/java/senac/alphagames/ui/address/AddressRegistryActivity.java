package senac.alphagames.ui.address;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import senac.alphagames.R;
import senac.alphagames.api.HttpServiceGenerator;
import senac.alphagames.api.service.AddressClient;
import senac.alphagames.api.service.CepClient;
import senac.alphagames.helper.ErrorUtils;
import senac.alphagames.helper.LoadingDialog;
import senac.alphagames.model.Address;
import senac.alphagames.model.SearchCepDTO;

public class AddressRegistryActivity extends AppCompatActivity {
    LoadingDialog loadingDialog;
    SearchCepDTO searchCepDTO;

    TextInputLayout nameInput, cepInput, streetAddressInput, cityInput, stateInput, addressNumberInput, complementInput;
    Button saveButton;
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_registry);

        // Habilita o botão de retorno na ActionBar
        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        loadingDialog = new LoadingDialog(this);

        nameInput = findViewById(R.id.TextInputLayoutAddressRegistryName);
        cepInput = findViewById(R.id.TextInputLayoutAddressRegistryCep);
        streetAddressInput = findViewById(R.id.TextInputLayoutAddressRegistryStreetAddress);
        cityInput = findViewById(R.id.TextInputLayoutAddressRegistryCity);
        stateInput = findViewById(R.id.TextInputLayoutAddressRegistryState);
        addressNumberInput = findViewById(R.id.TextInputLayoutAddressRegistryAddressNumber);
        complementInput = findViewById(R.id.TextInputLayoutAddressRegistryComplement);
        saveButton = findViewById(R.id.ButtonAddressRegistrySave);

        // Define a ação do click do ícone de busca de CEP
        cepInput.setEndIconOnClickListener(view -> searchCep());

        saveButton.setOnClickListener(view -> saveAddress());

        Bundle bundle = getIntent().getExtras();

        if (bundle != null && bundle.containsKey("id")) {
            String idString = bundle.getString("id");

            if (idString != null && !idString.isEmpty()) {
                id = Integer.parseInt(idString);
                loadAddress();
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
        String name = nameInput.getEditText().getText().toString();

        if (name.isEmpty()) {
            nameInput.setError("Preenchimento obrigatório!");
            return false;
        }

        nameInput.setError("");
        return true;
    }

    private boolean validateCep() {
        String cep = cepInput.getEditText().getText().toString();

        if (cep.isEmpty()) {
            cepInput.setError("Preenchimento obrigatório!");
            return false;
        } else if (cep.length() < 8) {
            cepInput.setError("O CEP precisa ter pelo menos 3 caracteres!");
            return false;
        }

        cepInput.setError("");
        return true;
    }

    private boolean validateAddressNumber() {
        String addressNumber = addressNumberInput.getEditText().getText().toString();

        if (addressNumber.isEmpty()) {
            addressNumberInput.setError("Preenchimento obrigatório!");
            return false;
        }

        addressNumberInput.setError("");
        return true;
    }

    private boolean isValidForm() {
        boolean isValid = true;

        if (!validateName()) {
            isValid = false;
        }

        if (!validateCep()) {
            isValid = false;
        }

        if (!validateAddressNumber()) {
            isValid = false;
        }

        return(isValid);
    }

    public void searchCep() {
        loadingDialog.show();

        String cep = cepInput.getEditText().getText().toString();

        CepClient client = HttpServiceGenerator.createHttpService(AddressRegistryActivity.this, CepClient.class);
        Call<SearchCepDTO> call = client.searchCep(cep);

        call.enqueue(new Callback<SearchCepDTO>() {
            @Override
            public void onResponse(Call<SearchCepDTO> call, Response<SearchCepDTO> response) {
                if (!response.isSuccessful()) {
                    ErrorUtils.validateUnsuccessfulResponse(AddressRegistryActivity.this, response);

                    streetAddressInput.getEditText().setText("");
                    cityInput.getEditText().setText("");
                    stateInput.getEditText().setText("");

                    loadingDialog.cancel();

                    return;
                }

                searchCepDTO = response.body();

                streetAddressInput.getEditText().setText(searchCepDTO.getLogradouro());
                cityInput.getEditText().setText(searchCepDTO.getCidade());
                stateInput.getEditText().setText(searchCepDTO.getUf());

                loadingDialog.cancel();
            }

            @Override
            public void onFailure(Call<SearchCepDTO> call, Throwable t) {
                loadingDialog.cancel();
                ErrorUtils.showErrorMessage(AddressRegistryActivity.this, getString(R.string.network_error_message));
            }
        });
    }

    private void loadAddress() {
        loadingDialog.show();

        AddressClient client = HttpServiceGenerator.createHttpService(this, AddressClient.class);
        Call<Address> call = client.getAddressById(id);

        call.enqueue(new Callback<Address>() {
            @Override
            public void onResponse(Call<Address> call, Response<Address> response) {
                if (!response.isSuccessful()) {
                    ErrorUtils.validateUnsuccessfulResponse(AddressRegistryActivity.this, response);
                    loadingDialog.cancel();

                    return;
                }

                Address address = response.body();

                nameInput.getEditText().setText(address.getENDERECO_NOME());
                cepInput.getEditText().setText(address.getENDERECO_CEP());
                streetAddressInput.getEditText().setText(address.getENDERECO_LOGRADOURO());
                cityInput.getEditText().setText(address.getENDERECO_CIDADE());
                stateInput.getEditText().setText(address.getENDERECO_ESTADO());
                addressNumberInput.getEditText().setText(address.getENDERECO_NUMERO());
                complementInput.getEditText().setText(address.getENDERECO_COMPLEMENTO());

                loadingDialog.cancel();
            }

            @Override
            public void onFailure(Call<Address> call, Throwable t) {
                loadingDialog.cancel();
                ErrorUtils.showErrorMessage(AddressRegistryActivity.this, getString(R.string.network_error_message));
            }
        });
    }

    private void saveAddress() {
        // Valida se o formulário é válido
        if(!isValidForm()) {
            return;
        }

        loadingDialog.show();

        Address address = new Address(cepInput.getEditText().getText().toString(),
                nameInput.getEditText().getText().toString(),
                streetAddressInput.getEditText().getText().toString(),
                addressNumberInput.getEditText().getText().toString(),
                complementInput.getEditText().getText().toString(),
                cityInput.getEditText().getText().toString(),
                stateInput.getEditText().getText().toString());

        AddressClient client = HttpServiceGenerator.createHttpService(this, AddressClient.class);
        Call<Address> call;

        if (id == 0) {
            call = client.createAddress(address);
        } else  {
            call = client.updateAddress(address, id);
        }

        call.enqueue(new Callback<Address>() {
            @Override
            public void onResponse(Call<Address> call, Response<Address> response) {
                if (!response.isSuccessful()) {
                    ErrorUtils.validateUnsuccessfulResponse(AddressRegistryActivity.this, response);
                    loadingDialog.cancel();

                    return;
                }

                onBackPressed();
            }

            @Override
            public void onFailure(Call<Address> call, Throwable t) {
                loadingDialog.cancel();
                ErrorUtils.showErrorMessage(AddressRegistryActivity.this, getString(R.string.network_error_message));
            }
        });
    }
}