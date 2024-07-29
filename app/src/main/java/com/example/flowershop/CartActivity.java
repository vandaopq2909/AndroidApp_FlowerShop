package com.example.flowershop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.flowershop.Adapter.CartAdapter;
import com.example.flowershop.Adapter.ProductAdapter;
import com.example.flowershop.Class.Product;
import com.example.flowershop.Interface.IClickItemProductListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity {
    ImageView imvBackCartToMain;
    Button btnCheckOut;
    RecyclerView rcvCart;
    CartAdapter cartAdapter;
    TextView tvSubTotalPrice, tvTotalPrice;
    private DatabaseReference myref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        initUI();

        //initRecycleView
        rcvCart = findViewById(R.id.rcv_cart);

        cartAdapter = new CartAdapter(this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);

        rcvCart.setLayoutManager(linearLayoutManager);

        cartAdapter.setData(getListProduct(), new IClickItemProductListener() {
            @Override
            public void onClickItemProduct(Product product) {

            }

            @Override
            public void onClickAddToCart(Product product) {

            }
        });

        rcvCart.setAdapter(cartAdapter);
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
                    if(product != null && product.isAddToCart()) {
                        list.add(product);
                        Log.e("CartActivity", product.getName());
                    }
                }
                cartAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle possible errors.
                Toast.makeText(CartActivity.this, "Lỗi khi lấy dữ liệu Product!", Toast.LENGTH_SHORT).show();
            }
        });
        return list;
    }

    private void initUI() {
        imvBackCartToMain = findViewById(R.id.imv_backCartToMain);
        imvBackCartToMain.setOnClickListener(v -> {
            finish();
        });

        btnCheckOut = findViewById(R.id.btn_checkOut);
        btnCheckOut.setOnClickListener(v -> {
            // Tạo AlertDialog.Builder
            AlertDialog.Builder builder = new AlertDialog.Builder(CartActivity.this);
            builder.setTitle("Xác nhận đặt hàng");
            builder.setMessage("Bạn có chắc chắn muốn đặt hàng");

            // Nút "Có"
            builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            onClickCheckOut();
                        }
                    }, 2000);
                    Toast.makeText(CartActivity.this, "Đặt hàng thành công!", Toast.LENGTH_SHORT).show();
                }
            });

            // Nút "Không"
            builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            // Tạo và hiển thị dialog
            AlertDialog alertDialog = builder.create();
            alertDialog.show();

        });


        tvSubTotalPrice = findViewById(R.id.tv_subTotalPrice);
        subTotalPriceCal();
        tvTotalPrice = findViewById(R.id.tv_totalPrice);
        TotalPriceCal();
    }

    private void TotalPriceCal() {
        // Tạo instance của lớp DecimalFormat với định dạng tiền tệ Việt Nam
        DecimalFormat f = new DecimalFormat("###,###,###đ");
        myref = FirebaseDatabase.getInstance().getReference("Product");

        myref.addValueEventListener(new ValueEventListener() {
            double price = 0;
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Product product = snapshot.getValue(Product.class);
                    if(product != null && product.isAddToCart()) {
                        // Cập nhật giá trị của trường isAddToCart
                        double t = product.getPrice();
                        price += t;
                    }
                }
                tvTotalPrice.setText(f.format(price));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle possible errors.
                Toast.makeText(CartActivity.this, "Lỗi khi lấy dữ liệu Product!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void subTotalPriceCal() {
        // Tạo instance của lớp DecimalFormat với định dạng tiền tệ Việt Nam
        DecimalFormat f = new DecimalFormat("###,###,###đ");
        myref = FirebaseDatabase.getInstance().getReference("Product");

        myref.addValueEventListener(new ValueEventListener() {
            double price = 0;
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Product product = snapshot.getValue(Product.class);
                    if(product != null && product.isAddToCart()) {
                        // Cập nhật giá trị của trường isAddToCart
                        double t = product.getPrice();
                        price += t;
                    }
                }
                tvSubTotalPrice.setText(f.format(price));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle possible errors.
                Toast.makeText(CartActivity.this, "Lỗi khi lấy dữ liệu Product!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void onClickCheckOut() {
        myref = FirebaseDatabase.getInstance().getReference("Product");

        myref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Product product = snapshot.getValue(Product.class);
                    if(product != null && product.isAddToCart()) {
                        // Cập nhật giá trị của trường isAddToCart
                        product.setAddToCart(false);
                        myref.child(String.valueOf(product.getId())).updateChildren(product.toMap());
                        Log.e("CartActivity", product.getName());
                    }
                }
                cartAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle possible errors.
                Toast.makeText(CartActivity.this, "Lỗi khi lấy dữ liệu Product!", Toast.LENGTH_SHORT).show();
            }
        });


    }
}