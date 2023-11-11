package senac.alphagames.ui.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import senac.alphagames.R;
import senac.alphagames.api.HttpServiceGenerator;
import senac.alphagames.api.service.AuthClient;
import senac.alphagames.api.service.UserClient;
import senac.alphagames.helper.ErrorUtils;
import senac.alphagames.helper.LoadingDialog;
import senac.alphagames.helper.SharedUtils;
import senac.alphagames.model.User;
import senac.alphagames.ui.login.LoginActivity;

public class ProfileFragment extends Fragment {
    private LoadingDialog loadingDialog;
    private User user;
    private TextView txtGreeting, txtPersonalData, txtAddresses, txtOrders, txtLogout;

    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_profile, container, false);

        loadingDialog = new LoadingDialog(getContext());

        txtGreeting = root.findViewById(R.id.TextViewProfileGreeting);
        txtPersonalData = root.findViewById(R.id.TextViewProfilePersonalData);
        txtAddresses = root.findViewById(R.id.TextViewProfileAddresses);
        txtOrders = root.findViewById(R.id.TextViewProfileOrders);
        txtLogout = root.findViewById(R.id.TextViewProfileLogout);

        loadCurrentUserData();

        // Define os clicks das opções do menu
        txtLogout.setOnClickListener(view -> logout());

        return root;
    }

    private void loadCurrentUserData() {
        loadingDialog.show();
        UserClient client = HttpServiceGenerator.createHttpService(getContext(), UserClient.class);
        Call<User> call = client.getCurrentUser();

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (!response.isSuccessful()) {
                    ErrorUtils.validateUnsuccessfulResponse(getContext(), response);
                    loadingDialog.cancel();
                    return;
                }

                user = response.body();

                // Define a saudação
                txtGreeting.setText(String.format("Olá, %s 👋", user.getUSUARIO_NOME()));

//                Toast.makeText(getContext(), user.getUSUARIO_NOME(),Toast.LENGTH_SHORT).show();
//                Log.i("ProfileFragment", user.getUSUARIO_NOME());

                loadingDialog.cancel();
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                ErrorUtils.showErrorMessage(getContext(), getString(R.string.network_error_message));
                loadingDialog.cancel();
            }
        });
    }

    private void logout() {
        loadingDialog.show();

        AuthClient client = HttpServiceGenerator.createHttpService(getContext(), AuthClient.class);
        Call<Void> call = client.logout();

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (!response.isSuccessful()) {
                    ErrorUtils.validateUnsuccessfulResponse(getContext(), response);
                    loadingDialog.cancel();
                    return;
                }

                // Clean a JWT token
                getContext().getSharedPreferences("AlphaGamesPrefs", Context.MODE_PRIVATE)
                        .edit()
                        .putString("JwtToken", "")
                        .apply();

                // Show successfully logout message
                SharedUtils.showMessage(getContext(), "Atenção", "Logout efetuado com sucesso.");
                loadingDialog.cancel();

                // What to do?
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                ErrorUtils.showErrorMessage(getContext(), getString(R.string.network_error_message));
                loadingDialog.cancel();
            }
        });
    }
}