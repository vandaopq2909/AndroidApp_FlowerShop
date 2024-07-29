package com.example.flowershop;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {
    private TextView tvDangNhap;
    private EditText edtEmail, edtPassword;
    private Button btnDangKy;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        innitUI();
        initListener();
    }
    private void innitUI() {
        tvDangNhap = findViewById(R.id.tvDangNhap);
        edtEmail = findViewById(R.id.edt_email);
        edtPassword = findViewById(R.id.edt_password);
        btnDangKy = findViewById(R.id.btnDangKy);

        progressDialog = new ProgressDialog(this);

    }
    private void initListener() {
        tvDangNhap.setOnClickListener(v -> {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        });

        btnDangKy.setOnClickListener(v -> {
            onClickListener();
        });
    }
    private void onClickListener() {
        String email = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();

        // Kiểm tra email không được để trống
        if (email.length() == 0) {
            edtEmail.setError("Email không được để trống");
            edtEmail.requestFocus();
        }
        // Kiểm tra định dạng email
        else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edtEmail.setError("Email không hợp lệ");
            edtEmail.requestFocus();
        }
        // Kiểm tra mật khẩu không được để trống
        else if (password.length() == 0) {
            edtPassword.setError("Mật khẩu không được để trống");
            edtPassword.requestFocus();
        }
        // Kiểm tra mật khẩu phải từ 6 kí tự
        else if(password.length() < 6) {
            edtPassword.setError("Mật khẩu phải từ 6 kí tự!");
            edtPassword.requestFocus();
        }
        // Xử lý khi email và mật khẩu hợp lệ => đăng ký
        else {
            FirebaseAuth mAuth = FirebaseAuth.getInstance();

            progressDialog.show();
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            progressDialog.dismiss();
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finishAffinity();
                            } else {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }

    }
}