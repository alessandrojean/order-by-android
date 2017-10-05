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
import etelg.etim3e.tcc.tcc.downloaders.DownloadHorario;
import etelg.etim3e.tcc.tcc.model.Aula;
import etelg.etim3e.tcc.tcc.model.Falta;


public class HorarioFragment extends Fragment {

    private ViewPager mViewPager;
    private RelativeLayout mRelativeLayout;

    private TabLayout mTabLayout;

    public static ArrayList<ArrayList<Aula>> mAulas = null;
    public static boolean erro=false;
    public int mPosition=0;

    public String[] dias = {"Segunda-feira","Terça-feira","Quarta-feira","Quinta-feira","Sexta-feira"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View myFragmentView = inflater.inflate(R.layout.fragment_horario, container, false);

        //Obtem da View
        mViewPager = (ViewPager) myFragmentView.findViewById(R.id.vp_tab);
        //Obtem da View
        mRelativeLayout = (RelativeLayout) myFragmentView.findViewById(R.id.pb_horario);

        //Obtem da View
        mTabLayout = (TabLayout) myFragmentView.findViewById(R.id.tl_horario);

        //Verifica se há erros
        if(erro)
        {
            //Mostra a mensagem de erro
            mRelativeLayout.findViewById(R.id.tv_erro).setVisibility(View.VISIBLE);
            mRelativeLayout.findViewById(R.id.pb_horario_indeterminate).setVisibility(View.GONE);
        }
        //Verifica se há horários
        else if(mAulas==null)
            //Baixa o horário
            new DownloadHorario(getActivity(),getFragmentManager(),ActivityPrincipal.mAluno.getIdTurma(),mViewPager,mTabLayout,mRelativeLayout).execute(Utils.getUrlApiHorario(getActivity(),false));
        else {
            //Configura e mostra o horário já obtido
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
        for(int i=0;i<5;i++)
        {
            tabAdapter.addFrag(HorarioLista.newInstance(DownloadHorario.filtrar(i+1,mAulas.get(0)), (i + 1)),dias[i]);
        }
        //Define o Adapter e Mostra os horários
        viewPager.setAdapter(tabAdapter);
        viewPager.setOffscreenPageLimit(5);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setCurrentItem(mPosition);
    }


}
