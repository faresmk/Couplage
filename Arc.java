public class Arc {
    int numero, source, destination;
    Etiquette etiquette;

    Arc(int n, int s, int d, Etiquette e) {
        numero = n;
        source = s;
        destination = d;
        etiquette = e;
    }

    public String toString() {
        return numero + "/" + (source+1) + "+" + (destination+1) + "/->"
                + etiquette;
    }
}
