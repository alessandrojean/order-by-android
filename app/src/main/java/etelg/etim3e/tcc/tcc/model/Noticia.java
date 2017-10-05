package etelg.etim3e.tcc.tcc.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Noticia implements Parcelable {
    private int id;
    private String titulo;
    private String conteudo;
    private String criador;
    private String data;
    private boolean destaque;
    private String noticia;
    private String tipo;

    public Noticia() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getConteudo() {
        return conteudo;
    }

    public void setConteudo(String conteudo) {
        this.conteudo = conteudo;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public boolean isDestaque() {
        return destaque;
    }

    public void setDestaque(boolean destaque) {
        this.destaque = destaque;
    }

    public String getNoticia() {
        return noticia;
    }

    public void setNoticia(String noticia) {
        this.noticia = noticia;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getCriador() {
        return criador;
    }

    public void setCriador(String criador) {
        this.criador = criador;
    }

    protected Noticia(Parcel in) {
        id = in.readInt();
        titulo = in.readString();
        conteudo = in.readString();
        criador = in.readString();
        data = in.readString();
        destaque = in.readByte() != 0x00;
        noticia = in.readString();
        tipo = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(titulo);
        dest.writeString(conteudo);
        dest.writeString(criador);
        dest.writeString(data);
        dest.writeByte((byte) (destaque ? 0x01 : 0x00));
        dest.writeString(noticia);
        dest.writeString(tipo);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Noticia> CREATOR = new Parcelable.Creator<Noticia>() {
        @Override
        public Noticia createFromParcel(Parcel in) {
            return new Noticia(in);
        }

        @Override
        public Noticia[] newArray(int size) {
            return new Noticia[size];
        }
    };
}