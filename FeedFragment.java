package com.example.admin.kinglaw;
import android.annotation.SuppressLint;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.opengl.Visibility;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URI;
import java.text.DateFormat;

import de.hdodenhof.circleimageview.CircleImageView;
public class  FeedFragment extends Fragment implements View.OnClickListener{
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;
    private TextView feedTextView;
    private View v;
    private RecyclerView feedrecyclerview;
    private DatabaseReference feedDatabase;
    private boolean bool = false;
    private FirebaseRecyclerAdapter<FirebaseHelper,BlogViewHolder> firebaseRecyclerAdapter;
    private void init() {
        feedTextView=(TextView)v.findViewById(R.id.feed_textview);
        feedrecyclerview=(RecyclerView)v.findViewById(R.id.feed_recyclerview);
        feedrecyclerview.setHasFixedSize(false);
        feedrecyclerview.setNestedScrollingEnabled(false);
        feedDatabase= FirebaseDatabase.getInstance().getReference();
        feedDatabase.keepSynced(true);
        feedTextView.setOnClickListener(this);
    }
    private void retrive() {
        firebaseRecyclerAdapter =
        new FirebaseRecyclerAdapter<FirebaseHelper,BlogViewHolder>(
                        FirebaseHelper.class,
                        R.layout.blog_row,
                        BlogViewHolder.class,
                        feedDatabase.child("post")
                ) {
                    @SuppressLint("ClickableViewAccessibility")
                    @Override
                    protected void populateViewHolder(final BlogViewHolder viewHolder, final FirebaseHelper model, int position) {
                    final String s_post = getRef(position).getKey();
                        viewHolder.setName("Name");
                        viewHolder.setDesc(model.getDesc());
                        feedDatabase.child("post").child(s_post).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if(dataSnapshot.hasChild("image")){
                                    viewHolder.setThumbNail(getContext(),model.getImage());
                                }
                                else {
                                    viewHolder.setThumbNail(getContext(),model.getThumbnail());
                                    viewHolder.blogVideoPlay.setVisibility(View.VISIBLE);
                                }
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                            }
                        });
                        viewHolder.blogVideoPlay.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(getContext(),VideoImageActivity.class);
                                intent.putExtra("video",s_post);
                                startActivity(intent);
                            }
                        });

//                        viewHolder.setDp(getActivity().getApplicationContext(),model.getU_id());
//                        viewHolder.setlikeImage(s_post);
                        viewHolder.setTime(model.getTime());
