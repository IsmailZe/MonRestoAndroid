package com.monresto.acidlabs.monresto.UI.Homepage;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Guideline;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.monresto.acidlabs.monresto.Model.ShoppingCart;
import com.monresto.acidlabs.monresto.R;
import com.monresto.acidlabs.monresto.UI.Delivery.DeliveryActivity;
import com.monresto.acidlabs.monresto.UI.Homepage.Snacks.SnacksActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class HomepageExtrasAdapter extends RecyclerView.Adapter<HomepageExtrasAdapter.ViewHolder> {
    private Context context;
    private ArrayList<String> images;
    private final static int FADE_DURATION = 1000; //FADE_DURATION in milliseconds

    public HomepageExtrasAdapter(Context context) {
        this.context = context;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_title)
        TextView item_title;
        @BindView(R.id.item_bg)
        ImageView item_bg;
        @BindView(R.id.itemContainer)
        ConstraintLayout itemContainer;
        @BindView(R.id.right_guideline)
        Guideline right_guideline;
        @BindView(R.id.bottom_guideline)
        Guideline bottom_guideline;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @NonNull
    @Override
    public HomepageExtrasAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v;
        v = LayoutInflater.from(context).inflate(R.layout.item_homepage_extra, viewGroup, false);

        return new HomepageExtrasAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull HomepageExtrasAdapter.ViewHolder viewHolder, int i) {
        String image;

        // Temporary fix to get the items fill the space, remove these 3 lines whenever you have more items
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float pxWidth = displayMetrics.widthPixels;
        viewHolder.right_guideline.setGuidelineBegin((int)pxWidth / 2);
        viewHolder.bottom_guideline.setGuidelineBegin(((int)pxWidth / 2));


        setFadeAnimation(viewHolder.itemView);
        /*if(dishes != null && dishes.size()>1)
            viewHolder.cardViewBg.setLayoutParams(new ConstraintLayout.LayoutParams(viewHolder.cardViewBg.getMeasuredWidth() - 20, viewHolder.cardViewBg.getMeasuredHeight()));*/
        if (images != null && !images.isEmpty()) {
            image = images.get(i);
            if (i == 0) {
                viewHolder.item_title.setText("SNACKS");
                Picasso.get().load(image).into(viewHolder.item_bg);
                viewHolder.itemContainer.setOnClickListener(e -> {
                    if (!ShoppingCart.getInstance().isEmpty() && !(ShoppingCart.getInstance().getRestoID() == 251 || ShoppingCart.getInstance().getRestoID() == 0)) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle("Vider le panier et changer de restaurant ?");

                        builder.setPositiveButton("OK", (dialog, which) -> {
                            ShoppingCart.getInstance().clear();
                            SharedPreferences sharedPreferences = context.getSharedPreferences("itemsList", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.clear();
                            editor.apply();
                            Intent intent = new Intent(context, SnacksActivity.class);
                            intent.putExtra("EXTRA_SESSION_ID", "251");
                            context.startActivity(intent);
                        });
                        builder.setNegativeButton("Annuler", (dialog, which) -> dialog.cancel());

                        AlertDialog dialog = builder.create();
                        dialog.setOnShowListener(arg0 -> {
                            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#33b998"));
                            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#33b998"));
                        });
                        dialog.show();
                    } else {
                        Intent intent = new Intent(context, SnacksActivity.class);
                        intent.putExtra("EXTRA_SESSION_ID", "251");
                        context.startActivity(intent);
                    }


                });
            } else {
                viewHolder.item_title.setText("COURSIER");
                Picasso.get().load(image).into(viewHolder.item_bg);
                viewHolder.itemContainer.setOnClickListener(e -> {
                    //Toast.makeText(context, "Ce service sera bientôt disponible", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context, DeliveryActivity.class);
                    context.startActivity(intent);
                });
            }



        }
    }

    @Override
    public int getItemCount() {
        if (images == null)
            return 0;
        return images.size();
    }

    public void setImages(ArrayList<String> images) {
        this.images = images;
    }

    private void setFadeAnimation(View view) {
        AlphaAnimation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(FADE_DURATION);
        view.startAnimation(anim);
    }


}
