package senac.alphagames.ui.cart;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import senac.alphagames.R;
import senac.alphagames.adapters.CartItemAdapter;
import senac.alphagames.api.HttpServiceGenerator;
import senac.alphagames.api.service.AddressClient;
import senac.alphagames.api.service.OrderClient;
import senac.alphagames.api.service.UserClient;
import senac.alphagames.helper.ErrorUtils;
import senac.alphagames.helper.LoadingDialog;
import senac.alphagames.helper.SharedUtils;
import senac.alphagames.model.Address;
import senac.alphagames.model.CartItem;
import senac.alphagames.model.Order;
import senac.alphagames.ui.address.AddressRegistryActivity;

public class CartActivity extends AppCompatActivity {
    AutoCompleteTextView addressInput;
    LoadingDialog loadingDialog;
    List<Address> addressList;
    List<CartItem> cartItemList;
    CartItemAdapter cartItemAdapter;
    RecyclerView cartItemRec;
    TextView subtotalText, discountText, totalText;
    Button checkoutButton;
    int selectAddressId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        // Habilita o botão de retorno na ActionBar
        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        addressInput = findViewById(R.id.AutoCompleteTextViewCartAddress);
        cartItemRec = findViewById(R.id.RecyclerViewCartItems);
        subtotalText = findViewById(R.id.TextViewCartSubtotal);
        discountText = findViewById(R.id.TextViewCartDiscount);
        totalText = findViewById(R.id.TextViewCartTotal);
        checkoutButton = findViewById(R.id.ButtonCartCheckout);

        loadingDialog = new LoadingDialog(this);

        getCartItems();

        checkoutButton.setOnClickListener(view -> checkout());
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

    @Override
    protected void onResume() {
        super.onResume();

        loadUserAddresses();
    }

    private void loadUserAddresses() {
        UserClient client = HttpServiceGenerator.createHttpService(CartActivity.this, UserClient.class);
        Call<List<Address>> call = client.getAdresses();

        call.enqueue(new Callback<List<Address>>() {
            @Override
            public void onResponse(Call<List<Address>> call, Response<List<Address>> response) {
                if (!response.isSuccessful()) {
                    ErrorUtils.validateUnsuccessfulResponse(CartActivity.this, response);

                    return;
                }

                addressList = response.body();
                ArrayList<String> options = new ArrayList<>();

                for (Address address: addressList) {
                    options.add(address.getENDERECO_NOME());
                }

                ArrayAdapter arrayAdapter = new ArrayAdapter(CartActivity.this, R.layout.dropdown_item, options);
                addressInput.setAdapter(arrayAdapter);

                addressInput.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        selectAddressId = addressList.get(i).getENDERECO_ID();
                    }
                });
            }

            @Override
            public void onFailure(Call<List<Address>> call, Throwable t) {
                ErrorUtils.showErrorMessage(CartActivity.this, getString(R.string.network_error_message));
            }
        });
    }

    private void getCartItems() {
        loadingDialog.show();

        UserClient client = HttpServiceGenerator.createHttpService(CartActivity.this, UserClient.class);
        Call<List<CartItem>> call = client.getCartItems();

        call.enqueue(new Callback<List<CartItem>>() {
            @Override
            public void onResponse(Call<List<CartItem>> call, Response<List<CartItem>> response) {
                if (!response.isSuccessful()) {
                    ErrorUtils.validateUnsuccessfulResponse(CartActivity.this, response);
                    loadingDialog.cancel();

                    return;
                }

                cartItemList = response.body();

                cartItemRec.setLayoutManager(new LinearLayoutManager(CartActivity.this, RecyclerView.VERTICAL, false));
                cartItemAdapter = new CartItemAdapter(CartActivity.this, cartItemList);
                cartItemRec.setAdapter(cartItemAdapter);

                calculateTotal();

                loadingDialog.cancel();
            }

            @Override
            public void onFailure(Call<List<CartItem>> call, Throwable t) {
                loadingDialog.cancel();
                ErrorUtils.showErrorMessage(CartActivity.this, getString(R.string.network_error_message));
            }
        });
    }

    public void calculateTotal() {
        double subtotal = 0, discount = 0;

        for (CartItem item: cartItemList) {
            subtotal += item.getProduct().getPRODUTO_PRECO() * item.getITEM_QTD();
            discount += item.getProduct().getPRODUTO_DESCONTO() * item.getITEM_QTD();
        }

        subtotalText.setText(SharedUtils.formatToCurrency(subtotal));
        discountText.setText(SharedUtils.formatToCurrency(discount));
        totalText.setText(SharedUtils.formatToCurrency(subtotal - discount));
    }

    private boolean isValidOrder() {
        if (selectAddressId == 0) {
            SharedUtils.showMessage(CartActivity.this, "Atenção", "Selecione um endereço!");
            return false;
        }

        if (cartItemList.isEmpty()) {
            SharedUtils.showMessage(CartActivity.this, "Atenção", "Coloque pelo menos um item no carrinho para finalizar a compra!");
            return false;
        }

        return true;
    }

    private void checkout() {
        if (!isValidOrder()) {
            return;
        }

        loadingDialog.show();

        Order order = new Order(selectAddressId);

        OrderClient client = HttpServiceGenerator.createHttpService(this, OrderClient.class);
        Call<Void> call = client.createOrder(order);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (!response.isSuccessful()) {
                    ErrorUtils.validateUnsuccessfulResponse(CartActivity.this, response);
                    loadingDialog.cancel();

                    return;
                }

                onBackPressed();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                loadingDialog.cancel();
                ErrorUtils.showErrorMessage(CartActivity.this, getString(R.string.network_error_message));
            }
        });
    }
}