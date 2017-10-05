package etelg.etim3e.tcc.tcc;

import android.content.SharedPreferences;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceGroup;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class SettingsActivity extends AppCompatActivity {

    public static Toolbar mToolbar;
    public static final int PREF_HOME = 1;
    public static final int PREF_DEVELOPER=2;
    public static int escolhido=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        //Obtem a tela atual
//        escolhido = savedInstanceState.getInt("escolhido",0);

        //Obtem da View
        mToolbar = (Toolbar) findViewById(R.id.tb_settings);
        mToolbar.setTitle("Configurações");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Switch que trocará as telas de configuração
                switch(escolhido)
                {
                    case 0:
                    case PREF_HOME:
                    {
                        finish();
                        break;
                    }
                    case PREF_DEVELOPER:
                    {
                        getFragmentManager().beginTransaction().replace(R.id.content_frame, new SettingsFragment()).commit();
                        escolhido = PREF_HOME;
                        break;
                    }
                }

            }
        });

        //Verifica se é a primeira vez que a tela abre
        if(escolhido==0)
            getFragmentManager().beginTransaction().add(R.id.content_frame, new SettingsFragment()).commit();
        else
            mostrar(escolhido);
    }

    //Método que restora as informações da rotação de tela
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        escolhido = savedInstanceState.getInt("escolhido");
        mostrar(escolhido);
    }

    //Método que salva as informações na rotação de tela
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("escolhido", escolhido);
    }

    private void mostrar(int e)
    {
        //Escolhe a tela certa a exibir
        switch(e)
        {
            case 0:
            case PREF_HOME:
            {
                getFragmentManager().beginTransaction().add(R.id.content_frame, new SettingsFragment()).commit();
                break;
            }
            case PREF_DEVELOPER:
            {
                getFragmentManager().beginTransaction().replace(R.id.content_frame, new DeveloperSettingsFragment()).commit();
                break;
            }
        }
    }

    @Override
    public void onBackPressed() {
        //Escolhe a tela certa a exibir
        switch(escolhido)
        {
            case PREF_HOME:
            {
                finish();
                break;
            }
            case PREF_DEVELOPER:
            {
                getFragmentManager().beginTransaction().replace(R.id.content_frame, new SettingsFragment()).commit();
                escolhido = PREF_HOME;
                break;
            }
        }
    }

    public static class SettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {

        @Override public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_general);
            getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);

            findPreference("screen_developer").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    getFragmentManager().beginTransaction().replace(R.id.content_frame, new DeveloperSettingsFragment()).commit();
                    escolhido = PREF_DEVELOPER;
                    return true;
                }
            });
        }

        @Override
        public void onResume() {
            super.onResume();
            //Atribui os valores selecionados
            for (int i = 0; i < getPreferenceScreen().getPreferenceCount(); ++i) {
                Preference preference = getPreferenceScreen().getPreference(i);
                if (preference instanceof PreferenceGroup) {
                    PreferenceGroup preferenceGroup = (PreferenceGroup) preference;
                    for (int j = 0; j < preferenceGroup.getPreferenceCount(); ++j) {
                        updatePreference(preferenceGroup.getPreference(j));
                    }
                } else {
                    updatePreference(preference);
                }
            }
        }

        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            updatePreference(findPreference(key));
        }

        private void updatePreference(Preference preference) {
            //Mostra corretamente os objetos
            if (preference instanceof ListPreference) {
                ListPreference listPreference = (ListPreference) preference;
                listPreference.setSummary(listPreference.getEntry());
            }
            else if(preference instanceof EditTextPreference)
            {
                EditTextPreference editTextPreference = (EditTextPreference) preference;
                editTextPreference.setSummary(editTextPreference.getText());
            }
        }
    }

    public static class DeveloperSettingsFragment extends PreferenceFragment  implements SharedPreferences.OnSharedPreferenceChangeListener{

        @Override public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_developer);
            getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
        }

        @Override
        public void onResume() {
            super.onResume();
            //Atribui os valores selecionados
            for (int i = 0; i < getPreferenceScreen().getPreferenceCount(); ++i) {
                Preference preference = getPreferenceScreen().getPreference(i);
                if (preference instanceof PreferenceGroup) {
                    PreferenceGroup preferenceGroup = (PreferenceGroup) preference;
                    for (int j = 0; j < preferenceGroup.getPreferenceCount(); ++j) {
                        updatePreference(preferenceGroup.getPreference(j));
                    }
                } else {
                    updatePreference(preference);
                }
            }
        }

        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            updatePreference(findPreference(key));
        }

        private void updatePreference(Preference preference) {
            //Mostra corretamente os objetos
            if (preference instanceof ListPreference) {
                ListPreference listPreference = (ListPreference) preference;
                listPreference.setSummary(listPreference.getEntry());
            }
            else if(preference instanceof EditTextPreference)
            {
                EditTextPreference editTextPreference = (EditTextPreference) preference;
                editTextPreference.setSummary(editTextPreference.getText());
            }
        }
    }
}
