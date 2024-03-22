package ua.meta.atipikin;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
        System.out.println("Введіть логін: ");
        String login = sc.nextLine();
        System.out.println("Введіть логін співрозмовника, кому призначене повідомлення");
        System.out.println("(якщо ввід порожній, повідомлення буде загальнодоступним): ");
        String companion = sc.nextLine();
        boolean permission = true; // заборона виведення списку, поки не ввели текст

        Thread th = new Thread(new GetThread(permission, login));
        th.setDaemon(true);
        th.start();

        System.out.println("Введіть повідомлення: ");
        while (true) {
            permission = false; // забороняємо виведення повідомлень, поки не дописали своє
            String text = sc.nextLine(); // текст повідомлення
            permission = true;
            if (text.isEmpty()) break; // вихід з циклу при введенні порожнього повідомлення
            Message m = new Message(login, companion, text); // новий об'єкт класу Message
            int res = 0; // змінна для коду відповіді сервера
            try {
                res = m.sendPost(Utils.getURL() + "/add"); // відправлення повідомлення на сервер
                Thread.sleep(1000); // "вікно" для отримання повідомлень
            } catch (URISyntaxException | IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
            if(res != 200) { // повідомлення, яке виведеться при відсутності зв'язку
                System.out.println("HTTP error ocurred: " + res);
            }
        }
    }
}