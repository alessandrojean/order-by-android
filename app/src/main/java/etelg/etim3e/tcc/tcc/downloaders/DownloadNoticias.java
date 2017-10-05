package etelg.etim3e.tcc.tcc.downloaders;

import android.content.Context;
import android.os.AsyncTask;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;

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

import etelg.etim3e.tcc.tcc.NotasFragment;
import etelg.etim3e.tcc.tcc.NotasLista;
import etelg.etim3e.tcc.tcc.NoticiasLista;
import etelg.etim3e.tcc.tcc.R;
import etelg.etim3e.tcc.tcc.adapters.NoticiaAdapter;
import etelg.etim3e.tcc.tcc.adapters.TabAdapter;
import etelg.etim3e.tcc.tcc.model.Materia;
import etelg.etim3e.tcc.tcc.model.Nota;
import etelg.etim3e.tcc.tcc.model.Noticia;

/**
 * Created by Alessandro on 09/09/2015.
 */
public class DownloadNoticias extends AsyncTask<String, Void, String> {

    private Context mContext;
    private RelativeLayout mRelativeLayout;
    private RecyclerView mRecyclerView;

    private MaterialDialog mMaterialDialog;

    private ArrayList<Noticia> noticias = new ArrayList<>();

    public DownloadNoticias(Context c, RelativeLayout rl, RecyclerView r) {
        mContext = c;
        mRelativeLayout = rl;
        mRecyclerView = r;
    }

    //Método que executa antes do início do processamento
    @Override
    protected void onPreExecute() {
        mRelativeLayout.findViewById(R.id.tv_erro).setVisibility(View.GONE);
        mRelativeLayout.findViewById(R.id.pb_noticia_indeterminate).setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.GONE);
    }

    //Método que executa em background o processamento
    @Override
    protected String doInBackground(String... url) {
        String json = "";

        //Instancia um cliente http, que fará a requisição do json
        DefaultHttpClient lHttpClient = new DefaultHttpClient();
        //Define um HttpGet, que contém a url
        HttpGet lHttpGet = new HttpGet(url[0]);
        try {
            //Executa e obtem a resposta da página da API
            HttpResponse lHttpResponse = lHttpClient.execute(lHttpGet);
            //Lê o resultado
            BufferedReader reader = new BufferedReader(new InputStreamReader(lHttpResponse.getEntity().getContent(), "UTF-8"));

            json = reader.readLine();
        } catch (Exception e) {
            e.printStackTrace();
        }


        return json;
    }

    //Método que executa após o processamento
    @Override
    protected void onPostExecute(String jsons) {
        super.onPostExecute(jsons);

        try {
            //Verifica se houve resposta
            if (isValid(jsons)) {
                ArrayList<Noticia> resultados = getNoticias(jsons);

                //Mostra as informações e oculta os erros
                NoticiaAdapter adapter = new NoticiaAdapter(mContext, resultados);
                mRecyclerView.setAdapter(adapter);

                mRecyclerView.setVisibility(View.VISIBLE);
                mRelativeLayout.findViewById(R.id.tv_erro).setVisibility(View.GONE);
                mRelativeLayout.findViewById(R.id.pb_noticia_indeterminate).setVisibility(View.GONE);
                NoticiasLista.mNoticias = resultados;
            } else {
                //Mostra o erro
                mRelativeLayout.findViewById(R.id.tv_erro).setVisibility(View.VISIBLE);
                mRelativeLayout.findViewById(R.id.pb_nota_indeterminate).setVisibility(View.GONE);
                NoticiasLista.erro = true;
                NoticiasLista.mNoticias = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Método que verifica se é válido
    private boolean isValid(String json) throws JSONException {
        return new JSONObject(json).getBoolean("success");
    }

    //Método que processa o JSON e transforma em objetos
    private ArrayList<Noticia> getNoticias(String json) throws JSONException {
        ArrayList<Noticia> noticias = new ArrayList<>();

        JSONObject jsonObject = new JSONObject(json);
        JSONArray jsonArray = jsonObject.getJSONArray("noticias");
        //Laço que percorre os resultados
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject noticia = jsonArray.getJSONObject(i);

            Noticia n = new Noticia();
            n.setId(noticia.getInt("id"));
            n.setTitulo(noticia.getString("titulo"));
            n.setConteudo(noticia.getString("conteudo"));
            n.setCriador(noticia.getString("criador"));
            n.setData(noticia.getString("data"));

            noticias.add(n);
        }

        return noticias;
    }
}
