package etelg.etim3e.tcc.tcc;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;

import java.util.ArrayList;
import java.util.List;

import etelg.etim3e.tcc.tcc.adapters.TabAdapter;
import etelg.etim3e.tcc.tcc.classes.Utils;
import etelg.etim3e.tcc.tcc.downloaders.DownloadNotas;
import etelg.etim3e.tcc.tcc.model.Nota;


public class NotasFragment extends Fragment {

    private ViewPager mViewPager;
    private RelativeLayout mRelativeLayout;

    private TabLayout mTabLayout;

    public static ArrayList<ArrayList<Nota>> mNotas = null;
    public static boolean erro=false;
    public int mPosition=0;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View myFragmentView = inflater.inflate(R.layout.fragment_notas, container, false);

        //Obtem da View
        mViewPager = (ViewPager) myFragmentView.findViewById(R.id.vp_tab);
        //Obtem da View
        mRelativeLayout = (RelativeLayout) myFragmentView.findViewById(R.id.pb_nota);

        //Obtem da View
        mTabLayout = (TabLayout) myFragmentView.findViewById(R.id.tl_notas);

        //Verifica se há erros
        if(erro)
        {
            //Mostra a mensagem de erro
            mRelativeLayout.findViewById(R.id.tv_erro).setVisibility(View.VISIBLE);
            mRelativeLayout.findViewById(R.id.pb_nota_indeterminate).setVisibility(View.GONE);
        }
        //Verifica se há faltas
        else if(mNotas==null)
            //Baixa as faltas
            new DownloadNotas(getActivity(),getFragmentManager(),ActivityPrincipal.mAluno.getRm(),mViewPager,mTabLayout,mRelativeLayout).execute(Utils.getUrlApiNota(getActivity()));
        else {
            //Configura e mostra as faltas já obtidas
            setupViewPager(mViewPager, mTabLayout);
            mRelativeLayout.setVisibility(View.GONE);
            mTabLayout.setVisibility(View.VISIBLE);
            mViewPager.setVisibility(View.VISIBLE);
        }

        return myFragmentView;
    }

    //Método que configura o ViewPager
    public void setupViewPager(ViewPager viewPager,TabLayout tabLayout)
    {
        //Instancia o TabAdapter, responsável pelo controle das tabs
        TabAdapter tabAdapter = new TabAdapter(getFragmentManager(),getActivity());
        for(int i=0;i<mNotas.size();i++)
        {
            tabAdapter.addFrag(NotasLista.newInstance(mNotas.get(i),(i+1)),(i+1)+"º Bimestre");
        }
        //Define o Adapter e Mostra as Notas
        tabAdapter.setNotas(mNotas);
        tabAdapter.setTipo(TabAdapter.TIPO_NOTA);
        viewPager.setAdapter(tabAdapter);
        viewPager.setOffscreenPageLimit(mNotas.size());
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setCurrentItem(mPosition);
    }


}
