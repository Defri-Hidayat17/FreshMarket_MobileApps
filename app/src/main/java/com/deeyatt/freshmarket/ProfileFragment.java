package com.deeyatt.freshmarket;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProfileFragment extends Fragment {

    private static final int REQUEST_PERMISSIONS = 123;
    private static final int PICK_IMAGE_REQUEST = 100;
    private static final int CAMERA_REQUEST = 101;

    private CircleImageView imageProfile;
    private ImageView btnEdit;
    private TextView textView11;

    private ApiService apiService;

    public ProfileFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        imageProfile = view.findViewById(R.id.imageProfile);
        btnEdit = view.findViewById(R.id.btnEdit);
        textView11 = view.findViewById(R.id.textView11);

        // Retrofit init
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.1.34/freshmarket/") // ganti dengan IP server kamu
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiService = retrofit.create(ApiService.class);

        View.OnClickListener clickListener = v -> {
            playScaleAnimation(v);
            requestPermissions();
        };

        imageProfile.setOnClickListener(clickListener);
        btnEdit.setOnClickListener(clickListener);

        initMenu(view);
        setupMenuItems(view);
    }

    @Override
    public void onResume() {
        super.onResume();

        // Ambil email user login aktif
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String email = currentUser.getEmail();

            // Reset agar data lama tidak nyangkut
            textView11.setText("");
            imageProfile.setImageResource(R.drawable.baseline_person_24);

            loadProfileFromServer(email);
        } else {
            Toast.makeText(getContext(), "Email pengguna tidak ditemukan", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadProfileFromServer(String email) {
        apiService.getProfile(email).enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                if (response.isSuccessful() && response.body() != null
                        && "success".equals(response.body().status)) {

                    UserModel.Data user = response.body().data;
                    textView11.setText(user.name);

                    if (user.photo != null && !user.photo.isEmpty()) {
                        Glide.with(requireContext())
                                .load(user.photo)
                                .placeholder(R.drawable.baseline_person_24) // default jika kosong
                                .into(imageProfile);
                    }
                } else {
                    Toast.makeText(getContext(), "Gagal memuat profil", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initMenu(View view) {
        int[] menuIds = {R.id.itemEditProfil, R.id.itemVoucher, R.id.itemPesanan,
                R.id.itemPengaturanAkun, R.id.itemNotifikasi, R.id.itemHubungi, R.id.itemTentang};
        Class<?>[] menuClasses = {EditProfileActivity.class, VoucherPage.class, PesananPage.class,
                PengaturanAkunActivity.class, PengaturanNotifikasi.class, HubungiKami.class, TentangKami.class};

        for (int i = 0; i < menuIds.length; i++) {
            View item = view.findViewById(menuIds[i]);
            if (item != null) {
                Class<?> destination = menuClasses[i];
                item.setOnClickListener(v -> {
                    playScaleAnimation(v);
                    v.postDelayed(() -> {
                        Intent intent = new Intent(requireContext(), destination);
                        startActivity(intent);
                        requireActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    }, 150);
                });
            }
        }

        View itemLogout = view.findViewById(R.id.itemLogout);
        if (itemLogout != null) {
            itemLogout.setOnClickListener(this::showLogoutDialog);
        }
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

    private void showLogoutDialog(View anchorView) {
        playScaleAnimation(anchorView);

        Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_logout);
        dialog.setCancelable(true);

        Window window = dialog.getWindow();
        if (window != null) {
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            window.setDimAmount(0.6f);
            window.setGravity(Gravity.CENTER);
        }

        Button btnTidak = dialog.findViewById(R.id.btnTidak);
        Button btnIya = dialog.findViewById(R.id.btnIya);

        btnTidak.setOnClickListener(v -> dialog.dismiss());

        btnIya.setOnClickListener(v -> {
            dialog.dismiss();

            SharedPreferences sharedPreferences = requireContext().getSharedPreferences("loginPrefs", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("rememberMe", false);
            editor.apply();

            FirebaseAuth.getInstance().signOut();

            Toast.makeText(requireContext(), "Berhasil logout akun", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(() -> {
                Intent intent = new Intent(requireContext(), loginpage.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }, 500);
        });

        dialog.show();
    }

    private void setupMenuItems(View view) {
        setMenuItem(view, R.id.itemEditProfil, "Edit Profil", R.drawable.baseline_edit_24);
        setMenuItem(view, R.id.itemVoucher, "Voucher Saya", R.drawable.baseline_card_giftcard_24);
        setMenuItem(view, R.id.itemPesanan, "Pesanan Saya", R.drawable.baseline_shopping_bag_24);
        setMenuItem(view, R.id.itemPengaturanAkun, "Pengaturan Akun", R.drawable.baseline_settings_24);
        setMenuItem(view, R.id.itemNotifikasi, "Notifikasi", R.drawable.baseline_notifications_24);
        setMenuItem(view, R.id.itemHubungi, "Hubungi Kami", R.drawable.baseline_call_24);
        setMenuItem(view, R.id.itemTentang, "Tentang Kami", R.drawable.baseline_info_24);
    }

    private void setMenuItem(View root, int id, String text, int iconRes) {
        View item = root.findViewById(id);
        if (item != null) {
            ImageView icon = item.findViewById(R.id.iconMenu);
            TextView label = item.findViewById(R.id.textMenu);
            if (icon != null) icon.setImageResource(iconRes);
            if (label != null) label.setText(text);
        }
    }

    private void requestPermissions() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_EXTERNAL_STORAGE
            }, REQUEST_PERMISSIONS);
        } else {
            showImagePickerDialog();
        }
    }

    private void showImagePickerDialog() {
        BottomSheetDialog dialog = new BottomSheetDialog(requireContext());
        View sheet = getLayoutInflater().inflate(R.layout.dialog_image_picker, null);
        dialog.setContentView(sheet);

        sheet.findViewById(R.id.btnKamera).setOnClickListener(v -> {
            openCamera();
            dialog.dismiss();
        });

        sheet.findViewById(R.id.btnGaleri).setOnClickListener(v -> {
            openGallery();
            dialog.dismiss();
        });

        dialog.show();
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    private void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(requireActivity().getPackageManager()) != null) {
            startActivityForResult(intent, CAMERA_REQUEST);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == AppCompatActivity.RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            imageProfile.setImageURI(imageUri);
        } else if (requestCode == CAMERA_REQUEST && resultCode == AppCompatActivity.RESULT_OK && data != null && data.getExtras() != null) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            imageProfile.setImageBitmap(photo);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSIONS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                showImagePickerDialog();
            } else {
                Toast.makeText(getContext(), "Akses ditolak.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
