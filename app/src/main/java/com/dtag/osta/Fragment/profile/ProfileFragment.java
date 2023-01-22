package com.dtag.osta.Fragment.profile;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.asksira.bsimagepicker.BSImagePicker;
import com.bumptech.glide.Glide;
import com.dtag.osta.Activity.MainActivity;
import com.dtag.osta.Fragment.ViewModel.profile.ProfileViewModel;
import com.dtag.osta.databinding.ProfileFragmentBinding;
import com.dtag.osta.utility.Sal7haSharedPreference;

import java.util.List;

public class ProfileFragment extends Fragment implements BSImagePicker.OnSingleImageSelectedListener,
        BSImagePicker.OnMultiImageSelectedListener,
        BSImagePicker.ImageLoaderDelegate,
        BSImagePicker.OnSelectImageCancelledListener {

    private ProfileViewModel mViewModel;
    ProfileFragmentBinding profileFragmentBinding;
    String argument;
    private static final int PICTURE_RC = 1001;

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        profileFragmentBinding = ProfileFragmentBinding.inflate(inflater, container, false);

        return profileFragmentBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mViewModel = ViewModelProviders.of(this).get(ProfileViewModel.class);
       if(!Sal7haSharedPreference.isLoggedIn(getContext())){
           
       }
        if (Sal7haSharedPreference.getRole(getContext()).equals("user")) {
            ((MainActivity) getActivity()).showBottomMenu();
            mViewModel.Init(profileFragmentBinding, getContext(), "user");
        } else {
            mViewModel.Init(profileFragmentBinding, getContext(), "agent");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mViewModel.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
   /* private boolean permissionsAreGranted() {
        if (getContext().checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                || getContext().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || getContext().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PICTURE_RC);
            return false;
        }
        return true;
    }*/


    @Override
    public void onMultiImageSelected(List<Uri> uriList, String tag) {
        mViewModel.onMultiImageSelected(uriList, "profile");
    }

    @Override
    public void onCancelled(boolean isMultiSelecting, String tag) {
        mViewModel.onCancelled(isMultiSelecting, "profile");
    }

    @Override
    public void onSingleImageSelected(Uri uri, String tag) {
        mViewModel.onSingleImageSelected(uri, "profile");
    }

    @Override
    public void loadImage(Uri imageUri, ImageView ivImage) {
        Glide.with(this).load(imageUri).into(ivImage);
    }

}
