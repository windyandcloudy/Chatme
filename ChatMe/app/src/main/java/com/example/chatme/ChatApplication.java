package com.example.chatme;

import android.app.Application;



import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;

public class ChatApplication extends Application {
    public static Socket mSocket;

    static {
        try {
            mSocket = IO.socket(Constant.BASE_URL);
            mSocket.connect();
        } catch (URISyntaxException e) {
        }
    }

//    public static Socket getmSocket() {
//        return mSocket;
//    }
}
