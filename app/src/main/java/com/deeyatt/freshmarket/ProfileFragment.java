package com.deeyatt.freshmarket;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
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

import com.google.android.material.bottomsheet.BottomSheetDialog;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {

    private static final int REQUEST_PERMISSIONS = 123;
    private static final int PICK_IMAGE_REQUEST = 100;
    private static final int CAMERA_REQUEST = 101;

    private CircleImageView imageProfile;
    private ImageView btnEdit;

    public ProfileFragment() {}

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

        View.OnClickListener clickListener = v -> {
            playScaleAnimation(v);
            requestPermissions();
        };

        imageProfile.setOnClickListener(clickListener);
        btnEdit.setOnClickListener(clickListener);

        View itemEditProfil = view.findViewById(R.id.itemEditProfil);
        if (itemEditProfil != null) {
            itemEditProfil.setOnClickListener(v -> {
                playScaleAnimation(v);
                v.postDelayed(() -> {
                    Intent intent = new Intent(requireContext(), EditProfileActivity.class);
                    startActivity(intent);
                    requireActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }, 300);
            });
        }

        View itemVoucher = view.findViewById(R.id.itemVoucher);
        if (itemVoucher != null) {
            itemVoucher.setOnClickListener(v -> {
                playScaleAnimation(v);
                v.postDelayed(() -> {
                    Intent intent = new Intent(requireContext(), VoucherPage.class);
                    startActivity(intent);
                    requireActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }, 300);
            });
        }

        View itemPesanan = view.findViewById(R.id.itemPesanan);
        if (itemPesanan != null) {
            itemPesanan.setOnClickListener(v -> {
                playScaleAnimation(v);
                v.postDelayed(() -> {
                    Intent intent = new Intent(requireContext(), PesananPage.class);
                    startActivity(intent);
                    requireActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }, 150);
            });
        }

        View pengaturanAkun = view.findViewById(R.id.itemPengaturanAkun);
        if (pengaturanAkun != null) {
            pengaturanAkun.setOnClickListener(v -> {
                playScaleAnimation(v);
                v.postDelayed(() -> {
                    Intent intent = new Intent(requireContext(), PengaturanAkunActivity.class);
                    startActivity(intent);
                    requireActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }, 150);
            });
        }

        View Notifikasi = view.findViewById(R.id.itemNotifikasi);
        if (Notifikasi != null) {
            Notifikasi.setOnClickListener(v -> {
                playScaleAnimation(v);
                v.postDelayed(() -> {
                    Intent intent = new Intent(requireContext(), PengaturanNotifikasi.class);
                    startActivity(intent);
                    requireActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }, 150);
            });
        }

        View hubungiKami = view.findViewById(R.id.itemHubungi);
        if (hubungiKami != null) {
            hubungiKami.setOnClickListener(v -> {
                playScaleAnimation(v);
                v.postDelayed(() -> {
                    Intent intent = new Intent(requireContext(), HubungiKami.class);
                    startActivity(intent);
                    requireActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }, 150);
            });
        }

        View tentangKami = view.findViewById(R.id.itemTentang);
        if (tentangKami != null) {
            tentangKami.setOnClickListener(v -> {
                playScaleAnimation(v);
                v.postDelayed(() -> {
                    Intent intent = new Intent(requireContext(), TentangKami.class);
                    startActivity(intent);
                    requireActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }, 150);
            });
        }

        // Tambahkan logout
        View itemLogout = view.findViewById(R.id.itemLogout);
        if (itemLogout != null) {
            itemLogout.setOnClickListener(v -> showLogoutDialog(v));
        }

        setupMenuItems(view);
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
            window.setDimAmount(0.6f); // background gelap
            window.setGravity(Gravity.CENTER);
        }

        Button btnTidak = dialog.findViewById(R.id.btnTidak);
        Button btnIya = dialog.findViewById(R.id.btnIya);

        btnTidak.setOnClickListener(v -> dialog.dismiss());

        btnIya.setOnClickListener(v -> {
            dialog.dismiss();

            // Optional: logout logic kalau pakai Firebase
            // FirebaseAuth.getInstance().signOut();

            Toast.makeText(getContext(), "Logout berhasil!", Toast.LENGTH_SHORT).show();

            // Delay 500ms agar toast sempat tampil, lalu navigasi ke LoginPage
            new android.os.Handler().postDelayed(() -> {
                Intent intent = new Intent(requireContext(), loginpage.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // hapus semua activity sebelumnya
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

        if (requestCode == PICK_IMAGE_REQUEST &&
                resultCode == AppCompatActivity.RESULT_OK &&
                data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            imageProfile.setImageURI(imageUri);
        }

        if (requestCode == CAMERA_REQUEST &&
                resultCode == AppCompatActivity.RESULT_OK &&
                data != null && data.getExtras() != null) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            imageProfile.setImageBitmap(photo);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSIONS) {
            if (grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                showImagePickerDialog();
            } else {
                Toast.makeText(getContext(), "Akses ditolak.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
