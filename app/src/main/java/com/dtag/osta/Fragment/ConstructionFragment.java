package com.dtag.osta.Fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.asksira.bsimagepicker.BSImagePicker;
import com.bumptech.glide.Glide;
import com.dtag.osta.Activity.MainActivity;
import com.dtag.osta.Fragment.maps.MapFragment;
import com.dtag.osta.R;
import com.dtag.osta.databinding.ConstructionFragmentBinding;
import com.dtag.osta.network.Interface.OnInputSelected;
import com.dtag.osta.utility.DatePickerFragment;
import com.dtag.osta.utility.Sal7haSharedPreference;
import com.dtag.osta.utility.TimePickerFragment;

import java.util.ArrayList;
import java.util.List;

public class ConstructionFragment extends Fragment implements TimePickerFragment.OnTimePicked, BSImagePicker.OnSingleImageSelectedListener,
        BSImagePicker.OnMultiImageSelectedListener,
        BSImagePicker.ImageLoaderDelegate,
        BSImagePicker.OnSelectImageCancelledListener,
        OnInputSelected {

    private ConstructionViewModel mViewModel;
    ConstructionFragmentBinding constructionFragmentBinding;
    private static final int PICTURE_RC = 1001;
    int PLACE_PICKER_REQUEST = 1;

    public static ConstructionFragment newInstance() {
        return new ConstructionFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        constructionFragmentBinding = ConstructionFragmentBinding.inflate(inflater, container, false);
        return constructionFragmentBinding.getRoot();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ConstructionViewModel.class);
        ((MainActivity) getActivity()).showBottomMenu();
        if (Sal7haSharedPreference.isLoggedIn(getContext())) {
            if (Sal7haSharedPreference.getRole(getContext()).equals("agent")) {
                constructionFragmentBinding.createOrder.setEnabled(false);
                noOrder();
            }
        }
        constructionFragmentBinding.selectLocationTxt.setOnClickListener(view -> {
            mViewModel.dialog.setmOnInputSelected(this);
            mViewModel.dialog.setTargetFragment(ConstructionFragment.this, 1);
            mViewModel.dialog.show(getFragmentManager(), "MyCustomDialog");
        });
        mViewModel =new ViewModelProvider(this).get(ConstructionViewModel.class);
        if (mViewModel.dialog == null)
            mViewModel.dialog = new MapFragment();

        int cat_id;
        if (getArguments() != null)
            cat_id = getArguments().getInt("categoryId");
        else cat_id = 0;

        int cat_postion;
        if (getArguments() != null)
            cat_postion = getArguments().getInt("catPositon");
        else cat_postion = 0;
        mViewModel.init(constructionFragmentBinding, getActivity(), cat_id,cat_postion, getActivity(), getArguments().getString("lat"), getArguments().getString("long"), getArguments().getString("add"));

        /*if (getArguments() != null) {
            requestInfromationFragmentBinding.serviceName.setText(
                    (getArguments().containsKey("categoryName")
                            ? getArguments().getString("categoryName")
                            : "")
            );
        }*/
        constructionFragmentBinding.gallery.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            ArrayList<String> MemberList = new ArrayList<>();
            MemberList.add(getContext().getResources().getString(R.string.camera));
            MemberList.add(getContext().getResources().getString(R.string.gallery));
            builder.setTitle(R.string.choose).setIcon(R.drawable.logo);
            builder.setItems(MemberList.toArray(new String[0]), (dialogInterface, i) -> {
                if (i==0){
                    if (permissionsAreGranted()){
                        mViewModel.pickImageFromCamera();}
                }
                else {
                    if (permissionsAreGranted()){
                        mViewModel.pickImageFromGallery();
                    }}
            }).show();
        });
        constructionFragmentBinding.dateBtn.setOnClickListener(view -> showDatePickerDialog());
        constructionFragmentBinding.dateBtn.setOnFocusChangeListener((view, b) -> {
            if (b) {
                showDatePickerDialog();
            }
        });

        constructionFragmentBinding.timeBtn.setOnClickListener(view -> showTimePickerDialog());
        constructionFragmentBinding.timeBtn.setOnFocusChangeListener((view, b) -> {
            if (b) {
                showTimePickerDialog();
            }
        });
    }
    /**
     * show up date picker dialog
     */
    private void showDatePickerDialog() {
        DatePickerFragment pickerFragment = new DatePickerFragment(mViewModel, getContext(), true);
        pickerFragment.show(getActivity().getSupportFragmentManager(), "date_picker");
    }

    public void noOrder() {
        final Dialog dialog = new Dialog(getContext());
        dialog.setCancelable(false);
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
        }
        dialog.setContentView(R.layout.worker_dialog);
        Button cancel;
        cancel = dialog.findViewById(R.id.okDialog);
        cancel.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }


    /**
     * show up date picker dialog
     */
    private void showTimePickerDialog() {
        TimePickerFragment pickerFragment = new TimePickerFragment(this, getContext(), false);
        pickerFragment.show(getActivity().getSupportFragmentManager(), "Time_picker");
    }


    @Override
    public void onTimeSetDone(String timeText) {
        constructionFragmentBinding.timeBtn.setText(timeText);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean permissionsAreGranted() {
        if (getContext().checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                || getContext().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || getContext().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PICTURE_RC);
            return false;

        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mViewModel.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onMultiImageSelected(List<Uri> uriList, String tag) {
        mViewModel.onMultiImageSelected(uriList, "makeOrder");
    }

    @Override
    public void onCancelled(boolean isMultiSelecting, String tag) {
        mViewModel.onCancelled(isMultiSelecting, "makeOrder");
    }

    @Override
    public void onSingleImageSelected(Uri uri, String tag) {
        mViewModel.onSingleImageSelected(uri, "makeOrder");
    }

    @Override
    public void loadImage(Uri imageUri, ImageView ivImage) {
        Glide.with(this).load(imageUri).into(ivImage);
    }

    @Override
    public void sendInput(String lat, String lang, String address) {
        mViewModel.sendInput(lat, lang, address);
    }

}