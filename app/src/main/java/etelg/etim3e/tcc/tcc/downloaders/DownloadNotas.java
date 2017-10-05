package etelg.etim3e.tcc.tcc.downloaders;

import android.app.ActionBar;
import android.content.Context;
import android.os.AsyncTask;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import etelg.etim3e.tcc.tcc.R;
import etelg.etim3e.tcc.tcc.adapters.TabAdapter;
import etelg.etim3e.tcc.tcc.model.Aluno;
import etelg.etim3e.tcc.tcc.model.Materia;
import etelg.etim3e.tcc.tcc.model.Nota;

/**
 * Created by Alessandro on 09/09/2015.
 */
public class DownloadNotas extends AsyncTask<String,Void,List<String>> {

    private Context mContext;
    private String mAluno;
    private ViewPager mViewPager;
    private FragmentManager mFragmentManager;
    private TabLayout mTabLayout;
    private RelativeLayout mRelativeLayout;

    private ArrayList<ArrayList<Nota>> notas = new ArrayList<ArrayList<Nota>>();

    public DownloadNotas(Context c, FragmentManager m, String rm, ViewPager v, TabLayout t, RelativeLayout r)
    {
        mContext = c;
        mAluno = rm;
        mViewPager = v;
        mFragmentManager = m;
        mTabLayout = t;
        mRelativeLayout = r;
    }

    //Método que executa antes do início do processamento
    @Override
    protected void onPreExecute() {
        mRelativeLayout.setVisibility(View.VISIBLE);
        mRelativeLayout.findViewById(R.id.tv_erro).setVisibility(View.GONE);
        mRelativeLayout.findViewById(R.id.pb_nota_indeterminate).setVisibility(View.VISIBLE);
        mViewPager.setVisibility(View.GONE);
        mTabLayout.setVisibility(View.GONE);
    }

    //Método que executa em background o processamento
    @Override
    protected List<String> doInBackground(String... url) {
        List<String> jsons = new ArrayList<String>();
        //Laço de repetição que percorre os quatro bimestres
        for(int i=0;i<4;i++) {
            //Instancia um cliente http, que fará a requisição do json
            DefaultHttpClient lHttpClient = new DefaultHttpClient();
            //Define um HttpGet, que contém a url
            HttpGet lHttpGet = new HttpGet(url[0].replace("$rm", mAluno).replace("$bim",String.valueOf(i+1)));
            try {
                //Executa e obtem a resposta da página da API
                HttpResponse lHttpResponse = lHttpClient.execute(lHttpGet);
                //Lê o resultado
                BufferedReader reader = new BufferedReader(new InputStreamReader(lHttpResponse.getEntity().getContent(), "UTF-8"));

                jsons.add(reader.readLine());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return jsons;
    }

    //Método que executa após o processamento
    @Override
    protected void onPostExecute(List<String> jsons) {
        super.onPostExecute(jsons);

        //Instancia o TabAdapter, responsável pelo controle das tabs
        TabAdapter tabs = new TabAdapter(mFragmentManager,mContext);
        try {
            int validos=0;
            //Laço de repetição que percorre todas as requisições
            for (int i = 0; i < jsons.size(); i++) {
                //Verifica se houve resposta
                if (isValid(jsons.get(i))) {
                    //Adiciona as tabs
                    tabs.addFrag(NotasLista.newInstance(getNota(jsons.get(i)),(i+1)),(i+1)+"º Bimestre");
                    notas.add(getNota(jsons.get(i)));
                    validos++;
                }
            }
            //Verifica se houveram respostas
            if(validos>0) {
                //Mostra as informações e oculta os erros
                mRelativeLayout.setVisibility(View.GONE);
                mViewPager.setVisibility(View.VISIBLE);
                mTabLayout.setVisibility(View.VISIBLE);
                NotasFragment.mNotas = notas;
            }
            else
            {
                //Mostra o erro
                mRelativeLayout.findViewById(R.id.tv_erro).setVisibility(View.VISIBLE);
                mRelativeLayout.findViewById(R.id.pb_nota_indeterminate).setVisibility(View.GONE);
                NotasFragment.erro=true;
                NotasFragment.mNotas = null;
            }

            tabs.setNotas(notas);
            tabs.setTipo(TabAdapter.TIPO_NOTA);
            //Define o Adapter no ViewPager
            mViewPager.setAdapter(tabs);
            //Define o número de tabs que permanecerão ativas
            mViewPager.setOffscreenPageLimit(notas.size());
            //Define o ViewPager no TabLayout
            mTabLayout.setupWithViewPager(mViewPager);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    //Método que verifica se é válido
    private boolean isValid(String json) throws JSONException
    {
        return new JSONObject(json).getBoolean("success");
    }

    //Método que processa o JSON e transforma em objetos
    private ArrayList<Nota> getNota(String json) throws JSONException
    {
        ArrayList<Nota> notas = new ArrayList<Nota>();

        JSONObject jsonObject = new JSONObject(json);
        JSONArray jsonArray = jsonObject.getJSONArray("notas");
        //Laço que percorre os resultados
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
