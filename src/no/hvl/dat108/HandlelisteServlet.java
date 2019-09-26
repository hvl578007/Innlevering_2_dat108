package no.hvl.dat108;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static no.hvl.dat108.LoginUtil.*;
import static no.hvl.dat108.ListeUtil.*;

/**
 * HandlelisteServlet
 */
@WebServlet("/" + HANDLELISTE_URL)
public class HandlelisteServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    //private static List<String> handleliste;
    private static Handleliste liste;

    @Override
    public void init() throws ServletException {
        //handleliste = new ArrayList<>();
        liste = new Handleliste();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // sjekke om ein er innlogga:
        if (!erInnlogga(request)) {
            settFeilmelding(request, "Du har ikkje lov til å sjå denne sida utan å vere logga inn!");
            response.sendRedirect(LOGGINN_URL);
        }

        String tittel = "Handleliste";

        response.setContentType("text/html; charset=ISO-8859-1");

        PrintWriter out = response.getWriter();

        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<meta charset=\"ISO-8859-1\">");
        out.println("<title>" + tittel + "</title>");
        out.println("</head>");
        out.println("<body>");
        out.println("<h1>Min handleliste</h1>");

        out.println("<form action=\"" + HANDLELISTE_URL + "\" method=\"POST\">");
        out.println("<p>");
        out.println("<input type=\"submit\" value=\"Legg til\"> <input type=\"text\" name=\"nyttHandleObjekt\">");
        out.println("</p>");
        out.println("</form>");

        if (liste.getStorrelse() > 0) {
            Iterator<Entry<Integer,String>> lopar = liste.getIteratorOverPar();

            for (int i = 0; i < liste.getStorrelse() && lopar.hasNext(); i++) {
                Entry<Integer,String> e = lopar.next();
                out.println("<p>");
                out.println("<form action=\"" + HANDLELISTE_URL + "\" method=\"POST\">");
                //lagar hidden som noklar som mappar til det spesifikke handleobjektet.
                //brukar iterator for å gå igjennom alle para i map-et
                out.println("<input type=\"hidden\" name=\"nokkel\" value=\"" + e.getKey() + "\">");
                out.println("<input type=\"submit\" value=\"Slett\">" + e.getValue());
                out.println("</form>");
                out.println("</p>");
            }
        }

        //ved bruk av berre liste
        /*
         * for(String s : handleliste) { out.println("<p>");
         * out.println("<form action=\"" + HANDLELISTE_URL + "\" method=\"POST\">");
         * out.println("<input type=\"hidden\" name=\"objektNamn\" value=\"" + s +
         * "\">"); out.println("<input type=\"submit\" value=\"Slett\">" + s);
         * out.println("</form>"); out.println("</p>"); }
         */

        out.println("</body>");
        out.println("</html>");

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // TRÅDSIKKERHEIT!!! det må jo vere her i doPost i handlelista, det er den som
        // endrar på lista.
        // evt i ListeUtil klassen som faktisk gjer endringa i list (add / remove)

        // sjekke om ein er innlogga: dette vil skje om sesjonen har utløpt
        if (!erInnlogga(request)) {
            response.sendRedirect(LOGGINN_URL);

        } else {

            // sidan eg brukar "unike" id-ar til å lagre varene i handlelista så om 2 slettar på likt vil det berre slette eit objekt
            // og vise rett tilstand når ein får sida tilbake
            // Synkronisert på lista(?) slik at berre ein person kan leggje til / slette på likt: kan også evt. setje sync i handleliste klassen på leggtil/fjern metodane
            synchronized (liste) {
                String nyttHandleObjekt = request.getParameter("nyttHandleObjekt");

                // Kunne evt ha brukt 
                // sjekke om det er å leggje til eller at ein skal slette:
                if (nyttHandleObjekt == null) {
                    // skal slette?

                    try {
                        int nokkel = Integer.parseInt(request.getParameter("nokkel"));
                        fjernFraHandleliste(liste, nokkel);
                    } catch (Exception e) {
                    }

                    //Med berre ei liste av strenger i klassen:
                    /*
                     * String sletteObjekt = request.getParameter("objektNamn"); if(sletteObjekt !=
                     * null) { slettFraLista(handleliste, sletteObjekt); }
                     */
                } else {
                    // må leggje til i lista

                    leggTilHandleliste(liste, nyttHandleObjekt);

                    // leggTilILista(handleliste, nyttHandleObjekt);
                }
            }

            // redirect
            response.sendRedirect(HANDLELISTE_URL);
        }

    }

}