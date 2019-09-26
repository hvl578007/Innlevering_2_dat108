package no.hvl.dat108;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static no.hvl.dat108.LoginUtil.*;

/**
 * Innlogging
 */
@WebServlet("/" + LOGGINN_URL)
public class InnloggingServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static String rettPassord;
    private static int timeoutTid;

    @Override
    public void init() throws ServletException {
        rettPassord = getServletContext().getInitParameter("Passord");
        try {
            timeoutTid = Integer.parseInt(getServletContext().getInitParameter("TimeoutTid"));
        } catch (Exception e) {
            timeoutTid = 60;
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //hente inn evt feilmelding
        String feilmelding = hentFeilmelding(request);
        
        String tittel = "Innlogging";

        response.setContentType("text/html; charset=ISO-8859-1");
        
        PrintWriter out = response.getWriter();

        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<meta charset=\"ISO-8859-1\">");
        out.println("<title>" + tittel + "</title>");
        out.println("</head>");
        out.println("<body>");
        //skriv ut feilmelding
        if (feilmelding != null && !feilmelding.equals("")) {
            out.println("<p><font color=\"red\">" + feilmelding + "</font></p>");
        } else {
            out.println("<p>Skriv inn passord:</p>");
        }
        out.println("<form action=\"" + LOGGINN_URL + "\" method=\"POST\">");
        out.println("<p><input type=\"text\" name=\"passord\"></p>");
        out.println("<p><input type=\"submit\" value=\"Logg inn\"></p>");
        out.println("</form>");
        out.println("</body>");
        out.println("</html>");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String passord = request.getParameter("passord");

        //sjekke om rett passord: (init i web.xml)
        if(!erRettPassord(passord, rettPassord)) {
            //nei -> feilmelding og redirect tilbake med ein feilmelding
            settFeilmelding(request, "Du skreiv inn feil passord. Prøv igjen:");
            response.sendRedirect(LOGGINN_URL);
        } else {
            //logg inn: + sett ein timer (init i web.xml)
            loggInn(request, timeoutTid);

            //redirect
            response.sendRedirect(HANDLELISTE_URL);
        }
    }
}