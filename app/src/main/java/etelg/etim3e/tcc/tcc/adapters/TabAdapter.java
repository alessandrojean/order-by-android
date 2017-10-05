package etelg.etim3e.tcc.tcc.adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import etelg.etim3e.tcc.tcc.FaltasLista;
import etelg.etim3e.tcc.tcc.HorarioLista;
import etelg.etim3e.tcc.tcc.NotasLista;
import etelg.etim3e.tcc.tcc.downloaders.DownloadHorario;
import etelg.etim3e.tcc.tcc.model.Aula;
import etelg.etim3e.tcc.tcc.model.Falta;
import etelg.etim3e.tcc.tcc.model.Nota;

/**
 * Created by Victor on 20/08/2015.
 */
public class TabAdapter extends FragmentPagerAdapter {
    private Context mContext;
    private List<Fragment> mFragmentList = new ArrayList<Fragment>();
    private List<String> mFragmentTitleList = new ArrayList<String>();
    private int[] bims = {1,2,3,4};

    public static final int TIPO_NOTA=1;
    public static final int TIPO_FALTA=2;
    public static final int TIPO_HORARIO=3;

    private int tipo;

    public void setNotas(ArrayList<ArrayList<Nota>> notas) {
        this.notas = notas;
    }

    public void setFaltas(ArrayList<ArrayList<Falta>> faltas) {
        this.faltas = faltas;
    }

    public void setAulas(ArrayList<ArrayList<Aula>> aulas) {
        this.aulas = aulas;
    }

    private ArrayList<ArrayList<Nota>> notas;
    private ArrayList<ArrayList<Falta>> faltas;
    private ArrayList<ArrayList<Aula>> aulas;

    FragmentManager mFragmentManager;

    public TabAdapter(FragmentManager fm, Context c) {
        super(fm);
        mFragmentManager=fm;
        mContext = c;
    }

    public void addFrag(Fragment frag, String title)
    {
        mFragmentList.add(frag);
        mFragmentTitleList.add(title);
    }

    @Override
    public boolean isViewFromObject(View view, Object fragment) {
        return ((Fragment) fragment).getView() == view;
    }

    @Override
    public Fragment getItem(int position) {
        assert(0 <= position && position < mFragmentList.size());
        if(mFragmentList.get(position) == null){
            switch(tipo)
            {
                case TIPO_NOTA:
                {
                    mFragmentList.set(position,NotasLista.newInstance(notas.get(position),(position+1)));
                    break;
                }
                case TIPO_FALTA:
                {
                    mFragmentList.set(position, FaltasLista.newInstance(faltas.get(position), (position + 1)));
                    break;
                }
                case TIPO_HORARIO:
                {
                    mFragmentList.set(position, HorarioLista.newInstance(DownloadHorario.filtrar(position+1,aulas.get(position)),(position+1)));
                    break;
                }
            }
             //make your fragment here
        }
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        assert(0 <= position && position < mFragmentList.size());
        FragmentTransaction trans = mFragmentManager.beginTransaction();
        trans.remove(mFragmentList.get(position));
        trans.commit();
        mFragmentList.set(position,null);
    }

    @Override
    public Fragment instantiateItem(ViewGroup container, int position){
        Fragment fragment = getItem(position);
        FragmentTransaction trans = mFragmentManager.beginTransaction();
        trans.add(container.getId(),fragment,"fragment:"+position);
        trans.commit();
        return fragment;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }
}
