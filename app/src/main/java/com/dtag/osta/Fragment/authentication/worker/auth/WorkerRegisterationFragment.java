package com.dtag.osta.Fragment.authentication.worker.auth;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.asksira.bsimagepicker.BSImagePicker;
import com.bumptech.glide.Glide;
import com.dtag.osta.Activity.MainActivity;
import com.dtag.osta.Fragment.ViewModel.authentication.user.auth.RegisterationViewModel;
import com.dtag.osta.Fragment.ViewModel.authentication.worker.auth.WorkerRegisterationViewModel;
import com.dtag.osta.Fragment.maps.MapFragment;
import com.dtag.osta.R;
import com.dtag.osta.databinding.WorkerRegisterationFragmentBinding;
import com.dtag.osta.network.Interface.Api;
import com.dtag.osta.network.Interface.OnInputSelected;
import com.dtag.osta.network.ResponseModel.Model.agent.AgentRegisterModel;
import com.dtag.osta.network.ResponseModel.Model.common.LoginRequest;
import com.dtag.osta.network.ResponseModel.wrapper.ApiResponse;
import com.dtag.osta.network.ResponseModel.wrapper.RetrofitClient;
import com.dtag.osta.network.ResponseModel.wrapper.SetToken;
import com.dtag.osta.utility.DatePickerFragment;
import com.dtag.osta.utility.DatePickerRegisterationFragment;
import com.dtag.osta.utility.Sal7haSharedPreference;
import com.dtag.osta.utility.Utility;
import com.github.vipulasri.timelineview.TimelineView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.rilixtech.widget.countrycodepicker.CountryCodePicker;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class WorkerRegisterationFragment extends Fragment implements
        OnInputSelected,
        BSImagePicker.OnSingleImageSelectedListener,
        BSImagePicker.OnMultiImageSelectedListener,
        BSImagePicker.ImageLoaderDelegate,
        BSImagePicker.OnSelectImageCancelledListener,
        DatePickerFragment.OnDatePicked, DatePickerRegisterationFragment.OnDatePicked {

    private WorkerRegisterationViewModel mViewModel;
    WorkerRegisterationFragmentBinding workerRegisterationFragmentBinding;
    private Api apiInterface = RetrofitClient.getInstance().getApi();
    private static final int PICTURE_RC = 1001;
    private static final int PICK_RC = 1004;
    LoginRequest loginRequest = new LoginRequest();
    SetToken setToken = new SetToken();
    private static final int PICK_IMAGE_RC = 1002;
    private static final int TAKE_PHOTO_RC = 1003;
    int PLACE_PICKER_REQUEST = 1;
    MultipartBody.Part profile_instructor_imageList;
    List<MultipartBody.Part> imagesEncodedList1 = new ArrayList<>();
    List<MultipartBody.Part> imagesEncodedList2 = new ArrayList<>();
    CountryCodePicker ccp;

    private int imagePickerChoice, insertImage;
    // MultipartBody.Part profile_instructor_image, add_attach_image, add_doc_image;
    AgentRegisterModel workerRegistration;
    //image list to upload
    String imageEncoded;
    MapFragment dialog = new MapFragment();

    public static WorkerRegisterationFragment newInstance() {
        return new WorkerRegisterationFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        workerRegisterationFragmentBinding = WorkerRegisterationFragmentBinding.inflate(inflater, container, false);

        return workerRegisterationFragmentBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(WorkerRegisterationViewModel.class);
        mViewModel.Init(workerRegisterationFragmentBinding, getContext());
        ccp = (CountryCodePicker) workerRegisterationFragmentBinding.getRoot().findViewById(R.id.ccp);

//        workerRegisterationFragmentBinding.instructorEditPicture.setOnClickListener(view -> {
//            if (hasStoragePermission()) {
//                insertImage = 100;
//                pickupImage();
//            }
//        });

//        workerRegisterationFragmentBinding.informationPasswordId.setOnClickListener(view -> {
//            final Dialog dialog = new Dialog(getContext());
//            dialog.setCancelable(false);
//            if (dialog != null) {
//                int width = ViewGroup.LayoutParams.FILL_PARENT;
//                int height = ViewGroup.LayoutParams.MATCH_PARENT;
//                dialog.getWindow().setLayout(width, height);
//            }
//            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//            dialog.setContentView(R.layout.password_helper_dialog);
//            dialog.show();
//            Button done;
//            done = dialog.findViewById(R.id.doneInformationId);
//            done.setOnClickListener(view12 -> dialog.dismiss());
//        });

//        workerRegisterationFragmentBinding.brithdayId.setOnClickListener(view -> {
//            showDatePickerDialog();
//            //     Toast.makeText(getContext(), Sal7haSharedPreference.getAgentImage(getContext()), Toast.LENGTH_SHORT).show();
//        });

//        workerRegisterationFragmentBinding.brithdayId.setOnFocusChangeListener((view, b) -> {
//            if (b) {
//                showDatePickerDialog();
//            }
//        });
        workerRegisterationFragmentBinding.addDocumentsDoc.setOnClickListener(view -> {
            if (permissionsAreGranted()) {
                pickImageFromGalleryForDoc();

            }
        });
//        workerRegisterationFragmentBinding.addAttachDoc.setOnClickListener(view -> {
//            if (permissionsAreGranted()) {
//                pickImageFromGalleryForAttach();
//            }
//        });

        workerRegisterationFragmentBinding.location.setOnClickListener(view -> {
            dialog.setmOnInputSelected(this);
            dialog.setTargetFragment(WorkerRegisterationFragment.this, 1);
            dialog.show(getFragmentManager(), "MyCustomDialog");
        });
        if (Sal7haSharedPreference.getSelectedLanguage(getContext()) == 0) {
            workerRegisterationFragmentBinding.firstname.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if (workerRegisterationFragmentBinding.firstname.getText().toString().length() >= 1) {
                        workerRegisterationFragmentBinding.firstname.setCompoundDrawablesWithIntrinsicBounds(null, null, getContext().getResources().getDrawable(R.drawable.done), null);
                    } else {
                        workerRegisterationFragmentBinding.firstname.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                    }

                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
            workerRegisterationFragmentBinding.secondname.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if (workerRegisterationFragmentBinding.secondname.getText().toString().length() >= 1) {
                        workerRegisterationFragmentBinding.secondname.setCompoundDrawablesWithIntrinsicBounds(null, null, getContext().getResources().getDrawable(R.drawable.done), null);
                    } else {
                        workerRegisterationFragmentBinding.secondname.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                    }


                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
//            workerRegisterationFragmentBinding.nationalId.addTextChangedListener(new TextWatcher() {
//                @Override
//                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//                }
//
//                @Override
//                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                    if (workerRegisterationFragmentBinding.nationalId.getText().toString().length() >= 14) {
//                        workerRegisterationFragmentBinding.nationalId.setCompoundDrawablesWithIntrinsicBounds(null, null, getContext().getResources().getDrawable(R.drawable.done), null);
//                    } else {
//                        workerRegisterationFragmentBinding.nationalId.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
//                    }
//
//
//                }
//
//                @Override
//                public void afterTextChanged(Editable editable) {
//
//                }
//            });
            workerRegisterationFragmentBinding.secondname.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if (workerRegisterationFragmentBinding.secondname.getText().toString().length() >= 1) {
                        workerRegisterationFragmentBinding.secondname.setCompoundDrawablesWithIntrinsicBounds(null, null, getContext().getResources().getDrawable(R.drawable.done), null);
                    } else {
                        workerRegisterationFragmentBinding.secondname.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                    }


                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
            workerRegisterationFragmentBinding.phoneId.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if (phoneValid()) {
                        workerRegisterationFragmentBinding.phoneId.setCompoundDrawablesWithIntrinsicBounds(null, null, getContext().getResources().getDrawable(R.drawable.done), null);
                    } else {
                        workerRegisterationFragmentBinding.phoneId.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                    }


                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
            workerRegisterationFragmentBinding.passwordId.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    if (passwordStrongValid()) {
                        workerRegisterationFragmentBinding.passwordId.setCompoundDrawablesWithIntrinsicBounds(null, null, getContext().getResources().getDrawable(R.drawable.done), null);
                    } else {
                        workerRegisterationFragmentBinding.passwordId.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                    }

                }
            });
            workerRegisterationFragmentBinding.confirmId.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    if (passwordsIsValid()) {
                        workerRegisterationFragmentBinding.passwordId.setCompoundDrawablesWithIntrinsicBounds(null, null, getContext().getResources().getDrawable(R.drawable.done), null);
                        workerRegisterationFragmentBinding.confirmId.setCompoundDrawablesWithIntrinsicBounds(null, null, getContext().getResources().getDrawable(R.drawable.done), null);
                    } else {
                        workerRegisterationFragmentBinding.passwordId.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                        workerRegisterationFragmentBinding.confirmId.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                    }

                }
            });
