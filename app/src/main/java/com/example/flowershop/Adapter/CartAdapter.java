package com.example.flowershop.Adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.flowershop.Class.Product;
import com.example.flowershop.Interface.IClickItemProductListener;
import com.example.flowershop.R;

import java.text.DecimalFormat;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder>{
    private Context mContext;
    private List<Product> mProducts;
    private IClickItemProductListener iClickItemProductListener;

    public CartAdapter(Context mContext) {
        this.mContext = mContext;
    }
    public void setData(List<Product> list, IClickItemProductListener mListener) {
        this.mProducts = list;
        this.iClickItemProductListener = mListener;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart_product, parent, false);
        return new CartAdapter.CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        Product product = mProducts.get(position);
        if(product == null) {
            return;
        }
        holder.tvNameItemCartProduct.setText(product.getName());
        // Tạo instance của lớp DecimalFormat với định dạng tiền tệ Việt Nam
        DecimalFormat f = new DecimalFormat("###,###,###đ");
        holder.tvPriceItemCartProduct.setText(f.format(product.getPrice()));
        holder.tvPriceTotalItemCartProduct.setText(f.format(product.getPrice()));
        Uri uri = Uri.parse(product.getImagePath());
        Glide.with(holder.imvImageItemCartProduct.getContext()).load(uri).into(holder.imvImageItemCartProduct);

//        holder.layoutItemProduct.setOnClickListener(v -> {
//            iClickItemProductListener.onClickItemProduct(product);
//        });
//
//        holder.tvAddCart.setOnClickListener(v -> {
//            iClickItemProductListener.onClickAddToCart(product);
//        });
    }

    @Override
    public int getItemCount() {
        if(mProducts != null) {
            return mProducts.size();
        }
        return 0;
    }

    public class CartViewHolder extends RecyclerView.ViewHolder {
        ImageView imvImageItemCartProduct;
        TextView tvNameItemCartProduct, tvPriceItemCartProduct, tvPriceTotalItemCartProduct;
        public CartViewHolder(@NonNull View itemView) {
            super(itemView);

            imvImageItemCartProduct = itemView.findViewById(R.id.imv_imageItemCartProduct);
            tvNameItemCartProduct = itemView.findViewById(R.id.tv_nameItemCartProduct);
            tvPriceItemCartProduct = itemView.findViewById(R.id.tv_priceItemCartProduct);
            tvPriceTotalItemCartProduct = itemView.findViewById(R.id.tv_priceTotalItemCartProduct);

        }
    }
}
