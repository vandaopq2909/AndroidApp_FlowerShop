package com.example.flowershop.Adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.flowershop.Class.Category;
import com.example.flowershop.Class.Product;
import com.example.flowershop.Interface.IClickItemCategoryListener;
import com.example.flowershop.Interface.IClickItemProductListener;
import com.example.flowershop.R;

import java.text.DecimalFormat;
import java.util.List;

public class CategoryAdater extends RecyclerView.Adapter<CategoryAdater.CategoryViewHolder> {
    private Context mContext;
    private List<Category> categories;
    private IClickItemCategoryListener iClickItemCategoryListener;

    public CategoryAdater(Context mContext) {
        this.mContext = mContext;
    }
    public void setData(List<Category> list, IClickItemCategoryListener mListener) {
        this.categories = list;
        this.iClickItemCategoryListener = mListener;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false);
        return new CategoryAdater.CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category category = categories.get(position);
        if(category == null) {
            return;
        }
        holder.tvNameCategory.setText(category.getName());
        Uri uri = Uri.parse(category.getImagePath());
        Glide.with(holder.imvImageCategory.getContext()).load(uri).into(holder.imvImageCategory);

        holder.layoutCategory.setOnClickListener(v -> {
            iClickItemCategoryListener.onClickItemCategory(category);
        });
    }

    @Override
    public int getItemCount() {
        if(categories != null) {
            return categories.size();
        }
        return 0;
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder {
        ImageView imvImageCategory;
        TextView tvNameCategory;
        CardView layoutCategory;
        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);

            imvImageCategory = itemView.findViewById(R.id.imv_imageCategory);
            tvNameCategory = itemView.findViewById(R.id.tv_nameCategory);
            layoutCategory = itemView.findViewById(R.id.layout_item_category);
        }
    }
}
