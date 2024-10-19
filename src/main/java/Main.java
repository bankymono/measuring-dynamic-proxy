import external.DatabaseReader;
import external.HttpClient;
import external.impl.DatabaseReaderImpl;
import external.impl.HttpClientImpl;

public class Main {
    public static void main(String[] args) throws InterruptedException {

        HttpClient httpClient = new HttpClientImpl();
        DatabaseReader databaseReader = new DatabaseReaderImpl();

        useHttpClient(httpClient);
        useDatabaseReader(databaseReader);

    }

    public static void useHttpClient(HttpClient httpClient) {
        httpClient.initialize();
        String response = httpClient.sendRequest("some request");

        System.out.println(String.format("Http response is : %s", response));
    }

    public static void useDatabaseReader(DatabaseReader databaseReader) throws InterruptedException {
        int rowsInGamesTable = databaseReader.countRowsInTable("GamesTable");

        System.out.println(String.format("There are %s rows in GamesTable", rowsInGamesTable));

        String [] data = databaseReader.readRow("SELECT * from GamesTable");

        System.out.println(String.format("Received %s", String.join(",", data)));
    }
}
