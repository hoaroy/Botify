package com.example.Sachpee.Fragment.BottomNav;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.Sachpee.Adapter.VoucherAdapter;
import com.example.Sachpee.Model.Voucher;
import com.example.Sachpee.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;



import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

public class ChatFragment extends Fragment {
    private WebSocket webSocket;
    private EditText messageInput;
    private EditText nameInput;
    private TextView chatOutput;
    private String userName = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        messageInput = view.findViewById(R.id.messageInput);
        nameInput = view.findViewById(R.id.nameInput); // EditText để nhập tên
        chatOutput = view.findViewById(R.id.chatOutput);
        Button sendButton = view.findViewById(R.id.sendButton);
        Button submitNameButton = view.findViewById(R.id.submitNameButton); // Button để gửi tên

        submitNameButton.setOnClickListener(v -> {
            userName = nameInput.getText().toString().trim();
            if (!userName.isEmpty()) {
                initiateWebSocket(); // Chỉ kết nối khi tên đã được nhập
                nameInput.setVisibility(View.GONE);
                submitNameButton.setVisibility(View.GONE);
                chatOutput.append("Connecting as " + userName + "...\n");
            }
        });

        sendButton.setOnClickListener(v -> {
            String message = messageInput.getText().toString();
            if (!message.isEmpty() && !userName.isEmpty()) {
                // Gửi message dưới dạng JSON
                webSocket.send("{\"type\":\"chat\", \"text\":\"" + message + "\"}");
                messageInput.setText("");
            } else if (userName.isEmpty()) {
                Toast.makeText(getActivity(), "Please enter your name first.", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    private void initiateWebSocket() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url("ws://192.168.0.2:8080").build();
        webSocket = client.newWebSocket(request, new WebSocketListener() {
            @Override
            public void onOpen(@NonNull WebSocket webSocket, @NonNull okhttp3.Response response) {
                getActivity().runOnUiThread(() -> chatOutput.append("Connected to server as " + userName + "\n"));
                // Gửi tên người dùng sau khi kết nối
                webSocket.send("{\"type\":\"setName\", \"name\":\"" + userName + "\"}");
            }

            @Override
            public void onMessage(@NonNull WebSocket webSocket, @NonNull String text) {
                getActivity().runOnUiThread(() -> chatOutput.append("Received: " + text + "\n"));
            }

            @Override
            public void onClosing(@NonNull WebSocket webSocket, int code, @NonNull String reason) {
                webSocket.close(1000, null);
                getActivity().runOnUiThread(() -> chatOutput.append("Closing: " + reason + "\n"));
            }

            @Override
            public void onFailure(@NonNull WebSocket webSocket, @NonNull Throwable t, @Nullable okhttp3.Response response) {
                getActivity().runOnUiThread(() -> chatOutput.append("Error: " + t.getMessage() + "\n"));
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (webSocket != null) {
            webSocket.close(1000, "App is closing");
        }
    }
}


