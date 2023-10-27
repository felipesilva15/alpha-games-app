package senac.alphagames.ui.product;

import android.graphics.Paint;
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
import senac.alphagames.adapters.ExploreProductsAdapter;
import senac.alphagames.adapters.ProductImageAdapter;
import senac.alphagames.api.HttpServiceGenerator;
import senac.alphagames.api.service.ProductClient;
import senac.alphagames.helper.ErrorUtils;
import senac.alphagames.helper.LoadingDialog;
import senac.alphagames.helper.SharedUtils;
import senac.alphagames.model.Product;
import senac.alphagames.model.ProductImage;

public class ProductActivity extends AppCompatActivity {
    private int productId;
    private Product product;
    private LoadingDialog loadingDialog;
    private TextView name, price, discount, stock, description;
    private ProductImageAdapter productImageAdapter;
    private RecyclerView imagesRec;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        // Habilita o botão de retorno na ActionBar
        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        loadingDialog = new LoadingDialog(this);
        productId = getIntent().getExtras().getInt("productId");

        name = findViewById(R.id.TextViewProductName);
        price = findViewById(R.id.TextViewProductPrice);
        discount = findViewById(R.id.TextViewProductDiscount);
        stock = findViewById(R.id.TextViewProductStock);
        description = findViewById(R.id.TextViewProductDescription);
        imagesRec = findViewById(R.id.RecyclerViewProductImages);

        getProduct();
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

    public void getProduct() {
        loadingDialog.show();

        ProductClient client = HttpServiceGenerator.createHttpService(this, ProductClient.class);
        Call<Product> call = client.getProductById(productId);

        call.enqueue(new Callback<Product>() {
            @Override
            public void onResponse(Call<Product> call, Response<Product> response) {
                if (!response.isSuccessful()) {
                    loadingDialog.cancel();
                    ErrorUtils.validateUnsuccessfulResponse(ProductActivity.this, response);
                }

                product = response.body();

                name.setText(product.getPRODUTO_NOME());
                price.setText(SharedUtils.formatToCurrency(product.getPRODUTO_PRECO() - product.getPRODUTO_DESCONTO()));
                stock.setText(String.format("%d em estoque", product.getPRODUTO_QTD()));
                description.setText(product.getPRODUTO_DESC());

                if (product.getPRODUTO_DESCONTO() != 0) {
                    discount.setText(SharedUtils.formatToCurrency(product.getPRODUTO_PRECO()));
                    discount.setPaintFlags(discount.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                } else {
                    discount.setText("");
                }

                if (product.getImages().size() == 0) {
                    product.getImages().add(new ProductImage());
                }

                imagesRec.setLayoutManager(new LinearLayoutManager(ProductActivity.this, RecyclerView.HORIZONTAL, false));
                productImageAdapter = new ProductImageAdapter(ProductActivity.this, product.getImages());
                imagesRec.setAdapter(productImageAdapter);

                loadingDialog.cancel();
            }

            @Override
            public void onFailure(Call<Product> call, Throwable t) {
                loadingDialog.cancel();
                ErrorUtils.showErrorMessage(ProductActivity.this, getString(R.string.network_error_message));
            }
        });
    }
}