package com.connecteddeveloper.androiddatingapptemplate;

import com.connecteddeveloper.androiddatingapptemplate.models.User;
import com.connecteddeveloper.androiddatingapptemplate.util.Message;

public interface IMainActivity {
    void inflateViewProfileFragment(User user);
    void onMessageSelected(Message message);
}
