package com.example.weather_app;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.weather_app.databinding.ActivityMainBinding;
import com.example.weather_app.ui.NavigationController;
import com.example.weather_app.util.SharedPreferences;
import com.example.weather_app.viewmodel.ForecastViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import static com.example.weather_app.model.Status.ERROR;
import static com.example.weather_app.model.Status.LOADING;
import static com.example.weather_app.model.Status.SUCCESS;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {
    private ForecastViewModel viewModel;
    private AppBarConfiguration appBarConfiguration;
    DrawerLayout drawer;
    private NavigationController navigationController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 1) Инициализируем ViewModel
        viewModel = new ViewModelProvider(this).get(ForecastViewModel.class);

        // 2) Настраиваем NavController + AppBarConfiguration
        DrawerLayout drawer = findViewById(R.id.drawerLayout);
        NavigationView drawerNav = findViewById(R.id.navView);
        BottomNavigationView bottomNav = findViewById(R.id.nav_view);

        NavController navController =
                Navigation.findNavController(this, R.id.nav_host_fragment);

        // Сохраняем в поле, а не только в локальную переменную
        appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_today,
                R.id.navigation_weekly,
                R.id.navigation_share,
                R.id.settings,
                R.id.about
        )
                .setOpenableLayout(drawer)
                .build();

        // Привязываем тулбар/бургер…
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        // …и боковое меню
        NavigationUI.setupWithNavController(drawerNav, navController);
        // …и нижнюю навигацию
        NavigationUI.setupWithNavController(bottomNav, navController);

        // Обработка "Share" сразу после навигации
        navController.addOnDestinationChangedListener((controller, destination, args) -> {
            if (destination.getId() == R.id.navigation_share) {
                String temp = SharedPreferences.getInstance(this).getTemp();
                String desc = SharedPreferences.getInstance(this).getDesc();
                Intent sendIntent = new Intent(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT,
                        "Today's weather is " + desc + " with temperature: " + temp);
                sendIntent.setType("text/plain");
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

}