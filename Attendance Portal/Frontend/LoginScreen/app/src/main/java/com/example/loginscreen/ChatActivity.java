package com.example.loginscreen;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;

import android.widget.Button;
import android.widget.TextView;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Activity for chatting with your class
 */
public class ChatActivity extends AppCompatActivity {

    /**
     * Displays the chat's message(s)
     */
    private TextView output;

    /**
     * User's inputted message
     */
    public TextInputLayout userMessage;

    /**
     * Websocket Client used to send messages
     */
    private static WebSocketClient wc;

    /**
     * Button that sends the user's message
     */
    private Button sendMessage;

    /**
     * Button that connects the user to the chat room
     */
    private Button connect;

    /**
     * Given user
     */
    public User user;

    /**
     * Course chat room that you are in
     */
    public Course course;

    /**
     * Goes back and disconnects from the chat room
     */
    public Button disconnect;

    /**
     * Checks if the connection is closed
     */
    public boolean notClosed;

    /**
     * Tells us where things we are testing are located
     */
    private static final String TAG = "ChatActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        notClosed = false;
        output = findViewById(R.id.messages);
        output.setMovementMethod(new ScrollingMovementMethod());
        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra("user");
        course = (Course)intent.getSerializableExtra("course");
        TextView chatName = findViewById(R.id.chatName);
        chatName.setText("Chat with: " + course.getCourseName());
        disconnect = findViewById(R.id.disconnect);
        assert course != null;

        connect = findViewById(R.id.connect);
        sendMessage = findViewById(R.id.sendMessage);
        userMessage = findViewById(R.id.enterMessage);
        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Draft[] drafts = {new Draft_6455()};
                String url = "ws://cs309-ad-2.misc.iastate.edu:8080/chat/" + user.getEmail() + "/" + course.getCourseName();

                try {
                    Log.d("Socket:", "Trying socket");
                    wc = new WebSocketClient(new URI(url), drafts[0]) {

                        /**
                         * Adds the received message to the string builder and adds it to the text view
                         * @param message Received message
                         */
                        @Override
                        public void onMessage(String message) {
                            output.append(message + "\n");
                        }

                        /**
                         * Connected to the chat room
                         * @param handshake Server handshake
                         */
                        @Override
                        public void onOpen(ServerHandshake handshake) {
                            notClosed = true;
                            Log.d("OPEN", "connected");
                        }

                        /**
                         * Chat room is closed
                         * @param code Code for closing
                         * @param reason Reason for closing
                         * @param remote True or false
                         */
                        @Override
                        public void onClose(int code, String reason, boolean remote) {
                            notClosed = false;
                            Log.d("CLOSE", "onClose() returned: " + reason);
                        }

                        /**
                         * Error happened
                         * @param e Exception that was thrown
                         */
                        @Override
                        public void onError(Exception e) {
                            Log.d("Exception:", e.toString());
                        }
                    };
                }
                catch (URISyntaxException e) {
                    Log.d("Exception:", e.getMessage());
                    e.printStackTrace();
                }
                connect();
            }
        });

        sendMessage.setOnClickListener(new View.OnClickListener() {

            /**
             * On click listener for the send button
             * @param v Send Button
             */
            @Override
            public void onClick(View v) {
                try {
                    if(notClosed) wc.send(userMessage.getEditText().getText().toString());
                }
                catch (Exception e)
                {
                    Log.d("ExceptionSendMessage", e.getMessage());
                }
            }
        });

        disconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wc.close(1000, "Deliberate disconnection");
            }
        });
    }

    public void connect(){
        if(notClosed)wc.connect();
        else Log.d(TAG, "Already Connected");
    }

}
