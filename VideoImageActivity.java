package com.example.admin.kinglaw;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class VideoImageActivity extends AppCompatActivity {
    private VideoView videoView;
    private String s_uid;
    private DatabaseReference databaseReference;
    private Uri uri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_image);
        videoView = (VideoView)findViewById(R.id.video_view);
        s_uid = getIntent().getStringExtra("video");

        databaseReference = FirebaseDatabase.getInstance().getReference().child("post").child(s_uid);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                uri = Uri.parse(dataSnapshot.child("video").getValue(String.class));
                MediaController mediaController = new MediaController(VideoImageActivity.this);
                mediaController.setAnchorView(videoView);
                videoView.setMediaController(mediaController);
                videoView.setVideoURI(uri);
                videoView.start();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
}
