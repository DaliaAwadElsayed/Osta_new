package com.dtag.osta.Fragment.maps;

import android.content.pm.PackageManager;
import android.graphics.Point;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProviders;

import com.dtag.osta.Fragment.ViewModel.maps.MapViewModel;
import com.dtag.osta.R;
import com.dtag.osta.databinding.MapFragmentBinding;
import com.dtag.osta.network.Interface.OnInputSelected;
import com.dtag.osta.utility.Utility;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class MapFragment extends DialogFragment implements OnMapReadyCallback {


    public OnInputSelected mOnInputSelected;

    public void setmOnInputSelected(OnInputSelected mOnInputSelected) {
        this.mOnInputSelected = mOnInputSelected;
    }

    private SupportMapFragment mapDetail;
    private MapViewModel mViewModel;
    MapFragmentBinding mapFragmentBinding;
    private GoogleMap mMap;
    // New variables for Current Place Picker
    private static final String TAG = "MapsActivity";
    private PlacesClient mPlacesClient;
    private FusedLocationProviderClient mFusedLocationProviderClient;

    // The geographical location where the device is currently located. That is, the last-known
    // location retrieved by the Fused Location Provider.
    private Location mLastKnownLocation;

    // A default location (Sydney, Australia) and default zoom to use when location permission is
    // not granted.
    private final LatLng mDefaultLocation = new LatLng(-33.8523341, 151.2106085);
    private static final int DEFAULT_ZOOM = 15;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean mLocationPermissionGranted;

    String latitude;
    String longitude;
    String address;
    // Used for selecting the current place.
    private static final int M_MAX_ENTRIES = 5;
    private String[] mLikelyPlaceNames;
    private String[] mLikelyPlaceAddresses;
    private String[] mLikelyPlaceAttributions;
    private LatLng[] mLikelyPlaceLatLngs;
    Button cancel, done;
    TextView add, lang, lat;

    public static MapFragment newInstance() {
        return new MapFragment();
    }

    View view;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.map_fragment, container, false);
        return view;
