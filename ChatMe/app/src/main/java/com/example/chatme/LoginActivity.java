package com.example.chatme;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chatme.Models.Account;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    EditText edtUsername, edtPassword, edtRePassword, edtFullname;
    CheckBox btnSavePassword;
    TextView tvError, tvRegister;
    Button btnLogin;

    boolean isLogin=true;

    static  final  String SHARE_PRE_NAME="User";
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public static Account currentAccount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        anhXa();

        ActionBar actionBar= getSupportActionBar();
        actionBar.hide();

        sharedPreferences = getSharedPreferences(SHARE_PRE_NAME,MODE_PRIVATE);
        String username =sharedPreferences.getString("username","");
        String password = sharedPreferences.getString("password","");
        edtUsername.setText(username);
        edtPassword.setText(password);

        btnSavePassword.setChecked(true);

        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isLogin){
                    edtRePassword.setVisibility(View.VISIBLE);
                    edtFullname.setVisibility(View.VISIBLE);
                    btnLogin.setText("Đăng ký");
                    tvRegister.setText("Hủy");
                    isLogin= false;
                }else {
                    edtRePassword.setVisibility(View.GONE);
                    edtFullname.setVisibility(View.GONE);
                    btnLogin.setText("Đăng nhập");
                    tvRegister.setText("Bạn chưa có tài khoản? Đăng ký ngay");
                    isLogin= true;
                }
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isLogin){
                    login();
                }else {
                    register();
                }
            }
        });


    }

    private void register() {
        String username=  edtUsername.getText().toString().trim();
        String password=  edtPassword.getText().toString().trim();
        String rePassword= edtRePassword.getText().toString().trim();
        String fullname= edtFullname.getText().toString().trim();

        if (!password.equals(rePassword)){
            Toast.makeText(this, "Mật khẩu không trùng khớp", Toast.LENGTH_SHORT).show();
        }else
        ApiController.apiService.register(username, password, fullname).enqueue(new Callback<Account>() {
            @Override
            public void onResponse(Call<Account> call, Response<Account> response) {
                if (response.isSuccessful()){
                    currentAccount= response.body();
                    if (btnSavePassword.isChecked()){
                        savePassword(username, password);
                    }
                    Intent intent= new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                }else {
                    String err="";
                    try {
                        JSONObject jsonObject= new JSONObject(response.errorBody().string());
                        err= jsonObject.getString("message");
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }

                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                    builder.setTitle("LỖI");
                    builder.setMessage(err);
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    builder.show();
                }
            }

            @Override
            public void onFailure(Call<Account> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Lỗi đường truyền", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void login() {
        String username=  edtUsername.getText().toString().trim();
        String password=  edtPassword.getText().toString().trim();

        ApiController.apiService.login(username, password).enqueue(new Callback<Account>() {
            @Override
            public void onResponse(Call<Account> call, Response<Account> response) {
                if (response.isSuccessful()){
                    currentAccount= response.body();
                    if (btnSavePassword.isChecked()){
                        savePassword(username, password);
                    }
                    Intent intent= new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                }else {
                    String err="";
                    try {
                        JSONObject jsonObject= new JSONObject(response.errorBody().string());
                        err= jsonObject.getString("message");
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }

                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                    builder.setTitle("LỖI");
                    builder.setMessage(err);
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    builder.show();
                }
            }

            @Override
            public void onFailure(Call<Account> call, Throwable t) {
                System.out.println(t);
                Toast.makeText(LoginActivity.this, "Lỗi đường truyền", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void anhXa() {
        edtUsername= findViewById(R.id.edtUsername);
        edtPassword= findViewById(R.id.edtPassword);
        edtRePassword= findViewById(R.id.edtRePassword);
        edtFullname= findViewById(R.id.edtFullName);
        btnSavePassword= findViewById(R.id.btnSavepassword);
        tvError= findViewById(R.id.tvError);
        tvRegister= findViewById(R.id.tvRegister);
        btnLogin= findViewById(R.id.btnLogin);
    }

    private void savePassword(String username, String password) {
        sharedPreferences = getSharedPreferences(SHARE_PRE_NAME,MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putString("username", username);
        editor.putString("password", password);
        editor.commit();
    }


}