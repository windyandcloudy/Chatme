package com.example.chatme.Home.Comment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.chatme.Home.PostAdapter;
import com.example.chatme.Models.Comment;
import com.example.chatme.R;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {
    Context context;
    List<Comment> comments;

    public CommentAdapter(Context context, List<Comment> comments) {
        this.context = context;
        this.comments = comments;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_comment, parent, false);
        return new CommentAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Comment comment= comments.get(position);
        Glide.with(context).load(comment.getId_acc().getAvt()).centerCrop().into(holder.imgAvtCmt);
        holder.tvFullnameCmt.setText(comment.getId_acc().getFullname());
        holder.tvContentCmt.setText(comment.getContent());
        holder.tvTimeCmt.setText(comment.getCreatedAt());
    }

    @Override
    public int getItemCount() {
        return comments==null?0:comments.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgAvtCmt;
        TextView tvFullnameCmt, tvTimeCmt, tvContentCmt;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgAvtCmt= itemView.findViewById(R.id.img_avt_cmt);
            tvFullnameCmt= itemView.findViewById(R.id.tv_fullnam_cmt);
            tvTimeCmt= itemView.findViewById(R.id.tvTimeCmt);
            tvContentCmt= itemView.findViewById(R.id.tv_content_cmt);
        }
    }
}
