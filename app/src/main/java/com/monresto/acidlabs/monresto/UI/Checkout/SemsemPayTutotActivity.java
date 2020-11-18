package com.monresto.acidlabs.monresto.UI.Checkout;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.monresto.acidlabs.monresto.MySpannableStringBuilder;
import com.monresto.acidlabs.monresto.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SemsemPayTutotActivity extends AppCompatActivity {
    @BindView(R.id.iv_arrow_back)
    ImageView ivBack;
    @BindView(R.id.tv_i_understand)
    View tvDone;
    @BindView(R.id.tv_description1)
    TextView tvDescription1;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_semsem_pay_tuto);
        ButterKnife.bind(this);

        MySpannableStringBuilder mySpannableStringBuilder = new MySpannableStringBuilder();
        mySpannableStringBuilder.appendColoredBoldText(" Andek sarf ?", ContextCompat.getColor(getApplicationContext(), R.color.gray0));
        mySpannableStringBuilder.appendColoredText(" Plus besoin de poser cette question \uD83D\uDE00 ni de se soucier de la monnaie.\n" +
                "                Désormais vous pouvez utiliser Semsem Pay pour payer vos livreurs avec une expérience 0 Contact en chargeant votre wallet et en scannant le QR code du livreur", ContextCompat.getColor(getApplicationContext(), R.color.gray0));
        tvDescription1.setText(mySpannableStringBuilder);


        ivBack.setOnClickListener(e -> finish());
        tvDone.setOnClickListener(e -> finish());


    }


}
