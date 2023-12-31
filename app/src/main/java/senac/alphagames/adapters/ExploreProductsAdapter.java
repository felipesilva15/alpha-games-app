package senac.alphagames.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.util.Log;
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
import senac.alphagames.model.Product;
import senac.alphagames.ui.product.ProductActivity;

public class ExploreProductsAdapter extends RecyclerView.Adapter<ExploreProductsAdapter.ViewHolder> {
    Context context;
    List<Product> list;

    public ExploreProductsAdapter(Context context, List<Product> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.nav_explore_listitem, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        GlideTrustManager.allowAllSSL();

        if (list.get(position).getImages() != null && list.get(position).getImages().size() > 0 && list.get(position).getImages().get(0).getIMAGEM_URL().contains("https")) {
            Glide.with(context).load(list.get(position).getImages().get(0).getIMAGEM_URL()).into(holder.image);
        } else {
            SharedUtils.setInvalidImageToImageView(holder.image);
        }

        holder.name.setText(list.get(position).getPRODUTO_NOME());
        holder.category.setText(list.get(position).getCategory().getCATEGORIA_NOME());
        holder.price.setText(SharedUtils.formatToCurrency(list.get(position).getPRODUTO_PRECO() - list.get(position).getPRODUTO_DESCONTO()));

        if (list.get(position).getPRODUTO_DESCONTO() != 0) {
            holder.discount.setText(SharedUtils.formatToCurrency(list.get(position).getPRODUTO_PRECO()));
            holder.discount.setPaintFlags(holder.discount.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            holder.discount.setText("");
        }

        holder.itemView.setOnClickListener(view -> {
            Log.i("ItemView", "ID do produto: " + list.get(position).getPRODUTO_ID());

            Intent intent = new Intent(context, ProductActivity.class);
            intent.putExtra("productId", list.get(position).getPRODUTO_ID());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView name, category, price, discount;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.ListitemExploreProductImage);
            name = itemView.findViewById(R.id.ListitemExploreProductName);
            category = itemView.findViewById(R.id.ListitemExploreProductCategory);
            price = itemView.findViewById(R.id.ListitemExploreProductPrice);
            discount = itemView.findViewById(R.id.ListitemExploreProductDiscount);
        }
    }
}
