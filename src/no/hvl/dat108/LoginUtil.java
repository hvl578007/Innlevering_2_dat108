package no.hvl.dat108;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * LoginUtil
 */
public class LoginUtil {

    public static final String HANDLELISTE_URL = "handleliste";
    public static final String LOGGINN_URL = "logginn";

    public static boolean erInnlogga(HttpServletRequest request) {
        //at "innlogga" attributten finst er nok
        return request.getSession().getAttribute("innlogga") != null;
    }

    public static boolean erRettPassord(String passord, String rettPassord) {
        return passord.equals(rettPassord);
    }

    public static void loggInn(HttpServletRequest request, int sesjonTid) {
        loggUt(request);
        HttpSession sesjon = request.getSession();
        sesjon.setAttribute("innlogga", "ER_INNLOGGA");
        sesjon.setMaxInactiveInterval(sesjonTid);
    }

    //blir brukt i loggInn for å invalidere ein sesjon før ein lagar ein ny ein og loggar inn
    public static void loggUt(HttpServletRequest request) {
        HttpSession sesjon = request.getSession(false);
        if(sesjon != null) {
            sesjon.invalidate();
        }
    }

    //hentar feilmelding frå sesjonen og fjernar den etterpå
    //kunne evt ha brukt kodar i feilmeldingar og så hatt noko switch-case / if-else greier og gitt ein tekststreng tilbake
    public static String hentFeilmelding(HttpServletRequest request) {
        String feil = (String)request.getSession().getAttribute("feilmelding");
        request.getSession().removeAttribute("feilmelding");
        return feil;
    }

    //set feilmelding i sesjonen
    public static void settFeilmelding(HttpServletRequest request, String feilmelding) {
        request.getSession().setAttribute("feilmelding", feilmelding);
    }
}