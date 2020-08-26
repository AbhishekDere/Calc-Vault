package com.azoroapps.calcVault;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.EditTextPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import java.util.Objects;

import es.dmoral.toasty.Toasty;

public class SettingsActivity extends AppCompatActivity {
    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String TEXT = "text";
    static String pass;
    Context context;
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
        PrefManager prefManager;
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            prefManager = new PrefManager(getActivity());
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
            final EditTextPreference numberPreference = findPreference("password");
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
            numberPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    Toast.makeText(getActivity(),"Changed",Toast.LENGTH_SHORT).show();
                    pass = numberPreference.getText();
                    SharedPreferences sharedPreferences = requireActivity().getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(TEXT, pass);
                    editor.apply();
                    Toasty.success(getActivity(),"Password Saved, Re-Login",Toasty.LENGTH_SHORT).show();
                    return true;
                }
            });
        }
    }

}