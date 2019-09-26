package no.hvl.dat108;

import java.util.List;

import static org.apache.commons.lang3.StringEscapeUtils.*;

/**
 * ListeUtil
 */
public class ListeUtil {

    public static void leggTilILista(List<String> liste, String objekt) {
        if(objekt != null && !objekt.equals("")) {
            objekt = escapeHtml4(objekt);
            liste.add(objekt);
        }
    }

    public static void slettFraLista(List<String> liste, String objekt) {
        if (objekt != null) {
            objekt = escapeHtml4(objekt);
            liste.remove(objekt);
        }
    }

    public static void leggTilHandleliste(Handleliste liste, String objekt) {
        if (objekt != null && liste != null && !objekt.equals("")) {
            objekt = escapeHtml4(objekt);
            liste.leggTil(objekt);
        }
    }

    public static void fjernFraHandleliste(Handleliste liste, int nokkel) {
        if (liste != null) {
            liste.fjern(nokkel);
        }
    }
}