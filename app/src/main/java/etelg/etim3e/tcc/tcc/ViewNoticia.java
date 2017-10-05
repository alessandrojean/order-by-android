package etelg.etim3e.tcc.tcc;

import android.content.Intent;
import android.graphics.Point;
import android.net.Uri;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import etelg.etim3e.tcc.tcc.classes.Utils;
import etelg.etim3e.tcc.tcc.model.Noticia;


public class ViewNoticia extends ActionBarActivity {

    private Toolbar mToolbar;
    private ImageView mImageView;
    private Noticia mNoticia;
    private WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_noticia);

        //Pega a notícia passada pela ActivityPrincipal
        mNoticia = (Noticia)getIntent().getExtras().get("noticia");

        //Obtem da View
        mToolbar = (Toolbar) findViewById(R.id.tb_noticias);
        mToolbar.setTitle(mNoticia.getTitulo());
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        //Obtem da View
        mImageView = (ImageView) findViewById(R.id.iv_noticias);
        //Carrega a imagem com a biblioteca Picasso
        Picasso.with(this).load(Uri.parse(Utils.getUrlApiThumbnail(this).replace("$tipo", "noticias").replace("$id", String.valueOf(mNoticia.getId()))+"&thumb=false")).placeholder(R.drawable.no_avatar).into(mImageView);

        //Obtem da View
        mWebView = (WebView) findViewById(R.id.web_noticias);
        //Carrega o conteúdo
        mWebView.loadDataWithBaseURL("file:///android_asset/","<link rel=\"stylesheet\" type=\"text/css\" href=\"estilo.css\" />"+mNoticia.getConteudo(),"text/html", "UTF-8", null);
        //Permite Javascript e outros fatores
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setAllowContentAccess(true);
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setPluginState(WebSettings.PluginState.ON);
        webSettings.setUseWideViewPort(true);
        mWebView.setWebChromeClient(new WebChromeClient() {});
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view_noticia, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Pega o ID do Popup Menu
        int id = item.getItemId();

        //Verifica e faz a ação apropriada
        if (id == R.id.action_share) {
            String shareBody = mNoticia.getConteudo();
            //Instancia o Intent de compartilhamento
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            //Define o tipo
            sharingIntent.setType("text/plain");
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, mNoticia.getTitulo());
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
            //Inicia a Activity
            startActivity(Intent.createChooser(sharingIntent, "Compartilhar com..."));
        }

        return super.onOptionsItemSelected(item);
    }
}
