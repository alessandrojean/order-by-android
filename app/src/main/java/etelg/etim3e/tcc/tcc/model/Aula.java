package etelg.etim3e.tcc.tcc.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Alessandro on 04/10/2015.
 */
public class Aula implements Parcelable {
    private int id;
    private String sala;
    private Materia materia;
    private String dia;
    private ArrayList<String> professores;
    private String inicio;
    private String fim;

    public Aula(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSala() {
        return sala;
    }

    public void setSala(String sala) {
        this.sala = sala;
    }

    public Materia getMateria() {
        return materia;
    }

    public void setMateria(Materia materia) {
        this.materia = materia;
    }

    public String getDia() {
        return dia;
    }

    public void setDia(String dia) {
        this.dia = dia;
    }

    public ArrayList<String> getProfessores() {
        return professores;
    }

    public void setProfessores(ArrayList<String> professores) {
        this.professores = professores;
    }

    public String getInicio() {
        return inicio;
    }

    public void setInicio(String inicio) {
        this.inicio = inicio;
    }

    public String getFim() {
        return fim;
    }

    public void setFim(String fim) {
        this.fim = fim;
    }

    protected Aula(Parcel in) {
        id = in.readInt();
        sala = in.readString();
        materia = (Materia) in.readValue(Materia.class.getClassLoader());
        dia = in.readString();
        if (in.readByte() == 0x01) {
            professores = new ArrayList<String>();
            in.readList(professores, String.class.getClassLoader());
        } else {
            professores = null;
        }
        inicio = in.readString();
        fim = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(sala);
        dest.writeValue(materia);
        dest.writeString(dia);
        if (professores == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(professores);
        }
        dest.writeString(inicio);
        dest.writeString(fim);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Aula> CREATOR = new Parcelable.Creator<Aula>() {
        @Override
        public Aula createFromParcel(Parcel in) {
            return new Aula(in);
        }

        @Override
        public Aula[] newArray(int size) {
            return new Aula[size];
        }
    };
}