//                        viewHolder.setBlogLikeView(s_post);
//                        viewHolder.setBlogComment(s_post);
//                        viewHolder.blogComment.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                Intent intent = new Intent(getActivity(), CommentActivity.class);
//                                intent.putExtra("s_post", s_post);
//                                intent.putExtra("posts", "Post1");
//                                getActivity().startActivity(intent);
//                            }
//                        });
//                        viewHolder.blogLikeView.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                Intent intent = new Intent(getActivity(), LikeActivity.class);
//                                intent.putExtra("s_post", s_post);
//                                intent.putExtra("posts", "Post1Like");
//                                getActivity().startActivity(intent);
//                            }
//                        });
//                        viewHolder.post_image.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                Intent intent = new Intent(getActivity(), SinglePostActivity.class);
//                                intent.putExtra("s_post", s_post);
//                                intent.putExtra("posts", "Post1");
//                                getActivity().startActivity(intent);
//                            }
//                        });
                        viewHolder.blogShare.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Bitmap bitmap = viewHolder.getBitmapFromView(viewHolder.idforShare);
                                try {
                                    File file = new File(getContext().getExternalCacheDir(), "logicchip.png");
                                    FileOutputStream fOut = new FileOutputStream(file);
                                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
                                    fOut.flush();
                                    fOut.close();
                                    file.setReadable(true, false);
                                    final Intent intent = new Intent(android.content.Intent.ACTION_SEND);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
                                    intent.setType("image/png");
                                    startActivity(Intent.createChooser(intent, "Share image via"));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
//                        viewHolder.blogLike.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                like = true;
//                                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
//                                Date date = new Date();
//                                System.out.println(dateFormat.format(date));
//                                Calendar cal = Calendar.getInstance();
//                                final String time1 = dateFormat.format(cal.getTime());
//                                likedatabaseReference.child(s_post).addValueEventListener(new ValueEventListener() {
//                                    @Override
//                                    public void onDataChange(DataSnapshot dataSnapshot) {
//                                        if(like == true){
//                                            if(dataSnapshot.hasChild(s_uid)){
//                                                DatabaseReference data = likedatabaseReference.child(s_post).child(s_uid);
//                                                data.child("Name").removeValue();
//                                                data.child("dp").removeValue();
//                                                data.child("time").removeValue();
//                                                like = false;
//                                            }
//                                            else{
//                                                loginstatusdatabaseReference.addValueEventListener(new ValueEventListener() {
//                                                    @Override
//                                                    public void onDataChange(DataSnapshot dataSnapshot) {
//                                                        String likename = dataSnapshot.child(s_uid).child("Name").getValue().toString();
//                                                        String dp = dataSnapshot.child(s_uid).child("Image").getValue().toString();
//                                                        likedatabaseReference.child(s_post).child(s_uid).child("Name").setValue(likename);
//                                                        likedatabaseReference.child(s_post).child(s_uid).child("dp").setValue(dp);
//                                                        likedatabaseReference.child(s_post).child(s_uid).child("time").setValue(time1);
//                                                        like = false;
//                                                    }
//                                                    @Override
//                                                    public void onCancelled(DatabaseError databaseError) {
//                                                    }
//                                                });
//                                            }
//                                        }
//                                        else{
//                                        }
//                                    }
//                                    @Override
//                                    public void onCancelled(DatabaseError databaseError) {
//                                    }
//                                });
//                            }
//                        });
                    }
                };
        final LinearLayoutManager mLayoutManager1= new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, true);
        feedrecyclerview.setLayoutManager(mLayoutManager1);
        feedrecyclerview.setAdapter(firebaseRecyclerAdapter);
    }
    @Override
    public void onClick(View v) {
    switch (v.getId()){
        case R.id.feed_textview:{
            startActivity(new Intent(getContext(),PostActivity.class));
            break;
        }
    }
    }
    public FeedFragment() {
    }
    public static FeedFragment newInstance(String param1, String param2) {
        FeedFragment fragment = new FeedFragment();
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
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v=inflater.inflate(R.layout.fragment_feed, container, false);
        feedTextView=(TextView)v.findViewById(R.id.feed_textview);
        feedrecyclerview=(RecyclerView)v.findViewById(R.id.feed_recyclerview);
        feedrecyclerview.setHasFixedSize(false);
        feedrecyclerview.setNestedScrollingEnabled(false);
        feedDatabase= FirebaseDatabase.getInstance().getReference();
        feedDatabase.keepSynced(true);

        feedTextView.setOnClickListener(this);
        retrive();
        init();
        return v;
    }
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
    public static class BlogViewHolder extends RecyclerView.ViewHolder {
    private View mView;
    private DatabaseReference databaseReference;
    private TextView blogLikeView;
    private ImageView post_image;
    private TextView blogLike;
    private TextView blogComment;
    private CircleImageView dp;
    private DatabaseReference setlike;
    private FirebaseAuth firebaseAuth;
    private TextView blogTime;
    private TextView blogShare;
    private LinearLayout idforShare;
    private TextView post_desc;
    private TextView blogName;
    private TextView blogVideoPlay;
    public BlogViewHolder(View itemView) {
        super(itemView);
        mView = itemView;
        setlike = FirebaseDatabase.getInstance().getReference().child("Post1Like");
        databaseReference = FirebaseDatabase.getInstance().getReference().child("PostComment");
        databaseReference.keepSynced(true);
        blogLike = (TextView) mView.findViewById(R.id.blog_like);
        blogLikeView = (TextView) mView.findViewById(R.id.blog_likeview);
        firebaseAuth = FirebaseAuth.getInstance();
        blogComment = (TextView) mView.findViewById(R.id.blog_comment);
        blogTime = (TextView) mView.findViewById(R.id.blog_time);
        blogShare = (TextView) mView.findViewById(R.id.blog_share);
        idforShare = (LinearLayout) mView.findViewById(R.id.sharingimage);
        dp = (CircleImageView) mView.findViewById(R.id.blog_dp);
        post_desc = (TextView) mView.findViewById(R.id.post_desc);
        blogName = (TextView) mView.findViewById(R.id.blog_name);
        blogVideoPlay = (TextView)mView.findViewById(R.id.blog_videoplay);
    }
    public void setTime(String time){
        blogTime.setText(time);
    }
    public void setDesc(String desc){
        post_desc.setText(desc);
    }
    public void setName(String name){
        String s = "<b><font color='#000000'>"+String.valueOf(name)+"</font></b>"+" posted a new Post";
        blogName.setText(Html.fromHtml(s));
    }
    public void setThumbNail(final Context ctx, final String image) {
        post_image = (ImageView) mView.findViewById(R.id.blog_image);
        Picasso.with(ctx).load(image).networkPolicy(NetworkPolicy.OFFLINE).into(post_image, new com.squareup.picasso.Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError() {
                Picasso.with(ctx).load(image).into(post_image);
            }

        });
    }
    public void setDp(final Context ctx,final String u_id){
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("LoginStatus");
        try {
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(final DataSnapshot dataSnapshot) {
                    try {
                        if(dataSnapshot.child(u_id).child("Image").getValue().toString()!=null){
                            Picasso.with(ctx).load(dataSnapshot.child(u_id).child("Image").getValue().toString())
                                    .networkPolicy(NetworkPolicy.OFFLINE).into(dp, new com.squareup.picasso.Callback() {
                                @Override
                                public void onSuccess() {
                                }
                                @Override
                                public void onError() {
                                    Picasso.with(ctx).load(dataSnapshot.child(u_id).child("Image").getValue().toString()).into(dp);
                                }
                            });
                        }
                        else {
                        }
                    }
                    catch (Exception e){
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }
        catch (Exception e){
        }
    }
    public void setlikeImage(final String post_key){
        setlike.addValueEventListener(new ValueEventListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(post_key).hasChild(firebaseAuth.getCurrentUser().getUid())){
                    blogLike.setBackgroundResource(R.drawable.ic_thumb_up_blue);
                }
                else {
                    blogLike.setBackgroundResource(R.drawable.ic_thumb_up_white);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public void setBlogLikeView(final String post_key){
        setlike.child(post_key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int likecount = (int) dataSnapshot.getChildrenCount();
                if(likecount>0){
                    if(dataSnapshot.hasChild(firebaseAuth.getUid().toString())){
                        likecount = likecount - 1;
                        blogLikeView.setText(Html.fromHtml("<b>You</b> and "+likecount+" others liked this post"));
                    }
                    else{
                        blogLikeView.setText(Html.fromHtml(likecount+" others liked this post"));
                    }
                }
                else{
                    blogLikeView.setText("No one liked this post...");
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public void setBlogComment(final String post_key){
        databaseReference.child(post_key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int commentcount = (int) dataSnapshot.getChildrenCount();
                blogComment.setText("Comment"+"("+commentcount +")");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public Bitmap getBitmapFromView(View view) {
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(),Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        Drawable bgDrawable =view.getBackground();
        if (bgDrawable!=null) {
            //has background drawable, then draw it on the canvas
            bgDrawable.draw(canvas);
        }   else{
            //does not have background drawable, then draw white background on the canvas
            canvas.drawColor(Color.WHITE);
        }
        view.draw(canvas);
        return returnedBitmap;
    }


}
}
