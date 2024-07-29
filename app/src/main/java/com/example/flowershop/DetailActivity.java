package com.example.flowershop;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.flowershop.Class.Product;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DecimalFormat;

public class DetailActivity extends AppCompatActivity {
    TextView tvNameDetailProduct, tvPriceDetailProduct, tvDescDetailProduct;
    ImageView imvImgDetailProduct, imvBackDetailToMain;
    Button btnAddCart;
    private DatabaseReference myref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Bundle bundle = getIntent().getExtras();
        if(bundle == null) {
            return;
        }

        Product pro = (Product) bundle.get("object_product");
        tvNameDetailProduct = findViewById(R.id.tv_nameDetailProduct);
        tvPriceDetailProduct = findViewById(R.id.tv_priceDetailProduct);
        tvDescDetailProduct = findViewById(R.id.tv_descDetailProduct);
        imvImgDetailProduct = findViewById(R.id.imv_ImgDetailProduct);
        imvBackDetailToMain = findViewById(R.id.imv_backDetailToMain);
        btnAddCart = findViewById(R.id.btn_addCart);

        tvNameDetailProduct.setText(pro.getName());
        // Tạo instance của lớp DecimalFormat với định dạng tiền tệ Việt Nam
        DecimalFormat f = new DecimalFormat("###,###,###đ");
        tvPriceDetailProduct.setText(f.format(pro.getPrice()));
        tvDescDetailProduct.setText(pro.getDescription());

        imvBackDetailToMain.setOnClickListener(v -> {
            finish();
        });

        Uri uri = Uri.parse(pro.getImagePath());
        Glide.with(DetailActivity.this).load(uri).into(imvImgDetailProduct);

        btnAddCart.setOnClickListener(v -> {
            myref = FirebaseDatabase.getInstance().getReference("Product");
            myref.child(String.valueOf(pro.getId())).child("addToCart").setValue(true);
            Toast.makeText(DetailActivity.this,"Đã thêm sản phẩm "+pro.getName()+" vào giỏ hàng!", Toast.LENGTH_SHORT).show();
        });
    }
}