package etelg.etim3e.tcc.tcc.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Nota implements Parcelable {
    private int id;
    private String nota;
    private int bimestre;
    private Materia materia;

    public Nota() {
        this(0, "", 0, new Materia());
    }

    public Nota(String nota, int bimestre, Materia materia) {
        this.nota = nota;
        this.bimestre = bimestre;
        this.materia = materia;
    }

    public Nota(int id, String nota, int bimestre, Materia materia) {
        this.id = id;
        this.nota = nota;
        this.bimestre = bimestre;
        this.materia = materia;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setNota(String nota) {
        this.nota = nota;
    }

    public String getNota() {
        return nota;
    }

    public void setBimestre(int bimestre) {
        this.bimestre = bimestre;
    }

    public int getBimestre() {
        return bimestre;
    }

    public void setMateria(Materia materia) {
        this.materia = materia;
    }

    public Materia getMateria() {
        return materia;
    }


    protected Nota(Parcel in) {
        id = in.readInt();
        nota = in.readString();
        bimestre = in.readInt();
        materia = (Materia) in.readValue(Materia.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(nota);
        dest.writeInt(bimestre);
        dest.writeValue(materia);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Nota> CREATOR = new Parcelable.Creator<Nota>() {
        @Override
        public Nota createFromParcel(Parcel in) {
            return new Nota(in);
        }

        @Override
        public Nota[] newArray(int size) {
            return new Nota[size];
        }
    };
}