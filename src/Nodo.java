import java.util.ArrayList;

public class Nodo {
    private String url;
    private int depth;
    private Nodo padre;
    private ArrayList<Nodo> hijos;

    public Nodo(String url, int depth, Nodo padre) {
        this.url = url;
        this.depth = depth;
        this.padre = padre;
        this.hijos = new ArrayList<>();
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getDepth() {
        return this.depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public Nodo getPadre() {
        return this.padre;
    }

    public void setPadre(Nodo padre) {
        this.padre = padre;
    }

    public ArrayList<Nodo> getHijos() {
        return this.hijos;
    }

    public void setHijos(ArrayList<Nodo> hijos) {
        this.hijos = hijos;
    }

    public void addHijos(Nodo hijo) {
        hijos.add(hijo);
    }

    @Override
    public String toString() {
        return "{" +
                " url='" + getUrl() + "'" +
                ", depth='" + getDepth() + "'" +
                ", padre='" + getPadre().getUrl() + "'" +
                "}";
    }

}