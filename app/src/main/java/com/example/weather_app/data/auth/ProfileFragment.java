package com.example.weather_app.data.auth;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavOptions;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import com.example.weather_app.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileFragment extends Fragment {

    private ImageView ivAvatar;
    private TextView tvName, tvEmail;
    private Button btnLogout;
    private FirebaseAuth auth;

    @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // inflate your fragment_profile.xml
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // инициализируем view и FirebaseAuth
        ivAvatar   = view.findViewById(R.id.profile_avatar);
        tvName     = view.findViewById(R.id.profile_name);
        tvEmail    = view.findViewById(R.id.profile_email);
        btnLogout  = view.findViewById(R.id.btn_logout);
        auth       = FirebaseAuth.getInstance();

        // получаем текущего пользователя
        FirebaseUser user = auth.getCurrentUser();
        if (user == null) {
            // если вдруг не залогинен — сразу на экран логина
            navigateToLogin(view);
            return;
        }

        // заполняем имя и e-mail
        String displayName = user.getDisplayName();
        if (displayName == null || displayName.isEmpty()) {
            displayName = user.getEmail() != null
                    ? user.getEmail().split("@")[0]
                    : getString(R.string.profile_email_placeholder);
        }
        tvName.setText(displayName);
        tvEmail.setText(user.getEmail());

        // здесь можно загрузить пользовательское фото в ivAvatar, если оно есть

        // кнопка «Выйти»
        btnLogout.setOnClickListener(v -> {
            auth.signOut();
            navigateToLogin(view);
        });
    }

    private void navigateToLogin(View view) {
        NavController nav = Navigation.findNavController(view);
        // очищаем back-stack чтобы нельзя было назад попасть в профиль
        NavOptions opts = new NavOptions.Builder()
                .setPopUpTo(R.id.navigation_today, true)
                .build();
        nav.navigate(R.id.loginFragment, null, opts);
    }
}
