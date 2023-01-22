package com.dtag.osta.Activity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
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
import com.google.firebase.messaging.FirebaseMessaging;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Utility.updateResources(this);
        super.onCreate(savedInstanceState);
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        navController = Navigation.findNavController(this, R.id.home_nav_fragment);
        activityMainBinding.homeNavigationView.setItemIconTintList(null);
        NavigationUI.setupWithNavController(activityMainBinding.homeNavigationView, navController);
        NavigationUI.setupWithNavController(activityMainBinding.bottomNav, navController);
        activityMainBinding.homeDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        bottomMenu();
        appSetting();
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

    private void bottomMenu() {
        activityMainBinding.bottomNav.getMenu().findItem(R.id.offersFragment).setIcon(R.drawable.ic_select_home);
        activityMainBinding.bottomNav.getMenu().findItem(R.id.offersFragment).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                navController.navigate(R.id.offersFragment);
                activityMainBinding.bottomNav.getMenu().findItem(R.id.offersFragment).setIcon(R.drawable.ic_select_home);
                activityMainBinding.bottomNav.getMenu().findItem(R.id.salesFragment).setIcon(R.drawable.ic_offer);
                activityMainBinding.bottomNav.getMenu().findItem(R.id.requestsFragment).setIcon(R.drawable.ic_requests);
                activityMainBinding.bottomNav.getMenu().findItem(R.id.profileFragment).setIcon(R.drawable.ic_profile);
                activityMainBinding.bottomNav.getMenu().findItem(R.id.moreFragment).setIcon(R.drawable.ic_more);
                return true;
            }
        });
        activityMainBinding.bottomNav.getMenu().findItem(R.id.salesFragment).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                navController.navigate(R.id.salesFragment);
                activityMainBinding.bottomNav.getMenu().findItem(R.id.offersFragment).setIcon(R.drawable.ic_home);
                activityMainBinding.bottomNav.getMenu().findItem(R.id.salesFragment).setIcon(R.drawable.ic_select_offer);
                activityMainBinding.bottomNav.getMenu().findItem(R.id.requestsFragment).setIcon(R.drawable.ic_requests);
                activityMainBinding.bottomNav.getMenu().findItem(R.id.profileFragment).setIcon(R.drawable.ic_profile);
                activityMainBinding.bottomNav.getMenu().findItem(R.id.moreFragment).setIcon(R.drawable.ic_more);
                return true;
            }
        });
        activityMainBinding.bottomNav.getMenu().findItem(R.id.requestsFragment).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                navController.navigate(R.id.requestsFragment);
                activityMainBinding.bottomNav.getMenu().findItem(R.id.offersFragment).setIcon(R.drawable.ic_home);
                activityMainBinding.bottomNav.getMenu().findItem(R.id.salesFragment).setIcon(R.drawable.ic_offer);
                activityMainBinding.bottomNav.getMenu().findItem(R.id.requestsFragment).setIcon(R.drawable.ic_select_requests);
                activityMainBinding.bottomNav.getMenu().findItem(R.id.profileFragment).setIcon(R.drawable.ic_profile);
                activityMainBinding.bottomNav.getMenu().findItem(R.id.moreFragment).setIcon(R.drawable.ic_more);
                return true;
            }
        });
        activityMainBinding.bottomNav.getMenu().findItem(R.id.profileFragment).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                navController.navigate(R.id.profileFragment);
                activityMainBinding.bottomNav.getMenu().findItem(R.id.offersFragment).setIcon(R.drawable.ic_home);
                activityMainBinding.bottomNav.getMenu().findItem(R.id.salesFragment).setIcon(R.drawable.ic_offer);
                activityMainBinding.bottomNav.getMenu().findItem(R.id.requestsFragment).setIcon(R.drawable.ic_requests);
                activityMainBinding.bottomNav.getMenu().findItem(R.id.profileFragment).setIcon(R.drawable.ic_select_profile);
                activityMainBinding.bottomNav.getMenu().findItem(R.id.moreFragment).setIcon(R.drawable.ic_more);
                return true;
            }
        });
        activityMainBinding.bottomNav.getMenu().findItem(R.id.moreFragment).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                navController.navigate(R.id.moreFragment);
                activityMainBinding.bottomNav.getMenu().findItem(R.id.offersFragment).setIcon(R.drawable.ic_home);
                activityMainBinding.bottomNav.getMenu().findItem(R.id.salesFragment).setIcon(R.drawable.ic_offer);
                activityMainBinding.bottomNav.getMenu().findItem(R.id.requestsFragment).setIcon(R.drawable.ic_requests);
                activityMainBinding.bottomNav.getMenu().findItem(R.id.profileFragment).setIcon(R.drawable.ic_profile);
                activityMainBinding.bottomNav.getMenu().findItem(R.id.moreFragment).setIcon(R.drawable.ic_select_more);
                return true;
            }
        });

    }

    public void showBottomMenu() {
        activityMainBinding.bottomNav.setVisibility(View.VISIBLE);
    }

    public void hideBottomMenu() {
        activityMainBinding.bottomNav.setVisibility(View.GONE);
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


    private void appSetting() {
        if (Sal7haSharedPreference.isLoggedIn(this)) {
            if (Sal7haSharedPreference.getRole(this).equals("user")) {
                navController.navigate(R.id.offersFragment);
            } else {
                //TODO AGENT FRAGMENT
            }
            checkLog = true;
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
