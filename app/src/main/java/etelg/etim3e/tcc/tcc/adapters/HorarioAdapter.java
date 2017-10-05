package etelg.etim3e.tcc.tcc.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import etelg.etim3e.tcc.tcc.R;

import etelg.etim3e.tcc.tcc.model.Aula;
import etelg.etim3e.tcc.tcc.model.Falta;

/**
 * Created by Alessandro on 16/07/2015.
 */
public class HorarioAdapter extends RecyclerView.Adapter<HorarioAdapter.MyViewHolder> {
    private List<Aula> mList;
    private LayoutInflater mLayoutInflater;

    public HorarioAdapter(Context c, List<Aula> l) {
        mList = l;
        //Pega do sistema o LayoutInflater
        mLayoutInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Infla a View com o layout
        View v = mLayoutInflater.inflate(R.layout.item_horario, parent, false);
        MyViewHolder mvh = new MyViewHolder(v);

        return mvh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        //Define os textos nas TextViews
        holder.tvProfessorSala.setText(mList.get(position).getProfessores().toString().replace("[","").replace("]","").replace(", ","/")+ " - Sala "+mList.get(position).getSala());
        holder.tvMateria.setText(mList.get(position).getMateria().getNome());
        holder.tvSigla.setText(mList.get(position).getMateria().getSigla());
        holder.tvHorario.setText(mList.get(position).getInicio()+"\n~\n"+mList.get(position).getFim());
    }

    //Método que retorna a quantidade de items
    @Override
    public int getItemCount() {
        return mList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView tvProfessorSala;
        public TextView tvMateria;
        public TextView tvSigla;
        public TextView tvHorario;

        public MyViewHolder(View itemView) {
            super(itemView);

            //Obtem os objetos da View
            tvProfessorSala = (TextView) itemView.findViewById(R.id.tv_professorsala);
            tvMateria = (TextView) itemView.findViewById(R.id.tv_materia);
            tvSigla = (TextView) itemView.findViewById(R.id.tv_sigla);
            tvHorario = (TextView) itemView.findViewById(R.id.tv_horario);
        }
    }
}
