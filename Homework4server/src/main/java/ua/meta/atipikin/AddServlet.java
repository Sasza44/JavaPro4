package ua.meta.atipikin;

import java.io.BufferedReader;
import java.io.IOException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(value = "/add")
public class AddServlet extends HttpServlet {
    private MessageList msgList = MessageList.getInstance();
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        BufferedReader br = request.getReader(); // зчитування тексту запиту
        String requestString = br.readLine();
        Message m = Message.fromJSON(requestString); // повідомлення, яке тільки отримали від клієнта

        if (m != null) msgList.add(m);
        else response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }
}