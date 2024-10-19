import external.DatabaseReader;
import external.HttpClient;
import external.impl.DatabaseReaderImpl;
import external.impl.HttpClientImpl;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class Main {
    public static void main(String[] args) throws InterruptedException {

        HttpClient httpClient = createProxy(new HttpClientImpl());
        DatabaseReader databaseReader = createProxy(new DatabaseReaderImpl());

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

    public static <T> T createProxy(Object originalObject) {
        Class<?>[] interfaces = originalObject.getClass().getInterfaces();

        TimeMeasuringProxyHandler timeMeasuringProxyHandler = new TimeMeasuringProxyHandler(originalObject);

        return (T) Proxy.newProxyInstance(originalObject.getClass().getClassLoader(), interfaces, timeMeasuringProxyHandler);
    }

    public static class TimeMeasuringProxyHandler implements InvocationHandler {
        private final Object originalObject;

        public TimeMeasuringProxyHandler(Object originalObject) {
            this.originalObject = originalObject;
        }

        @Override
        public Object invoke(Object o, Method method, Object[] args) throws Throwable {
            Object result;

            System.out.println(String.format("Measuring Proxy - Before Executing method : %s", method.getName()));

            long startTime = System.currentTimeMillis();
            result = method.invoke(originalObject,args);
            long endTime = System.currentTimeMillis();

            System.out.println();

            System.out.println(String.format("Measuring Proxy - Executing of %s() took %dms \n", method.getName(),
                    endTime - startTime));

            return result;
        }
    }
}
