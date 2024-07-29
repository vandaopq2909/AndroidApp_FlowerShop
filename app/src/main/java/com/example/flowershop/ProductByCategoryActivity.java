package com.example.flowershop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.flowershop.Adapter.CategoryAdater;
import com.example.flowershop.Adapter.ProductAdapter;
import com.example.flowershop.Class.Category;
import com.example.flowershop.Class.Product;
import com.example.flowershop.Interface.IClickItemProductListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ProductByCategoryActivity extends AppCompatActivity {
    private RecyclerView rcvProductByCategory;
    private ImageView imvBackProductByCategoryToMain;
    private ProductAdapter productAdapter;
    private DatabaseReference myref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_by_category);

        Bundle bundle = getIntent().getExtras();
        if(bundle == null) {
            return;
        }

        int category_id = bundle.getInt("category_id");

        imvBackProductByCategoryToMain = findViewById(R.id.imv_backProductByCategoryToMain);
        imvBackProductByCategoryToMain.setOnClickListener(v -> {
            finish();
        });

        //init Recycle
        rcvProductByCategory = findViewById(R.id.rcv_productByCategory);
        productAdapter = new ProductAdapter(this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);

        rcvProductByCategory.setLayoutManager(gridLayoutManager);
        productAdapter.setData(getListProduct(category_id), new IClickItemProductListener() {
            @Override
            public void onClickItemProduct(Product product) {
                Intent intent = new Intent(ProductByCategoryActivity.this, DetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("object_product", product);
                intent.putExtras(bundle);
                startActivity(intent);
            }

            @Override
            public void onClickAddToCart(Product product) {
                myref = FirebaseDatabase.getInstance().getReference("Product");
                myref.child(String.valueOf(product.getId())).child("addToCart").setValue(true);

                Toast.makeText(ProductByCategoryActivity.this,"Đã thêm sản phẩm "+product.getName()+" vào giỏ hàng!", Toast.LENGTH_SHORT).show();
            }
        });
        rcvProductByCategory.setAdapter(productAdapter);
    }

    private List<Product> getListProduct(int category_id) {
        List<Product> list = new ArrayList<>();
        myref = FirebaseDatabase.getInstance().getReference("Product");

        myref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Product product = snapshot.getValue(Product.class);
                    if(product != null && product.getCategoryId() == category_id) {
                        list.add(product);
                        Log.e("MainActivity", product.getName());
                    }
                }
                productAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle possible errors.
                Toast.makeText(ProductByCategoryActivity.this, "Lỗi khi lấy dữ liệu Product!", Toast.LENGTH_SHORT).show();
            }
        });
        return list;
    }
}