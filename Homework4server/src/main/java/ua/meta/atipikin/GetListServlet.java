package ua.meta.atipikin;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = "/get")
public class GetListServlet extends HttpServlet {
    public  void doGet (HttpServletRequest request, HttpServletResponse response) throws IOException {
        int n = Integer.parseInt(request.getParameter("from"));
        String msgListFromN = MessageList.getInstance().toJSON(n); // кінець списку (від n)

        response.setContentType("text/plain");

        if(msgListFromN != null) {
            PrintWriter out = response.getWriter();
            out.println(msgListFromN);
            out.close();
        }
    }
}