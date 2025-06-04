package com.example.weather_app.data.auth;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.weather_app.presentation.MainActivity;
import com.example.weather_app.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterFragment extends Fragment {
    private EditText etEmail, etPassword;
    private Button btnRegister;
    private FirebaseAuth auth;

    @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_register, container, false);
        etEmail     = v.findViewById(R.id.email_et);
        etPassword  = v.findViewById(R.id.password_et);
        btnRegister = v.findViewById(R.id.register_btn);
        auth        = FirebaseAuth.getInstance();

        btnRegister.setOnClickListener(btn -> {
            String email = etEmail.getText().toString().trim();
            String pass  = etPassword.getText().toString().trim();

            if (TextUtils.isEmpty(email)) {
                etEmail.setError("E-mail не может быть пустым");
                return;
            }
            if (TextUtils.isEmpty(pass) || pass.length() < 6) {
                etPassword.setError("Пароль минимум 6 символов");
                return;
            }

            btnRegister.setEnabled(false);
            auth.createUserWithEmailAndPassword(email, pass)
                    .addOnCompleteListener(task -> {
                        btnRegister.setEnabled(true);
                        if (task.isSuccessful()) {
                            FirebaseUser user = auth.getCurrentUser();
                            if (getActivity() instanceof MainActivity) {
                                ((MainActivity) getActivity()).onAuthSuccess(user);
                            }
                        } else {
                            Toast.makeText(getContext(),
                                    "Ошибка регистрации: " + task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        return v;
    }
}
