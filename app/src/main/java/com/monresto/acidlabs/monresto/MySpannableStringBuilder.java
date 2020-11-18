package com.monresto.acidlabs.monresto;

import android.content.Context;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.StyleSpan;


/**
 * Created by Ismail on 31/05/2018.
 */

public class MySpannableStringBuilder extends SpannableStringBuilder {

    public MySpannableStringBuilder appendColoredText(String text, int color) {
        try {
            final ForegroundColorSpan fcs = new ForegroundColorSpan(color);
            this.append(text);
            this.setSpan(fcs, this.length() - text.length(), this.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        } catch (Exception e) {
        }
        return this;
    }

    public void appendColoredBoldText(String text, int color) {
        try {
            final ForegroundColorSpan fcs = new ForegroundColorSpan(color);
            StyleSpan b = new StyleSpan(Typeface.BOLD);
            this.append(text);
            this.setSpan(fcs, this.length() - text.length(), this.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            this.setSpan(b, this.length() - text.length(), this.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        } catch (Exception e) {
        }

    }


    public void setColor(int color) {
        try {
            final ForegroundColorSpan fcs = new ForegroundColorSpan(color);
            this.setSpan(fcs, 0, this.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        } catch (Exception e) {
        }
    }


    public void apppendDrawable(Context context, int icon) {
        try {
            this.setSpan(new ImageSpan(context, icon), this.length() - 1, this.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        } catch (Exception e) {
        }
    }

}
