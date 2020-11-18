package com.monresto.acidlabs.monresto.UI.Profile.Address;

import android.app.Activity;
import android.content.Intent;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.monresto.acidlabs.monresto.Config;
import com.monresto.acidlabs.monresto.LocationPickerActivity;
import com.monresto.acidlabs.monresto.Model.Address;
import com.monresto.acidlabs.monresto.Model.City;
import com.monresto.acidlabs.monresto.Model.Semsem;
import com.monresto.acidlabs.monresto.Model.User;
import com.monresto.acidlabs.monresto.R;
import com.monresto.acidlabs.monresto.Service.City.CityAsyncResponse;
import com.monresto.acidlabs.monresto.Service.City.CityService;
import com.monresto.acidlabs.monresto.Service.User.UserAsyncResponse;
import com.monresto.acidlabs.monresto.Service.User.UserService;
import com.monresto.acidlabs.monresto.UI.Address.UserAddress;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.monresto.acidlabs.monresto.Config.REQUEST_CODE_MAP_INFO;

public class NewAddressActivity extends AppCompatActivity implements CityAsyncResponse, UserAsyncResponse {
    @BindView(R.id.location_spinner)
    Spinner locationSpinner;
    @BindView(R.id.textAddress)
    EditText textAddress;
    @BindView(R.id.textStreet)
    EditText textStreet;
    @BindView(R.id.textAppart)
    EditText textAppart;
    @BindView(R.id.textComment)
    EditText textComment;
    @BindView(R.id.city_spinner)
    Spinner citySpinner;
    @BindView(R.id.buttonSubmitAddress)
    Button buttonSubmitAddress;
    @BindView(R.id.imageProfileBack)
    ImageView imageProfileBack;
    @BindView(R.id.progressBarAddAddress)
    ProgressBar progressBarAddAddress;

    ArrayList<City> cities;
    Address address;
    Geocoder geocoder;
    UserService userService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_new);
        ButterKnife.bind(this);

        address = new Address();
        geocoder = new Geocoder(this);

        List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG);
        /*Intent intent = new Autocomplete.IntentBuilder(
                AutocompleteActivityMode.OVERLAY, fields)
                .build(this);
        startActivityForResult(intent, Config.AUTOCOMPLETE_REQUEST_CODE);*/

        /*PlacesClient placesClient = Places.createClient(this);
        // Construct a request object, passing the place ID and fields array.
        final FetchPlaceRequest request = FetchPlaceRequest.newInstance(placeId, fields);

        placesClient.fetchPlace(request).addOnSuccessListener((response) -> {
            Place place = response.getPlace();
            Log.i(TAG, "Place found: " + place.getName());
        }).addOnFailureListener((exception) -> {
            if (exception instanceof ApiException) {
                final ApiException apiException = (ApiException) exception;
                Log.e(TAG, "Place not found: " + exception.getMessage());
                final int statusCode = apiException.getStatusCode();
                // TODO: Handle error with given status code.
            }
        });*/

        Intent locationPickerIntent = new Intent(this, LocationPickerActivity.class);
        startActivityForResult(locationPickerIntent, Config.REQUEST_PLACE_PICKER);

        buttonSubmitAddress.setOnClickListener(e -> {
            address.setAdresse(textAddress.getText().toString());
            address.setEmplacement(locationSpinner.getSelectedItem().toString());
            address.setRue(textStreet.getText().toString());
            address.setAppartement(textAppart.getText().toString());
            address.setCityID(0);

            userService = new UserService(this);
            userService.addAddress(address);

            progressBarAddAddress.setVisibility(View.VISIBLE);
            buttonSubmitAddress.setVisibility(View.GONE);
        });
        imageProfileBack.setOnClickListener(e -> {
            finish();
        });

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.location_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        locationSpinner.setAdapter(adapter);

        ArrayAdapter<CharSequence> adapter_city = ArrayAdapter.createFromResource(this,
                R.array.default_city_array, android.R.layout.simple_spinner_item);
        adapter_city.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        citySpinner.setAdapter(adapter_city);

        cities = new ArrayList<>();
        CityService cityService = new CityService(this);
        cityService.getCities();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case (REQUEST_CODE_MAP_INFO): {
                if (resultCode == Activity.RESULT_OK) {
                    double lat = data.getDoubleExtra("lat", 36.849109);
                    double lon = data.getDoubleExtra("lon", 10.166124);

                    android.location.Address currentAddress;
                    try {
                        List<android.location.Address> addressList = geocoder.getFromLocation(lat, lon, 1);
                        if (addressList != null && !addressList.isEmpty()) {
                            currentAddress = addressList.get(0);
                            address.setPostalCode(currentAddress.getPostalCode());
                            textAddress.setText(currentAddress.getAddressLine(0));
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    address.setLat(lat);
                    address.setLon(lon);

                } else if (resultCode == Activity.RESULT_CANCELED)
                    finish();
            }
            break;
            case (Config.REQUEST_PLACE_PICKER): {
                if (data == null)
                    finish();
                else {
                    /*Place place = Autocomplete.getPlaceFromIntent(data);
                    textAddress.setText(place.getAddress());
                    address.setAdresse(place.getAddress().toString());
                    address.setLat(place.getLatLng().latitude);
                    address.setLon(place.getLatLng().longitude);*/
                    UserAddress userAddress = (UserAddress) data.getSerializableExtra("userAdress");
                    address.setLat(userAddress.getMapLocation().getLatitude());
                    address.setLon(userAddress.getMapLocation().getLongtiude());
                }
            }
            break;
        }
    }

    @Override
    public void onCitiesReceived(ArrayList<City> cities) {
        ArrayList<CharSequence> list = new ArrayList<>();
        for (int i = 0; i < cities.size(); i++)
            list.add(cities.get(i).getName());

        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        citySpinner.setAdapter(adapter);
    }

    @Override
    public void onAddressAddResponse(boolean success) {
        if (success && User.getInstance() != null) {
            userService.getAddress(User.getInstance().getId());
            finish();
        } else
            Toast.makeText(this, "Une erreur est survenue lors de l'ajout de votre adresse", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onAddressListReceived(ArrayList<Address> addresses) {
        User user = User.getInstance();
        if (user != null) {
            user.setAddresses(addresses);
        }
    }


}
