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
import senac.alphagames.helper.SharedUtils;
import senac.alphagames.model.Order;
import senac.alphagames.ui.order.OrderDetailsActivity;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.ViewHolder> {
    Context context;
    List<Order> list;

    public OrdersAdapter(Context context, List<Order> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public OrdersAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OrdersAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.nav_order_listitem, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull OrdersAdapter.ViewHolder holder, int position) {
        Order item = list.get(position);

        holder.number.setText(SharedUtils.formatCode(item.getPEDIDO_ID(), 0));
        holder.date.setText(SharedUtils.formatDate(item.getPEDIDO_DATA()));
        holder.status.setText(item.getStatus().getSTATUS_DESC());

        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, OrderDetailsActivity.class);

            intent.putExtra("id", String.valueOf(item.getPEDIDO_ID()));
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView number, date, status;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            number = itemView.findViewById(R.id.ListitemOrderNumber);
            date = itemView.findViewById(R.id.ListitemOrderDate);
            status = itemView.findViewById(R.id.ListitemOrderStatus);
        }
    }
}
