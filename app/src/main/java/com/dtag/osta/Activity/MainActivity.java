package com.dtag.osta.Activity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.asksira.bsimagepicker.BSImagePicker;
import com.bumptech.glide.Glide;
import com.dtag.osta.Fragment.authentication.worker.auth.WorkerRegisterationFragment;
import com.dtag.osta.Fragment.main.user.makeOrder.RequestInfromationFragment;
import com.dtag.osta.Fragment.profile.ProfileFragment;
import com.dtag.osta.R;
import com.dtag.osta.databinding.ActivityMainBinding;
import com.dtag.osta.network.Interface.Api;
import com.dtag.osta.network.ResponseModel.wrapper.ApiResponse;
import com.dtag.osta.network.ResponseModel.wrapper.RetrofitClient;
import com.dtag.osta.utility.Sal7haSharedPreference;
import com.dtag.osta.utility.Utility;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.messaging.FirebaseMessaging;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity implements
        BSImagePicker.OnSingleImageSelectedListener,
        BSImagePicker.ImageLoaderDelegate,
        BSImagePicker.OnMultiImageSelectedListener,
        BSImagePicker.OnSelectImageCancelledListener,
        OnMapReadyCallback {
    NavController navController;
    private static final String TAG = "MainActivity";
    private Boolean checkLog = false;
    private Api apiInterface = RetrofitClient.getInstance().getApi();
    private FragmentTransaction fc;
    private Fragment fragment;
    private FragmentManager fm = getSupportFragmentManager();
    GoogleSignInOptions gso;
    GoogleSignInClient mGoogleSignInClient;
    private static int RC_SIGN_IN = 1000;
    private CallbackManager callbackManager;
    private LoginManager loginManager;
//    @Override
//    public void onBackPressed() {
//        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.main_container);
//        if (!(fragment instanceof IOnBackPressed) || !((IOnBackPressed) fragment).onBackPressed()) {
//            super.onBackPressed();
//        }
//    }

    ActivityMainBinding activityMainBinding;
    private DrawerLayout.DrawerListener drawerListener = new DrawerLayout.DrawerListener() {
        @Override
        public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
            activityMainBinding.mainContainer.setTranslationX(Sal7haSharedPreference.getSelectedLanguage(MainActivity.this)
                    == 0 ? (activityMainBinding.homeDrawer.getWidth() * slideOffset) : (-activityMainBinding.homeDrawer.getWidth() * slideOffset));
            activityMainBinding.mainContainer.setScaleX(1 - (slideOffset / 6));
            activityMainBinding.mainContainer.setScaleY(1 - (slideOffset / 6));
        }

        @Override
        public void onDrawerOpened(@NonNull View drawerView) {
        }

        @Override
        public void onDrawerClosed(@NonNull View drawerView) {

        }

        @Override
        public void onDrawerStateChanged(int newState) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Utility.updateResources(this);
        super.onCreate(savedInstanceState);
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        navController = Navigation.findNavController(this, R.id.home_nav_fragment);
        activityMainBinding.homeNavigationView.setItemIconTintList(null);
        NavigationUI.setupWithNavController(activityMainBinding.homeNavigationView, navController);
        activityMainBinding.homeDrawer.addDrawerListener(drawerListener);
        appSetting();
        NavigationView navigationView = findViewById(R.id.home_navigation_view);
//        ImageView setting = navigationView.getHeaderView(0).findViewById(R.id.settingId);
        //facebook
        printHashKey();
        FacebookSdk.sdkInitialize(MainActivity.this);
        callbackManager = CallbackManager.Factory.create();
        facebookLogin();
        /////
        String serverClientId = getString(R.string.server_client_id);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestIdToken((serverClientId))
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

        activityMainBinding.homeNavigationView.getMenu().findItem(R.id.settingFragment).setOnMenuItemClickListener(menuItem -> {
            navController.navigate(R.id.settingFragment);
            activityMainBinding.homeDrawer.closeDrawers();
            return false;
        });
        activityMainBinding.homeNavigationView.getMenu().findItem(R.id.signinfragmentt).setOnMenuItemClickListener(menuItem -> {
            if (Sal7haSharedPreference.isLoggedIn(MainActivity.this)) {
                activityMainBinding.homeDrawer.closeDrawers();
                navController.navigate(R.id.profileFragment);
            }
            return false;
        });
//TODO i commented firebase as has errors
        FirebaseMessaging.getInstance().subscribeToTopic("weather")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = getString(R.string.project_id);
                        if (!task.isSuccessful()) {
                            msg = getString(R.string.project_id);
                        }
                        Log.d(TAG, msg);
                        //        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });
//        Log.i(TAG, getIntent().getAction());
        if (Sal7haSharedPreference.isLoggedIn(this)) {
            if (getIntent().getAction() != null) {
                if (getIntent().getAction().equals("offer")) {
                    Log.i(TAG, "OFFER NOTIFICATIONS");
                    Bundle bundle = new Bundle();
                    int i = Integer.parseInt(getIntent().getStringExtra("item_id"));
                    bundle.putInt("id", i);
                    navController.navigate(R.id.salesDetailsFragment, bundle);
                } else if (getIntent().getAction().equals("order")) {
                    Log.i(TAG, "ORDER NOTIFICATIONS");
                    Bundle bundle = new Bundle();
                    int i = Integer.parseInt(getIntent().getStringExtra("item_id"));
                    Log.i(TAG, "MainActivityTAG" + getIntent().getStringExtra("item_id"));
                    bundle.putInt("order_id", i);
                    bundle.putString("body", getIntent().getStringExtra("body"));
                    bundle.putBoolean("isNotification", true);
                    navController.navigate(R.id.orderDetailsFragment, bundle);
                }
//                    else if (getIntent().getAction().equals("profile")) {
//                        Log.i(TAG, "activiation NOTIFICATIONS");
//                        Bundle bundle = new Bundle();
//                        bundle.putBoolean("isNotification", true);
//                        navController.navigate(R.id.workerSignInFragment,bundle);
//                    }
                else if (getIntent().getAction().equals("newRequest")) {
                    Log.i(TAG, "newRequest NOTIFICATIONS");
                    Bundle bundle = new Bundle();
                    int i = Integer.parseInt(getIntent().getStringExtra("item_id"));
                    Log.i(TAG, "MainActivityTAG" + getIntent().getStringExtra("item_id"));
                    bundle.putInt("order_id", i);
                    bundle.putBoolean("isNotification", true);
                    // bundle.putString("body",getIntent().getStringExtra("body"));
                    // bundle.putBoolean("isNotification",true);
                    navController.navigate(R.id.workerChartFragment, bundle);
                }
            } else {
            }

        } else {
            Log.i(TAG, "NO LOGIN");
        }

    }


    public void lockDrawer() {
        activityMainBinding.homeDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }

    public void openDrawer() {
        activityMainBinding.homeDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
    }

    public void showBottomMenu() {
        activityMainBinding.bottomId.setVisibility(View.VISIBLE);
    }

    public void hideBottomMenu() {
        activityMainBinding.bottomId.setVisibility(View.GONE);
    }

    @Override
    public void onMultiImageSelected(List<Uri> uriList, String tag) {
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.home_nav_fragment);
        if (Utility.fixNullString(tag).equals("makeOrder")) {
            RequestInfromationFragment requestInfromationFragment = (RequestInfromationFragment) navHostFragment.getChildFragmentManager().getFragments().get(0);
            requestInfromationFragment.onMultiImageSelected(uriList, tag);
        } else if (Utility.fixNullString(tag).equals("profile")) {
            ProfileFragment profileFragment = (ProfileFragment) navHostFragment.getChildFragmentManager().getFragments().get(0);
            profileFragment.onMultiImageSelected(uriList, tag);
        } else {
            WorkerRegisterationFragment workerRegisterationFragment = (WorkerRegisterationFragment) navHostFragment.getChildFragmentManager().getFragments().get(0);
            workerRegisterationFragment.onMultiImageSelected(uriList, tag);
            Sal7haSharedPreference.saveAgentImage(this, uriList.get(0).getPath());
        }
        //  Toast.makeText(this, R.string.donesuccessfully, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCancelled(boolean isMultiSelecting, String tag) {
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.home_nav_fragment);
        if (Utility.fixNullString(tag).equals("makeOrder")) {
            RequestInfromationFragment requestInfromationFragment = (RequestInfromationFragment) navHostFragment.getChildFragmentManager().getFragments().get(0);
            requestInfromationFragment.onCancelled(isMultiSelecting, tag);
        } else if (Utility.fixNullString(tag).equals("profile")) {
            ProfileFragment profileFragment = (ProfileFragment) navHostFragment.getChildFragmentManager().getFragments().get(0);
            profileFragment.onCancelled(isMultiSelecting, tag);
        } else {
            WorkerRegisterationFragment workerRegisterationFragment = (WorkerRegisterationFragment) navHostFragment.getChildFragmentManager().getFragments().get(0);
            workerRegisterationFragment.onCancelled(isMultiSelecting, tag);
        }
        Toast.makeText(this, R.string.cancelledd, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void loadImage(Uri imageUri, ImageView ivImage) {
        Glide.with(this).load(imageUri).into(ivImage);

    }

    @Override
    public void onSingleImageSelected(Uri uri, String tag) {
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.home_nav_fragment);
        if (Utility.fixNullString(tag).equals("makeOrder")) {
            RequestInfromationFragment requestInfromationFragment = (RequestInfromationFragment) navHostFragment.getChildFragmentManager().getFragments().get(0);
            requestInfromationFragment.onSingleImageSelected(uri, tag);
        } else {
            WorkerRegisterationFragment workerRegisterationFragment = (WorkerRegisterationFragment) navHostFragment.getChildFragmentManager().getFragments().get(0);
            workerRegisterationFragment.onSingleImageSelected(uri, tag);
            Sal7haSharedPreference.saveAgentImage(this, uri.getPath());
            Toast.makeText(this, uri.getPath(), Toast.LENGTH_SHORT).show();
        }
        Toast.makeText(this, R.string.donesuccessfully, Toast.LENGTH_SHORT).show();
    }

    private void userProfile() {
        activityMainBinding.homeNavigationView.getMenu().findItem(R.id.workerChartFragment).setVisible(false);
        NavigationView navigationView = findViewById(R.id.home_navigation_view);
        LinearLayout linearLayout = navigationView.getHeaderView(0).findViewById(R.id.creditlinearHeader);
        linearLayout.setVisibility(View.VISIBLE);
        RatingBar ratingBar = navigationView.getHeaderView(0).findViewById(R.id.ratingHeader);
        ratingBar.setVisibility(View.GONE);
        ImageView imageView = navigationView.getHeaderView(0).findViewById(R.id.profile_image);
        apiInterface.getCustomerProfile(Sal7haSharedPreference.getToken(this)).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse> call, @NonNull Response<ApiResponse> response) {
                if (response.body() != null && response.isSuccessful()) {
                    if (response.body().getStatus()) {
                        TextView name, credit, pointsText;
                        name = findViewById(R.id.nameHeader);
                        credit = findViewById(R.id.creditHeader);
                        pointsText = findViewById(R.id.pointHeader);
                        String basicName = response.body().getData().getUser().getName();
                        Integer points = response.body().getData().getUser().getPoints();
                        Integer credits = response.body().getData().getUser().getCredits();
                        name.setText(Utility.fixNullString(basicName));
                        pointsText.setText("" + points);
                        credit.setText("" + credits);
                        Picasso.get().load(RetrofitClient.BASE_URL + '/' + response.body().getData().getUser().getImage()).error(R.drawable.ic_account).placeholder(R.drawable.ic_account)
                                .into(imageView);
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {

            }
        });
    }


    private void agentProfile() {
        activityMainBinding.homeNavigationView.getMenu().findItem(R.id.workerChartFragment).setVisible(true);
        NavigationView navigationView = findViewById(R.id.home_navigation_view);
        LinearLayout linearLayout = navigationView.getHeaderView(0).findViewById(R.id.creditlinearHeader);
        linearLayout.setVisibility(View.GONE);
        ImageView imageView = navigationView.getHeaderView(0).findViewById(R.id.profile_image);
        RatingBar ratingBar = navigationView.getHeaderView(0).findViewById(R.id.ratingHeader);
        ratingBar.setVisibility(View.VISIBLE);
        apiInterface.getAgentProfile(Sal7haSharedPreference.getToken(this)).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse> call, @NonNull Response<ApiResponse> response) {
                if (response.body() != null && response.isSuccessful()) {
                    if (response.body().getStatus()) {
                        TextView name;
                        name = findViewById(R.id.nameHeader);
                        String basicName = response.body().getData().getAgent().getName();
                        name.setText(Utility.fixNullString(basicName));
                        LayerDrawable stars = (LayerDrawable) ratingBar.getProgressDrawable();
                        stars.getDrawable(1).setColorFilter(getResources().getColor(R.color.whiteColor), PorterDuff.Mode.SRC_ATOP);
                        stars.getDrawable(2).setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP);
                        stars.getDrawable(0).setColorFilter(getResources().getColor(R.color.whiteColor), PorterDuff.Mode.SRC_ATOP);
                        ratingBar.setRating(response.body().getData().getAgent().getRate());
                        Picasso.get().load(RetrofitClient.BASE_URL + '/' + response.body().getData().getAgent().getImage()).error(R.drawable.ic_account).placeholder(R.drawable.ic_account)
                                .into(imageView);
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {

            }
        });
    }

    private void noLogin() {
        activityMainBinding.homeNavigationView.getMenu().findItem(R.id.requestsFragment).setVisible(false);
        activityMainBinding.homeNavigationView.getMenu().findItem(R.id.workerChartFragment).setVisible(false);
        activityMainBinding.homeNavigationView.getMenu().findItem(R.id.signinfragmentt).setVisible(false);
        activityMainBinding.logoutTv.setText(R.string.login);
        NavigationView navigationView = findViewById(R.id.home_navigation_view);
//        ImageView logoutImage = navigationView.getHeaderView(0).findViewById(R.id.logoutId);

        activityMainBinding.logoutTv.setOnClickListener(v -> {
            navController.navigate(R.id.signinfragment);
            activityMainBinding.homeDrawer.closeDrawers();
        });
        checkLog = false;
        ImageView imageView = navigationView.getHeaderView(0).findViewById(R.id.profile_image);
        imageView.setImageResource(R.drawable.logo);
        TextView name;
        LinearLayout linearLayout = navigationView.getHeaderView(0).findViewById(R.id.creditlinearHeader);
        linearLayout.setVisibility(View.GONE);
        RatingBar ratingBar = navigationView.getHeaderView(0).findViewById(R.id.ratingHeader);
        ratingBar.setVisibility(View.GONE);
        name = navigationView.getHeaderView(0).findViewById(R.id.nameHeader);
        name.setText(R.string.sal7ha);
    }

    private void appSetting() {
        if (Sal7haSharedPreference.isLoggedIn(this)) {
            if (Sal7haSharedPreference.getRole(this).equals("user")) {
                userProfile();
            } else if (Sal7haSharedPreference.getRole(this).equals("agent")) {
                agentProfile();
            }
            activityMainBinding.homeNavigationView.getMenu().findItem(R.id.requestsFragment).setVisible(true);
            activityMainBinding.homeNavigationView.getMenu().findItem(R.id.signinfragmentt).setVisible(true);
            activityMainBinding.logoutTv.setText(R.string.logout);
//            NavigationView navigationView = findViewById(R.id.home_navigation_view);
//            ImageView logoutImage = navigationView.getHeaderView(0).findViewById(R.id.logoutId);
            activityMainBinding.logoutTv.setOnClickListener(v -> {
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(MainActivity.this);
                builder.setTitle(R.string.app_name);
                builder.setIcon(R.drawable.logo);
                builder.setMessage(R.string.logoutQuestion)
                        .setCancelable(false)

                        .setPositiveButton(R.string.yes, (dialog, id) -> {
                            Sal7haSharedPreference.clearSharedPreference(MainActivity.this);
                            Log.i(TAG, "TOKEN" + Sal7haSharedPreference.getToken(this));
                            navController.navigate(R.id.offersFragment);
                            activityMainBinding.homeDrawer.closeDrawers();
                            //    resetApp();
//                            finish();
//                            Intent intent = new Intent(MainActivity.this, MainActivity.class);
//                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
//                                intent.addFlags(0x8000); // equal to Intent.FLAG_ACTIVITY_CLEAR_TASK which is only available from API level 11
//                            this.startActivity(intent);
                            // Toast.makeText(this, "bye", Toast.LENGTH_SHORT).show();
                            //
                        })
                        .setNeutralButton(R.string.no, (dialog, id) -> dialog.cancel())
                        .show();
            });
            checkLog = true;
        } else {
            noLogin();

        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Typeface face = Typeface.createFromAsset(
                getAssets(), "fonts/urw_geometric_arabic_regular.ttf");
        ((TextView) menu).setTypeface(face);
        return super.onCreateOptionsMenu(menu);
    }

    //socail login
    private void loginWithSocialApi(String loginAs, String name, String email, String phone, String socialId, String socialType) {
        apiInterface.socialLogin(loginAs, name, email, phone, socialId, socialType).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.code() == 200) {
                    navController.navigate(R.id.offersFragment);
                    Log.i("RESULTTTT", response.body().getMessage() + "?");
                    Sal7haSharedPreference.saveUserData(MainActivity.this, response.body().getData().getUser(), response.body().getData().getUser().getId());
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {

            }
        });
    }

    public void facebookLogin() {
        FacebookSdk.sdkInitialize(MainActivity.this);
        callbackManager = CallbackManager.Factory.create();
        loginManager
                = LoginManager.getInstance();
        callbackManager
                = CallbackManager.Factory.create();

        loginManager
                .registerCallback(
                        callbackManager,
                        new FacebookCallback<LoginResult>() {

                            @Override
                            public void onSuccess(LoginResult loginResult) {
                                GraphRequest request = GraphRequest.newMeRequest(
                                        loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                                            @Override
                                            public void onCompleted(JSONObject object,
                                                                    GraphResponse response) {
                                                if (object != null) {
                                                    try {
                                                        String name = object.getString("name");
                                                        String fbUserID = object.getString("id");
                                                        disconnectFromFacebook();
                                                        if (object.has("email")) {
                                                            Log.i("TOKEEEEEENEMAIL", loginResult.getAccessToken().getToken() + "?");
                                                            String email = object.getString("email");
//                                                            if (GoldenNoLoginSharedPreference.getUserEmail(MainActivity.this).equals(email)) {
//                                                                loginWithGoogleApi(loginResult.getAccessToken().getToken(), "facebook", "second", "yes");
//
//                                                            } else {
//                                                                loginWithGoogleApi(loginResult.getAccessToken().getToken(), "facebook", "first", "yes");
//
//                                                            }
                                                        } else {
                                                            if (!loginResult.getAccessToken().isDataAccessExpired())
                                                                Log.i("TOKEEEEEEN", loginResult.getAccessToken().getToken() + "?");
//                                                            loginWithGoogleApi(loginResult.getAccessToken().getToken(), "facebook", "first", "no");
                                                        }
                                                        Log.i("FACEBOOKDATA", name + "//" + "//" + fbUserID);

                                                        // do action after Facebook login success
                                                        // or call your API
                                                    } catch (NullPointerException | JSONException e) {
                                                        e.printStackTrace();
                                                        Log.i("FACEBOOKEXCEPTION", e.toString());
                                                    }
                                                }
                                            }
                                        });

                                Bundle parameters = new Bundle();
                                parameters.putString(
                                        "fields",
                                        "id, name, email, gender, birthday");
                                request.setParameters(parameters);
                                request.executeAsync();
                            }

                            @Override
                            public void onCancel() {
                                Log.v("LoginScreen", "---onCancel");
                            }

                            @Override
                            public void onError(FacebookException error) {
                                // here write code when get error
                                Log.v("LoginScreen", "----onError: "
                                        + error.getMessage());
                                AccessToken.setCurrentAccessToken(null);
                                LoginManager.getInstance().logInWithReadPermissions(MainActivity.this, Arrays.asList("email"));
                            }
                        });

    }

    public void disconnectFromFacebook() {
        if (AccessToken.getCurrentAccessToken() == null) {
            return; // already logged out
        }

        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/me/permissions/",
                null,
                HttpMethod.DELETE,
                new GraphRequest
                        .Callback() {
                    @Override
                    public void onCompleted(GraphResponse graphResponse) {
                        LoginManager.getInstance().logOut();
                    }
                })
                .executeAsync();
    }

    public void faceBookLogin() {
        loginManager.logInWithReadPermissions(
                MainActivity.this,
                Arrays.asList(
                        "email",
                        "public_profile",
                        "user_birthday"));
    }

    public void printHashKey() {
        // Add code to print out the key hash
        try {
            PackageInfo info
                    = getPackageManager().getPackageInfo(
                    "com.dtag.osta",
                    PackageManager.GET_SIGNATURES);

            for (Signature signature : info.signatures) {

                MessageDigest md
                        = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash",
                        Base64.encodeToString(
                                md.digest(),
                                Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
        } catch (NoSuchAlgorithmException e) {
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // add this line
        callbackManager.onActivityResult(
                requestCode,
                resultCode,
                data);
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
            loginWithSocialApi("user", acct.getDisplayName(), acct.getEmail(), "", acct.getId(), "google");


        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.d("massageGOOGLE", e.toString());

        }
    }

    public void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    public void googleLogOut() {
        LoginManager.getInstance().logOut();
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                });
    }
}
