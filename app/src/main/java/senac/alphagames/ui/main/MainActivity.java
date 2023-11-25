package senac.alphagames.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.Objects;

import senac.alphagames.R;
import senac.alphagames.databinding.ActivityMainBinding;
import senac.alphagames.ui.cart.CartActivity;

public class MainActivity extends AppCompatActivity {
    public ActivityMainBinding binding;
    public boolean isProgrammaticSelection = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new HomeFragment());

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("");

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            if(isProgrammaticSelection) {
                isProgrammaticSelection =  false;
                return true;
            }

            if (item.getItemId() == R.id.home_menu_item) {
                replaceFragment(new HomeFragment());
            } else if (item.getItemId() == R.id.explore_menu_item) {
                replaceFragment(new ExploreFragment());
            } else if (item.getItemId() == R.id.profile_menu_item) {
                replaceFragment(new ProfileFragment());
            }

            return true;
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.menuItemSearchProduct);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setQueryHint("Buscar produtos...");

        // Configura a ação de pesquisa de produtos
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Fragment exploreFragment = new ExploreFragment();
                Bundle args = new Bundle();

                args.putString("search", query);
                exploreFragment.setArguments(args);

                replaceFragment(exploreFragment);

                isProgrammaticSelection = true;
                binding.bottomNavigationView.setSelectedItemId(R.id.explore_menu_item);

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menuItemSearchProduct) {
            // Nada a fazer
            return true;
        } else if (id == R.id.menuItemCart) {
            // Abrir activity de carrinho
            Intent intent = new Intent(this, CartActivity.class);
            startActivity(intent);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main_frame_layout, fragment);
        fragmentTransaction.commit();
    }
}