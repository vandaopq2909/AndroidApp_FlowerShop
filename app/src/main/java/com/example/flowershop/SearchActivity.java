package com.example.flowershop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.flowershop.Adapter.ProductAdapter;
import com.example.flowershop.Adapter.SearchProductAdapter;
import com.example.flowershop.Class.Product;
import com.example.flowershop.Interface.IClickItemProductListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SearchActivity extends AppCompatActivity {
    SearchView svSearch;
    RecyclerView rcvSearchProduct;
    SearchProductAdapter searchProductAdapter;
    private DatabaseReference myref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        //initRecycleView
        rcvSearchProduct = findViewById(R.id.rcv_searchProduct);

        searchProductAdapter = new SearchProductAdapter(this);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);

        rcvSearchProduct.setLayoutManager(gridLayoutManager);

        searchProductAdapter.setData(getListSearchProduct(), new IClickItemProductListener() {
            @Override
            public void onClickItemProduct(Product product) {
                Intent intent = new Intent(SearchActivity.this, DetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("object_product", product);
                intent.putExtras(bundle);
                startActivity(intent);
            }

            @Override
            public void onClickAddToCart(Product product) {
                myref = FirebaseDatabase.getInstance().getReference("Product");
                myref.child(String.valueOf(product.getId())).child("addToCart").setValue(true);

                Toast.makeText(SearchActivity.this,"Đã thêm sản phẩm "+product.getName()+" vào giỏ hàng!", Toast.LENGTH_SHORT).show();
            }
        });

        rcvSearchProduct.setAdapter(searchProductAdapter);

        svSearch = findViewById(R.id.sv_search);
        svSearch.clearFocus();
        svSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String text) {
                searchProductAdapter.filter(text.trim());
                return true;
            }
        
        });
    }

    public List<Product> getListSearchProduct() {
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
                        Log.e("SearchActivity", product.getName());
                    }
                }
                searchProductAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle possible errors.
                Toast.makeText(SearchActivity.this, "Lỗi khi lấy dữ liệu Product!", Toast.LENGTH_SHORT).show();
            }
        });
        return list;
    }
}