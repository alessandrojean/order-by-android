package etelg.etim3e.tcc.tcc.downloaders;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;

import com.afollestad.materialdialogs.MaterialDialog;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import etelg.etim3e.tcc.tcc.ActivityLogin;
import etelg.etim3e.tcc.tcc.ActivityPrincipal;

import etelg.etim3e.tcc.tcc.model.Aluno;

/**
 * Created by Alessandro on 09/09/2015.
 */
public class VerificaAluno extends AsyncTask<String,Void,Aluno> {

    private String url;
    private boolean success;
    private String motivo;
    private boolean salvar;

    private Context mContext;
    private MaterialDialog mMaterialDialog;

    public VerificaAluno(String u, Context c, boolean s)
    {
        url = u;
        mContext = c;
        salvar = s;
    }

    @Override
    protected Aluno doInBackground(String... rm) {

        //Instancia um cliente http, que fará a requisição do json
        DefaultHttpClient lHttpClient = new DefaultHttpClient();
        //Define um HttpGet, que contém a url
        HttpGet lHttpGet = new HttpGet(url.replace("$rm",rm[0]));
        try {
            //Executa e obtem a resposta da página da API
            HttpResponse lHttpResponse = lHttpClient.execute(lHttpGet);
            //Lê o resultado
            BufferedReader reader = new BufferedReader(new InputStreamReader(lHttpResponse.getEntity().getContent(), "UTF-8"));

            return analisar(reader.readLine());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //Método que executa antes do início do processamento
    @Override
    protected void onPreExecute() {
        mMaterialDialog = new MaterialDialog.Builder(mContext)
                .content("Validando RM...")
                .progress(true, 0)
                .cancelable(false)
                .show();
    }

    //Método que executa após o processamento
    @Override
    protected void onPostExecute(Aluno aluno) {
        super.onPostExecute(aluno);

        mMaterialDialog.hide();

        //Verifica se houve êxito
        if(aluno!=null && success) {
            //Verifica se precisa salvar e salva se necessário
            if (salvar) {
                savePessoa(aluno);
            }
            //Instancia uma Intent
            Intent it = new Intent(mContext, ActivityPrincipal.class);
            //Define o aluno para a nova Activity
            it.putExtra("aluno", aluno);
            //Inicia a Activity
            mContext.startActivity(it);
        }
        else
        {
            new MaterialDialog.Builder(mContext)
                    .title("Erro")
                    .content(motivo)
                    .positiveText("OK")
                    .callback(new MaterialDialog.ButtonCallback() {
                        @Override
                        public void onPositive(MaterialDialog dialog) {
                            super.onPositive(dialog);
                            ActivityLogin.txtrm.requestFocus();
                        }
                    })
                    .cancelable(false)
                    .show();
        }
    }

    private void savePessoa(Aluno aluno) {
        //Obtem as preferências do sistema
        SharedPreferences lSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        //Obtem o editor
        SharedPreferences.Editor lEditor = lSharedPreferences.edit();
        //Altera as preferências
        lEditor.putString(Aluno.KEY_RM, aluno.getRm());
        lEditor.putString(Aluno.KEY_NOME, aluno.getNome());
        lEditor.putInt(Aluno.KEY_TURMA,aluno.getIdTurma());
        //Salva
        lEditor.commit();
    }

    //Método que converte o JSON para objeto
    private Aluno analisar(String json) throws JSONException
    {
        Aluno a = null;

        JSONObject jsonObject = new JSONObject(json);
        success = jsonObject.getBoolean("success");
        motivo = jsonObject.optString("motivo");

        //Verifica se houve sucesso
        if(success)
        {
            JSONObject aluno = jsonObject.getJSONObject("aluno");

            a = new Aluno();
            a.setRm(aluno.getString("rm"));
            a.setNome(aluno.getString("nome"));
            a.setCpf(aluno.getString("cpf"));
            a.setTelefone(aluno.getString("telefone"));
            a.setCelular(aluno.getString("celular"));
            a.setIdTurma(aluno.getInt("idTurma"));
        }

        return a;
    }
}
