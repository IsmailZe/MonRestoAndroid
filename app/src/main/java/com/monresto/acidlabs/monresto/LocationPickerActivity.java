package com.monresto.acidlabs.monresto;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.monresto.acidlabs.monresto.UI.Address.Coordinates;
import com.monresto.acidlabs.monresto.UI.Address.MapLocation;
import com.monresto.acidlabs.monresto.UI.Address.UserAddress;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class LocationPickerActivity extends AppCompatActivity implements OnMapReadyCallback {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_validate)
    TextView tvValidate;
    @BindView(R.id.iv_search)
    ImageView ivSearch;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    private SupportMapFragment mapfragment;
    private Geocoder geocoder;
    private Marker marker;
    private GoogleMap map;
    private double lat = 36.806495;
    private double lng = 10.181532;
    private Unbinder unbinder;
    private List<Address> addresses;
    private String address;
    private BitmapDescriptor bitmapDescriptor;
    private int from;
    private UserAddress userAddress = new UserAddress();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_picker);

        unbinder = ButterKnife.bind(this);
        try {
            ivBack.setOnClickListener(view -> finish());
        } catch (Exception ignored) {

        }
        Coordinates coordinates = (Coordinates) getIntent().getSerializableExtra("coordinates");
        try {
            bitmapDescriptor
                    = BitmapDescriptorFactory
                    .fromResource(R.drawable.ic_map_marker);
        } catch (Exception e) {
            e.printStackTrace();
        }
        geocoder = new Geocoder(this, Locale.getDefault());
        if (mapfragment == null) {
            mapfragment = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapfragment));
            assert mapfragment != null;
            if (coordinates == null) {
                GPSTracker gpsTracker = new GPSTracker(this);
                coordinates = new Coordinates();
                coordinates.setLatitude(gpsTracker.latitude);
                coordinates.setLongitude(gpsTracker.longitude);
                userAddress.setMapLocation(new MapLocation(coordinates));
            }
            userAddress.setMapLocation(new MapLocation(coordinates));
            mapfragment.getMapAsync(this);

        }

        try {
            ivSearch.setOnClickListener(v -> startGooglePlaces());
        } catch (Exception ignored) {

        }

        try {
            tvValidate.setOnClickListener(v -> {
//                Toast.makeText(this, userAddress.getMapLocation().getLatitude() + "," + userAddress.getMapLocation().getLongtiude(), Toast.LENGTH_LONG).show();
                Intent intent = new Intent();
                intent.putExtra("userAdress", userAddress);
                intent.putExtra("serviceType", getIntent().getStringExtra("serviceType"));
                setResult(Config.REQUEST_PLACE_PICKER, intent);
                finish();
            });
        } catch (Exception ignored) {

        }
    }


    private void startGooglePlaces() {
        if (!Places.isInitialized()) {
            Places.initialize(this, getResources().getString(R.string.google_maps_key));
        }
        List<Place.Field> placeFields = new ArrayList<>();
        placeFields.add(Place.Field.ADDRESS);
        placeFields.add(Place.Field.NAME);
        placeFields.add(Place.Field.LAT_LNG);
        placeFields.add(Place.Field.ADDRESS_COMPONENTS);
        Intent intent = new Autocomplete.IntentBuilder(
                AutocompleteActivityMode.FULLSCREEN, placeFields)
                .build(this);
        startActivityForResult(intent, Config.AUTOCOMPLETE_REQUEST_CODE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setOnMapClickListener(latLng -> {
            initUserAdress(latLng, "");
            tvValidate.setEnabled(true);
        });
        try {
            LatLng latLng = new LatLng(userAddress.getMapLocation().getLatitude(), userAddress.getMapLocation().getLongtiude());
            initUserAdress(latLng, "");
        } catch (Exception e) {
            e.printStackTrace();
        }
//        map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), 12.0f));

    }

    private void initMap() {
        try {
            map.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
        LatLng pp = new LatLng(userAddress.getMapLocation().getLatitude(), userAddress.getMapLocation().getLongtiude());
        try {
            if (marker != null)
                marker.remove();
            MarkerOptions options = new MarkerOptions()
                    .position(pp)
                    .icon(bitmapDescriptor);
            map.addMarker(options);
            CameraPosition cameraPosition = new CameraPosition.Builder().target(pp).zoom(16.0f).build();
            CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
            map.setOnMarkerClickListener(marker -> false);
            map.animateCamera(cameraUpdate);
        } catch (Exception ignored) {
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Config.AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                assert data != null;
                Place place = Autocomplete.getPlaceFromIntent(data);
                initUserAdress(Objects.requireNonNull(place.getLatLng()), place.getAddress());
                tvValidate.setEnabled(true);
            }
        }
    }

    private void initUserAdress(LatLng latLng, String addressName) {
        Coordinates coordinates = new Coordinates();
        coordinates.setLatitude(latLng.latitude);
        coordinates.setLongitude(latLng.longitude);
        MapLocation mapLocation = new MapLocation(coordinates);
        userAddress.setMapLocation(mapLocation);

        /*try {
            addresses = geocoder.getFromLocation(userAddress.getMapLocation().getLatitude(), userAddress.getMapLocation().getLongtiude(), 1);
            if (addresses.size() != 0) {
                try {
                    if (addressName == null || addressName.trim().isEmpty()) {
                        addressName = addresses.get(0).getAddressLine(0);
                    }
                    userAddress.setAddressName(addressName);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    String city = "";
                    if (addresses.get(0).getLocality() != null) {
                        city = addresses.get(0).getLocality();
                    } else if (addresses.get(0).getAdminArea() != null) {
                        city = addresses.get(0).getAdminArea();
                    } else {
                        city = addresses.get(0).getSubAdminArea();
                    }
                    userAddress.setCity(city);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException | IndexOutOfBoundsException e) {
            e.printStackTrace();
        }*/
        initMap();
    }
}
