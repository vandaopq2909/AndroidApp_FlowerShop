package com.example.flowershop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.flowershop.Adapter.ProductAdapter;
import com.example.flowershop.Class.Product;
import com.example.flowershop.Interface.IClickItemProductListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AllProductActivity extends AppCompatActivity {
    private RecyclerView rcvAllProduct;
    private ImageView imvBackAllProductToMain;
    private ProductAdapter productAdapter;
    private DatabaseReference myref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_product);

        imvBackAllProductToMain = findViewById(R.id.imv_backAllProductToMain);
        imvBackAllProductToMain.setOnClickListener(v -> {
            finish();
        });

        //init Recycle
        rcvAllProduct = findViewById(R.id.rcv_allProduct);
        productAdapter = new ProductAdapter(this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);

        rcvAllProduct.setLayoutManager(gridLayoutManager);
        productAdapter.setData(getListProduct(), new IClickItemProductListener() {
            @Override
            public void onClickItemProduct(Product product) {
                Intent intent = new Intent(AllProductActivity.this, DetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("object_product", product);
                intent.putExtras(bundle);
                startActivity(intent);
            }

            @Override
            public void onClickAddToCart(Product product) {
                myref = FirebaseDatabase.getInstance().getReference("Product");
                myref.child(String.valueOf(product.getId())).child("addToCart").setValue(true);

                Toast.makeText(AllProductActivity.this,"Đã thêm sản phẩm "+product.getName()+" vào giỏ hàng!", Toast.LENGTH_SHORT).show();
            }
        });
        rcvAllProduct.setAdapter(productAdapter);
    }

    private List<Product> getListProduct() {
        List<Product> list = new ArrayList<>();
        myref = FirebaseDatabase.getInstance().getReference("Product");

        myref.addValueEventListener(new ValueEventListener() {
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
                productAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle possible errors.
                Toast.makeText(AllProductActivity.this, "Lỗi khi lấy dữ liệu Product!", Toast.LENGTH_SHORT).show();
            }
        });
        return list;
    }
}