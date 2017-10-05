package etelg.etim3e.tcc.tcc;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.afollestad.materialdialogs.MaterialDialog;

import java.util.ArrayList;
import java.util.List;

import etelg.etim3e.tcc.tcc.adapters.TabAdapter;
import etelg.etim3e.tcc.tcc.classes.Utils;
import etelg.etim3e.tcc.tcc.downloaders.DownloadFaltas;
import etelg.etim3e.tcc.tcc.downloaders.DownloadNotas;
import etelg.etim3e.tcc.tcc.model.Falta;
import etelg.etim3e.tcc.tcc.model.Nota;


public class FaltasFragment extends Fragment {

    private ViewPager mViewPager;
    private RelativeLayout mRelativeLayout;

    private TabLayout mTabLayout;

    public static ArrayList<ArrayList<Falta>> mFaltas = null;
    public static boolean erro=false;
    public int mPosition=0;

    public String[] meses = {"Janeiro","Fevereiro","Março","Abril","Maio","Junho","Julho","Agosto","Setembro","Outubro","Novembro","Dezembro"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View myFragmentView = inflater.inflate(R.layout.fragment_faltas, container, false);

        //Obtem da View
        mViewPager = (ViewPager) myFragmentView.findViewById(R.id.vp_tab);
        //Obtem da View
        mRelativeLayout = (RelativeLayout) myFragmentView.findViewById(R.id.pb_falta);

        //Obtem da View
        mTabLayout = (TabLayout) myFragmentView.findViewById(R.id.tl_faltas);

        //Verifica se há erros
        if(erro)
        {
            //Mostra a mensagem de erro
            mRelativeLayout.findViewById(R.id.tv_erro).setVisibility(View.VISIBLE);
            mRelativeLayout.findViewById(R.id.pb_falta_indeterminate).setVisibility(View.GONE);
        }
        //Verifica se há faltas
        else if(mFaltas==null)
            //Baixa as faltas
            new DownloadFaltas(getActivity(),getFragmentManager(),ActivityPrincipal.mAluno.getRm(),mViewPager,mTabLayout,mRelativeLayout).execute(Utils.getUrlApiFalta(getActivity()));
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
        for(int i=0;i<mFaltas.size();i++)
        {
            tabAdapter.addFrag(FaltasLista.newInstance(mFaltas.get(i),(i+1)),meses[i]);
        }
        //Define o Adapter e Mostra as Faltas
        viewPager.setAdapter(tabAdapter);
        viewPager.setOffscreenPageLimit(mFaltas.size());
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setCurrentItem(mPosition);
    }

}
