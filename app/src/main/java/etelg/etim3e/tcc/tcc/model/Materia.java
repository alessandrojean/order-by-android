package etelg.etim3e.tcc.tcc.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Materia implements Parcelable {

    private int id;
    private String nome;
    private String sigla;

    public Materia() {
        this(0, "", "");
    }

    public Materia(String nome, String sigla) {
        this.nome = nome;
        this.sigla = sigla;
    }

    public Materia(int id, String nome, String sigla) {
        this.id = id;
        this.nome = nome;
        this.sigla = sigla;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public void setSigla(String sigla) {
        this.sigla = sigla;
    }

    public String getSigla() {
        return sigla;
    }


    protected Materia(Parcel in) {
        id = in.readInt();
        nome = in.readString();
        sigla = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(nome);
        dest.writeString(sigla);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Materia> CREATOR = new Parcelable.Creator<Materia>() {
        @Override
        public Materia createFromParcel(Parcel in) {
            return new Materia(in);
        }

        @Override
        public Materia[] newArray(int size) {
            return new Materia[size];
        }
    };
}