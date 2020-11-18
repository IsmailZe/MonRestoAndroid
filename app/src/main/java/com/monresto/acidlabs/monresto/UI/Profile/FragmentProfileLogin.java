package com.monresto.acidlabs.monresto.UI.Profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.monresto.acidlabs.monresto.Model.User;
import com.monresto.acidlabs.monresto.R;
import com.monresto.acidlabs.monresto.SharedPreferenceManager;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FragmentProfileLogin extends Fragment {
    @BindView(R.id.userData)
    TextView userData;
    @BindView(R.id.buttonLogout)
    Button buttonLogout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup root;
        root = (ViewGroup) inflater.inflate(R.layout.fragment_profile_login, container, false);
        ButterKnife.bind(this, root);

        buttonLogout.setOnClickListener(e -> {
            SharedPreferenceManager.logout(getContext());
            ViewPager currentViewPager = getActivity().findViewById(R.id.viewPagerProfile);
            currentViewPager.setCurrentItem(0);
        });
        return root;
    }

    public void update() {
        userData.setText(User.getInstance().getId() + " - " + User.getInstance().getFname());
    }
}
