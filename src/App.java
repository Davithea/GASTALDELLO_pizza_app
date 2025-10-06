import com.google.gson.Gson;
import okhttp3.*;
import java.io.IOException;
import java.util.Scanner;

public class App {
    private static final String BASE_URL = "https://crudcrud.com/api/a12a6d37aff1489295ad0dc49bdf2cef/pizze";
    private final OkHttpClient client;
    private final Gson gson;
    private final Scanner scanner;

    public App() {
        client = new OkHttpClient();
        gson = new Gson();
        scanner = new Scanner(System.in);
    }

    //GET - Recupera tutte le pizze
    public void doGet() throws IOException {
        Request request = new Request.Builder()
                .url(BASE_URL)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful())
                throw new IOException("Errore GET: " + response);

            Pizza[] pizze = gson.fromJson(response.body().string(), Pizza[].class);

            if (pizze.length == 0) {
                System.out.println("Nessuna pizza trovata.");
                return;
            }

            System.out.println("\n--- LISTA PIZZE ---");
            for (Pizza pizza : pizze) {
                System.out.println(pizza);
            }
        }
    }

    //POST - Aggiunge una nuova pizza
    public void doPost(Pizza pizza) throws IOException {
        String json = gson.toJson(pizza);

        RequestBody body = RequestBody.create(
                MediaType.get("application/json; charset=utf-8"),
                json
        );

        Request request = new Request.Builder()
                .url(BASE_URL)
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful())
                throw new IOException("Errore durante POST: " + response);
            System.out.println("✅ Pizza aggiunta con successo: " + response.body().string());
        }
    }

    //PUT - Aggiorna una pizza esistente
    public void doPut(String id, Pizza pizzaAggiornata) throws IOException {
        String url = BASE_URL + "/" + id;
        pizzaAggiornata.set_id(null); // CrudCrud non consente di aggiornare l'_id

        String json = gson.toJson(pizzaAggiornata);

        RequestBody body = RequestBody.create(
                MediaType.get("application/json; charset=utf-8"),
                json
        );

        Request request = new Request.Builder()
                .url(url)
                .put(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful())
                throw new IOException("Errore durante PUT: " + response);
            System.out.println("✅ Pizza aggiornata con successo. Codice: " + response.code());
        }
    }

    //DELETE - Elimina una pizza tramite ID
    public void doDelete(String id) throws IOException {
        String url = BASE_URL + "/" + id;

        Request request = new Request.Builder()
                .url(url)
                .delete()
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful())
                throw new IOException("Errore durante DELETE: " + response);
            System.out.println("✅ Pizza eliminata con successo. Codice: " + response.code());
        }
    }

    //MENU UTENTE
    public void run() {
        boolean exit = false;

        while (!exit) {
            System.out.println("\n===== MENU PIZZERIA =====");
            System.out.println("1. Visualizza tutte le pizze");
            System.out.println("2. Aggiungi una pizza");
            System.out.println("3. Modifica una pizza");
            System.out.println("4. Elimina una pizza");
            System.out.println("0. Esci");
            System.out.print("Scelta: ");

            String choice = scanner.nextLine();

            try {
                switch (choice) {
                    case "1":
                        doGet();
                        break;
                    case "2":
                        aggiungiPizza();
                        break;
                    case "3":
                        modificaPizza();
                        break;
                    case "4":
                        eliminaPizza();
                        break;
                    case "0":
                        exit = true;
                        System.out.println("Uscita...");
                        break;
                    default:
                        System.out.println("Scelta non valida.");
                }
            } catch (IOException e) {
                System.out.println("❌ Errore: " + e.getMessage());
            }
        }
    }

    // --- Metodi di supporto per il menu ---

    private void aggiungiPizza() throws IOException {
        System.out.print("Nome: ");
        String nome = scanner.nextLine();

        System.out.print("Prezzo: ");
        double prezzo = Double.parseDouble(scanner.nextLine());

        System.out.print("Ingredienti: ");
        String ingredienti = scanner.nextLine();

        Pizza nuova = new Pizza(nome, prezzo, ingredienti);
        doPost(nuova);
    }

    private void modificaPizza() throws IOException {
        System.out.print("Inserisci ID della pizza da modificare: ");
        String id = scanner.nextLine();

        System.out.print("Nuovo nome: ");
        String nome = scanner.nextLine();

        System.out.print("Nuovo prezzo: ");
        double prezzo = Double.parseDouble(scanner.nextLine());

        System.out.print("Nuovi ingredienti: ");
        String ingredienti = scanner.nextLine();

        Pizza aggiornata = new Pizza(nome, prezzo, ingredienti);
        doPut(id, aggiornata);
    }

    private void eliminaPizza() throws IOException {
        System.out.print("Inserisci ID della pizza da eliminare: ");
        String id = scanner.nextLine();
        doDelete(id);
    }

    public static void main(String[] args) {
        new App().run();
    }
}
