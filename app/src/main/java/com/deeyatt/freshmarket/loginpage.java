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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class loginpage extends AppCompatActivity {

    private FirebaseAuth auth;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private ImageView loginBtn;
    private ImageView kotakCeklis;
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
        ImageView imageView12 = findViewById(R.id.imageView12); // Tombol login Google

        TextView textView6 = findViewById(R.id.textView6);
        TextView textView8 = findViewById(R.id.textView8);

        auth = FirebaseAuth.getInstance();
        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        // Google One Tap setup
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

        imageView12.setOnClickListener(v -> {
            oneTapClient.beginSignIn(signInRequest)
                    .addOnSuccessListener(this, result -> {
                        try {
                            startIntentSenderForResult(
                                    result.getPendingIntent().getIntentSender(),
                                    REQ_ONE_TAP,
                                    null, 0, 0, 0
                            );
                        } catch (Exception e) {
                            Toast.makeText(this, "Gagal membuka One Tap UI", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(this, e -> {
                        Toast.makeText(this, "Tidak dapat menampilkan akun Google", Toast.LENGTH_SHORT).show();
                    });
        });

        // Auto-login jika Remember Me aktif
        if (sharedPreferences.getBoolean("rememberMe", false)
                && auth.getCurrentUser() != null
                && auth.getCurrentUser().isEmailVerified()) {
            startActivity(new Intent(loginpage.this, homepage.class));
            finish();
        }

        // Tombol Login Manual
        loginBtn.setOnClickListener(v -> {
            String email = inputEmail.getText().toString().trim();
            String password = inputPassword.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(loginpage.this, "Isi Email dan Password", Toast.LENGTH_SHORT).show();
                return;
            }

            auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            FirebaseUser user = auth.getCurrentUser();
                            if (user != null && user.isEmailVerified()) {
                                Toast.makeText(loginpage.this, "Berhasil Masuk", Toast.LENGTH_SHORT).show();
                                if (sharedPreferences.getBoolean("rememberMe", false)) {
                                    editor.putBoolean("rememberMe", true);
                                    editor.apply();
                                }
                                startActivity(new Intent(loginpage.this, homepage.class));
                                finish();
                            } else {
                                Toast.makeText(loginpage.this, "Email belum diverifikasi", Toast.LENGTH_SHORT).show();
                                auth.signOut();
                            }
                        } else {
                            Toast.makeText(loginpage.this, "Email atau password salah", Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        // UI Insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Efek Scaling
        addScaleEffect(textView6);
        addScaleEffect(textView8);
        addScaleEffect(loginBtn);
        addScaleEffect(imageView12);

        textView6.setOnClickListener(v -> {
            startActivity(new Intent(loginpage.this, resetpassword.class));
            finish();
        });

        textView8.setOnClickListener(v -> {
            startActivity(new Intent(loginpage.this, daftarakunpage.class));
        });

        final boolean[] isPasswordVisible = {false};
        inputPassword.setCompoundDrawablesWithIntrinsicBounds(R.drawable.lock, 0, R.drawable.eye_off, 0);
        inputPassword.setOnTouchListener((v, event) -> {
            final int DRAWABLE_RIGHT = 2;
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (event.getRawX() >= (inputPassword.getRight()
                        - inputPassword.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
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

        boolean isRememberMe = sharedPreferences.getBoolean("rememberMe", false);
        kotakCeklis.setImageResource(isRememberMe ? R.drawable.kotak_ceklis_on : R.drawable.kotak_ceklis);
        kotakCeklis.setOnClickListener(v -> {
            boolean currentStatus = sharedPreferences.getBoolean("rememberMe", false);
            editor.putBoolean("rememberMe", !currentStatus);
            editor.apply();
            kotakCeklis.setImageResource(!currentStatus ? R.drawable.kotak_ceklis_on : R.drawable.kotak_ceklis);
        });
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = auth.getCurrentUser();
                        if (user != null) {
                            Toast.makeText(loginpage.this, "Login Google berhasil!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(loginpage.this, homepage.class));
                            finish();
                        }
                    } else {
                        Toast.makeText(loginpage.this, "Autentikasi Google gagal", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(this, "Login gagal ", Toast.LENGTH_SHORT).show();
            }
        }
    }

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
