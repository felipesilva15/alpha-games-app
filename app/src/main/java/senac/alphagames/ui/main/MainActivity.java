package senac.alphagames.ui.main;

import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import retrofit2.Call;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import retrofit2.Callback;
import retrofit2.Response;
import senac.alphagames.R;
import senac.alphagames.api.HttpServiceGenerator;
import senac.alphagames.api.service.UserClient;
import senac.alphagames.databinding.ActivityMainBinding;
import senac.alphagames.helper.ErrorUtils;
import senac.alphagames.model.User;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new HomeFragment());

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
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

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main_frame_layout, fragment);
        fragmentTransaction.commit();
    }
}