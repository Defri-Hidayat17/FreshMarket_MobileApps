package com.deeyatt.freshmarket;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.Identity;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.common.api.ApiException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class loginpage extends AppCompatActivity {

    private FirebaseAuth auth;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private ImageView loginBtn;
    private ImageView kotakCeklis;
    private boolean rememberMeChecked = false;
    private static final int REQ_ONE_TAP = 1001;
    private SignInClient oneTapClient;
    private BeginSignInRequest signInRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginpage);

        EditText inputEmail = findViewById(R.id.inputEmail);
        EditText inputPassword = findViewById(R.id.inputPassword);
        kotakCeklis = findViewById(R.id.imageView5);
        loginBtn = findViewById(R.id.imageView10);
        ImageView imageView12 = findViewById(R.id.imageView12);
        TextView textView6 = findViewById(R.id.textView6);
        TextView textView8 = findViewById(R.id.textView8);

        auth = FirebaseAuth.getInstance();
        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        oneTapClient = Identity.getSignInClient(this);
        signInRequest = BeginSignInRequest.builder()
                .setGoogleIdTokenRequestOptions(
                        BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                                .setSupported(true)
                                .setServerClientId(getString(R.string.default_web_client_id))
                                .setFilterByAuthorizedAccounts(false)
                                .build())
                .setAutoSelectEnabled(false)
                .build();

        kotakCeklis.setOnClickListener(v -> {
            rememberMeChecked = !rememberMeChecked;
            kotakCeklis.setImageResource(rememberMeChecked ? R.drawable.kotak_ceklis_on : R.drawable.kotak_ceklis);
        });

        loginBtn.setOnClickListener(v -> {
            String email = inputEmail.getText().toString().trim();
            String password = inputPassword.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Isi Email dan Password", Toast.LENGTH_SHORT).show();
                return;
            }

            auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            FirebaseUser user = auth.getCurrentUser();
                            if (user != null) {
                                if (user.isEmailVerified()) {
                                    Toast.makeText(this, "Berhasil Masuk", Toast.LENGTH_SHORT).show();
                                    editor.putBoolean("rememberMe", rememberMeChecked);
                                    editor.putBoolean("rememberMeGoogle", false);
                                    editor.apply();
                                    startActivity(new Intent(this, homepage.class));
                                    finish();
                                } else {
                                    Toast.makeText(this, "Email belum diverifikasi", Toast.LENGTH_LONG).show();
                                    auth.signOut();
                                }
                            }
                        } else {
                            Exception exception = task.getException();
                            if (exception instanceof FirebaseAuthInvalidUserException) {
                                Toast.makeText(this, "Email belum terdaftar. Silakan daftar dulu.", Toast.LENGTH_LONG).show();
                            } else if (exception instanceof FirebaseAuthInvalidCredentialsException) {
                                Toast.makeText(this, "Password salah. Coba lagi.", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(this, "Login gagal: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        });

        imageView12.setOnClickListener(v -> {
            oneTapClient.beginSignIn(signInRequest)
                    .addOnSuccessListener(this, result -> {
                        try {
                            startIntentSenderForResult(result.getPendingIntent().getIntentSender(),
                                    REQ_ONE_TAP, null, 0, 0, 0);
                        } catch (Exception e) {
                            Toast.makeText(this, "Gagal membuka One Tap UI", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(this, e -> {
                        Toast.makeText(this, "Tidak dapat menampilkan akun Google", Toast.LENGTH_SHORT).show();
                    });
        });

        textView6.setOnClickListener(v -> startActivity(new Intent(this, resetpassword.class)));
        textView8.setOnClickListener(v -> startActivity(new Intent(this, daftarakunpage.class)));

        final boolean[] isPasswordVisible = {false};
        inputPassword.setCompoundDrawablesWithIntrinsicBounds(R.drawable.lock, 0, R.drawable.eye_off, 0);
        inputPassword.setOnTouchListener((v, event) -> {
            final int DRAWABLE_RIGHT = 2;
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (event.getRawX() >= (inputPassword.getRight() - inputPassword.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                    if (isPasswordVisible[0]) {
                        inputPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        inputPassword.setCompoundDrawablesWithIntrinsicBounds(R.drawable.lock, 0, R.drawable.eye_off, 0);
                    } else {
                        inputPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                        inputPassword.setCompoundDrawablesWithIntrinsicBounds(R.drawable.lock, 0, R.drawable.eye, 0);
                    }
                    isPasswordVisible[0] = !isPasswordVisible[0];
                    inputPassword.setSelection(inputPassword.getText().length());
                    return true;
                }
            }
            return false;
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // 🚀 Tambah animasi ke tombol & elemen penting
        addScaleEffect(loginBtn);
        addScaleEffect(imageView12);
        addScaleEffect(kotakCeklis);
        addScaleEffect(textView6);
        addScaleEffect(textView8);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        boolean isRememberMe = sharedPreferences.getBoolean("rememberMe", false);
        boolean isRememberMeGoogle = sharedPreferences.getBoolean("rememberMeGoogle", false);

        if (currentUser != null && currentUser.isEmailVerified()) {
            if (isRememberMe || isRememberMeGoogle) {
                startActivity(new Intent(this, homepage.class));
                finish();
            } else {
                FirebaseAuth.getInstance().signOut();
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = auth.getCurrentUser();
                        if (user != null) {
                            Toast.makeText(this, "Login Google berhasil!", Toast.LENGTH_SHORT).show();
                            editor.putBoolean("rememberMeGoogle", true);
                            editor.putBoolean("rememberMe", false);
                            editor.apply();
                            startActivity(new Intent(this, homepage.class));
                            finish();
                        }
                    } else {
                        Toast.makeText(this, "Autentikasi Google gagal", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_ONE_TAP) {
            try {
                SignInCredential credential = oneTapClient.getSignInCredentialFromIntent(data);
                String idToken = credential.getGoogleIdToken();
                if (idToken != null) {
                    firebaseAuthWithGoogle(idToken);
                } else {
                    Toast.makeText(this, "ID Token Google tidak ditemukan", Toast.LENGTH_SHORT).show();
                }
            } catch (ApiException e) {
                Toast.makeText(this, "Login gagal", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // 🌟 Method untuk efek animasi scale
    private void addScaleEffect(View view) {
        view.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    v.animate().scaleX(1.1f).scaleY(1.1f).setDuration(100).start();
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    v.animate().scaleX(1f).scaleY(1f).setDuration(100).start();
                    break;
            }
            return false;
        });
    }
}
