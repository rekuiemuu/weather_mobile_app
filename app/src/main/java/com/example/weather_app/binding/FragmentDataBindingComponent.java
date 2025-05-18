package com.example.weather_app.binding;

import androidx.fragment.app.Fragment;

public class FragmentDataBindingComponent {
    private final FragmentBindingAdapters fragmentBindingAdapters;

    public FragmentDataBindingComponent(Fragment fragment) {
        this.fragmentBindingAdapters = new FragmentBindingAdapters(fragment);
    }

    public FragmentBindingAdapters getFragmentBindingAdapters() {
        return fragmentBindingAdapters;
    }
}
