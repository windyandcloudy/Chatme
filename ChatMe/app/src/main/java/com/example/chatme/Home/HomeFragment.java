package com.example.chatme.Home;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.example.chatme.Home.Comment.CommentAdapter;
import com.example.chatme.LoginActivity;
import com.example.chatme.Models.Comment;
import com.example.chatme.Models.Post;
import com.example.chatme.R;
import com.example.chatme.databinding.FragmentHomeBinding;

import java.io.File;
import java.io.IOException;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int SELECT_PICTURE = 1;
    private static final int MY_REQUEST_CODE = 10;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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

    FragmentHomeBinding binding;
    ImageView img_choose_img_to_Post;
    Uri mUri;
    PostAdapter postAdapter;
    List<Post> posts;
    List<Comment> comments;
    CommentAdapter commentAdapter;
    boolean isChooseImg= false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding= DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);

        binding.tvCreatePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLayoutPost();
            }
        });

        getPosts();

        Glide.with(getActivity()).load(LoginActivity.currentAccount.getAvt()).centerCrop().into(binding.imgAvt);

        return binding.getRoot();
    }

    private void getPosts() {
        ApiController.apiService.getPosts().enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                if(response.isSuccessful()){
                    RecyclerView.LayoutManager layoutManager= new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
                    posts= response.body();
                    postAdapter= new PostAdapter(getActivity(), posts);
                    binding.revPosts.setLayoutManager(layoutManager);
                    binding.revPosts.setAdapter(postAdapter);

                    postAdapter.setiOnClickPost(new IOnClickPost() {
                        @Override
                        public void iOnClickCmt(Post post, int pos) {
                            Dialog dialog= new Dialog(getActivity());
                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            dialog.setContentView(R.layout.layout_dialog_cmt);

                            Window window = dialog.getWindow();
                            if (window == null) {
                                return;
                            }
                            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
                            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                            WindowManager.LayoutParams windowAtributes = window.getAttributes();
                            windowAtributes.gravity = Gravity.CENTER;
                            window.setAttributes(windowAtributes);
                            dialog.setCancelable(true);
                            dialog.setCanceledOnTouchOutside(true);

                            RecyclerView revComment= dialog.findViewById(R.id.revComment);
                            EditText edtContentCmt= dialog.findViewById(R.id.edt_type_to_cmt);
                            ImageView img_btn_send_cmt= dialog.findViewById(R.id.img_btn_send_cmt);

                            ApiController.apiService.getComments(post.get_id()).enqueue(new Callback<List<Comment>>() {
                                @Override
                                public void onResponse(Call<List<Comment>> call, Response<List<Comment>> response) {
                                    if (response.isSuccessful()){
                                        comments= response.body();
                                        RecyclerView.LayoutManager layoutManager1= new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
                                        commentAdapter= new CommentAdapter(getActivity(), comments);
                                        revComment.setLayoutManager(layoutManager1);
                                        revComment.setAdapter(commentAdapter);
                                    }
                                }

                                @Override
                                public void onFailure(Call<List<Comment>> call, Throwable t) {
                                    Toast.makeText(getActivity(), "Lỗi đường truyền lấy comment", Toast.LENGTH_SHORT).show();
                                }
                            });

                            img_btn_send_cmt.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    String content= edtContentCmt.getText().toString().trim();
                                    if (!content.isEmpty()){
                                        ApiController.apiService.createCmt(post.get_id(), content, LoginActivity.currentAccount.get_id()).enqueue(new Callback<Comment>() {
                                            @Override
                                            public void onResponse(Call<Comment> call, Response<Comment> response) {
                                                if (response.isSuccessful()){
                                                    comments.add(response.body());
                                                    commentAdapter.setComments(comments);
                                                    edtContentCmt.setText("");
                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<Comment> call, Throwable t) {
                                                Toast.makeText(getActivity(), "Lỗi đường truyền", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                }
                            });

                            dialog.show();
                        }

                        @Override
                        public void iOnClickLike(Post post, int pos) {

                        }
                    });

                }else {
                    Toast.makeText(getActivity(), "Lỗi đường truyền", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                Toast.makeText(getActivity(), "Lỗi đường truyền", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showLayoutPost() {
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_create_posts);

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

        ImageView img_avt_createP = dialog.findViewById(R.id.img_avt_lg1);
        TextView tv_fullname_createP = dialog.findViewById(R.id.tv_Fullname_createP);
        EditText edt_content_createP = dialog.findViewById(R.id.edt_content_createP);
        LinearLayout add_Image_createP = dialog.findViewById(R.id.add_Image_CreateP);
        Button btn_createP = dialog.findViewById(R.id.btn_createP);
        LinearLayout progress_Postsing = dialog.findViewById(R.id.progress_postsing);
        TextView tv_loadding = dialog.findViewById(R.id.tv_Loading);

        Glide.with(getActivity()).load(LoginActivity.currentAccount.getAvt()).centerCrop().into(img_avt_createP);
        tv_fullname_createP.setText(LoginActivity.currentAccount.getFullname());

        img_choose_img_to_Post = dialog.findViewById(R.id.img_choose_img_to_post);



        edt_content_createP.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                btn_createP.setBackgroundColor(getResources().getColor(R.color.dis_Like));
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                btn_createP.setBackgroundColor(getResources().getColor(R.color.like));
                if (edt_content_createP.getText().toString().trim().compareTo("") == 0) {
                    btn_createP.setBackgroundColor(getResources().getColor(R.color.dis_Like));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (edt_content_createP.getText().toString().trim().compareTo("") == 0) {
                    btn_createP.setBackgroundColor(getResources().getColor(R.color.dis_Like));
                }
            }
        });

        btn_createP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String contentP = edt_content_createP.getText().toString().trim();
                if (contentP.isEmpty())
                    Toast.makeText(getContext(), "Hãy điền nội dung cho bài viết bạn nhé!", Toast.LENGTH_SHORT).show();
                else {
                    progress_Postsing.setVisibility(View.VISIBLE);
                    Animation loadingAnim = new AlphaAnimation(1, 0);
                    loadingAnim.setDuration(500);
                    loadingAnim.setRepeatCount(Animation.INFINITE);
                    loadingAnim.setRepeatMode(Animation.REVERSE);
                    tv_loadding.startAnimation(loadingAnim);

                    if (isChooseImg && mUri!=null){
                        RequestBody requestContentP = RequestBody.create(MediaType.parse("multipart/form-data"), contentP);
                        RequestBody requestIdAcc = RequestBody.create(MediaType.parse("multipart/form-data"), LoginActivity.currentAccount.get_id());

                        String realPath= RealPathUtil.getRealPath(getContext(), mUri);
                        File file = new File(realPath);

                        RequestBody requestBodyAvt = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                        MultipartBody.Part multipartBodyAvt = MultipartBody.Part.createFormData("img", file.getName(), requestBodyAvt);

                        ApiController.apiService.createPost(requestContentP, multipartBodyAvt, requestIdAcc).enqueue(new Callback<Post>() {
                            @Override
                            public void onResponse(Call<Post> call, Response<Post> response) {
                                if (response.isSuccessful()){
                                    posts.add(0, response.body());
                                    postAdapter.setPosts(posts);
                                    isChooseImg= false;
                                    dialog.dismiss();
                                }else {

                                }
                            }

                            @Override
                            public void onFailure(Call<Post> call, Throwable t) {
                                Log.d("path", "onFailure: "+ t.getMessage());
                                Toast.makeText(getActivity(), "Lỗi đường truyền", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }else {
                        ApiController.apiService.createPostNoImg(contentP, LoginActivity.currentAccount.get_id()).enqueue(new Callback<Post>() {
                            @Override
                            public void onResponse(Call<Post> call, Response<Post> response) {
                                if (response.isSuccessful()){
                                    posts.add(0, response.body());
                                    postAdapter.setPosts(posts);
                                    dialog.dismiss();
                                }else {

                                }
                            }

                            @Override
                            public void onFailure(Call<Post> call, Throwable t) {
                                Log.d("path", "onFailure: "+ t.getMessage());
                                Toast.makeText(getActivity(), "Lỗi đường truyền", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }


                }

            }
        });

        ///choose image
        add_Image_createP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickRequestPermission();
            }
        });

        dialog.show();
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
                        mUri= uri;
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), uri);
                            img_choose_img_to_Post.setVisibility(View.VISIBLE);
                            img_choose_img_to_Post.setImageBitmap(bitmap);
                            isChooseImg= true;
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
    );
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==MY_REQUEST_CODE){
            if (grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED){
                openGallary();
            }
        }

    }

    private void openGallary() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        mActivityResultLauncher.launch(Intent.createChooser(intent, "Select Picture"));
    }

}