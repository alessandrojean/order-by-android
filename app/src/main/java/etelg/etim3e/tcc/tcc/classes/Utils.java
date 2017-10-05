package etelg.etim3e.tcc.tcc.classes;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;


/**
 * Created by Alessandro on 30/07/2015.
 */
public class Utils {

    public static final String URL_API_ALUNO = "http://192.168.0.102:8080/tcc/api/aluno/aluno.php?rm=$rm";
    public static final String URL_API_NOTA = "http://192.168.0.102:8080/tcc/api/nota/nota.php?rm=$rm&bimestre=$bim";
    public static final String URL_API_THUMBNAIL = "http://192.168.0.102:8080/tcc/api/thumbnail/thumbnail.php?tipo=$tipo&id=$id";
    public static final String URL_API_NOTICIA = "http://192.168.0.102:8080/tcc/api/noticias/noticias.php";
    public static final String URL_API_FALTA = "http://192.168.0.102:8080/tcc/api/falta/falta.php?rm=$rm&mes=$mes";
    public static final String URL_API_HORARIO = "http://192.168.0.102:8080/tcc/api/horario/horario.php?turma=$turma&dia=$dia";

    //Método que verifica se há Internet
    public static boolean haveInternet(Context context) {
        //Obtem o gerenciador de conectividade do sistema
        ConnectivityManager lConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        //Obtem a informação de rede
        NetworkInfo lNetworkInfo = lConnectivityManager.getActiveNetworkInfo();

        //Retorna se está conectado ou não
        return lNetworkInfo != null && lNetworkInfo.isConnectedOrConnecting();
    }

    //Método que retorna a Url da API do Aluno
    public static String getUrlApiAluno(Context c)
    {
        //Obtem as preferências do sistema
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(c);
        //Verifica se há definida a propriedade
        if (sp.getBoolean("api_personalizada", false))
            return sp.getString("api_aluno", URL_API_ALUNO);

        return URL_API_ALUNO;
    }

    //Método que retorna a Url da API da Nota
    public static String getUrlApiNota(Context c)
    {
        //Obtem as preferências do sistema
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(c);
        //Verifica se há definida a propriedade
        if (sp.getBoolean("api_personalizada", false))
            return sp.getString("api_nota", URL_API_NOTA);

        return URL_API_NOTA;
    }

    //Método que retorna a Url da API da Nota
    public static String getUrlApiFalta(Context c)
    {
        //Obtem as preferências do sistema
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(c);
        //Verifica se há definida a propriedade
        if (sp.getBoolean("api_personalizada", false))
            return sp.getString("api_falta", URL_API_FALTA);

        return URL_API_FALTA;
    }

    //Método que retorna a Url da API do Horário
    public static String getUrlApiHorario(Context c,boolean dia)
    {
        //Obtem as preferências do sistema
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(c);
        //Verifica se há definida a propriedade
        if (sp.getBoolean("api_personalizada", false))
            return sp.getString("api_horario", URL_API_HORARIO).replace((!dia ? "&dia=$dia" : ""),"");

        return URL_API_HORARIO;
    }

    //Método que retorna a Url da API da Thumbnail
    public static String getUrlApiThumbnail(Context c)
    {
        //Obtem as preferências do sistema
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(c);
        //Verifica se há definida a propriedade
        if (sp.getBoolean("api_personalizada", false))
            return sp.getString("api_thumbnail", URL_API_THUMBNAIL);

        return URL_API_THUMBNAIL;
    }

    //Método que retorna a Url da API das Notícias
    public static String getUrlApiNoticia(Context c)
    {
        //Obtem as preferências do sistema
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(c);
        //Verifica se há definida a propriedade
        if (sp.getBoolean("api_personalizada", false))
            return sp.getString("api_noticia", URL_API_NOTICIA);

        return URL_API_NOTICIA;
    }

    //Método que retorna em String o dia da semana
    public static String getDia(int numero)
    {
        //Verifica e retorna o certo
        switch (numero)
        {
            default:
            case 1:
            {
                return "SEGUNDA";
            }
            case 2:
            {
                return "TERÇA";
            }
            case 3:
            {
                return "QUARTA";
            }
            case 4:
            {
                return "QUINTA";
            }
            case 5:
            {
                return "SEXTA";
            }
        }
    }

}
