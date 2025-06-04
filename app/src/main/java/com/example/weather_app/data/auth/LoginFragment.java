package com.example.weather_app.data.auth;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.weather_app.presentation.MainActivity;
import com.example.weather_app.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginFragment extends Fragment {

    private EditText etEmail, etPassword;
    private Button btnLogin;
    private TextView tvGoRegister;
    private FirebaseAuth auth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // «v» — корневой View нашего фрагмента
        View v = inflater.inflate(R.layout.fragment_login, container, false);

        // биндим поля
        etEmail        = v.findViewById(R.id.username_et);
        etPassword     = v.findViewById(R.id.password_et);
        btnLogin       = v.findViewById(R.id.login_btn);
        tvGoRegister   = v.findViewById(R.id.register_btn);

        // инстанс FirebaseAuth
        auth = FirebaseAuth.getInstance();

        // логика кнопки «Войти»
        btnLogin.setOnClickListener(btn -> {
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

            btnLogin.setEnabled(false);
            auth.signInWithEmailAndPassword(email, pass)
                    .addOnCompleteListener(task -> {
                        btnLogin.setEnabled(true);
                        if (task.isSuccessful()) {
                            FirebaseUser user = auth.getCurrentUser();
                            if (getActivity() instanceof MainActivity) {
                                ((MainActivity) getActivity()).onAuthSuccess(user);
                            }
                        } else {
                            Toast.makeText(requireContext(),
                                    "Ошибка входа: " + task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        // логика перехода на фрагмент регистрации
        tvGoRegister.setOnClickListener(ignore -> {
            // Навигация по действию, заданному в навиграфе
            NavHostFragment.findNavController(this)
                    .navigate(R.id.action_loginFragment_to_registerFragment);
        });

        return v;
    }
}
