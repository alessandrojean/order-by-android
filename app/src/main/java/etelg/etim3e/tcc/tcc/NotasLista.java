package etelg.etim3e.tcc.tcc;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
import com.nispok.snackbar.enums.SnackbarType;
import com.nispok.snackbar.listeners.ActionClickListener;

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

import etelg.etim3e.tcc.tcc.model.Materia;
import etelg.etim3e.tcc.tcc.model.Nota;


public final class NotasLista extends Fragment{

    private RecyclerView mRecyclerView;

    private String resultado = "";

    private SwipeRefreshLayout mSwipeRefreshLayout;

    private boolean carregou=false;
    private int bimestre;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View myFragmentView = inflater.inflate(R.layout.fragment_notas_lista, container, false);

        //Pega o bimestre definido pelo Downloader
        bimestre = getArguments().getInt("bimestre",1);

        //Obtem da View
        mRecyclerView = (RecyclerView) myFragmentView.findViewById(R.id.rv_notas);
        mRecyclerView.setHasFixedSize(true);

        //Define o LinearLayoutManager, que exibe os itens na vertical
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(llm);

        //Obtem da View
        mSwipeRefreshLayout = (SwipeRefreshLayout) myFragmentView.findViewById(R.id.srl_nota);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorAccent, R.color.colorPrimaryDark);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Recarregar();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

        //Pega as notas definidas
        ArrayList<Nota> n = getArguments().getParcelableArrayList("notas");

        //Define as notas no Adapter
        NotaAdapter adapter = new NotaAdapter(getActivity(), n);
        mRecyclerView.setAdapter(adapter);

        return myFragmentView;
    }

    private void Recarregar()
    {
        //Verifica se há internet
        if (Utils.haveInternet(getActivity())) {
            //Carrega as Faltas
            new DownloadJSON().execute(Utils.getUrlApiNota(getActivity().getBaseContext()).replace("$rm",ActivityPrincipal.mAluno.getRm()).replace("$bim", String.valueOf(bimestre)));
        } else
            SnackbarManager.show(
                    Snackbar.with(getActivity())
                            .text("Por favor verifique sua conexão com a Internet")
                            .type(SnackbarType.MULTI_LINE)
                            .actionLabel("CONECTAR")
                            .actionColor(getResources().getColor(R.color.colorAccent))
                            .duration(Snackbar.SnackbarDuration.LENGTH_INDEFINITE)
                            .actionListener(new ActionClickListener() {
                                @Override
                                public void onActionClicked(Snackbar snackbar) {
                                    //Inicia as configurações de rede
                                    startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                                }
                            })
            );
    }

    //Método responsável pelo Instanciamento
    public static NotasLista newInstance(ArrayList<Nota> notas, int bimestre)
    {
        //Instancia e define os argumentos
        NotasLista notasLista = new NotasLista();
        Bundle args = new Bundle();
        args.putInt("bimestre",bimestre);
        args.putParcelableArrayList("notas",notas);
        notasLista.setArguments(args);
        return notasLista;
    }

    //Classe que faz o download individual
    private class DownloadJSON extends AsyncTask<String, Void, String> {

        //Método que realiza o processamento em background
        @Override
        protected String doInBackground(String... URL) {

            String json = "";
            for (String s : URL) {
                //Instancia um cliente http, que fará a requisição do json
                DefaultHttpClient lHttpClient = new DefaultHttpClient();
                //Define um HttpGet, que contém a url
                HttpGet lHttpGet = new HttpGet(s);
                try {
                    //Executa e obtem a resposta da página da API
                    HttpResponse lHttpResponse = lHttpClient.execute(lHttpGet);
                    //Lê o resultado
                    BufferedReader reader = new BufferedReader(new InputStreamReader(lHttpResponse.getEntity().getContent(), "UTF-8"));

                    json += reader.readLine();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            return json;
        }

        @Override
        protected void onPostExecute(String result) {
            resultado = result;

            //Verifica se houve êxito
            if(!resultado.equals("")) {
                carregou = true;

                NotaAdapter adapter = null;
                try {
                    //Define o NotaAdapter
                    adapter = new NotaAdapter(getActivity(), getNota(resultado));
                    NotasFragment.mNotas.set(bimestre - 1, getNota(resultado));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //Define o Adapter no RecyclerView
                mRecyclerView.setAdapter(adapter);
                mSwipeRefreshLayout.post(new Runnable() {
                    @Override
                    public void run() {
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                });
            }
            else
            {
                new MaterialDialog.Builder(getActivity())
                        .title("Erro")
                        .content("Erro ao obter os dados!")
                        .positiveText("OK")
                        .cancelable(false)
                        .show();
            }
        }

        //Método que converte o JSON para objetos
        private ArrayList<Nota> getNota(String json) throws JSONException
        {
            ArrayList<Nota> notas = new ArrayList<Nota>();

            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject.getJSONArray("notas");
            //Percorre todos os resultados
            for(int i=0;i<jsonArray.length();i++)
            {
                JSONObject nota = jsonArray.getJSONObject(i);

                Nota n = new Nota();
                n.setId(nota.getInt("id"));
                n.setNota(nota.getString("nota"));

                JSONObject materia = nota.getJSONObject("materia");
                Materia m = new Materia();
                m.setId(materia.getInt("id"));
                m.setNome(materia.getString("nome"));
                m.setSigla(materia.getString("sigla"));

                n.setMateria(m);

                notas.add(n);
            }

            return notas;
        }
    }

}