//            workerRegisterationFragmentBinding.lat.addTextChangedListener(new TextWatcher() {
//                @Override
//                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//                }
//
//                @Override
//                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                    if (workerRegisterationFragmentBinding.lat.getText().toString().length() >= 1 && workerRegisterationFragmentBinding.lang.getText().toString().length() >= 1 && workerRegisterationFragmentBinding.lang.getText().toString().length() >= 1) {
//                        workerRegisterationFragmentBinding.lat.setCompoundDrawablesWithIntrinsicBounds(null, null, getContext().getResources().getDrawable(R.drawable.done), null);
//                        workerRegisterationFragmentBinding.lang.setCompoundDrawablesWithIntrinsicBounds(null, null, getContext().getResources().getDrawable(R.drawable.done), null);
//
//                    } else {
//                        workerRegisterationFragmentBinding.lat.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
//                        workerRegisterationFragmentBinding.lang.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
//                    }
//                }
//
//                @Override
//                public void afterTextChanged(Editable editable) {
//
//                }
//            });
//            workerRegisterationFragmentBinding.brithdayId.addTextChangedListener(new TextWatcher() {
//                @Override
//                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//                }
//
//                @Override
//                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//                }
//
//                @Override
//                public void afterTextChanged(Editable editable) {
//                    if (workerRegisterationFragmentBinding.brithdayId.getText().toString().length() > 1) {
//                        workerRegisterationFragmentBinding.brithdayId.setCompoundDrawablesWithIntrinsicBounds(null, null, getContext().getResources().getDrawable(R.drawable.done), null);
//                    } else {
//                        workerRegisterationFragmentBinding.brithdayId.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
//                    }
//
//                }
//            });
        } else {
            workerRegisterationFragmentBinding.firstname.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if (workerRegisterationFragmentBinding.firstname.getText().toString().length() >= 1) {
                        workerRegisterationFragmentBinding.firstname.setCompoundDrawablesWithIntrinsicBounds(getContext().getResources().getDrawable(R.drawable.done), null, null, null);
                    } else {
                        workerRegisterationFragmentBinding.firstname.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                    }

                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
            workerRegisterationFragmentBinding.secondname.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if (workerRegisterationFragmentBinding.secondname.getText().toString().length() >= 1) {
                        workerRegisterationFragmentBinding.secondname.setCompoundDrawablesWithIntrinsicBounds(getContext().getResources().getDrawable(R.drawable.done), null, null, null);
                    } else {
                        workerRegisterationFragmentBinding.secondname.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                    }


                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
//            workerRegisterationFragmentBinding.nationalId.addTextChangedListener(new TextWatcher() {
//                @Override
//                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//                }
//
//                @Override
//                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                    if (workerRegisterationFragmentBinding.nationalId.getText().toString().length() >= 14) {
//                        workerRegisterationFragmentBinding.nationalId.setCompoundDrawablesWithIntrinsicBounds(getContext().getResources().getDrawable(R.drawable.done), null, null, null);
//                    } else {
//                        workerRegisterationFragmentBinding.nationalId.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
//                    }
//
//
//                }
//
//                @Override
//                public void afterTextChanged(Editable editable) {
//
//                }
//            });
            workerRegisterationFragmentBinding.secondname.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if (workerRegisterationFragmentBinding.secondname.getText().toString().length() >= 1) {
                        workerRegisterationFragmentBinding.secondname.setCompoundDrawablesWithIntrinsicBounds(getContext().getResources().getDrawable(R.drawable.done), null, null, null);
                    } else {
                        workerRegisterationFragmentBinding.secondname.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                    }


                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
            workerRegisterationFragmentBinding.phoneId.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if (workerRegisterationFragmentBinding.phoneId.getText().toString().length() >= 11) {
                        workerRegisterationFragmentBinding.phoneId.setCompoundDrawablesWithIntrinsicBounds(getContext().getResources().getDrawable(R.drawable.done), null, null, null);
                    } else {
                        workerRegisterationFragmentBinding.phoneId.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                    }


                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
            workerRegisterationFragmentBinding.passwordId.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    if (passwordStrongValid()) {
                        workerRegisterationFragmentBinding.passwordId.setCompoundDrawablesWithIntrinsicBounds(getContext().getResources().getDrawable(R.drawable.done), null, null, null);
                    } else {
                        workerRegisterationFragmentBinding.passwordId.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                    }

                }
            });
            workerRegisterationFragmentBinding.confirmId.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    if (passwordsIsValid()) {
                        workerRegisterationFragmentBinding.passwordId.setCompoundDrawablesWithIntrinsicBounds(getContext().getResources().getDrawable(R.drawable.done), null, null, null);
                        workerRegisterationFragmentBinding.confirmId.setCompoundDrawablesWithIntrinsicBounds(getContext().getResources().getDrawable(R.drawable.done), null, null, null);
                    } else {
                        workerRegisterationFragmentBinding.passwordId.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                        workerRegisterationFragmentBinding.confirmId.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                    }

                }
            });
