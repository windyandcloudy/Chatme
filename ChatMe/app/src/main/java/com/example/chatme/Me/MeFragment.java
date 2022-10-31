package com.example.chatme.Me;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.chatme.ApiController;
import com.example.chatme.Home.RealPathUtil;
import com.example.chatme.LoginActivity;
import com.example.chatme.Models.Account;
import com.example.chatme.R;
import com.example.chatme.databinding.FragmentMeBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int MY_REQUEST_CODE = 10;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MeFragment newInstance(String param1, String param2) {
        MeFragment fragment = new MeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    FragmentMeBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding= DataBindingUtil.inflate(inflater, R.layout.fragment_me, container, false);
        Glide.with(getContext()).load(LoginActivity.currentAccount.getAvt()).centerCrop().into(binding.imgAvtMe);

        binding.imgCameraMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickRequestPermission();
            }
        });

        binding.tvUpdateInfor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogUpdateInfor();
            }
        });

        binding.tvUpdatePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogUpdatePassword();
            }
        });

        binding.tvLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        });

        binding.tvUsernameMe.setText(LoginActivity.currentAccount.getUsername());
        binding.tvFullnameMe.setText(LoginActivity.currentAccount.getFullname());
        binding.tvPhoneMe.setText(LoginActivity.currentAccount.getPhone());

        return binding.getRoot();
    }

    private void showDialogUpdatePassword() {
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_update_password);

        Window window = dialog.getWindow();
        if (window == null) {
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAtributes = window.getAttributes();
        windowAtributes.gravity = Gravity.CENTER;
        window.setAttributes(windowAtributes);

        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);

        EditText edtOldPass= dialog.findViewById(R.id.etOldPass);
        EditText edtNewPass= dialog.findViewById(R.id.etNewPass);
        EditText edtReNewPass= dialog.findViewById(R.id.etReNewPass);
        Button btnUpdatePass= dialog.findViewById(R.id.btnUpdatePass);
        Button btnCancle= dialog.findViewById(R.id.btnCancle);
        ImageView imgBack= dialog.findViewById(R.id.imgBack);

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        btnCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        btnUpdatePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String oldPass= edtOldPass.getText().toString().trim();
                String newPass= edtNewPass.getText().toString().trim();
                String reNewPass= edtReNewPass.getText().toString().trim();

                if (!oldPass.equals(LoginActivity.currentAccount.getPassword())){
                    Toast.makeText(getActivity(), "Mật khẩu cũng không đúng", Toast.LENGTH_SHORT).show();
                }else if (!newPass.equals(reNewPass)){
                    Toast.makeText(getActivity(), "Hãy nhập xác nhận mật khẩu trùng khớp", Toast.LENGTH_SHORT).show();
                }else {
                    ApiController.apiService.updateAccountPassword(LoginActivity.currentAccount.get_id(), newPass).enqueue(new Callback<Account>() {
                        @Override
                        public void onResponse(Call<Account> call, Response<Account> response) {
                            if (response.isSuccessful()){
                                LoginActivity.currentAccount.setPassword(response.body().getPassword());
                                Toast.makeText(getActivity(), "Đổi mật khẩu thành công", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }else {
                                Toast.makeText(getActivity(), "Đã xảy ra lỗi. Hãy thử lại!", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Account> call, Throwable t) {
                            Toast.makeText(getActivity(), "Lỗi đường truyền", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        dialog.show();
    }

    private void showDialogUpdateInfor() {
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_update_infor);

        Window window = dialog.getWindow();
        if (window == null) {
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAtributes = window.getAttributes();
        windowAtributes.gravity = Gravity.CENTER;
        window.setAttributes(windowAtributes);

        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);

        EditText edtFullname= dialog.findViewById(R.id.edtFullnameU);
        EditText edtPhone= dialog.findViewById(R.id.edtPhoneU);
        Button btnUpdateInfor= dialog.findViewById(R.id.btnUpdateInfor);
        Button btnCancle= dialog.findViewById(R.id.btnCancle);
        ImageView imgBack= dialog.findViewById(R.id.imgBack);

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        edtFullname.setText(LoginActivity.currentAccount.getFullname());
        edtPhone.setText(LoginActivity.currentAccount.getPhone());

        btnCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        btnUpdateInfor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fullname= edtFullname.getText().toString().trim();
                String phone= edtPhone.getText().toString().trim();
                if (fullname==null || fullname.isEmpty() || phone==null || phone.isEmpty()){
                    Toast.makeText(getActivity(), "Thông tin không được để trống", Toast.LENGTH_SHORT).show();
                }else {
                    ApiController.apiService.updateAccountInfor(LoginActivity.currentAccount.get_id(), fullname, phone).enqueue(new Callback<Account>() {
                        @Override
                        public void onResponse(Call<Account> call, Response<Account> response) {
                            if (response.isSuccessful()){
                                LoginActivity.currentAccount.setFullname(response.body().getFullname());
                                LoginActivity.currentAccount.setPhone(response.body().getPhone());
                                binding.tvFullnameMe.setText(response.body().getFullname());
                                binding.tvPhoneMe.setText(response.body().getPhone());
                                dialog.dismiss();
                            }else {
                                Toast.makeText(getContext(), "Lỗi, hãy thử lại sau", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        }

                        @Override
                        public void onFailure(Call<Account> call, Throwable t) {
                            Toast.makeText(getActivity(), "Lỗi đường truyền", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    });
                }
            }
        });



        dialog.show();
    }


    public void onClickRequestPermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
            openGallary();
            return;
        }
        if (getActivity().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){
            openGallary();
//            startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
        }else {
            String [] requestPermission= {Manifest.permission.READ_EXTERNAL_STORAGE};
            getActivity().requestPermissions(requestPermission, MY_REQUEST_CODE);
        }

    }
    private ActivityResultLauncher<Intent> mActivityResultLauncher= registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode()== RESULT_OK){
                        Intent data= result.getData();
                        if (data==null){
                            return;
                        }
                        Uri uri = data.getData();

                        final Dialog dialog = new Dialog(getContext());
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setContentView(R.layout.layout_choose_img);

                        Window window = dialog.getWindow();
                        if (window == null) {
                            return;
                        }
                        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                        WindowManager.LayoutParams windowAtributes = window.getAttributes();
                        windowAtributes.gravity = Gravity.CENTER;
                        window.setAttributes(windowAtributes);

                        dialog.setCancelable(true);
                        dialog.setCanceledOnTouchOutside(true);

                        ImageView imgAvtUpdate= dialog.findViewById(R.id.imgAvtUpdate);
                        Button btnPost= dialog.findViewById(R.id.btnPost);
                        LinearLayout progress_Postsing = dialog.findViewById(R.id.progress_postsing);
                        TextView tv_loadding = dialog.findViewById(R.id.tv_Loading);

                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), uri);
                            imgAvtUpdate.setImageBitmap(bitmap);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        btnPost.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                progress_Postsing.setVisibility(View.VISIBLE);
                                Animation loadingAnim = new AlphaAnimation(1, 0);
                                loadingAnim.setDuration(500);
                                loadingAnim.setRepeatCount(Animation.INFINITE);
                                loadingAnim.setRepeatMode(Animation.REVERSE);
                                tv_loadding.startAnimation(loadingAnim);

                                String realPath= RealPathUtil.getRealPath(getContext(), uri);
                                File file = new File(realPath);

                                RequestBody requestBodyAvt = RequestBody.create(MediaType.parse("multipart/form-data"), file);

                                Log.d("avt", "onClick: "+ realPath);
                                Log.d("avt", "onClick: "+ file.getName());
                                Log.d("avt", "id : "+ LoginActivity.currentAccount.get_id());

                                MultipartBody.Part multipartBodyAvt = MultipartBody.Part.createFormData("avt", file.getName(), requestBodyAvt);
                                ApiController.apiService.updateAccountAvt(LoginActivity.currentAccount.get_id(), multipartBodyAvt).enqueue(new Callback<Account>() {
                                    @Override
                                    public void onResponse(Call<Account> call, Response<Account> response) {
                                        if (response.isSuccessful()){
                                            LoginActivity.currentAccount.setAvt(response.body().getAvt());
                                            Glide.with(getActivity()).load(response.body().getAvt()).centerCrop().into(binding.imgAvtMe);
                                            dialog.dismiss();
                                        }else {
                                            Log.d("avt", "onResponse: "+ response.errorBody().toString());
                                            String err="";
                                            try {
                                                JSONObject jsonObject= new JSONObject(response.errorBody().string());
                                                err= jsonObject.getString("message");
                                            } catch (JSONException | IOException e) {
                                                e.printStackTrace();
                                            }

                                            Toast.makeText(getActivity(), "Lỗi: "+ err, Toast.LENGTH_SHORT).show();
                                            dialog.dismiss();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<Account> call, Throwable t) {
                                        Toast.makeText(getActivity(), "Lỗi đường truyền, Hãy thử lại 1 lần nữa", Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();
                                    }
                                });
                            }
                        });

                        dialog.show();
                    }
                }
            }
    );
    private void openGallary() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        mActivityResultLauncher.launch(Intent.createChooser(intent, "Select Picture"));
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==MY_REQUEST_CODE){
            if (grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED){
                openGallary();
            }
        }

    }
}