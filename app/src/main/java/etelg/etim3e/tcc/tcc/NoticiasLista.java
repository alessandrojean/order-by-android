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
import android.widget.RelativeLayout;
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
import etelg.etim3e.tcc.tcc.adapters.NoticiaAdapter;
import etelg.etim3e.tcc.tcc.classes.Utils;
import etelg.etim3e.tcc.tcc.downloaders.DownloadNoticias;
import etelg.etim3e.tcc.tcc.model.Materia;
import etelg.etim3e.tcc.tcc.model.Nota;
import etelg.etim3e.tcc.tcc.model.Noticia;


public final class NoticiasLista extends Fragment {

    private RecyclerView mRecyclerView;
    public static ArrayList<Noticia> mNoticias = null;
    public static boolean erro = false;
    private RelativeLayout mRelativeLayout;


    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View myFragmentView = inflater.inflate(R.layout.fragment_noticias_lista, container, false);

        //Obtem da View
        mRecyclerView = (RecyclerView) myFragmentView.findViewById(R.id.rv_noticias);
        mRecyclerView.setHasFixedSize(true);

        //Define o LinearLayoutManager, que exibe os itens na vertical
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(llm);

        //Obtem da View
        mRelativeLayout = (RelativeLayout) myFragmentView.findViewById(R.id.pb_noticia);

        //Obtem da View
        mSwipeRefreshLayout = (SwipeRefreshLayout) myFragmentView.findViewById(R.id.srl_noticias);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorAccent, R.color.colorPrimaryDark);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Recarregar();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

        //Verifica se as notícias existem
        if (mNoticias == null)
            //Baixa as notícias
            new DownloadNoticias(container.getContext(), mRelativeLayout, mRecyclerView).execute(Utils.getUrlApiNoticia(getActivity()));
        else {
            //Define o Adapter e mostra as notícias
            NoticiaAdapter n = new NoticiaAdapter(container.getContext(), mNoticias);
            mRecyclerView.setAdapter(n);
            mRecyclerView.setVisibility(View.VISIBLE);
            mRelativeLayout.findViewById(R.id.pb_noticia_indeterminate).setVisibility(View.GONE);
            mRelativeLayout.findViewById(R.id.tv_erro).setVisibility(View.GONE);
        }
        return myFragmentView;
    }

    private void Recarregar() {
        //Verifica se há internet
        if (Utils.haveInternet(getActivity())) {
            //Carrega as Notícias
            new DownloadNoticias(getActivity(), mRelativeLayout, mRecyclerView).execute(Utils.getUrlApiNoticia(getActivity()));
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

}
