package senac.alphagames.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import senac.alphagames.R;
import senac.alphagames.helper.GlideTrustManager;
import senac.alphagames.helper.SharedUtils;
import senac.alphagames.model.OrderItems;

public class OrderDetailsItemAdapter extends RecyclerView.Adapter<OrderDetailsItemAdapter.ViewHolder> {
    Context context;
    List<OrderItems> list;

    public OrderDetailsItemAdapter(Context context, List<OrderItems> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.nav_order_details_listitem, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OrderItems item = list.get(position);
        GlideTrustManager.allowAllSSL();

        if (item.getProduct().getImages() != null && item.getProduct().getImages().size() > 0 && item.getProduct().getImages().get(0).getIMAGEM_URL().contains("https")) {
            Glide.with(context).load(item.getProduct().getImages().get(0).getIMAGEM_URL()).into(holder.image);
        } else {
            SharedUtils.setInvalidImageToImageView(holder.image);
        }

        holder.name.setText(item.getProduct().getPRODUTO_NOME());
        holder.category.setText(item.getProduct().getCategory().getCATEGORIA_NOME());
        holder.price.setText(SharedUtils.formatToCurrency(item.getITEM_PRECO()));
        holder.quantity.setText(String.valueOf(item.getITEM_QTD()));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView name, category, price, quantity;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.ListitemOrderDetailstImage);
            name = itemView.findViewById(R.id.ListitemOrderDetailsName);
            category = itemView.findViewById(R.id.ListitemOrderDetailsCategory);
            price = itemView.findViewById(R.id.ListitemOrderDetailsPrice);
            quantity = itemView.findViewById(R.id.ListitemOrderDetailsQuantity);
        }
    }
}