//            workerRegisterationFragmentBinding.lat.addTextChangedListener(new TextWatcher() {
//                @Override
//                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//                }
//
//                @Override
//                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                    if (workerRegisterationFragmentBinding.lat.getText().toString().length() >= 1 && workerRegisterationFragmentBinding.lang.getText().toString().length() >= 1 && workerRegisterationFragmentBinding.lang.getText().toString().length() >= 1) {
//                        workerRegisterationFragmentBinding.lat.setCompoundDrawablesWithIntrinsicBounds(getContext().getResources().getDrawable(R.drawable.done), null, null, null);
//                        workerRegisterationFragmentBinding.lang.setCompoundDrawablesWithIntrinsicBounds(getContext().getResources().getDrawable(R.drawable.done), null, null, null);
//
//                    } else {
//                        workerRegisterationFragmentBinding.lat.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
//                        workerRegisterationFragmentBinding.lang.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
//                    }
//                }
//
//                @Override
//                public void afterTextChanged(Editable editable) {
//
//                }
//            });
//            workerRegisterationFragmentBinding.brithdayId.addTextChangedListener(new TextWatcher() {
//                @Override
//                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//                }
//
//                @Override
//                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//                }
//
//                @Override
//                public void afterTextChanged(Editable editable) {
//                    if (workerRegisterationFragmentBinding.brithdayId.getText().toString().length() > 1) {
//                        workerRegisterationFragmentBinding.brithdayId.setCompoundDrawablesWithIntrinsicBounds(getContext().getResources().getDrawable(R.drawable.done), null, null, null);
//                    } else {
//                        workerRegisterationFragmentBinding.brithdayId.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
//                    }
//
//                }
//            });

        }

        workerRegisterationFragmentBinding.checkbox.setOnClickListener(view -> {
            if (workerRegisterationFragmentBinding.checkbox.isChecked()) {
                workerRegisterationFragmentBinding.saveWorker.setEnabled(true);
                workerRegisterationFragmentBinding.saveWorker.setOnClickListener(view1 -> {
                    if (inputValid()) {
                        workerRegisteration(view);

                    }
                });
            } else {
                workerRegisterationFragmentBinding.saveWorker.setEnabled(false);
            }
        });

    }

    /**
     * show up date picker dialog
     */
    private void showDatePickerDialog() {
        //TODO Replace `(getActivity().getSupportFragmentManager()` by getChildFragmentManager() to invoke fragment method
        DatePickerRegisterationFragment pickerFragment = new DatePickerRegisterationFragment(this, getContext(), false);
        pickerFragment.show(getActivity().getSupportFragmentManager(), "date_picker");
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private Boolean hasStoragePermission() {
        if (getContext().checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                || getContext().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || getContext().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PICTURE_RC);
            return false;
        }
        return true;
    }

    private void pickupImage() {
//        MaterialAlertDialogBuilder askForImage = new MaterialAlertDialogBuilder(getContext());
//        askForImage.setTitle("أختر مصدر الصورة")
//                .setSingleChoiceItems(new String[]{"التقط صورة بإستخدام الكاميرا", "اختر صورة من معرض الصور"}, 0, (dialogInterface, i) -> {
//                    imagePickerChoice = i;
//                }).setPositiveButton("إختر", (dialogInterface, i) -> {
//            if (imagePickerChoice == 0) {
//                takePhoto();
//            } else {
        pickImageFromGalleryProfile();
        // Glide.with(this).load(Sal7haSharedPreference.getAgentImage(getContext())).into(workerRegisterationFragmentBinding.profileInstructorId);
//            }

//        }).setNeutralButton("الغاء", null).show();
    }

    private void takePhoto() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, TAKE_PHOTO_RC);
    }

    public void pickImageFromGalleryForAttach() {
        BSImagePicker pickerDialog = new BSImagePicker.Builder("com.example.dalia.sal7ha.fileprovider")
                .setMaximumDisplayingImages(Integer.MAX_VALUE)
                .isMultiSelect()
                .setTag("nationalId")
                .setMinimumMultiSelectCount(2)
                .setMaximumMultiSelectCount(2)
                .build();
        pickerDialog.show(getActivity().getSupportFragmentManager(), "picker");
    }

    public void pickImageFromGalleryForDoc() {
        BSImagePicker pickerDialog = new BSImagePicker.Builder("com.example.dalia.sal7ha.fileprovider")
                .setMaximumDisplayingImages(Integer.MAX_VALUE)
                .isMultiSelect()
                .setTag("documents")
                .setMinimumMultiSelectCount(1)
                .setMaximumMultiSelectCount(10)
                .build();
        pickerDialog.show(getActivity().getSupportFragmentManager(), "picker");

    }

    public void pickImageFromGalleryProfile() {
        BSImagePicker pickerDialog = new BSImagePicker.Builder("com.example.dalia.sal7ha.fileprovider")
                .setMaximumDisplayingImages(Integer.MAX_VALUE)
                .isMultiSelect()
                .setTag("profileImage")
                .useFrontCamera()
                .setMinimumMultiSelectCount(1)
                .setMaximumMultiSelectCount(1)
                .build();
        pickerDialog.show(((AppCompatActivity) getContext()).getSupportFragmentManager(), "picker");
    }


   /* @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case TAKE_PHOTO_RC:
                if (resultCode == Activity.RESULT_OK) {
                    Bitmap bmp = (Bitmap) data.getExtras().get("data");
                    Log.i(TAG, "onActivityResult: capture image url: " + bmp + "");

                    File file = saveBitmap(bmp);
                    Log.i(TAG, "onActivityResult: image : " + file + "");
                    if (insertImage == 100) {
                        workerRegisterationFragmentBinding.profileInstructorId.setImageBitmap(bmp);
                        RequestBody fileReqBody = RequestBody.create(MediaType.parse("image/*"), file);
                        MultipartBody.Part part = MultipartBody.Part.createFormData("image", file.getName(), fileReqBody);
                        profile_instructor_image = part;
                    } else if (insertImage == 200) {
//                        workerRegisterationFragmentBinding.attachImage.setImageBitmap(bmp);
                        RequestBody fileReqBody = RequestBody.create(MediaType.parse("image/*"), file);
                        MultipartBody.Part part = MultipartBody.Part.createFormData("national_id", file.getName(), fileReqBody);
                        add_attach_image = part;
                    }

                    //   Toast.makeText(getContext(), "Image file:" + profile_instructor_image.body().toString(), Toast.LENGTH_SHORT).show();
                }
                break;
            case PICK_IMAGE_RC:
                if (resultCode == Activity.RESULT_OK) {
                    Uri uri_image = data.getData();
                    File file = new File(Utility.getRealPathFromURI(getContext(), uri_image));

                    Log.i(TAG, "uri :" + Utility.getRealPathFromURI(getContext(), uri_image));

                    if (insertImage == 100) {
                        Picasso.get().load(uri_image).fit().centerInside().rotate(360.0f).into((workerRegisterationFragmentBinding.profileInstructorId));
                        RequestBody fileReqBody = RequestBody.create(MediaType.parse("image/*"), file);
                        MultipartBody.Part part = MultipartBody.Part.createFormData("image", file.getName(), fileReqBody);
                        profile_instructor_image = part;
                    } else if (insertImage == 200) {
                        //  Picasso.get().load(uri_image).fit().centerInside().rotate(360.0f).into((workerRegisterationFragmentBinding.attachImage));
                        RequestBody fileReqBody = RequestBody.create(MediaType.parse("image/*"), file);
                        MultipartBody.Part part = MultipartBody.Part.createFormData("national_id", file.getName(), fileReqBody);
                        add_attach_image = part;
                    }
                }
                break;
        }
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, getContext());
                String latitude = String.valueOf(place.getLatLng().latitude);
                String longitude = String.valueOf(place.getLatLng().longitude);
                workerRegisterationFragmentBinding.lat.setText(latitude);
                workerRegisterationFragmentBinding.lang.setText(longitude);

            }
        }
    }*/

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


    private String getImagePath(Uri uri) {
        Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null, null);
        cursor.moveToNext();
        String documentId = cursor.getString(0);

        documentId = documentId.substring(documentId.lastIndexOf(":") + 1);
        cursor.close();

        cursor = getActivity().getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{documentId}, null, null
        );
        cursor.moveToNext();
        String imagePath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();
        return imagePath;
    }

    private File saveBitmap(Bitmap bmp) {
        String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
        OutputStream outputStream = null;
        File file = new File(extStorageDirectory, "temp.png");
        if (file.exists()) {
            file.delete();
            file = new File(extStorageDirectory, "temp.png");
        }
        Log.i(TAG, "file:" + file);
        try {
            outputStream = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    private ArrayList findUnAskedPermissions(ArrayList wanted) {
        ArrayList result = new ArrayList();

        for (Object perm : wanted) {
            if (!hasPermission((String) perm)) {
                result.add(perm);
            }
        }

        return result;
    }

    private boolean hasPermission(String permission) {
        if (canMakeSmores()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return (getContext().checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
            }
        }
        return true;
    }

    private boolean canMakeSmores() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(getContext())
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNeutralButton("Cancel", null)
                .create()
                .show();
    }

    private boolean confirmPasswordStrongValid() {
        String name = workerRegisterationFragmentBinding.confirmId.getText().toString();
        if (name.isEmpty()) {
            workerRegisterationFragmentBinding.confirmId.setError(getContext().getResources().getString(R.string.enterpassword));
            workerRegisterationFragmentBinding.confirmId.requestFocus();
            return false;
        } else {
            return true;
        }
    }

    private boolean passwordStrongValid() {
        String passwordhere = workerRegisterationFragmentBinding.passwordId.getText().toString();
        List<String> errorList = new ArrayList<String>();
        if (!isValidPassword(passwordhere, errorList)) {
            //  System.out.println("The password entered here  is invalid");
            workerRegisterationFragmentBinding.passwordId.setError(getContext().getResources().getString(R.string.passwordmustbemorethan6characters));
            workerRegisterationFragmentBinding.passwordId.requestFocus();
        } else {
            return true;
        }
        return false;
    }

    public static boolean isValidPassword(String passwordhere, List<String> errorList) {
        Pattern specailCharPatten = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
        Pattern UpperCasePatten = Pattern.compile("[A-Z ]");
        Pattern lowerCasePatten = Pattern.compile("[a-z ]");
        Pattern digitCasePatten = Pattern.compile("[0-9 ]");
        boolean flag = true;

        if (passwordhere.length() < 8) {
            errorList.add("Password lenght must have alleast 8 character !!");
            flag = false;
        }
        if (!specailCharPatten.matcher(passwordhere).find()) {
            errorList.add("Password must have atleast one specail character !!");
            flag = false;
        }
        if (!UpperCasePatten.matcher(passwordhere).find()) {
            errorList.add("Password must have atleast one uppercase character !!");
            flag = false;
        }
        if (!lowerCasePatten.matcher(passwordhere).find()) {
            errorList.add("Password must have atleast one lowercase character !!");
            flag = false;
        }
        if (!digitCasePatten.matcher(passwordhere).find()) {
            errorList.add("Password must have atleast one digit character !!");
            flag = false;
        }
        return flag;

    }


    private boolean passwordsIsValid() {
        String firstPass = workerRegisterationFragmentBinding.passwordId.getText().toString();
        String confirmPass = workerRegisterationFragmentBinding.confirmId.getText().toString();
        if (!firstPass.equals(confirmPass)) {
            Toast.makeText(getContext(), getContext().getResources().getString(R.string.passwordsdoesntmatch), Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

  /*  private boolean emailValid() {
        String email = workerRegisterationFragmentBinding.emailId.getText().toString();
        if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return true;
        } else {
            workerRegisterationFragmentBinding.emailId.setError(getContext().getResources().getString(R.string.entervalidemail));
            workerRegisterationFragmentBinding.emailId.requestFocus();
            return false;
        }
    }*/

    private boolean firstNameValid() {
        String name = workerRegisterationFragmentBinding.firstname.getText().toString();
        if (!name.isEmpty()) {
            return true;
        }
        workerRegisterationFragmentBinding.firstname.setError(getContext().getResources().getString(R.string.enterurname));
        workerRegisterationFragmentBinding.firstname.requestFocus();
        return false;
    }

    private boolean secondNameValid() {
        String name = workerRegisterationFragmentBinding.secondname.getText().toString();
        if (!name.isEmpty()) {
            return true;
        }
        workerRegisterationFragmentBinding.secondname.setError(getContext().getResources().getString(R.string.entersecondname));
        workerRegisterationFragmentBinding.secondname.requestFocus();
        return false;
    }

    private boolean lastNameValid() {
        String name = workerRegisterationFragmentBinding.secondname.getText().toString();
        if (!name.isEmpty()) {
            return true;
        }
        workerRegisterationFragmentBinding.secondname.setError(getContext().getResources().getString(R.string.enterlastname));
        workerRegisterationFragmentBinding.secondname.requestFocus();
        return false;
    }

    private boolean phoneValid() {
        String phone = workerRegisterationFragmentBinding.phoneId.getText().toString();
        if (!phone.isEmpty() && phone.length() <= 12 && phone.length() >= 4) {
            return true;
        }
        workerRegisterationFragmentBinding.phoneId.setError(getContext().getResources().getString(R.string.registerationphonenumber));
        workerRegisterationFragmentBinding.phoneId.requestFocus();
        return false;
    }


//    private boolean nationalValid() {
//        String national = workerRegisterationFragmentBinding.nationalId.getText().toString();
//        if (!national.isEmpty()) {
//            return true;
//        }
//        workerRegisterationFragmentBinding.nationalId.setError(getContext().getResources().getString(R.string.nationalnumberregistration));
//        workerRegisterationFragmentBinding.nationalId.requestFocus();
//        return false;
//    }
//
//    private boolean locationValid() {
//        if (workerRegisterationFragmentBinding.lat.getText().equals("0.0") || workerRegisterationFragmentBinding.lat.getText().length() == 0) {
//            workerRegisterationFragmentBinding.lat.setError(getContext().getResources().getString(R.string.currentlocation));
//            workerRegisterationFragmentBinding.lat.requestFocus();
//            return false;
//        }
//        return true;
//    }

    private boolean inputValid() {
        return firstNameValid() && secondNameValid() && lastNameValid() && phoneValid() && passwordStrongValid() && confirmPasswordStrongValid() && passwordsIsValid() ;
    }

//    private boolean birthdayValid() {
//
//        String brith = workerRegisterationFragmentBinding.brithdayId.getText().toString();
//        if (!brith.isEmpty()) {
//            return true;
//        }
//        workerRegisterationFragmentBinding.brithdayId.setError(getContext().getResources().getString(R.string.brithday));
//        workerRegisterationFragmentBinding.brithdayId.requestFocus();
//        return false;
//    }

  /*  private boolean pictureValid() {
        if (profile_instructor_image == null) {
            Toast.makeText(getContext(), R.string.imageValid, Toast.LENGTH_SHORT).show();
            workerRegisterationFragmentBinding.profileInstructorId.requestFocus();
            return false;
        } else if (add_attach_image == null) {
            Toast.makeText(getContext(), R.string.imageValid, Toast.LENGTH_SHORT).show();
            // workerRegisterationFragmentBinding.attachImage.requestFocus();
            return false;
        }

        return true;
    }*/

    private void workerRegisteration(View v) {
        //Toast.makeText(getContext(), "Success Validation ", Toast.LENGTH_LONG).show();
        workerRegisterationFragmentBinding.progress.setVisibility(View.VISIBLE);
        workerRegistration = new AgentRegisterModel();
        workerRegistration.setCode(getArguments().getString("code"));
        workerRegistration.setName(workerRegisterationFragmentBinding.firstname.getText().toString() + " " + workerRegisterationFragmentBinding.secondname.getText().toString() + " " + workerRegisterationFragmentBinding.secondname.getText().toString());
        //  workerRegistration.setEmail(workerRegisterationFragmentBinding.emailId.getText().toString());
        workerRegistration.setPassword(workerRegisterationFragmentBinding.passwordId.getText().toString());
        workerRegistration.setPassword_confirmation(workerRegisterationFragmentBinding.confirmId.getText().toString());
        workerRegistration.setPhone(workerRegisterationFragmentBinding.ccp.getSelectedCountryCodeWithPlus() + workerRegisterationFragmentBinding.phoneId.getText().toString());
//        workerRegistration.setBirthday(workerRegisterationFragmentBinding.brithdayId.getText().toString());
        workerRegistration.setCategoryId(mViewModel.category_id);
        workerRegistration.setCityId(mViewModel.City_areas_id);
//        workerRegistration.setLatitude(workerRegisterationFragmentBinding.lat.getText().toString());
//        workerRegistration.setLongitude(workerRegisterationFragmentBinding.lang.getText().toString());
        workerRegistration.setImage(profile_instructor_imageList);
//        workerRegistration.setNational_number(workerRegisterationFragmentBinding.nationalId.getText().toString());
        apiInterface.agentRegister(
                RequestBody.create(okhttp3.MediaType.parse("text/plain"), workerRegistration.getCode()),
                RequestBody.create(okhttp3.MediaType.parse("text/plain"), workerRegistration.getName()),
                RequestBody.create(okhttp3.MediaType.parse("text/plain"), workerRegistration.getPhone()),
                //  RequestBody.create(okhttp3.MediaType.parse("text/plain"), workerRegistration.getEmail()),
                RequestBody.create(okhttp3.MediaType.parse("text/plain"), workerRegistration.getPassword()),
                RequestBody.create(okhttp3.MediaType.parse("text/plain"), workerRegistration.getPassword_confirmation()),
//                RequestBody.create(okhttp3.MediaType.parse("text/plain"), workerRegisterationFragmentBinding.nationalId.getText().toString()),
                RequestBody.create(okhttp3.MediaType.parse("text/plain"), workerRegistration.getBirthday()),
                RequestBody.create(okhttp3.MediaType.parse("text/plain"), String.valueOf(mViewModel.category_id)),
                RequestBody.create(okhttp3.MediaType.parse("text/plain"), String.valueOf(workerRegistration.getCityId())),
                RequestBody.create(okhttp3.MediaType.parse("text/plain"), String.valueOf(workerRegistration.getLongitude())),
                RequestBody.create(okhttp3.MediaType.parse("text/plain"), String.valueOf(workerRegistration.getLatitude())),
//.                imagesEncodedList1,
                imagesEncodedList2,
                profile_instructor_imageList
        ).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.body() != null && response.isSuccessful()) {
                    if (response.body().getStatus()) {
                        //TODO JFN
                        Log.i(TAG, "TOKENEH" + response.body().getData().getAgent().getToken());
                        getDeviceToken(response.body().getData().getAgent().getToken());
                        Log.i(TAG, "token" + response.body().getData().getAgent().getToken());
                        workerRegisterationFragmentBinding.progress.setVisibility(View.GONE);
                        Toast.makeText(getContext(), R.string.successregisteration, Toast.LENGTH_SHORT).show();
                        final Dialog dialog = new Dialog(getContext());
                        dialog.setCancelable(false);
                        if (dialog != null) {
                            int width = ViewGroup.LayoutParams.MATCH_PARENT;
                            int height = ViewGroup.LayoutParams.MATCH_PARENT;
                            dialog.getWindow().setLayout(width, height);
                            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                        }
                        dialog.setContentView(R.layout.unactive_dialog);
                        Button cancel;
                        TextView text;
                        text = dialog.findViewById(R.id.textchange);
                        text.setText(R.string.loginactive);
                        cancel = dialog.findViewById(R.id.okDialog);
                        cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                                Navigation.findNavController(workerRegisterationFragmentBinding.getRoot()).navigate(R.id.workerSignInFragment);

                            }
                        });
                        dialog.show();
                    } else {
                        StringBuilder builder = new StringBuilder();
                        workerRegisterationFragmentBinding.progress.setVisibility(View.GONE);
                        if (response.body().getErrors().getPhone() != null) {
                            String[] email = new String[]{response.body().getErrors().getPhone().toString()};
                            for (String i : email) {
                                builder.append("" + i + " ");
                            }
//                            Toast.makeText(getContext(), builder, Toast.LENGTH_LONG).show();
                        } else if (response.body().getErrors().getCode() != null) {
                            String[] code = new String[]{response.body().getErrors().getCode().toString()};
                            for (String i : code) {
                                builder.append("" + i + " ");
                            }
//                            Toast.makeText(getContext(), builder1, Toast.LENGTH_LONG).show();
                        } else if (response.body().getErrors().getBirthday() != null) {
                            String[] code = new String[]{response.body().getErrors().getBirthday().toString()};
                            for (String i : code) {
                                builder.append("" + i + " ");
                            }
                            //  Toast.makeText(getContext(), builder1, Toast.LENGTH_LONG).show();
                        } else if (response.body().getErrors().getImage() != null) {
                            String[] code = new String[]{response.body().getErrors().getImage().toString()};
                            for (String i : code) {
                                builder.append("" + i + " ");
                            }
                        } else if (response.body().getErrors().getDocuments_images() != null) {
                            String[] code = new String[]{response.body().getErrors().getDocuments_images().toString()};
                            for (String i : code) {
                                builder.append("" + i + " ");
                            }

                        } else if (response.body().getErrors().getNational_id() != null) {
                            String[] code = new String[]{response.body().getErrors().getNational_id().toString()};
                            for (String i : code) {
                                builder.append("" + i + " ");
                            }

                        }
                        Toast.makeText(getContext(), builder, Toast.LENGTH_LONG).show();
//                        Log.i(TAG, "MYDATA" + workerRegistration.getImage() + "//" + workerRegistration.getCode() + "//" + workerRegistration.getName() + "//" + workerRegistration.getPhone() + "//" + workerRegistration.getPassword() + "//" + workerRegistration.getPassword_confirmation() + "//" + workerRegistration.getNational_number() + "//" + workerRegistration.getCategoryId() + "//" + workerRegistration.getBirthday() + "//" + workerRegistration.getCityId() + "//" + workerRegistration.getLatitude() + "//" + workerRegistration.getLongitude() + "//" + workerRegistration.getDocuments_images() + "//" + workerRegistration.getNational_id());

//                        Log.e(TAG,response.body().getErrors().toString());

                        //  Toast.makeText(getContext(), workerRegistration.getImage() + "//" + workerRegistration.getCode() + "//" + workerRegistration.getName() + "//" + workerRegistration.getPhone() + "//" + workerRegistration.getPassword() + "//" + workerRegistration.getPassword_confirmation() + "//" + workerRegistration.getNational_number() + "//" + workerRegistration.getCategoryId() + "//" + workerRegistration.getBirthday() + "//" + workerRegistration.getCityId() + "//" + workerRegistration.getLatitude() + "//" + workerRegistration.getLongitude() + "//" + workerRegistration.getDocuments_images() + "//" + workerRegistration.getNational_id(), Toast.LENGTH_SHORT).show();
                        // Toast.makeText(getContext(), "failed", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                workerRegisterationFragmentBinding.progress.setVisibility(View.GONE);
                //   Toast.makeText(getContext(), t.toString(), Toast.LENGTH_SHORT).show();
                Log.i(TAG, "REGISTERATION" + t.toString());
                //   Toast.makeText(getContext(), R.string.internetconnection, Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void getDeviceToken(String userToken) {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w("TAG", "Fetching FCM registration token failed", task.getException());
                            return;
                        }
                        // Get new FCM registration token
                        String deviceToken = task.getResult();
                        setToken.setDevice_token(deviceToken);
                        setToken.setType("android");
                        apiInterface.UserSetToken(userToken, setToken).enqueue(new Callback<ApiResponse>() {
                            @Override
                            public void onResponse(@NonNull Call<ApiResponse> call, @NonNull Response<ApiResponse> response) {
                                if (response.body() != null && response.isSuccessful()) {
                                    if (response.body().getStatus()) {
                                        //  Toast.makeText(context, setToken.getDevice_token(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }

                            @Override
                            public void onFailure(@NotNull Call<ApiResponse> call, @NotNull Throwable t) {
                                Log.e(TAG, "onFailure: ", t);
                            }
                        });
                    }
                });

    }


    @Override
    public void onDateSetDone(String dateText) {
//        workerRegisterationFragmentBinding.brithdayId.setText(dateText);
    }

  /*  @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mViewModel.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }*/

    @Override
    public void onMultiImageSelected(List<Uri> uriList, String tag) {
        if (tag.equals("nationalId")) {
            imagesEncodedList1 = Utility.compressImageArray(getContext(), uriList, "national_id");
        } else if (tag.equals("profileImage")) {
//            Picasso.get().load(uriList.get(0)).placeholder(R.drawable.ic_account).into(workerRegisterationFragmentBinding.profileInstructorId);
            profile_instructor_imageList = Utility.compressImage(getContext(), uriList.get(0), "image");
        } else if (tag.equals("documents")) {
            imagesEncodedList2 = Utility.compressImageArray(getContext(), uriList, "documents_images");
        }
//        for (int i = 0; i < uriList.size(); i++) {
        // Glide.with(this).load(Sal7haSharedPreference.getAgentImage(getContext())).into(workerRegisterationFragmentBinding.profileInstructorId);
//            File file = new File(Utility.getRealPathFromURI(getContext(), uriList.get(i)));
//            RequestBody fileReqBody = RequestBody.create(MediaType.parse("image/*"), file);
//            MultipartBody.Part part = MultipartBody.Part.createFormData("image[" + i + "]", file.getName(), fileReqBody);
        // imagesEncodedList1.add(part);
//        }
    }

    @Override
    public void onCancelled(boolean isMultiSelecting, String tag) {
    }

    @Override
    public void onSingleImageSelected(Uri uri, String tag) {
        //  Glide.with(this).load(uri).into(workerRegisterationFragmentBinding.profileInstructorId);
        // Glide.with(getContext()).load(uri).into(workerRegisterationFragmentBinding.profileInstructorId);
        profile_instructor_imageList = null;
        imagesEncodedList1.clear();
        imagesEncodedList2.clear();
//        imagesEncodedList.add(Utility.compressImage(getActivity(), uri, "images[0]"));
//        File file = new File(Utility.getRealPathFromURI(getContext(), uri));
//        RequestBody fileReqBody = RequestBody.create(MediaType.parse("image/*"), file);
//        MultipartBody.Part part = MultipartBody.Part.createFormData("images[0]", file.getName(), fileReqBody);
//        imagesEncodedList1.add(part);
    }

    @Override
    public void loadImage(Uri imageUri, ImageView ivImage) {
        Glide.with(this).load(imageUri).into(ivImage);
    }

    @Override
    public void sendInput(String lat, String lang, String address) {
//        workerRegisterationFragmentBinding.lat.setText(lat);
//        workerRegisterationFragmentBinding.lang.setText(lang);
        //workerRegisterationFragmentBinding.add.setText(address);
    }
}
