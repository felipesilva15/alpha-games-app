package senac.alphagames.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import senac.alphagames.R;
import senac.alphagames.model.Address;
import senac.alphagames.model.Product;
import senac.alphagames.ui.address.AddressRegistryActivity;
import senac.alphagames.ui.product.ProductActivity;

public class AddressConsultAdapter extends RecyclerView.Adapter<AddressConsultAdapter.ViewHolder> {
    Context context;
    List<Address> list;

    public AddressConsultAdapter(Context context, List<Address> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public AddressConsultAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.nav_address_consult_adresses, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AddressConsultAdapter.ViewHolder holder, int position) {
        String formattedAddress = list.get(position).getENDERECO_LOGRADOURO() + ", " + list.get(position).getENDERECO_NUMERO();

        if (!list.get(position).getENDERECO_COMPLEMENTO().isEmpty()) {
            formattedAddress += " - " + list.get(position).getENDERECO_COMPLEMENTO();
        }

        formattedAddress += " - " + list.get(position).getENDERECO_CIDADE() + ", " + list.get(position).getENDERECO_ESTADO();

        holder.name.setText(list.get(position).getENDERECO_NOME());
        holder.address.setText(formattedAddress);

        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, AddressRegistryActivity.class);

            intent.putExtra("id", list.get(position).getENDERECO_ID());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, address;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.ListitemAddressConsultName);
            address = itemView.findViewById(R.id.ListitemAddressConsultAddress);
        }
    }
}
