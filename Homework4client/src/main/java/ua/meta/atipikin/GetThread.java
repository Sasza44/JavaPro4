package ua.meta.atipikin;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GetThread implements Runnable {
    private final Gson gson;
    private int n = 0;
    private boolean permission;
    private String login;
    public GetThread(boolean permission, String login) {
        gson = new GsonBuilder().setDateFormat("dd-MM-yyyy HH:mm:ss").create();
        this.permission = permission;
        this.login = login;
    }

    // метод, який відправляє GET запит на сервер
    private void sendSet() throws URISyntaxException, IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(Utils.getURL() + "/get?from=" + n))
                .headers("Content-Type", "text/plain;charset=UTF-8").GET().build();

        HttpClient client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if(response.statusCode() == 200) {
            String msgListFromN = response.body(); // тіло відповіді у вигляді рядка (список повідомлень від індексу n)
            if(!msgListFromN.isEmpty()) {
                // редагування рядка для зручного поділу json-об'єкту на окремі повідомлення
                getMessagesList(msgListFromN);
            }
        }
    }

    private void getMessagesList(String msgListFromN) {
        StringBuilder msgListN = new StringBuilder(msgListFromN);
        msgListN.delete(0, 9); // обрізування зайвого
        int l = msgListN.length(); // довжина рядка
        msgListN.delete(l - 4, l);  // обрізування зайвого
        msgListFromN = msgListN.toString();

        // розділяємо отриманий рядок на список через кожну 4-ту кому
        String[] a1 = msgListFromN.split(","); // проміжний масив

        List<Message> l1 = new ArrayList<>(); // список отриманих повідомлень
        for(int i = 0; i + 3 < a1.length; i += 4) { // об'єднуємо кожні 4 елементи масиву в один
            l1.add(Message.fromJSON(a1[i] + "," + a1[i + 1] + "," + a1[i + 2] + "," + a1[i + 3]));
        }

        n += l1.size(); // зміна параметра n

        // використовуємо Predicate (залишаємо загальнодоступні повідомлення і ті, які адресовані цьому відправнику)
        l1.removeIf(a -> !a.getTo().isEmpty() && !a.getTo().equals(this.login));
        l1.forEach(b -> System.out.println(b)); // виводимо список після відсівання
    }

    @Override
    public void run() {
        while (!Thread.interrupted()) {
            try {
                if(permission) sendSet(); // GET запит відправимо після того, як ввели повідомлення
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}