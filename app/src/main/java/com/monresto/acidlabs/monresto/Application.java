package com.monresto.acidlabs.monresto;

import com.facebook.appevents.AppEventsLogger;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.firebase.FirebaseApp;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.perf.FirebasePerformance;
import com.onesignal.OneSignal;

import java.util.Locale;

import static com.facebook.FacebookSdk.setAdvertiserIDCollectionEnabled;
import static com.facebook.FacebookSdk.setAutoLogAppEventsEnabled;

public class Application extends android.app.Application {

    private static FirebaseAnalytics firebaseAnalytics;

    public static void logEvent(String eventName) {
        try {
            firebaseAnalytics.logEvent(eventName, null);
        } catch (Exception e) {

        }

        Config.logger.logEvent(eventName);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        FirebaseApp.initializeApp(this);
        FirebasePerformance.startTrace("");
        setAutoLogAppEventsEnabled(true);
        setAdvertiserIDCollectionEnabled(true);
//        sAnalytics = GoogleAnalytics.getInstance(this);
        firebaseAnalytics = FirebaseAnalytics.getInstance(this);

        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();
        Config.logger = AppEventsLogger.newLogger(this);
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), getString(R.string.api_key), Locale.US);

        }

    }
}
