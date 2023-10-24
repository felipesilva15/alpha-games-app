package senac.alphagames.ui.main;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import senac.alphagames.R;
import senac.alphagames.api.HttpServiceGenerator;
import senac.alphagames.api.service.ProductClient;
import senac.alphagames.helper.ErrorUtils;
import senac.alphagames.helper.LoadingDialog;
import senac.alphagames.model.Product;

public class ExploreFragment extends Fragment {
    private static final String ARG_PARAM1 = "search";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1, mParam2;
    private LoadingDialog loadingDialog;

    public ExploreFragment() {
        // Required empty public constructor
    }

    public static ExploreFragment newInstance(String param1, String param2) {
        ExploreFragment fragment = new ExploreFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mParam1 = "";

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        loadingDialog = new LoadingDialog(getContext());

        Log.i("ExploreFragment", "Parâmetro 1: " + mParam1);

        getProducts();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_explore, container, false);
    }

    public void getProducts() {
        loadingDialog.show();

        ProductClient client = HttpServiceGenerator.createHttpService(getContext(), ProductClient.class);
        Call<List<Product>> call = client.getProducts(mParam1);

        call.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (!response.isSuccessful()) {
                    loadingDialog.cancel();
                    ErrorUtils.validateUnsuccessfulResponse(getContext(), response);
                }

                List<Product> products = response.body();

                for (Product product: products) {
                    Log.i("ExploreFragment", product.getPRODUTO_NOME());
                }

                loadingDialog.cancel();
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                loadingDialog.cancel();
                ErrorUtils.showErrorMessage(getContext(), getString(R.string.network_error_message));
            }
        });
    }
}