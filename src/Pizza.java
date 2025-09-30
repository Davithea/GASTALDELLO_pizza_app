public class Pizza {
    public String nome;
    public String ingredienti;
    public double prezzo;

    @Override
    public String toString() {
        return nome + "\t\t\t" + ingredienti + "\t\t\t" + prezzo;
    }
}