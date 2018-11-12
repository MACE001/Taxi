package com.example.pcy.taxi;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;


public class post extends AppCompatActivity {
    private DatabaseReference mDatabase;
    int index=1;
    EditText postText;
    EditText startText;
    EditText arriveText;
    Button postButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        postText = (EditText)findViewById(R.id.post_title_Text);
        startText = (EditText)findViewById(R.id.startText);
        arriveText = (EditText)findViewById(R.id.arriveText);
        postButton = (Button)findViewById(R.id.postButton);
        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitPost();
            }
        });

    }
    private void submitPost(){
        final String title = postText.getText().toString();
        final String start = startText.getText().toString();
        final String arrive = arriveText.getText().toString();

        if (TextUtils.isEmpty(title)) {
            postText.setError("Required");
            return;
        }
        if (TextUtils.isEmpty(start)) {
            startText.setError("Required");
            return;
        }
        if (TextUtils.isEmpty(arrive)) {
            arriveText.setError("Required");
            return;
        }
/*
        setEditingEnabled(false);*/
        Toast.makeText(this, "Posting...", Toast.LENGTH_SHORT).show();
        mDatabase.child(getUid()).child(index+"번").setValue(title+"/"+start+"/"+arrive);
        index++;
       /* mDatabase.child("Posts").addListenerForSingleValueEvent(
                new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get user value
                        User user = dataSnapshot.getValue(User.class);

                        // [START_EXCLUDE]
                        if (user == null) {
                            writeNewPost(user.username, title, start,arrive);
                        } else {
                            // Write new post
                            writeNewPost(user.username, title, start,arrive);
                        }

                        // Finish this Activity, back to the stream
                        *//*setEditingEnabled(true);*//*
                        finish();
                        // [END_EXCLUDE]
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        *//*setEditingEnabled(true);*//*
                    }
                });*/
    }
    private String getUid() {
            return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }
/*
    private void setEditingEnabled(boolean b) {
        postText.setEnabled(b);
        startText.setEnabled(b);
        arriveText.setEnabled(b);

        if (b) {
            postButton.setVisibility(View.VISIBLE);
        } else {
            postButton.setVisibility(View.GONE);
        }
    }*/
    private void writeNewPost(String username, String title, String start,String arrive) {
        String key = mDatabase.child("posts").push().getKey();
        Post post = new Post(username, title, start,arrive);
        Map<String, Object> postValues = post.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/posts/" + key, postValues);
        childUpdates.put("/user-posts/"+ key, postValues);

        mDatabase.updateChildren(childUpdates);
    }

    public class Post {
        public String author;
        public String title;
        public String body;
        public String body2;
        public int starCount = 0;
        public Map<String, Boolean> stars = new HashMap<>();

        public Post() {
        }

        public Post(String author, String title, String body,String body2) {
            this.author = author;
            this.title = title;
            this.body = body;
            this.body = body2;
        }

        public Map<String, Object> toMap() {
            HashMap<String, Object> result = new HashMap<>();
            result.put("author", author);
            result.put("title", title);
            result.put("start", body);
            result.put("arrive",body2);
            result.put("starCount", starCount);
            result.put("stars", stars);

            return result;
        }
    }
}
