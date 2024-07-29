package com.example.flowershop;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.flowershop.Adapter.CategoryAdater;
import com.example.flowershop.Adapter.ProductAdapter;
import com.example.flowershop.Class.Category;
import com.example.flowershop.Class.Product;
import com.example.flowershop.Interface.IClickItemCategoryListener;
import com.example.flowershop.Interface.IClickItemProductListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import org.checkerframework.checker.units.qual.C;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView rcvBestProduct, rcvNewProduct, rcvChooseCategory;
    private DatabaseReference myref;
    private ProductAdapter bestProductAdapter, newProductAdapter;
    private CategoryAdater chooseCategoryAdapter;
    private ImageView imvLogout, imvSearch, imvCart;
    private TextView tvAllProduct1, tvAllProduct2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUI();

        //initRecycleView
        rcvBestProduct = findViewById(R.id.rcv_bestProduct);
        rcvNewProduct = findViewById(R.id.rcv_newProduct);
        rcvChooseCategory = findViewById(R.id.rcv_chooseCategory);

        bestProductAdapter = new ProductAdapter(this);
        newProductAdapter = new ProductAdapter(this);
        chooseCategoryAdapter = new CategoryAdater(this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 4);

        rcvBestProduct.setLayoutManager(linearLayoutManager);
        rcvNewProduct.setLayoutManager(linearLayoutManager2);
        rcvChooseCategory.setLayoutManager(gridLayoutManager);

        bestProductAdapter.setData(getListBestProduct(), new IClickItemProductListener() {
            @Override
            public void onClickItemProduct(Product product) {
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("object_product", product);
                intent.putExtras(bundle);
                startActivity(intent);
            }

            @Override
            public void onClickAddToCart(Product product) {
                myref = FirebaseDatabase.getInstance().getReference("Product");
                myref.child(String.valueOf(product.getId())).child("addToCart").setValue(true);

                Toast.makeText(MainActivity.this,"Đã thêm sản phẩm "+product.getName()+" vào giỏ hàng!", Toast.LENGTH_SHORT).show();
            }
        });
        newProductAdapter.setData(getListNewProduct(), new IClickItemProductListener() {
            @Override
            public void onClickItemProduct(Product product) {
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("object_product", product);
                intent.putExtras(bundle);
                startActivity(intent);
            }

            @Override
            public void onClickAddToCart(Product product) {
                myref = FirebaseDatabase.getInstance().getReference("Product");
                myref.child(String.valueOf(product.getId())).child("addToCart").setValue(true);

                Toast.makeText(MainActivity.this,"Đã thêm sản phẩm "+product.getName()+" vào giỏ hàng!", Toast.LENGTH_SHORT).show();
            }
        });
        chooseCategoryAdapter.setData(getListCategory(), new IClickItemCategoryListener() {
            @Override
            public void onClickItemCategory(Category category) {
                Intent intent = new Intent(MainActivity.this, ProductByCategoryActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("category_id", category.getId());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        rcvBestProduct.setAdapter(bestProductAdapter);
        rcvNewProduct.setAdapter(newProductAdapter);
        rcvChooseCategory.setAdapter(chooseCategoryAdapter);
    }

    private List<Category> getListCategory() {
        List<Category> list = new ArrayList<>();
        myref = FirebaseDatabase.getInstance().getReference("Category");

        myref.orderByKey().limitToLast(8).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Category category = snapshot.getValue(Category.class);
                    if(category != null) {
                        list.add(category);
                        Log.e("MainActivity", category.getName());
                    }
                }
                chooseCategoryAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle possible errors.
                Toast.makeText(MainActivity.this, "Lỗi khi lấy dữ liệu Product!", Toast.LENGTH_SHORT).show();
            }
        });
        return list;
    }

    private List<Product> getListNewProduct() {
        List<Product> list = new ArrayList<>();
        myref = FirebaseDatabase.getInstance().getReference("Product");

        myref.orderByKey().limitToLast(4).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Product product = snapshot.getValue(Product.class);
                    if(product != null) {
                        list.add(product);
                        Log.e("MainActivity", product.getName());
                    }
                }
                // Reverse the order to get the last 4 products in the correct sequence
                Collections.reverse(list);
                newProductAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle possible errors.
                Toast.makeText(MainActivity.this, "Lỗi khi lấy dữ liệu Product!", Toast.LENGTH_SHORT).show();
            }
        });
        return list;
    }
    private List<Product> getListBestProduct() {
        List<Product> list = new ArrayList<>();
        myref = FirebaseDatabase.getInstance().getReference("Product");

        myref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Product product = snapshot.getValue(Product.class);
                    if(product != null && product.isBestProduct() && list.size() < 4) {
                        list.add(product);
                        Log.e("MainActivity", product.getName());
                    }
                }
                bestProductAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle possible errors.
                Toast.makeText(MainActivity.this, "Lỗi khi lấy dữ liệu Product!", Toast.LENGTH_SHORT).show();
            }
        });
        return list;
    }
    private void initUI() {
        imvLogout = findViewById(R.id.imv_Logout);
        imvLogout.setOnClickListener(v -> {
            onClickLogout();
        });

        imvSearch = findViewById(R.id.imv_search);
        imvSearch.setOnClickListener(v -> {
            onClickSearch();
        });

        imvCart = findViewById(R.id.imv_cart);
        imvCart.setOnClickListener(v -> {
            onClickCart();
        });

        tvAllProduct1 = findViewById(R.id.tv_allProduct1);
        tvAllProduct1.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AllProductActivity.class);
            startActivity(intent);
        });
        tvAllProduct2 = findViewById(R.id.tv_allProduct2);
        tvAllProduct2.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AllProductActivity.class);
            startActivity(intent);
        });
    }

    private void onClickCart() {
        Intent intent = new Intent(MainActivity.this, CartActivity.class);
        startActivity(intent);
    }

    private void onClickSearch() {
        Intent intent = new Intent(MainActivity.this, SearchActivity.class);
        startActivity(intent);
    }
    private void onClickLogout() {
        FirebaseAuth.getInstance().signOut();

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finishAffinity();
    }
}