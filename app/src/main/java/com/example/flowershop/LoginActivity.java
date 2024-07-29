package com.example.flowershop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    private TextView tvDangKy;
    private EditText edtEmail, edtPassword;
    private Button btnDangNhap;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        innitUI();
        initListener();
    }
    private void innitUI() {
        tvDangKy = findViewById(R.id.tvDangKy);
        edtEmail = findViewById(R.id.edt_email);
        edtPassword = findViewById(R.id.edt_password);
        btnDangNhap = findViewById(R.id.btnDangNhap);

        progressDialog = new ProgressDialog(this);

    }
    private void initListener() {
        tvDangKy.setOnClickListener(v -> {
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
        });

        btnDangNhap.setOnClickListener(v -> {
            onClickLogin();
        });
    }

    private void onClickLogin() {
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
        // Xử lý khi email và mật khẩu hợp lệ
        else {
            FirebaseAuth mAuth = FirebaseAuth.getInstance();

            progressDialog.show();
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            progressDialog.dismiss();
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                                finishAffinity();
                            } else {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(LoginActivity.this, "Đăng nhập thất bại! tài khoản hoặc mật khẩu không chính xác!.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }

    }
}