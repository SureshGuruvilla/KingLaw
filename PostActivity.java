package com.example.admin.kinglaw;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.media.ThumbnailUtils;
import android.provider.MediaStore.Video.Thumbnails;

import static com.example.admin.kinglaw.GlideOptions.fitCenterTransform;


public class PostActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText postTitle;
    private EditText postDesc;
    private TextView postSubmit;
    private TextView postType;
    private static final int GALLERY_REQUEST = 1;
    private Uri mImagUri = null;
    private DatabaseReference postDatabase;
    private StorageReference postStorage;
    private ProgressBar progressBar;
    private Button postUpload;
    private ImageView postThumbImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        init();
    }

    private void init() {

        postTitle=(EditText)findViewById(R.id.post_title);
        postDesc=(EditText)findViewById(R.id.post_desc);
        postSubmit=(TextView)findViewById(R.id.post_submit);
        postType=(TextView)findViewById(R.id.post_type);
        postDatabase= FirebaseDatabase.getInstance().getReference();
        postStorage= FirebaseStorage.getInstance().getReference();
        progressBar=(ProgressBar)findViewById(R.id.post_progressbar);
        postUpload = (Button)findViewById(R.id.post_upload);
        postThumbImage=(ImageView)findViewById(R.id.post_thumbnail);
        postSubmit.setOnClickListener(this);
        postType.setOnClickListener(this);
        postUpload.setOnClickListener(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GALLERY_REQUEST && resultCode == RESULT_OK) {
            mImagUri = data.getData();
            try {
                GlideApp.with(getApplicationContext())
                        .load(mImagUri)
                        .thumbnail(0.2f)
                        .apply(fitCenterTransform())
                        .fitCenter()
                        .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                        .skipMemoryCache(false)
                        .into(postThumbImage);
            }
            catch (Exception e){
                Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
            }
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.post_upload: {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType(postType.getText().toString()+"/*");
                startActivityForResult(galleryIntent, GALLERY_REQUEST);
                break;
            }
            case R.id.post_submit: {
                if(!TextUtils.isEmpty(postTitle.getText())&&!TextUtils.isEmpty(postDesc.getText())) {
                    progressBar.setVisibility(View.VISIBLE);
                    try{
                        StorageReference storageReference = postStorage.child(postType.getText().toString()).child(mImagUri.getLastPathSegment());
                        storageReference.putFile(mImagUri)
                                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        final Uri downloadUrl = taskSnapshot.getDownloadUrl();
                                        if(postType.getText().toString().equals("video")){
                                            BitmapDrawable drawable = (BitmapDrawable)postThumbImage.getDrawable();
                                            Bitmap bitmap = drawable.getBitmap();
                                            bitmap = Bitmap.createScaledBitmap(bitmap, postThumbImage.getWidth(), postThumbImage.getHeight(),true);

                                            ByteArrayOutputStream stream = new ByteArrayOutputStream();
                                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                                            byte[] byteArray = stream.toByteArray();

                                            StorageReference storethumb = postStorage.child("Thumb").child(mImagUri.getLastPathSegment());
                                            storethumb.putBytes(byteArray).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                @Override
                                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot1) {
                                                    final Uri downloadThumbUrl = taskSnapshot1.getDownloadUrl();

                                                    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                                                    Date date = new Date();
                                                    System.out.println(dateFormat.format(date));
                                                    Calendar cal = Calendar.getInstance();
                                                    String time = dateFormat.format(cal.getTime());

                                                    DatabaseReference data=postDatabase.child("post").push();
                                                        data.child("video").setValue(downloadUrl.toString());
                                                        data.child("thumbnail").setValue(downloadThumbUrl.toString());
                                                        data.child("time").setValue(time);
                                                        data.child("uid").setValue("3ger3er5r43432hrt");
                                                        data.child("title").setValue(postTitle.getText().toString());
                                                        data.child("desc").setValue(postDesc.getText().toString());
                                                    postTitle.setText("");
                                                    postDesc.setText("");
                                                    mImagUri=null;
                                                    progressBar.setVisibility(View.GONE);
                                                    startActivity(new Intent(PostActivity.this,MainActivity.class));
                                        }
                                            });
                                        }
                                        else{
                                            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                                            Date date = new Date();
                                            System.out.println(dateFormat.format(date));
                                            Calendar cal = Calendar.getInstance();
                                            String time = dateFormat.format(cal.getTime());
                                            DatabaseReference data=postDatabase.child("post").push();
                                            data.child("image").setValue(downloadUrl.toString());
                                            data.child("time").setValue(time);
                                            data.child("uid").setValue("3ger3er5r43432hrt");
                                            data.child("title").setValue(postTitle.getText().toString());
                                            data.child("desc").setValue(postDesc.getText().toString());
                                            postTitle.setText("");
                                            postDesc.setText("");
                                            mImagUri=null;
                                            progressBar.setVisibility(View.GONE);
                                            startActivity(new Intent(PostActivity.this,MainActivity.class));
                                        }

                                    }
                                });

                    }
                    catch (Exception e){
                        Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(getApplicationContext(),"Fill all the fields...",Toast.LENGTH_SHORT).show();
            }
            break;
            }
            case R.id.post_type: {
                if(postType.getText().equals("image")){
                    postType.setText("video");
                }
                else if(postType.getText().equals("video")){
                    postType.setText("image");
                }
                break;
            }
        }
    }
}