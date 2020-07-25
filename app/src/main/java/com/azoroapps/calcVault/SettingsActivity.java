package com.azoroapps.calcVault;

import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.EditTextPreference;
import androidx.preference.PreferenceFragmentCompat;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings, new SettingsFragment())
                .commit();
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);

    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
            EditTextPreference numberPreference = findPreference("password");
            if (numberPreference != null) {
                numberPreference.setOnBindEditTextListener(
                        new EditTextPreference.OnBindEditTextListener() {
                            @Override
                            public void onBindEditText(@NonNull EditText editText) {
                                editText.setInputType(InputType.TYPE_CLASS_NUMBER);
                                InputFilter[] editFilters = editText.getFilters();
                                InputFilter[] newFilters = new InputFilter[editFilters.length + 1];
                                System.arraycopy(editFilters, 0, newFilters, 0, editFilters.length);
                                newFilters[editFilters.length] = new InputFilter.LengthFilter(8);
                                editText.setFilters(newFilters);
                            }
                        });
            }

        }
    }
}