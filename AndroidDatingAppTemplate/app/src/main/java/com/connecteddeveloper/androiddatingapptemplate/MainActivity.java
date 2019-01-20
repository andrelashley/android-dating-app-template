package com.connecteddeveloper.androiddatingapptemplate;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.connecteddeveloper.androiddatingapptemplate.models.User;
import com.connecteddeveloper.androiddatingapptemplate.util.Message;
import com.connecteddeveloper.androiddatingapptemplate.util.PreferenceKeys;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

public class MainActivity extends AppCompatActivity implements
        IMainActivity,
        BottomNavigationViewEx.OnNavigationItemSelectedListener {
    private static final String TAG = "MainActivity";

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.bottom_nav_home: {
                Log.d(TAG, "onNavigationItemSelected: HomeFragment.");
                HomeFragment homeFragment = new HomeFragment();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.main_content_frame,
                        homeFragment, getString(R.string.tag_fragment_home));
                transaction.addToBackStack(getString(R.string.tag_fragment_home));
                transaction.commit();
                item.setChecked(true);
                break;
            }

            case R.id.bottom_nav_connections: {
                Log.d(TAG, "onNavigationItemSelected: ConnectionsFragment.");
                SavedConnectionsFragment savedConnectionsFragment = new SavedConnectionsFragment();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.main_content_frame,
                        savedConnectionsFragment, getString(R.string.tag_fragment_saved_connections));
                transaction.addToBackStack(getString(R.string.tag_fragment_saved_connections));
                transaction.commit();
                item.setChecked(true);
                break;
            }

            case R.id.bottom_nav_messages: {
                Log.d(TAG, "onNavigationItemSelected: MessagesFragment.");
                MessagesFragment messagesFragment = new MessagesFragment();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.main_content_frame,
                        messagesFragment, getString(R.string.tag_fragment_messages));
                transaction.addToBackStack(getString(R.string.tag_fragment_messages));
                transaction.commit();
                item.setChecked(true);
                break;
            }
        }
        return false;
    }

    // widgets
    private BottomNavigationViewEx mBottomNavigationViewEx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBottomNavigationViewEx = findViewById(R.id.bottom_nav_view);

        mBottomNavigationViewEx.setOnNavigationItemSelectedListener(this);
        isFirstLogin();
        initBottomNavigationView();
        init();
    }

    private void initBottomNavigationView() {
        Log.d(TAG, "initBottomNavigationView: initializing the bottom navigation view.");
        mBottomNavigationViewEx.enableAnimation(false);
    }

    private void init() {
        HomeFragment homeFragment = new HomeFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main_content_frame,
                homeFragment, getString(R.string.tag_fragment_home));
        transaction.addToBackStack(getString(R.string.tag_fragment_home));
        transaction.commit();
    }

    private void isFirstLogin() {
        Log.d(TAG, "isFirstLogin: checking if this is the first login.");

        final SharedPreferences preferences =
                PreferenceManager.getDefaultSharedPreferences(this);
        boolean isFirstLogin = preferences.getBoolean(PreferenceKeys.FIRST_TIME_LOGIN, true);
        if(isFirstLogin) {
            Log.d(TAG, "isFirstLogin: launching alert dialog.");

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setMessage(getString(R.string.first_time_user_message));
            alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Log.d(TAG, "onClick: closing dialog.");

                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean(PreferenceKeys.FIRST_TIME_LOGIN, false);
                    editor.commit();
                    dialogInterface.dismiss();
                }
            });
            alertDialogBuilder.setIcon(R.drawable.appicon);
            alertDialogBuilder.setTitle(" ");
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }
    }

    @Override
    public void inflateViewProfileFragment(User user) {
        ViewProfileFragment fragment = new ViewProfileFragment();

        Bundle args = new Bundle();
        args.putParcelable(getString(R.string.intent_user), user);
        fragment.setArguments(args);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main_content_frame,
                fragment, getString(R.string.tag_fragment_view_profile));
        transaction.addToBackStack(getString(R.string.tag_fragment_view_profile));
        transaction.commit();
    }

    @Override
    public void onMessageSelected(Message message) {
        ChatFragment fragment = new ChatFragment();

        Bundle args = new Bundle();
        args.putParcelable(getString(R.string.intent_message), message);
        fragment.setArguments(args);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main_content_frame,
                fragment, getString(R.string.tag_fragment_chat));
        transaction.addToBackStack(getString(R.string.tag_fragment_chat));
        transaction.commit();
    }
}
