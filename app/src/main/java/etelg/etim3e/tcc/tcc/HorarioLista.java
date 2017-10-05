package etelg.etim3e.tcc.tcc;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
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

import etelg.etim3e.tcc.tcc.adapters.FaltaAdapter;
import etelg.etim3e.tcc.tcc.adapters.HorarioAdapter;
import etelg.etim3e.tcc.tcc.classes.Utils;

import etelg.etim3e.tcc.tcc.model.Aula;
import etelg.etim3e.tcc.tcc.model.Falta;
import etelg.etim3e.tcc.tcc.model.Materia;


public final class HorarioLista extends Fragment {

    private RecyclerView mRecyclerView;

    private String resultado = "";

    private SwipeRefreshLayout mSwipeRefreshLayout;

    private boolean carregou=false;
    private static int dia=1;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View myFragmentView = inflater.inflate(R.layout.fragment_horario_lista, container, false);

        //Pega o dia definido pelo Downloader
        dia = getArguments().getInt("dia",1);

        //Obtem da View
        mRecyclerView = (RecyclerView) myFragmentView.findViewById(R.id.rv_horario);
        mRecyclerView.setHasFixedSize(true);

        //Define o LinearLayoutManager, que exibe os itens na vertical
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(llm);

        //Obtem da View
        mSwipeRefreshLayout = (SwipeRefreshLayout) myFragmentView.findViewById(R.id.srl_horario);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorAccent, R.color.colorPrimaryDark);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Recarregar();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

        //Pega os horários definidas
        ArrayList<Aula> n = getArguments().getParcelableArrayList("aulas");

        //Define os horários no Adapter
        HorarioAdapter adapter = new HorarioAdapter(getActivity(), n);
        mRecyclerView.setAdapter(adapter);

        setHasOptionsMenu(true);
        return myFragmentView;
    }

    private void Recarregar()
    {
        //Verifica se há internet
        if (Utils.haveInternet(getActivity())) {
            //Carrega as Faltas
            new DownloadJSON().execute(Utils.getUrlApiHorario(getActivity().getBaseContext(),true).replace("$turma", String.valueOf(ActivityPrincipal.mAluno.getIdTurma())).replace("$dia", String.valueOf(dia)));
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
    public static HorarioLista newInstance(ArrayList<Aula> aulas, int dia)
    {
        //Instancia e define os argumentos
        HorarioLista notasLista = new HorarioLista();
        Bundle args = new Bundle();
        args.putInt("dia",dia);
        args.putParcelableArrayList("aulas",aulas);
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

                HorarioAdapter adapter = null;
                try {
                    //Define o HorarioAdapter
                    adapter = new HorarioAdapter(getActivity(), getHorario(resultado));
                    HorarioFragment.mAulas.set(dia - 1, getHorario(resultado));
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
        private ArrayList<Aula> getHorario(String json) throws JSONException
        {
            ArrayList<Aula> aulas = new ArrayList<Aula>();

            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject.getJSONArray("horarios");
            JSONArray d = jsonObject.getJSONObject("aulas").getJSONArray(Utils.getDia(dia));
            //Percorre todos os resultados
            for(int i=0;i<d.length();i++)
            {
                JSONObject aula = d.getJSONObject(i);

                Aula a = new Aula();
                a.setId(aula.getInt("id"));
                a.setSala(aula.getString("sala"));

                JSONObject materia = aula.getJSONObject("materia");
                Materia m = new Materia();
                m.setNome(materia.getString("nome"));
                m.setSigla(materia.getString("sigla"));

                a.setMateria(m);

                JSONArray professores = aula.getJSONArray("professores");
                ArrayList<String> p = new ArrayList<String>();
                for(int j=0;j<professores.length();j++)
                    p.add(professores.getString(j));

                a.setProfessores(p);

                a.setInicio(jsonArray.getJSONObject(i).getString("inicio"));
                a.setFim(jsonArray.getJSONObject(i).getString("fim"));

                aulas.add(a);
            }

            return aulas;
        }
    }

}
