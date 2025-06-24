package com.deeyatt.freshmarket;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.cardview.widget.CardView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.TextView;

public class CartFragment extends Fragment {

    // CardView 1
    private int quantity = 1;
    private TextView textQuantity;
    private ImageView buttonIncrease, buttonDecrease, buttonDelete;
    private CardView cardItem;

    // CardView 2
    private int quantity2 = 1;
    private TextView textQuantity2;
    private ImageView buttonIncrease2, buttonDecrease2, buttonDelete2;
    private CardView cardItem2;

    public CartFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate layout fragment_cart.xml
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        // ====================
        // CardView 1 - Beras
        // ====================
        textQuantity = view.findViewById(R.id.textQuantity);
        buttonIncrease = view.findViewById(R.id.buttonIncrease);
        buttonDecrease = view.findViewById(R.id.buttonDecrease);
        buttonDelete = view.findViewById(R.id.buttonDelete);
        cardItem = view.findViewById(R.id.cardItem);

        buttonIncrease.setOnClickListener(v -> {
            quantity++;
            textQuantity.setText(String.valueOf(quantity));
            applyScaleAnimation(buttonIncrease);
        });

        buttonDecrease.setOnClickListener(v -> {
            if (quantity > 1) {
                quantity--;
                textQuantity.setText(String.valueOf(quantity));
                applyScaleAnimation(buttonDecrease);
            }
        });

        buttonDelete.setOnClickListener(v -> {
            applyScaleAnimation(buttonDelete);
            cardItem.setVisibility(View.GONE);
        });

        // ====================
        // CardView 2 - Indomie
        // ====================
        textQuantity2 = view.findViewById(R.id.textQuantity2);
        buttonIncrease2 = view.findViewById(R.id.buttonIncrease2);
        buttonDecrease2 = view.findViewById(R.id.buttonDecrease2);
        buttonDelete2 = view.findViewById(R.id.buttonDelete2);
        cardItem2 = view.findViewById(R.id.cardItem2);

        buttonIncrease2.setOnClickListener(v -> {
            quantity2++;
            textQuantity2.setText(String.valueOf(quantity2));
            applyScaleAnimation(buttonIncrease2);
        });

        buttonDecrease2.setOnClickListener(v -> {
            if (quantity2 > 1) {
                quantity2--;
                textQuantity2.setText(String.valueOf(quantity2));
                applyScaleAnimation(buttonDecrease2);
            }
        });

        buttonDelete2.setOnClickListener(v -> {
            applyScaleAnimation(buttonDelete2);
            cardItem2.setVisibility(View.GONE);
        });

        return view;
    }

    // Fungsi animasi scale (sama untuk semua)
    private void applyScaleAnimation(View view) {
        ScaleAnimation scaleAnimation = new ScaleAnimation(
                1f, 1.2f,
                1f, 1.2f,
                ScaleAnimation.RELATIVE_TO_SELF, 0.5f,
                ScaleAnimation.RELATIVE_TO_SELF, 0.5f
        );
        scaleAnimation.setDuration(150);
        scaleAnimation.setRepeatCount(1);
        scaleAnimation.setRepeatMode(ScaleAnimation.REVERSE);
        view.startAnimation(scaleAnimation);
    }
}
