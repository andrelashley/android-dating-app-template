package com.connecteddeveloper.androiddatingapptemplate;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.connecteddeveloper.androiddatingapptemplate.models.User;
import com.connecteddeveloper.androiddatingapptemplate.util.PreferenceKeys;
import com.like.LikeButton;
import com.like.OnLikeListener;

import java.util.HashSet;
import java.util.Set;

import de.hdodenhof.circleimageview.CircleImageView;


public class ViewProfileFragment extends Fragment implements OnLikeListener {

    private static final String TAG = "ViewProfileFragment";

    // widgets
    private TextView mFragmentHeading, mName, mGender, mInterestedIn, mStatus;
    private LikeButton mLikeButton;
    private RelativeLayout mBackArrow;
    private CircleImageView mProfileImage;

    // vars
    private User mUser;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        if(bundle != null) {
            mUser = bundle.getParcelable(getString(R.string.intent_user));
            Log.d(TAG, "onCreate: got incoming bundle: " + mUser.getName());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_profile, container, false);
        Log.d(TAG, "onCreateView: started.");
        mBackArrow = view.findViewById(R.id.back_arrow);
        mFragmentHeading = view.findViewById(R.id.fragment_heading);
        mProfileImage = view.findViewById(R.id.profile_image);
        mLikeButton = view.findViewById(R.id.heart_button);
        mName = view.findViewById(R.id.name);
        mGender = view.findViewById(R.id.gender);
        mInterestedIn = view.findViewById(R.id.interested_in);
        mStatus = view.findViewById(R.id.status);

        // mBackArrow.setOnClickListener(this);
        mLikeButton.setOnLikeListener(this);
        checkIfConnected();
        init();

        return view;
    }

    private void init(){
        Log.d(TAG, "init: initializing " + getString(R.string.tag_fragment_view_profile));
        if(mUser != null){
            Glide.with(getActivity())
                    .load(mUser.getProfile_image())
                    .into(mProfileImage);

            mName.setText(mUser.getName());
            mGender.setText(mUser.getGender());
            mInterestedIn.setText(mUser.getInterested_in());
            mStatus.setText(mUser.getStatus());
        }
    }

    private void checkIfConnected(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        Set<String> savedNames = preferences.getStringSet(PreferenceKeys.SAVED_CONNECTIONS, new HashSet<String>());
        if(savedNames.contains(mUser.getName())){
            Log.d(TAG, "checkIfConnected: liked.");
            mLikeButton.setLiked(true);
        }
        else{
            Log.d(TAG, "checkIfConnected: not liked.");
            mLikeButton.setLiked(false);
        }
    }

    @Override
    public void liked(LikeButton likeButton) {
        Log.d(TAG, "liked: liked");
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        SharedPreferences.Editor editor = preferences.edit();

        Set<String> savedNames = preferences.getStringSet(PreferenceKeys.SAVED_CONNECTIONS, new HashSet<String>());
        savedNames.add(mUser.getName());
        editor.putStringSet(PreferenceKeys.SAVED_CONNECTIONS, savedNames);
        editor.commit();

    }

    @Override
    public void unLiked(LikeButton likeButton) {
        Log.d(TAG, "unLiked: unliked.");
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        SharedPreferences.Editor editor = preferences.edit();

        Set<String> savedNames = preferences.getStringSet(PreferenceKeys.SAVED_CONNECTIONS, new HashSet<String>());
        savedNames.remove(mUser.getName());
        editor.remove(PreferenceKeys.SAVED_CONNECTIONS);
        editor.commit();
        editor.putStringSet(PreferenceKeys.SAVED_CONNECTIONS, savedNames);
        editor.commit();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: called.");
    }



}
