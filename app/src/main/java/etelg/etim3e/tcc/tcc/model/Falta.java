package etelg.etim3e.tcc.tcc.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Alessandro on 04/10/2015.
 */
public class Falta implements Parcelable {

    private int id;
    private double faltas;
    private int mes;
    private Materia materia;

    public Falta(){}


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getFaltas() {
        return faltas;
    }

    public void setFaltas(double faltas) {
        this.faltas = faltas;
    }

    public int getMes() {
        return mes;
    }

    public void setMes(int mes) {
        this.mes = mes;
    }

    public Materia getMateria() {
        return materia;
    }

    public void setMateria(Materia materia) {
        this.materia = materia;
    }

    protected Falta(Parcel in) {
        id = in.readInt();
        faltas = in.readDouble();
        mes = in.readInt();
        materia = (Materia) in.readValue(Materia.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeDouble(faltas);
        dest.writeInt(mes);
        dest.writeValue(materia);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Falta> CREATOR = new Parcelable.Creator<Falta>() {
        @Override
        public Falta createFromParcel(Parcel in) {
            return new Falta(in);
        }

        @Override
        public Falta[] newArray(int size) {
            return new Falta[size];
        }
    };
}