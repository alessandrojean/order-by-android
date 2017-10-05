package etelg.etim3e.tcc.tcc.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import etelg.etim3e.tcc.tcc.model.Nota;
import etelg.etim3e.tcc.tcc.R;


/**
 * Created by Alessandro on 16/07/2015.
 */
public class NotaAdapter extends RecyclerView.Adapter<NotaAdapter.MyViewHolder> {
    private List<Nota> mList;
    private LayoutInflater mLayoutInflater;

    public NotaAdapter(Context c, List<Nota> l) {
        mList = l;
        //Pega do sistema o LayoutInflater
        mLayoutInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Infla a View com o layout
        View v = mLayoutInflater.inflate(R.layout.item_nota, parent, false);
        MyViewHolder mvh = new MyViewHolder(v);

        return mvh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        //Define a imagem correta na ImageView, com base na nota
        int img;
        if (mList.get(position).getNota().equals("MB"))
            img = R.drawable.notamb;
        else if (mList.get(position).getNota().equals("B"))
            img = R.drawable.notab;
        else if (mList.get(position).getNota().equals("R"))
            img = R.drawable.notar;
        else
            img = R.drawable.notai;
        //Define os textos nas TextViews
        holder.ivNota.setImageResource(img);
        holder.tvMateria.setText(mList.get(position).getMateria().getNome());
        holder.tvSigla.setText(mList.get(position).getMateria().getSigla());

    }

    //Método que retorna a quantidade de items
    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public ImageView ivNota;
        public TextView tvMateria;
        public TextView tvSigla;

        public MyViewHolder(View itemView) {
            super(itemView);

            //Obtem os objetos da View
            ivNota = (ImageView) itemView.findViewById(R.id.iv_nota);
            tvMateria = (TextView) itemView.findViewById(R.id.tv_materia);
            tvSigla = (TextView) itemView.findViewById(R.id.tv_sigla);
        }
    }
}
