package com.example.chatme.Home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.chatme.Models.Post;
import com.example.chatme.R;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {
    Context context;
    List<Post> posts;

    IOnClickPost iOnClickPost;

    public IOnClickPost getiOnClickPost() {
        return iOnClickPost;
    }

    public void setiOnClickPost(IOnClickPost iOnClickPost) {
        this.iOnClickPost = iOnClickPost;
    }

    public PostAdapter(Context context, List<Post> posts) {
        this.context = context;
        this.posts = posts;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_post, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Post post= posts.get(position);
        if (post.getImg()!=null){
            Glide.with(context).load(post.getImg()).centerCrop().into(holder.imgPost);
            holder.imgPost.setVisibility(View.VISIBLE);
        }
        Glide.with(context).load(post.getId_acc().getAvt()).centerCrop().into(holder.imgAvt);

        holder.tvFullname.setText(post.getId_acc().getFullname());
        holder.tvCreatedAt.setText(post.getCreatedAt());
        holder.tvContent.setText(post.getContent());
        holder.clickToCmt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iOnClickPost.iOnClickCmt(post, position);
            }
        });
        holder.clickToLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iOnClickPost.iOnClickLike(post, position);
                holder.clickToLike.setVisibility(View.GONE);
                holder.clickToDisLike.setVisibility(View.VISIBLE);
                holder.tv_so_like.setText(0+"");
            }
        });
        holder.clickToDisLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.clickToDisLike.setVisibility(View.GONE);
                holder.clickToLike.setVisibility(View.VISIBLE);
                holder.tv_so_like.setText(1+"");
            }
        });



    }

    @Override
    public int getItemCount() {
        return posts==null?0:posts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgAvt, imgPost, img_like_Click;
        TextView tvFullname, tvCreatedAt, tvContent, tvSeeMore, tv_so_like;
        LinearLayout clickToCmt, clickToLike, clickToDisLike;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgAvt= itemView.findViewById(R.id.img_avt);
            imgPost= itemView.findViewById(R.id.imgPosts);
            tvContent= itemView.findViewById(R.id.tv_content);
            tvCreatedAt= itemView.findViewById(R.id.tv_createAt);
            tvFullname= itemView.findViewById(R.id.tv_Fullname);
            tvSeeMore= itemView.findViewById(R.id.tv_see_more);
            clickToCmt= itemView.findViewById(R.id.click_to_comment);
            clickToLike= itemView.findViewById(R.id.click_to_like);
            clickToDisLike= itemView.findViewById(R.id.click_to_Dislike);
            img_like_Click= itemView.findViewById(R.id.img_like_Click);
            tv_so_like= itemView.findViewById(R.id.tv_so_like);
        }
    }
}
