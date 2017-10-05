package etelg.etim3e.tcc.tcc.downloaders;

import android.content.Context;
import android.os.AsyncTask;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
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

import etelg.etim3e.tcc.tcc.FaltasFragment;
import etelg.etim3e.tcc.tcc.FaltasLista;
import etelg.etim3e.tcc.tcc.HorarioFragment;
import etelg.etim3e.tcc.tcc.HorarioLista;
import etelg.etim3e.tcc.tcc.R;
import etelg.etim3e.tcc.tcc.adapters.TabAdapter;
import etelg.etim3e.tcc.tcc.classes.Utils;
import etelg.etim3e.tcc.tcc.model.Aula;
import etelg.etim3e.tcc.tcc.model.Falta;
import etelg.etim3e.tcc.tcc.model.Materia;

/**
 * Created by Alessandro on 09/09/2015.
 */
public class DownloadHorario extends AsyncTask<String,Void,List<String>> {

    private Context mContext;
    private int mTurma;
    private ViewPager mViewPager;
    private FragmentManager mFragmentManager;
    private TabLayout mTabLayout;
    private RelativeLayout mRelativeLayout;


    private ArrayList<ArrayList<Aula>> aulas = new ArrayList<ArrayList<Aula>>();

    public String[] dias = {"Segunda-feira","Terça-feira","Quarta-feira","Quinta-feira","Sexta-feira"};

    public DownloadHorario(Context c, FragmentManager m, int turma, ViewPager v, TabLayout t, RelativeLayout r)
    {
        mContext = c;
        mTurma = turma;
        mViewPager = v;
        mFragmentManager = m;
        mTabLayout = t;
        mRelativeLayout = r;
    }

    //Filtra as aulas por dia
    public static ArrayList<Aula> filtrar(int dia,ArrayList<Aula> filtro)
    {
        ArrayList<Aula> aulas = new ArrayList<Aula>();
        for(int i=0;i<filtro.size();i++)
        {
            if(filtro.get(i).getDia().equals(Utils.getDia(dia)))
                aulas.add(filtro.get(i));
        }
        return aulas;
    }

    //Método que executa antes do início do processamento
    @Override
    protected void onPreExecute() {
        mRelativeLayout.setVisibility(View.VISIBLE);
        mRelativeLayout.findViewById(R.id.tv_erro).setVisibility(View.GONE);
        mRelativeLayout.findViewById(R.id.pb_horario_indeterminate).setVisibility(View.VISIBLE);
        mViewPager.setVisibility(View.GONE);
        mTabLayout.setVisibility(View.GONE);
    }

    //Método que executa em background o processamento
    @Override
    protected List<String> doInBackground(String... url) {
        List<String> jsons = new ArrayList<String>();
        //Instancia um cliente http, que fará a requisição do json
        DefaultHttpClient lHttpClient = new DefaultHttpClient();
        //Define um HttpGet, que contém a url
        HttpGet lHttpGet = new HttpGet(url[0].replace("$turma", String.valueOf(mTurma)));
        try {
            //Executa e obtem a resposta da página da API
            HttpResponse lHttpResponse = lHttpClient.execute(lHttpGet);
            //Lê o resultado
            BufferedReader reader = new BufferedReader(new InputStreamReader(lHttpResponse.getEntity().getContent(), "UTF-8"));

            jsons.add(reader.readLine());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return jsons;
    }

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
                    for(int j=0;j<5;j++) {
                        tabs.addFrag(HorarioLista.newInstance(filtrar(j+1,getHorario(jsons.get(i))), (j + 1)), dias[j]);
                    }
                    aulas.add(getHorario(jsons.get(i)));
                    validos++;
                }
            }
            //Verifica se houveram respostas
            if(validos>0) {
                //Mostra as informações e oculta os erros
                mRelativeLayout.setVisibility(View.GONE);
                mViewPager.setVisibility(View.VISIBLE);
                mTabLayout.setVisibility(View.VISIBLE);
                HorarioFragment.mAulas = aulas;
            }
            else
            {
                //Mostra o erro
                mRelativeLayout.findViewById(R.id.tv_erro).setVisibility(View.VISIBLE);
                mRelativeLayout.findViewById(R.id.pb_horario_indeterminate).setVisibility(View.GONE);
                HorarioFragment.erro=true;
                HorarioFragment.mAulas = null;
            }

            tabs.setAulas(aulas);
            tabs.setTipo(TabAdapter.TIPO_HORARIO);
            //Define o Adapter no ViewPager
            mViewPager.setAdapter(tabs);
            //Define o número de tabs que permanecerão ativas
            mViewPager.setOffscreenPageLimit(dias.length);
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
    private ArrayList<Aula> getHorario(String json) throws JSONException
    {
        ArrayList<Aula> aulas = new ArrayList<Aula>();

        JSONObject jsonObject = new JSONObject(json);
        JSONArray jsonArray = jsonObject.getJSONArray("horarios");
        //Laço que percorre os resultados
        for(int dia=1;dia<=5;dia++) {
            JSONArray d = jsonObject.getJSONObject("aulas").getJSONArray(Utils.getDia(dia));
            for (int i = 0; i < d.length(); i++) {
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
                for (int j = 0; j < professores.length(); j++)
                    p.add(professores.getString(j));

                a.setProfessores(p);

                a.setInicio(aula.getString("inicio"));
                a.setFim(aula.getString("fim"));

                a.setDia(Utils.getDia(dia));

                aulas.add(a);
            }
        }

        return aulas;
    }
}
