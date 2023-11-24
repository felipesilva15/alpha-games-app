package senac.alphagames.ui.address;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import senac.alphagames.R;
import senac.alphagames.adapters.AddressConsultAdapter;
import senac.alphagames.adapters.ExploreProductsAdapter;
import senac.alphagames.api.HttpServiceGenerator;
import senac.alphagames.api.service.UserClient;
import senac.alphagames.helper.ErrorUtils;
import senac.alphagames.helper.LoadingDialog;
import senac.alphagames.model.Address;

public class AddressConsultActivity extends AppCompatActivity {
    LoadingDialog loadingDialog;
    List<Address> addressList;
    Button addAddresButton;
    AddressConsultAdapter addressConsultAdapter;
    RecyclerView adressesRec;
    boolean activityWasPaused = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_consult);

        // Habilita o botão de retorno na ActionBar
        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        loadingDialog = new LoadingDialog(this);
        adressesRec = findViewById(R.id.RecyclerViewAddressConsult);
        addAddresButton = findViewById(R.id.ButtonAddressConsultAddAddres);

        addAddresButton.setOnClickListener(view -> this.startActivity(new Intent(this, AddressRegistryActivity.class)));

        getAdresses();
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

    public void getAdresses() {
        loadingDialog.show();

        UserClient client = HttpServiceGenerator.createHttpService(AddressConsultActivity.this, UserClient.class);
        Call<List<Address>> call = client.getAdresses();

        call.enqueue(new Callback<List<Address>>() {
            @Override
            public void onResponse(Call<List<Address>> call, Response<List<Address>> response) {
                if (!response.isSuccessful()) {
                    ErrorUtils.validateUnsuccessfulResponse(AddressConsultActivity.this, response);
                    loadingDialog.cancel();

                    return;
                }

                addressList = response.body();

                adressesRec.setLayoutManager(new LinearLayoutManager(AddressConsultActivity.this, RecyclerView.VERTICAL, false));
                addressConsultAdapter = new AddressConsultAdapter(AddressConsultActivity.this, addressList);
                adressesRec.setAdapter(addressConsultAdapter);

                loadingDialog.cancel();
            }

            @Override
            public void onFailure(Call<List<Address>> call, Throwable t) {
                loadingDialog.cancel();
                ErrorUtils.showErrorMessage(AddressConsultActivity.this, getString(R.string.network_error_message));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (activityWasPaused) {
            getAdresses();
        }

        activityWasPaused = false;
    }

    @Override
    protected void onPause() {
        super.onPause();

        activityWasPaused = true;
    }
}