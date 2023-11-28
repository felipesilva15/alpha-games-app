package senac.alphagames.ui.order;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import senac.alphagames.R;
import senac.alphagames.adapters.AddressConsultAdapter;
import senac.alphagames.adapters.OrdersAdapter;
import senac.alphagames.api.HttpServiceGenerator;
import senac.alphagames.api.service.UserClient;
import senac.alphagames.helper.ErrorUtils;
import senac.alphagames.helper.LoadingDialog;
import senac.alphagames.model.Address;
import senac.alphagames.model.Order;
import senac.alphagames.ui.address.AddressConsultActivity;

public class OrderActivity extends AppCompatActivity {
    LoadingDialog loadingDialog;
    List<Order> ordersList;
    OrdersAdapter ordersAdapter;
    RecyclerView ordersRec;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        // Habilita o botão de retorno na ActionBar
        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        ordersRec = findViewById(R.id.RecyclerViewOrder);
        loadingDialog = new LoadingDialog(this);

        getOrders();
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

    private void getOrders() {
        loadingDialog.show();

        UserClient client = HttpServiceGenerator.createHttpService(this, UserClient.class);
        Call<List<Order>> call = client.getOrders();

        call.enqueue(new Callback<List<Order>>() {
            @Override
            public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {
                if (!response.isSuccessful()) {
                    ErrorUtils.validateUnsuccessfulResponse(OrderActivity.this, response);
                    loadingDialog.cancel();

                    return;
                }

                ordersList = response.body();

                ordersRec.setLayoutManager(new LinearLayoutManager(OrderActivity.this, RecyclerView.VERTICAL, false));
                ordersAdapter = new OrdersAdapter(OrderActivity.this, ordersList);
                ordersRec.setAdapter(ordersAdapter);

                loadingDialog.cancel();
            }

            @Override
            public void onFailure(Call<List<Order>> call, Throwable t) {
                loadingDialog.cancel();
                ErrorUtils.showErrorMessage(OrderActivity.this, getString(R.string.network_error_message));
            }
        });
    }
}