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

import etelg.etim3e.tcc.tcc.R;

import etelg.etim3e.tcc.tcc.model.Falta;
import etelg.etim3e.tcc.tcc.model.Nota;

/**
 * Created by Alessandro on 16/07/2015.
 */
public class FaltaAdapter extends RecyclerView.Adapter<FaltaAdapter.MyViewHolder> {
    private List<Falta> mList;
    private LayoutInflater mLayoutInflater;

    public FaltaAdapter(Context c, List<Falta> l) {
        mList = l;
        //Pega do sistema o LayoutInflater
        mLayoutInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Infla a View com o layout
        View v = mLayoutInflater.inflate(R.layout.item_falta, parent, false);
        MyViewHolder mvh = new MyViewHolder(v);

        return mvh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        //Define os textos nas TextViews
        holder.tvFalta.setText("Quantidade de faltas: "+String.valueOf(mList.get(position).getFaltas()).replace(".0",""));
        holder.tvMateria.setText(mList.get(position).getMateria().getNome());
        holder.tvSigla.setText(mList.get(position).getMateria().getSigla());

    }

    //Método que retorna a quantidade de items
    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView tvFalta;
        public TextView tvMateria;
        public TextView tvSigla;

        public MyViewHolder(View itemView) {
            super(itemView);

            //Obtem os objetos da View
            tvFalta = (TextView) itemView.findViewById(R.id.tv_faltas);
            tvMateria = (TextView) itemView.findViewById(R.id.tv_materia);
            tvSigla = (TextView) itemView.findViewById(R.id.tv_sigla);
        }
    }
}
