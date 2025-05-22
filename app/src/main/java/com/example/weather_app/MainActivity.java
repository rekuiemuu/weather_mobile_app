package com.example.weather_app;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.weather_app.util.SharedPreferences;
import com.example.weather_app.viewmodel.ForecastViewModel;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {
    private ForecastViewModel viewModel;
    private AppBarConfiguration appBarConfiguration;
    private TextView navLoginTv, navEmailTv;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        NavHostFragment navHost =
                (NavHostFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.nav_host_fragment);
        NavController navController = navHost.getNavController();


        // 2) Инициализируем шапку бокового меню и её поля
        drawerLayout = findViewById(R.id.drawerLayout);
        NavigationView navigationView = findViewById(R.id.navView);
        View header = navigationView.getHeaderView(0);
        navLoginTv = header.findViewById(R.id.login_acc);
        navEmailTv = header.findViewById(R.id.email_acc);

        // 3) Проверяем, залогинен ли пользователь, и заполняем шапку
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String name = user.getDisplayName();
            if (TextUtils.isEmpty(name) && user.getEmail() != null) {
                name = user.getEmail().split("@")[0];
            }
            navLoginTv.setText(name != null ? name : "—");
            navEmailTv.setText(user.getEmail());
        } else {
            navLoginTv.setText("Гость");
            navEmailTv.setText("");
        }

        // 4) Клик на header — если не залогинен, переходим на Login, иначе — на Profile
        header.setOnClickListener(v -> {
            FirebaseUser u = FirebaseAuth.getInstance().getCurrentUser();
            if (u == null) {
                navController.navigate(R.id.loginFragment);
            } else {
                navController.navigate(R.id.profileFragment);
            }
            drawerLayout.closeDrawer(GravityCompat.START);
        });


        // 5) Инициализируем ViewModel
        viewModel = new ViewModelProvider(this).get(ForecastViewModel.class);

        // 6) Настраиваем BottomNavigationView и привязываем его к NavController
        BottomNavigationView bottomNav = findViewById(R.id.nav_view);

        AppBarConfiguration appBarConfiguration =
                new AppBarConfiguration.Builder(navController.getGraph())
                        .setOpenableLayout(drawerLayout)
                        .build();

//        // 7) Настраиваем AppBarConfiguration, указываем, какие фрагменты — корневые
//        appBarConfiguration = new AppBarConfiguration.Builder(
//                R.id.navigation_today,
//                R.id.navigation_weekly,
//                R.id.navigation_share,
//                R.id.settings,
//                R.id.about
//        )
//                .setOpenableLayout(drawerLayout)
//                .build();


        // 8) Привязываем ActionBar, NavigationView и BottomNavigationView к NavController
        NavigationUI.setupWithNavController(toolbar, navController, appBarConfiguration);
        NavigationUI.setupWithNavController((NavigationView) findViewById(R.id.navView), navController);
        NavigationUI.setupWithNavController(bottomNav, navController);

        // 9) Обрабатываем пункт «Share» при смене вкладки
        navController.addOnDestinationChangedListener((controller, destination, args) -> {
            if (destination.getId() == R.id.navigation_share) {
                String temp = SharedPreferences.getInstance(this).getTemp();
                String desc = SharedPreferences.getInstance(this).getDesc();
                Intent sendIntent = new Intent(Intent.ACTION_SEND)
                        .putExtra(Intent.EXTRA_TEXT,
                                "Today's weather is " + desc +
                                        " with temperature: " + temp)
                        .setType("text/plain");
                if (sendIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(sendIntent);
                }
            }
        });
    }


    // Теперь appBarConfiguration не null:
    @Override
    public boolean onSupportNavigateUp() {
        NavController navController =
                Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

    // Меню поиска:
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem searchItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setQueryHint("Введите город");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override public boolean onQueryTextSubmit(String query) {
                if (query == null || query.trim().isEmpty()) return false;
                // Используем инициализированный viewModel:
                viewModel.fetchResults(query.trim(), "7")
                        .observe(MainActivity.this, resource -> {
                            switch (resource.status) {
                                case SUCCESS:
                                    Toast.makeText(MainActivity.this,
                                            "Прогноз обновлён", Toast.LENGTH_SHORT).show();
                                    break;
                                case ERROR:
                                    Toast.makeText(MainActivity.this,
                                            "Ошибка загрузки прогноза", Toast.LENGTH_SHORT).show();
                                    break;
                            }
                        });
                searchView.clearFocus();
                return true;
            }
            @Override public boolean onQueryTextChange(String newText) { return false; }
        });
        return true;
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawerLayout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    public void onAuthSuccess(FirebaseUser user) {
        if (user == null) return;

        // Обновляем header
        String displayName = user.getDisplayName();
        if (TextUtils.isEmpty(displayName) && user.getEmail() != null) {
            displayName = user.getEmail().split("@")[0];
        }
        navLoginTv.setText(displayName);
        navEmailTv.setText(user.getEmail());

        // Переходим на Today-фрагмент
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        navController.navigate(R.id.navigation_today);

        // Закрываем drawer
        drawerLayout.closeDrawer(GravityCompat.START);
    }
}