package senac.alphagames.ui.address;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.textfield.TextInputLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import senac.alphagames.R;
import senac.alphagames.api.HttpServiceGenerator;
import senac.alphagames.api.service.CepClient;
import senac.alphagames.helper.ErrorUtils;
import senac.alphagames.helper.LoadingDialog;
import senac.alphagames.model.SearchCepDTO;

public class AddressRegistryActivity extends AppCompatActivity {
    LoadingDialog loadingDialog;
    SearchCepDTO searchCepDTO;

    TextInputLayout nameInput, cepInput, streetAddressInput, cityInput, stateInput, addressNumberInput, complementInput;

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

        // Define a ação do click do ícone de busca de CEP
        cepInput.setEndIconOnClickListener(view -> searchCep());
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
}