package senac.alphagames.ui.cart;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

import senac.alphagames.R;

public class CartActivity extends AppCompatActivity {
    AutoCompleteTextView addressInput;

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

        ArrayList<String> options = new ArrayList<String>();

        options.add("Endereço 1");
        options.add("Endereço 2");

        ArrayAdapter arrayAdapter = new ArrayAdapter(this, R.layout.dropdown_item, options);
        addressInput.setAdapter(arrayAdapter);
    }
}