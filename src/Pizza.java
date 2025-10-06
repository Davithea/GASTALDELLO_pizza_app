public class Pizza {
    private String _id;
    private String nome;
    private double prezzo;
    private String ingredienti;

    public Pizza(String nome, double prezzo, String ingredienti) {
        this.nome = nome;
        this.prezzo = prezzo;
        this.ingredienti = ingredienti;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    @Override
    public String toString() {
        return "Pizza{" +
                "_id='" + _id + '\'' +
                ", nome='" + nome + '\'' +
                ", prezzo=" + prezzo +
                ", ingredienti='" + ingredienti + '\'' +
                '}';
    }
}
