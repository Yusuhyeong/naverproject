/*
 * SettingFragment
 * 2023.06.07
 */

package com.example.naverproject.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.naverproject.R;

public class SettingFragment extends Fragment {
        private static final String PREFS_NAME = "MyPrefs";
        private static final String SWITCH_STATE_KEY = "switch_state";
        private SharedPreferences sharedPreferences;
        public SettingFragment() {
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
                // Inflate the layout for this fragment
                super.onCreate(savedInstanceState);

                View view = inflater.inflate(R.layout.fragment_setting, container, false);

                SwitchCompat switchButton = view.findViewById(R.id.switch1);
                ImageButton imageButton = view.findViewById(R.id.backbtn);
                sharedPreferences = getActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

                boolean savedSwitchState = sharedPreferences.getBoolean(SWITCH_STATE_KEY, true);
                switchButton.setChecked(savedSwitchState);

                imageButton.setOnClickListener(v -> {
                        FragmentManager fragmentManager = getParentFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        MainActivity activity = (MainActivity) getActivity();

                        fragmentTransaction.show(activity.homeFragment);
                        fragmentTransaction.hide(activity.settingFragment);
                        fragmentTransaction.commit();

                        activity.setSelectedMenu(R.id.home); // 하단 버튼의 색상 변경
                });

                switchButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean(SWITCH_STATE_KEY, isChecked);
                        editor.apply();
                });

                return view;
        }

        @Override
        public void onAttach(@NonNull Context context) {
                super.onAttach(context);
                if (context instanceof MainActivity) {
                        MainActivity activity = (MainActivity) context;
                        activity.setSelectedMenu(R.id.home_fragment);
                }
        }
}