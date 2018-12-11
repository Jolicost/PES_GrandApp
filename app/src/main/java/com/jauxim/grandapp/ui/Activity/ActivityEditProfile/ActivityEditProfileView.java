package com.jauxim.grandapp.ui.Activity.ActivityEditProfile;

import com.jauxim.grandapp.models.UserModel;

/**
 * Created by ennur on 6/25/16.
 */
public interface ActivityEditProfileView {
    void showWait();

    void removeWait();

    void onFailure(String appErrorMessage);

    void showInfoUser(UserModel user);

    void showEditSuccess(int edit_profile_success);

    void getProfileInfo(String userId);

    void callEditProfile(String imageUrl);
}
