package etelg.etim3e.tcc.tcc.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import etelg.etim3e.tcc.tcc.ActivityPrincipal;
import etelg.etim3e.tcc.tcc.R;
import etelg.etim3e.tcc.tcc.ViewNoticia;
import etelg.etim3e.tcc.tcc.classes.Utils;

import etelg.etim3e.tcc.tcc.model.Nota;
import etelg.etim3e.tcc.tcc.model.Noticia;

/**
 * Created by Alessandro on 16/07/2015.
 */
public class NoticiaAdapter extends RecyclerView.Adapter<NoticiaAdapter.MyViewHolder> {
    private List<Noticia> mList;
    private LayoutInflater mLayoutInflater;
    private Context mContext;

    public NoticiaAdapter(Context c, List<Noticia> l) {
        mList = l;
        mContext = c;
        //Pega do sistema o LayoutInflater
        mLayoutInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Infla a View com o layout
        View v = mLayoutInflater.inflate(R.layout.item_noticia, parent, false);
        MyViewHolder mvh = new MyViewHolder(v,mContext);

        return mvh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        //Define os textos nas TextViews
        holder.tvTitulo.setText(mList.get(position).getTitulo());
        holder.tvDetalhes.setText("Por " + mList.get(position).getCriador() + " em " + mList.get(position).getData());
        //Coloca a imagem na ImageView, utilizando a biblioteca Picasso
        Picasso.with(mContext).load(Uri.parse(Utils.getUrlApiThumbnail(mContext).replace("$tipo", "noticias").replace("$id", String.valueOf(mList.get(position).getId())))).placeholder(R.drawable.no_avatar).into(holder.ivThumbnail);
        holder.mNoticia = mList.get(position);
    }

    //Método que retorna a quantidade de items
    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView ivThumbnail;
        public TextView tvTitulo;
        public TextView tvDetalhes;
        public Noticia mNoticia;
        Context mContext;

        public MyViewHolder(View itemView,Context c) {
            super(itemView);

            mContext = c;

            //Obtem os objetos da View
            ivThumbnail = (ImageView) itemView.findViewById(R.id.iv_noticia);
            tvTitulo = (TextView) itemView.findViewById(R.id.tv_noticia);
            tvDetalhes = (TextView) itemView.findViewById(R.id.tv_detalhes);

            //Define o evento de clique
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            //Instancia uma Intent
            Intent it = new Intent(mContext,ViewNoticia.class);
            //Define a notícia para a nova Activity
            it.putExtra("noticia",mNoticia);
            //Inicia a Activity
            mContext.startActivity(it);
        }
    }
}