//        if (view != null) {
//            ViewGroup parent = (ViewGroup) view.getParent();
//            if (parent != null)
//                parent.removeView(view);
//        }
//        try {
//            view = inflater.inflate(R.layout.map_fragment, container, false);
//        } catch (InflateException e) {
//            /* map is already there, just return view as it is */
//        }
//        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (Build.VERSION.SDK_INT < 21) {
            mapDetail = (SupportMapFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.map);
        } else {
            mapDetail = (SupportMapFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.map);
        }
        mapDetail.getMapAsync(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(MapViewModel.class);
        SupportMapFragment mapFragment = (SupportMapFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }


    /**
     * Handles user clicks on menu items.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_geolocate:

                // COMMENTED OUT UNTIL WE DEFINE THE METHOD
                // Present the current place picker
                pickCurrentPlace();
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    /**
     * Prompts the user for permission to use the device location.
     */
    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        mLocationPermissionGranted = false;
        if (ContextCompat.checkSelfPermission(getContext().getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        pickCurrentPlace();
        getDeviceLocation();
        mMap.getUiSettings().setZoomControlsEnabled(true);
        getLocationPermission();
    }

    private void getCurrentPlaceLikelihoods() {
        List<Place.Field> placeFields = Arrays.asList(Place.Field.NAME, Place.Field.ADDRESS,
                Place.Field.LAT_LNG);
        @SuppressWarnings("MissingPermission") final FindCurrentPlaceRequest request =
                FindCurrentPlaceRequest.builder(placeFields).build();
        Task<FindCurrentPlaceResponse> placeResponse = mPlacesClient.findCurrentPlace(request);
        placeResponse.addOnCompleteListener(getActivity(),
                new OnCompleteListener<FindCurrentPlaceResponse>() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onComplete(@NonNull Task<FindCurrentPlaceResponse> task) {
                        if (task.isSuccessful()) {
                            FindCurrentPlaceResponse response = task.getResult();
                            // Set the count, handling cases where less than 5 entries are returned.
                            int count;
                            if (response.getPlaceLikelihoods().size() < M_MAX_ENTRIES) {
                                count = response.getPlaceLikelihoods().size();
                            } else {
                                count = M_MAX_ENTRIES;
                            }

                            int i = 0;
                            mLikelyPlaceNames = new String[count];
                            mLikelyPlaceAddresses = new String[count];
                            mLikelyPlaceAttributions = new String[count];
                            mLikelyPlaceLatLngs = new LatLng[count];

                            for (PlaceLikelihood placeLikelihood : response.getPlaceLikelihoods()) {
                                Place currPlace = placeLikelihood.getPlace();
                                mLikelyPlaceNames[i] = currPlace.getName();
                                mLikelyPlaceAddresses[i] = currPlace.getAddress();
                                mLikelyPlaceAttributions[i] = (currPlace.getAttributions() == null) ?
                                        null : String.join(" ", currPlace.getAttributions());
                                mLikelyPlaceLatLngs[i] = currPlace.getLatLng();

                                String currLatLng = (mLikelyPlaceLatLngs[i] == null) ?
                                        "" : mLikelyPlaceLatLngs[i].toString();
                                mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
                                    @Override
                                    public void onMarkerDragStart(Marker marker) {
                                        Log.i(TAG,"MOVESTART");
                                        if (marker != null) {
                                            mMap.clear();
                                        }

                                    }

                                    @Override
                                    public void onMarkerDrag(Marker marker) {
                                        Log.i(TAG,"MOVENOW");
                                        if (marker != null) {
                                            mMap.clear();

                                        }

                                    }

                                    @Override
                                    public void onMarkerDragEnd(Marker marker) {
                                        Log.i(TAG,"MOVEEND");
                                        if (marker != null) {
                                            mMap.clear();

                                        }

                                    }
                                });

                                Log.i(TAG, String.format("Place " + currPlace.getName()
                                        + " has likelihood: " + placeLikelihood.getLikelihood()
                                        + " at " + currLatLng));

                                i++;
                                if (i > (count - 1)) {
                                    break;
                                }
                            }
                        } else {
                            Exception exception = task.getException();
                            if (exception instanceof ApiException) {
                                ApiException apiException = (ApiException) exception;
                                Log.e(TAG, "Place not found: " + apiException.getStatusCode());
                            }
                        }
                    }
                });
    }

    /**
     * Get the current location of the device, and position the map's camera
     */
    private void getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (mLocationPermissionGranted) {
                Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            // Set the map's camera position to the current location of the device.
                            mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
                                @Override
                                public void onMarkerDragStart(Marker marker) {
                                    Log.i(TAG,"MOVESTART");
                                    if (marker != null) {
                                        mMap.clear();
                                        MarkerOptions markerOptions = new MarkerOptions();
                                        mMap.addMarker(markerOptions
                                                .title(getString(R.string.default_info_title))
                                                .position(new LatLng(location.getLatitude(), location.getLongitude()))
                                                .snippet(getString(R.string.default_info_snippet)));

                                        mMap.addMarker(markerOptions);
                                    }

                                }

                                @Override
                                public void onMarkerDrag(Marker marker) {
                                    Log.i(TAG,"MOVENOW");
                                    if (marker != null) {
                                        mMap.clear();
                                        MarkerOptions markerOptions = new MarkerOptions();
                                        mMap.addMarker(markerOptions
                                                .title(getString(R.string.default_info_title))
                                                .position(new LatLng(location.getLatitude(), location.getLongitude()))
                                                .snippet(getString(R.string.default_info_snippet)));

                                        mMap.addMarker(markerOptions);
                                    }

                                }

                                @Override
                                public void onMarkerDragEnd(Marker marker) {
                                    Log.i(TAG,"MOVEEND");
                                    if (marker != null) {
                                        mMap.clear();
                                        MarkerOptions markerOptions = new MarkerOptions();
                                        mMap.addMarker(markerOptions
                                                .title(getString(R.string.default_info_title))
                                                .position(new LatLng(location.getLatitude(), location.getLongitude()))
                                                .snippet(getString(R.string.default_info_snippet)));

                                        mMap.addMarker(markerOptions);
                                    }

                                }
                            });

                            mLastKnownLocation = location;
                            if (add != null) {
                                add.setText(getCompleteAddressString(
                                        mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude()));
                                Log.d(TAG, "Latitude: " + mLastKnownLocation.getLatitude());
                                lat.setText("" + mLastKnownLocation.getLatitude());
                                Log.d(TAG, "Longitude: " + mLastKnownLocation.getLongitude());
                                lang.setText("" + mLastKnownLocation.getLongitude());
                                getCurrentPlaceLikelihoods();
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                        new LatLng(location.getLatitude(), location.getLongitude()), DEFAULT_ZOOM));
                                // Add a default marker, because the user hasn't selected a place.

                                MarkerOptions markerOptions = new MarkerOptions();
                              mMap.addMarker(markerOptions
                                        .title(getString(R.string.default_info_title))
                                        .position(new LatLng(location.getLatitude(), location.getLongitude()))
                                       .draggable(true)
                                        .snippet(getString(R.string.default_info_snippet)));

                            }

                        } else {
                            Log.d(TAG, "Current location is null. Using defaults.");
                            mMap.moveCamera(CameraUpdateFactory
                                    .newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
                        }

                    }
                });
            }
        } catch (Exception e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }


    private String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();
                Log.w("MyCurrentloctionaddress", strReturnedAddress.toString());
            } else {
                Log.w("MyCurrentloctionaddress", "No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.w("MyCurrentloctionaddress", "Canont get Address!");
        }
        return strAdd;
    }

    /**
     * Fetch a list of likely places, and show the current place on the map - provided the user
     * has granted location permission.
     */
    private void pickCurrentPlace() {
        if (mMap == null) {
            return;
        }

        if (mLocationPermissionGranted) {
            getDeviceLocation();
        } else {
            // The user has not granted permission.
            Log.i(TAG, "The user did not grant location permission.");
            // Add a default marker, because the user hasn't selected a place.
            mMap.addMarker(new MarkerOptions()
                    .title(getString(R.string.default_info_title))
                    .position(mDefaultLocation)
                    .snippet(getString(R.string.default_info_snippet))
                    .draggable(true)
            );
            mMap.moveCamera(CameraUpdateFactory.newLatLng(mDefaultLocation));
            mMap.isTrafficEnabled();
            mMap.isMyLocationEnabled();
            // Prompt the user for permission.
            getLocationPermission();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();

        WindowManager.LayoutParams windowParams = window.getAttributes();
        windowParams.dimAmount = 0.90f;
        windowParams.flags |= WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(windowParams);

        Point size = new Point();

        Display display = window.getWindowManager().getDefaultDisplay();
        display.getSize(size);

        int width = size.x;
        int height = size.y;

        window.setLayout((int) (width * .90), (int) (height * .75));
        window.setGravity(Gravity.CENTER);
        //  cancel = getDialog().findViewById(R.id.cancelMapDialog);
        done = getDialog().findViewById(R.id.locationMapDone);
        add = getDialog().findViewById(R.id.addressMapDialog);
        lat = getDialog().findViewById(R.id.latMapDialog);
        lang = getDialog().findViewById(R.id.langMapDialog);
        if (done != null) {
            done.setOnClickListener(view -> {
                if (mLastKnownLocation != null) {
                    latitude = Utility.fixNullString(mLastKnownLocation.getLatitude() + "");
                    longitude = Utility.fixNullString(mLastKnownLocation.getLongitude() + "");
                    address = Utility.fixNullString(getCompleteAddressString(
                            mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude()));
                    if (!latitude.equals("") && !longitude.equals("") && !address.equals("")) {
                        mOnInputSelected.sendInput(latitude, longitude, address);
                        //  Toast.makeText(getContext(), "data" + latitude + longitude + address, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    //  Toast.makeText(getContext(), "", Toast.LENGTH_SHORT).show();
                }

                getDialog().cancel();
            });
        }
        if (cancel != null) {
            cancel.setOnClickListener(view -> {
                getDialog().cancel();
            });
        }

        String apiKey = getString(R.string.google_maps_key);
        Places.initialize(getContext().getApplicationContext(), apiKey);
        mPlacesClient = Places.createClient(getContext());
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());

        pickCurrentPlace();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != mapDetail)
            getActivity().getSupportFragmentManager().beginTransaction()
                    .remove(mapDetail)
                    .commit();
    }


}
