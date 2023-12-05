package senac.alphagames.adapters;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import senac.alphagames.R;
import senac.alphagames.api.HttpServiceGenerator;
import senac.alphagames.api.service.CartClient;
import senac.alphagames.helper.ErrorUtils;
import senac.alphagames.helper.GlideTrustManager;
import senac.alphagames.helper.LoadingDialog;
import senac.alphagames.helper.SharedUtils;
import senac.alphagames.model.CartItem;
import senac.alphagames.ui.cart.CartActivity;


public class CartItemAdapter extends RecyclerView.Adapter<CartItemAdapter.ViewHolder> {
    Context context;
    List<CartItem> list;
    LoadingDialog loadingDialog;

    public CartItemAdapter(Context context, List<CartItem> list) {
        this.context = context;
        this.list = list;
        this.loadingDialog = new LoadingDialog(context);
    }

    @NonNull
    @Override
    public CartItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CartItemAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.nav_cart_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CartItemAdapter.ViewHolder holder, int position) {
        GlideTrustManager.allowAllSSL();

        if (list.get(position).getProduct().getImages() != null && list.get(position).getProduct().getImages().size() > 0 && list.get(position).getProduct().getImages().get(0).getIMAGEM_URL().contains("https")) {
            Glide.with(context).load(list.get(position).getProduct().getImages().get(0).getIMAGEM_URL()).into(holder.image);
        } else {
            SharedUtils.setInvalidImageToImageView(holder.image);
        }

        holder.name.setText(list.get(position).getProduct().getPRODUTO_NOME());
        holder.category.setText(list.get(position).getProduct().getCategory().getCATEGORIA_NOME());
        holder.price.setText(SharedUtils.formatToCurrency(list.get(position).getProduct().getPRODUTO_PRECO() - list.get(position).getProduct().getPRODUTO_DESCONTO()));
        holder.quantity.setText(String.valueOf(list.get(position).getITEM_QTD()));

        if (list.get(position).getProduct().getPRODUTO_DESCONTO() != 0) {
            holder.discount.setText(SharedUtils.formatToCurrency(list.get(position).getProduct().getPRODUTO_PRECO()));
            holder.discount.setPaintFlags(holder.discount.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            holder.discount.setText("");
        }

        holder.deleteButton.setOnClickListener(view -> deleteItemFromCart(list.get(position).getPRODUTO_ID(), position));

        holder.addButton.setOnClickListener(view -> updateCart(list.get(position).getITEM_QTD() + 1, position));

        holder.removeButton.setOnClickListener(view -> updateCart(list.get(position).getITEM_QTD() - 1, position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView name, category, price, discount, quantity;
        ImageButton deleteButton;
        FloatingActionButton addButton, removeButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.ImageViewListitemCartProductImage);
            name = itemView.findViewById(R.id.TextViewListitemCartProduct);
            category = itemView.findViewById(R.id.TextViewListitemCartCategory);
            price = itemView.findViewById(R.id.TextViewListitemCartPrice);
            discount = itemView.findViewById(R.id.TextViewListitemCartDiscount);
            quantity = itemView.findViewById(R.id.TextViewListitemCartQtyItem);
            deleteButton = itemView.findViewById(R.id.ImageButtonListitemCartDeleteItem);
            addButton = itemView.findViewById(R.id.FloatingActionButtonListitemCartQtyAdd);
            removeButton = itemView.findViewById(R.id.FloatingActionButtonListitemCartQtyRemove);
        }
    }

    private void deleteItemFromCart(int productId, int position) {
        loadingDialog.show();

        CartClient client = HttpServiceGenerator.createHttpService(context, CartClient.class);
        Call<Void> call = client.removeItemFromCart(productId);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (!response.isSuccessful()) {
                    ErrorUtils.validateUnsuccessfulResponse(context, response);
                    loadingDialog.cancel();

                    return;
                }

                list.remove(position);
                notifyItemRemoved(position);

                calculateTotal();

                loadingDialog.cancel();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                loadingDialog.cancel();
                ErrorUtils.showErrorMessage(context, context.getString(R.string.network_error_message));
            }
        });
    }

    private void updateCart(int qtyItem, int position) {
        loadingDialog.show();

        CartItem cartItem = new CartItem(list.get(position).getPRODUTO_ID(), qtyItem);

        CartClient client = HttpServiceGenerator.createHttpService(context, CartClient.class);
        Call<Void> call = client.addItemToCart(cartItem, list.get(position).getPRODUTO_ID());

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (!response.isSuccessful()) {
                    ErrorUtils.validateUnsuccessfulResponse(context, response);
                    loadingDialog.cancel();

                    return;
                }

                if (qtyItem == 0) {
                    list.remove(position);
                    notifyItemRemoved(position);
                } else {
                    list.get(position).setITEM_QTD(qtyItem);
                    notifyItemChanged(position);
                }

                calculateTotal();

                loadingDialog.cancel();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                loadingDialog.cancel();
                ErrorUtils.showErrorMessage(context, context.getString(R.string.network_error_message));
            }
        });
    }

    private void calculateTotal() {
        if (context instanceof CartActivity) {
            ((CartActivity) context).calculateTotal();
        }
    }
}
