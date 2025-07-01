package com.deeyatt.freshmarket;

import android.animation.ObjectAnimator;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class HubungiKami extends AppCompatActivity {

    ImageView btnBack, imageView30, imageView31;
    FrameLayout blurOverlay, contactSheet;
    CardView cardWa, cardIg, cardFb, cardEmail, cardTt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hubungi_kami);

        // Inisialisasi view
        btnBack = findViewById(R.id.btnBack);
        imageView30 = findViewById(R.id.imageView30);
        imageView31 = findViewById(R.id.imageView31);
        blurOverlay = findViewById(R.id.blurOverlay);
        contactSheet = findViewById(R.id.contactSheet);

        cardWa = findViewById(R.id.cardWa);
        cardIg = findViewById(R.id.cardIg);
        cardFb = findViewById(R.id.cardFb);
        cardEmail = findViewById(R.id.cardEmail);
        cardTt = findViewById(R.id.cardTt);


        // Animasi pulse terus menerus
        ObjectAnimator scaleUpX = ObjectAnimator.ofFloat(imageView31, "scaleX", 1f, 1.1f);
        ObjectAnimator scaleUpY = ObjectAnimator.ofFloat(imageView31, "scaleY", 1f, 1.1f);
        scaleUpX.setRepeatCount(ObjectAnimator.INFINITE);
        scaleUpY.setRepeatCount(ObjectAnimator.INFINITE);
        scaleUpX.setRepeatMode(ObjectAnimator.REVERSE);
        scaleUpY.setRepeatMode(ObjectAnimator.REVERSE);
        scaleUpX.setDuration(600);
        scaleUpY.setDuration(600);

        scaleUpX.start();
        scaleUpY.start();

        // Tombol kembali
        btnBack.setOnClickListener(v -> {
            playScaleAnimation(v);
            new Handler().postDelayed(() -> {
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }, 150);
        });

        // Saat gambar utama diklik
        imageView30.setOnClickListener(v -> {
            playScaleAnimation(v);
            new Handler().postDelayed(this::showContactSheet, 100);
        });

        // Klik di luar sheet (blur area) untuk dismiss
        blurOverlay.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                hideContactSheet();
            }
            return true;
        });

        // Semua CardView kontak dengan animasi + aksi
        setClickWithAnimation(cardWa, () -> openWhatsApp("6285210619890"));
        setClickWithAnimation(cardIg, () -> openInstagram("freshmarket2.id"));
        setClickWithAnimation(cardFb, () -> openFacebook("Defri Lugas Hidayat"));
        setClickWithAnimation(cardEmail, () -> openEmail("attrsyahdffa@gmail.com"));
        setClickWithAnimation(cardTt, () -> openTiktok("dffaatrsyh_"));
    }

    private void showContactSheet() {
        blurOverlay.setVisibility(View.VISIBLE);
        contactSheet.setVisibility(View.VISIBLE);
        contactSheet.animate().translationY(0).setDuration(300).start();
    }

    private void hideContactSheet() {
        contactSheet.animate().translationY(contactSheet.getHeight())
                .setDuration(300)
                .withEndAction(() -> {
                    contactSheet.setVisibility(View.GONE);
                    blurOverlay.setVisibility(View.GONE);
                }).start();
    }

    private void playScaleAnimation(View view) {
        ScaleAnimation scale = new ScaleAnimation(
                1f, 0.9f, 1f, 0.9f,
                ScaleAnimation.RELATIVE_TO_SELF, 0.5f,
                ScaleAnimation.RELATIVE_TO_SELF, 0.5f);
        scale.setDuration(100);
        scale.setRepeatCount(1);
        scale.setRepeatMode(ScaleAnimation.REVERSE);
        view.startAnimation(scale);
    }

    private void setClickWithAnimation(View view, Runnable action) {
        view.setOnClickListener(v -> {
            playScaleAnimation(v);
            new Handler().postDelayed(action, 150);
        });
    }

    private void openWhatsApp(String phoneNumber) {
        String url = "https://wa.me/" + phoneNumber;
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        intent.setPackage("com.whatsapp");
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "WhatsApp tidak terpasang", Toast.LENGTH_SHORT).show();
        }
    }

    private void openInstagram(String username) {
        String url = "https://instagram.com/" + username;
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
    }

    private void openFacebook(String name) {
        String url = "https://facebook.com/" + name.replace(" ", "");
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
    }

    private void openEmail(String email) {
        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + email));
        startActivity(Intent.createChooser(intent, "Kirim email ke kami"));
    }

    private void openTiktok(String username) {
        String url = "https://www.tiktok.com/@" + username;
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
    }
}
