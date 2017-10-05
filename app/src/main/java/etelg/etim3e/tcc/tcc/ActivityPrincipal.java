package etelg.etim3e.tcc.tcc;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.accountswitcher.AccountHeader;
import com.mikepenz.materialdrawer.accountswitcher.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.SwitchDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.OnCheckedChangeListener;
import com.squareup.picasso.Picasso;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import etelg.etim3e.tcc.tcc.classes.Utils;
import etelg.etim3e.tcc.tcc.model.Aluno;
import etelg.etim3e.tcc.tcc.model.Materia;
import etelg.etim3e.tcc.tcc.model.Nota;


public class ActivityPrincipal extends ActionBarActivity {

    private Toolbar mToolbar;


    private String jsonNotas = "";

    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;

    private int mPositionClicked=R.id.menu_noticias;

    public static Aluno mAluno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_principal);

        //Pega o aluno da ActivityLogin
        mAluno = (Aluno)getIntent().getExtras().get("aluno");

        //Obtem da View
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);

        //Instanciamento do Toolbar
        mToolbar = (Toolbar) findViewById(R.id.tb_principal);
        //Define o título da Toolbar
        mToolbar.setTitle("Etec");
        //Definir o mToolbar como o SupportActionBar
        setSupportActionBar(mToolbar);

        //Define o hamburger icon
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.open,
                R.string.close);
        //Define o listener
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        //Sincroniza
        mDrawerToggle.syncState();

        //Obtem da View
        mNavigationView = (NavigationView) findViewById(R.id.navigation_view);
        //Define o evento de troca
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                menuItem.setChecked(true);
                mDrawerLayout.closeDrawers();

                //Seleciona o item
                selecionar(menuItem.getItemId());

                return false;
            }
        });

        //Seleciona o item padrão
        selecionar(R.id.menu_noticias);

        ((TextView) findViewById(R.id.nome_drawer)).setText(mAluno.getNome());
        ((TextView) findViewById(R.id.rm_drawer)).setText(mAluno.getRm());

        //Obtem da View
        CircleImageView circleImageView = (CircleImageView) findViewById(R.id.profile_image);
        //Carrega a Imagem através da biblioteca Picasso
        Picasso.with(this).load(Utils.getUrlApiThumbnail(this).replace("$id",mAluno.getRm()).replace("$tipo","alunos")).placeholder(R.drawable.no_avatar).error(R.drawable.no_avatar).into(circleImageView);
    }

    //Método que seleciona o item no Navigation Drawer
    public void selecionar(int id)
    {
        //Escolhe entre as opções
        switch(id)
        {
            case R.id.menu_boletim:
            {
                //Pega o gerenciador de fragments do sistema
                FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
                //Define o fragment
                tx.replace(R.id.fragment_container, new NotasFragment());
                tx.commit();
                mToolbar.setSubtitle("Boletim");
                mPositionClicked = R.id.menu_boletim;
                break;
            }
            case R.id.menu_faltas:
            {
                //Pega o gerenciador de fragments do sistema
                FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
                //Define o fragment
                tx.replace(R.id.fragment_container, new FaltasFragment());
                tx.commit();
                mToolbar.setSubtitle("Faltas");
                mPositionClicked = R.id.menu_faltas;
                break;
            }
            case R.id.menu_horario:
            {
                //Pega o gerenciador de fragments do sistema
                FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
                //Define o fragment
                tx.replace(R.id.fragment_container, new HorarioFragment());
                tx.commit();
                mToolbar.setSubtitle("Horário");
                mPositionClicked = R.id.menu_horario;
                break;
            }
            case R.id.menu_noticias:
            {
                //Pega o gerenciador de fragments do sistema
                FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
                //Define o fragment
                tx.replace(R.id.fragment_container, new NoticiasLista());
                tx.commit();
                mToolbar.setSubtitle("Notícias");
                mPositionClicked = R.id.menu_noticias;
                break;
            }
        }
    }

    //Método que restora as informações da rotação de tela
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mPositionClicked = savedInstanceState.getInt("position");
        mNavigationView.getMenu().findItem(savedInstanceState.getInt("position")).setChecked(true);
        selecionar(savedInstanceState.getInt("position"));
    }

    //Método que salva as informações na rotação de tela
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("json_notas",jsonNotas);
        outState.putInt("position", mPositionClicked);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_principal, menu);
        return true;
    }

    //Método que substitui o toque do botão voltar
    @Override
    public void onBackPressed()
    {
        new MaterialDialog.Builder(ActivityPrincipal.this)
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        NotasFragment.mNotas = null;
                        NotasFragment.erro = false;
                        //Instancia o Intent
                        Intent it = new Intent(ActivityPrincipal.this, ActivityLogin.class);
                        //Inicia a Activity
                        startActivity(it);
                        finish();
                    }
                })
                .title("Confirmação")
                .content("Deseja realmente sair?")
                .positiveText("Sim")
                .negativeText("Não")
                .show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pega o ID do item selecionado no Popup Menu
        int id = item.getItemId();

        // Verifica qual item foi selecionado e faz sua ação
        if (id == R.id.action_sair) {
            new MaterialDialog.Builder(ActivityPrincipal.this)
                    .callback(new MaterialDialog.ButtonCallback() {
                        @Override
                        public void onPositive(MaterialDialog dialog) {
                            resetPreferences();
                            NotasFragment.mNotas = null;
                            NotasFragment.erro = false;
                            //Instancia o Intent
                            Intent it = new Intent(ActivityPrincipal.this, ActivityLogin.class);
                            finish();
                            //Inicia a Activity
                            startActivity(it);
                        }
                    })
                    .title("Confirmação")
                    .content("Deseja realmente sair?")
                    .positiveText("Sim")
                    .negativeText("Não")
                    .cancelable(false)
                    .show();
        } else if (id == R.id.action_pref_main) {
            //Inicia a Activity
            startActivity(new Intent(this, SettingsActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }


    private void resetPreferences() {
        SharedPreferences lSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor lEditor = lSharedPreferences.edit();
        lEditor.clear();
        lEditor.commit();
    }
}
