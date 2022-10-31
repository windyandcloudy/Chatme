package com.example.chatme.Chat;

import static com.example.chatme.ChatApplication.mSocket;
import static com.example.chatme.LoginActivity.currentAccount;

import android.app.Application;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.chatme.ChatApplication;
import com.example.chatme.Models.ChatObject;
import com.example.chatme.R;
import com.example.chatme.databinding.FragmentChatBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChatFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChatFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ChatFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChatFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ChatFragment newInstance(String param1, String param2) {
        ChatFragment fragment = new ChatFragment();
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
    FragmentChatBinding binding;

    List<ChatObject> chatObjects;
    ChatAdapter adapter;

    boolean isJoin=false;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding= DataBindingUtil.inflate(inflater, R.layout.fragment_chat, container, false);

        mSocket.on("message", onNewMessage);
        chatObjects= new ArrayList<>();
        adapter= new ChatAdapter(getActivity(), chatObjects);
        RecyclerView.LayoutManager layoutManager= new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        binding.revMessage.setLayoutManager(layoutManager);
        binding.revMessage.setAdapter(adapter);
        binding.btnJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!binding.edtRoom.getText().toString().trim().isEmpty()){
                    mSocket.emit("join", binding.edtRoom.getText().toString().trim());
                    isJoin= true;
                    Toast.makeText(getActivity(), "Bạn đã join "+ binding.edtRoom.getText().toString().trim(), Toast.LENGTH_SHORT).show();

                }else {
                    Toast.makeText(getActivity(), "Hãy nhập id room", Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.imgSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!binding.edtMessage.getText().toString().trim().isEmpty()  && isJoin){
                    Log.e("hp", "onClick: ");
                    JSONObject jsonObject= new JSONObject();
                    try {
                        jsonObject.put("username", currentAccount.getUsername());
                        jsonObject.put("fullname", currentAccount.getFullname());
                        jsonObject.put("message", binding.edtMessage.getText().toString().trim());
                        jsonObject.put("avt", currentAccount.getAvt());
                        mSocket.emit("message", jsonObject);

                        binding.edtMessage.setText("");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else {
                    binding.edtMessage.setText("");
                }
            }
        });


        return binding.getRoot();
    }
    Emitter.Listener onNewMessage= new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            try{
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject data= (JSONObject) args[0];
                            String username= data.getString("username");
                            String fullname= data.getString("fullname");
                            String message= data.getString("message");
                            String avt= data.getString("avt");

                            ChatObject chatObject= new ChatObject(username, fullname, message, avt);
                            chatObjects.add(chatObject);
                            adapter.setChatObjects(chatObjects);
                            binding.revMessage.smoothScrollToPosition(chatObjects.size());
                        } catch (JSONException e) {
                            System.out.println(e.getMessage());
                        }
                    }
                });
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
    };
}