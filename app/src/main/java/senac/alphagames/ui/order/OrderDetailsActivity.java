package senac.alphagames.ui.order;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import senac.alphagames.R;
import senac.alphagames.adapters.OrderDetailsItemAdapter;
import senac.alphagames.api.HttpServiceGenerator;
import senac.alphagames.api.service.OrderClient;
import senac.alphagames.helper.ErrorUtils;
import senac.alphagames.helper.LoadingDialog;
import senac.alphagames.helper.SharedUtils;
import senac.alphagames.model.Order;
import senac.alphagames.model.OrderItems;

public class OrderDetailsActivity extends AppCompatActivity {
    LoadingDialog loadingDialog;
    int id;
    Order order;
    TextView numberText, dateText, statusText, addressText, totalText;
    OrderDetailsItemAdapter orderDetailsItemAdapter;
    RecyclerView itemsRec;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);

        // Habilita o botão de retorno na ActionBar
        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        numberText = findViewById(R.id.TextViewOrderDetailsNumber);
        dateText = findViewById(R.id.TextViewOrderDetailsDate);
        statusText = findViewById(R.id.TextViewOrderDetailsStatus);
        addressText = findViewById(R.id.TextViewOrderDetailsAddress);
        totalText = findViewById(R.id.TextViewOrderDetailsTotal);
        itemsRec = findViewById(R.id.RecyclerViewOrderDetailsItems);

        loadingDialog = new LoadingDialog(this);

        Bundle bundle = getIntent().getExtras();

        if (bundle != null && bundle.containsKey("id")) {
            String idString = bundle.getString("id");

            if (idString != null && !idString.isEmpty()) {
                id = Integer.parseInt(idString);
                loadOrder();
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

    private void loadOrder() {
        loadingDialog.show();

        OrderClient client = HttpServiceGenerator.createHttpService(this, OrderClient.class);
        Call<Order> call = client.getOrderById(id);

        call.enqueue(new Callback<Order>() {
            @Override
            public void onResponse(Call<Order> call, Response<Order> response) {
                if (!response.isSuccessful()) {
                    ErrorUtils.validateUnsuccessfulResponse(OrderDetailsActivity.this, response);
                    loadingDialog.cancel();

                    return;
                }

                order = response.body();
                double total = 0;

                for (OrderItems item: order.getItems()) {
                    total += item.getITEM_QTD() * item.getITEM_PRECO();
                }

                numberText.setText(SharedUtils.formatCode(order.getPEDIDO_ID(), 0));
                dateText.setText(SharedUtils.formatDate(order.getPEDIDO_DATA()));
                statusText.setText(order.getStatus().getSTATUS_DESC());
                addressText.setText(SharedUtils.formatAddress(order.getAddress()));
                totalText.setText(SharedUtils.formatToCurrency(total));

                itemsRec.setLayoutManager(new LinearLayoutManager(OrderDetailsActivity.this, RecyclerView.VERTICAL, false));
                orderDetailsItemAdapter = new OrderDetailsItemAdapter(OrderDetailsActivity.this, order.getItems());
                itemsRec.setAdapter(orderDetailsItemAdapter);

                loadingDialog.cancel();
            }

            @Override
            public void onFailure(Call<Order> call, Throwable t) {
                ErrorUtils.showErrorMessage(OrderDetailsActivity.this, getString(R.string.network_error_message));
                loadingDialog.cancel();
            }
        });
    }
}