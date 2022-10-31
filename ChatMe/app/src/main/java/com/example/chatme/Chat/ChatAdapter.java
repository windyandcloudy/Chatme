package com.example.chatme.Chat;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.chatme.Models.ChatObject;
import com.example.chatme.R;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    List<ChatObject> chatObjects;
    static  final  String SHARE_PRE_NAME="User";
    SharedPreferences sharedPreferences;

    public List<ChatObject> getChatObjects() {
        return chatObjects;
    }

    public void setChatObjects(List<ChatObject> chatObjects) {
        this.chatObjects = chatObjects;
        notifyDataSetChanged();
    }

    public ChatAdapter(Context context, List<ChatObject> chatObjects) {
        this.context = context;
        this.chatObjects = chatObjects;
        sharedPreferences= context.getSharedPreferences(SHARE_PRE_NAME, Context.MODE_PRIVATE);
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType){
            case 1:
                LayoutInflater inflater1 = LayoutInflater.from(context);
                View view1 = inflater1.inflate(R.layout.layout_message1, parent, false);
                ViewHolder1 viewHolder1 = new ChatAdapter.ViewHolder1(view1);
                return viewHolder1;
            default:
                LayoutInflater inflater2 = LayoutInflater.from(context);
                View view2 = inflater2.inflate(R.layout.layout_message2, parent, false);
                ViewHolder2 viewHolder2 = new ChatAdapter.ViewHolder2(view2);
                return viewHolder2;
        }
    }

//    @SuppressLint("WrongConstant")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ChatObject chatObject= chatObjects.get(position);
        switch (holder.getItemViewType()) {
            case 1:
                ViewHolder1 holder1 = (ViewHolder1)holder;
//                ...
                if (!chatObject.getUsername().equals(sharedPreferences.getString("username", ""))){
                    holder1.lnMessage.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                }

                Glide.with(context).load(chatObject.getAvt()).error(R.drawable.ic_baseline_perm_identity_24).centerCrop().into(holder1.imgAvt);
                holder1.tvMessage.setText(chatObject.getMessage());
                holder1.imgAvt.setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onClick(View view) {
                        holder1.imgAvt.setTooltipText(chatObject.getFullName());
                    }
                });
                break;

            case 2:
                ViewHolder2 holder2 = (ViewHolder2)holder;
//                ...
                if (!chatObject.getUsername().equals(sharedPreferences.getString("username", ""))){
                    holder2.lnMessage.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                }

                Glide.with(context).load(chatObject.getAvt()).error(R.drawable.ic_baseline_perm_identity_24).centerCrop().into(holder2.imgAvt);
                holder2.tvMessage.setText(chatObject.getMessage());
                holder2.imgAvt.setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onClick(View view) {
                        holder2.imgAvt.setTooltipText(chatObject.getFullName());
                    }
                });
                break;
        }



    }

    @Override
    public int getItemCount() {
        return chatObjects==null?0:chatObjects.size();
    }


    public class ViewHolder1 extends RecyclerView.ViewHolder {
        ImageView imgAvt;
        TextView tvMessage;
        LinearLayout lnMessage;
        CardView cvImg;
        public ViewHolder1(@NonNull View itemView) {
            super(itemView);
            imgAvt= itemView.findViewById(R.id.imgAvt);
            tvMessage= itemView.findViewById(R.id.tvMessage);
            lnMessage= itemView.findViewById(R.id.lnMessage);
            cvImg= itemView.findViewById(R.id.cvImg);
        }
    }
    public class ViewHolder2 extends RecyclerView.ViewHolder {
        ImageView imgAvt;
        TextView tvMessage;
        LinearLayout lnMessage;
        CardView cvImg;
        public ViewHolder2(@NonNull View itemView) {
            super(itemView);
            imgAvt= itemView.findViewById(R.id.imgAvt);
            tvMessage= itemView.findViewById(R.id.tvMessage);
            lnMessage= itemView.findViewById(R.id.lnMessage);
            cvImg= itemView.findViewById(R.id.cvImg);
        }
    }

    @Override
    public int getItemViewType(int position) {
        ChatObject chatObject= chatObjects.get(position);

        if (!chatObject.getUsername().equals(sharedPreferences.getString("username", ""))){
            return 1;
        }
        return 2;
    }
}
