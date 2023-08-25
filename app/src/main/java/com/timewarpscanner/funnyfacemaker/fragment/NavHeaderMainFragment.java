package com.timewarpscanner.funnyfacemaker.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.timewarpscanner.funnyfacemaker.MainActivity;
import com.timewarpscanner.funnyfacemaker.R;
import com.timewarpscanner.funnyfacemaker.databinding.NavHeaderMainBinding;

public class NavHeaderMainFragment extends Fragment {

    NavHeaderMainBinding binding;
    MainActivity activity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.nav_header_main, container, false);

        activity = (MainActivity) requireActivity();

        return binding.getRoot();
    }
}
