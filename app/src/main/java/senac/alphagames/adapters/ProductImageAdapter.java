package senac.alphagames.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import senac.alphagames.R;
import senac.alphagames.helper.GlideTrustManager;
import senac.alphagames.helper.SharedUtils;
import senac.alphagames.model.ProductImage;

public class ProductImageAdapter extends RecyclerView.Adapter<ProductImageAdapter.ViewHolder> {
    Context context;
    List<ProductImage> list;

    public ProductImageAdapter(Context context, List<ProductImage> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ProductImageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ProductImageAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.nav_product_image, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ProductImageAdapter.ViewHolder holder, int position) {
        GlideTrustManager.allowAllSSL();

        if (list.get(position) != null && list.get(position).getIMAGEM_URL() != null && list.get(position).getIMAGEM_URL().contains("https")) {
            Glide.with(context).load(list.get(position).getIMAGEM_URL()).into(holder.image);
        } else {
            SharedUtils.setInvalidImageToImageView(holder.image);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.ListitemProductImage);
        }
    }
}
