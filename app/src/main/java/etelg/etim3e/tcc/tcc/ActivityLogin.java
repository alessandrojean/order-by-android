package etelg.etim3e.tcc.tcc;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import etelg.etim3e.tcc.tcc.adapters.NotaAdapter;
import etelg.etim3e.tcc.tcc.classes.Utils;
import etelg.etim3e.tcc.tcc.downloaders.VerificaAluno;
import etelg.etim3e.tcc.tcc.model.Aluno;


public class ActivityLogin extends AppCompatActivity {

    private Toolbar mToolbar;
    private Button btLogar;
    public static EditText txtrm;
    private CheckBox lembrar;

    private Aluno mAluno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_login);

        //Obtem as preferências do sistema
        SharedPreferences lSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        //Obtem o RM armazenado
        String rm = lSharedPreferences.getString(Aluno.KEY_RM, "");
        //Verifica se o RM não é nulo
        if (!rm.equals("")) {
            mAluno = new Aluno();
            mAluno.setRm(rm);
            mAluno.setNome(lSharedPreferences.getString(Aluno.KEY_NOME, ""));
            mAluno.setIdTurma(lSharedPreferences.getInt(Aluno.KEY_TURMA, 1));

            //Instancia uma Intent
            Intent it = new Intent(ActivityLogin.this, ActivityPrincipal.class);
            //Define o aluno para a nova Activity
            it.putExtra("aluno", mAluno);
            //Inicia a Activity
            startActivity(it);
        }

        //Obtem do View
        mToolbar = (Toolbar) findViewById(R.id.tb_login);
        mToolbar.setTitle(" ");
        //Definir o mToolbar como o SupportActionBar
        setSupportActionBar(mToolbar);

        //Instanciamento do EditText
        txtrm = (EditText) findViewById(R.id.txtrm);

        //Obtem do View
        lembrar = (CheckBox) findViewById(R.id.checkBox_login);

        //Instanciamento do Button
        btLogar = (Button) findViewById(R.id.btlogar);
        //Evento OnClickListener
        btLogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Verifica se há internet
                if (Utils.haveInternet(ActivityLogin.this)) {
                    //Verifica o Aluno
                    new VerificaAluno(Utils.getUrlApiAluno(getBaseContext()), ActivityLogin.this, lembrar.isChecked()).execute(txtrm.getText().toString());
                } else
                    new MaterialDialog.Builder(ActivityLogin.this)
                            .callback(new MaterialDialog.ButtonCallback() {
                                @Override
                                public void onNeutral(MaterialDialog dialog) {
                                    startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                                }
                            })
                            .title("Erro")
                            .content("Por favor verifique sua conexão com a Internet.")
                            .positiveText("OK")
                            .neutralText("CONECTAR")
                            .cancelable(false)
                            .show();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Infla o menu e adiciona os items para a action bar se estiver presente
        getMenuInflater().inflate(R.menu.menu_activity_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pega o ID do item selecionado no Popup Menu
        int id = item.getItemId();

        // Verifica qual item foi selecionado e faz sua ação
        if (id == R.id.action_sobre) {
            new MaterialDialog.Builder(ActivityLogin.this)
                    .title("Ajuda")
                    .content("O RM é composto por uma letra verificadora (L, C ou T), seguida pelos dois ultimos dígitos do ano da matrícula e a posição.\n\n" +
                            "Exemplo: L150001")
                    .positiveText("OK")
                    .show();

        } else if (id == R.id.action_pref) {
            //Inicia a tela de Configurações
            startActivity(new Intent(this, SettingsActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }
}
