package no.hvl.dat108;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Handleliste
 */
public class Handleliste {

    private Map<Integer, String> liste;
    private AtomicInteger teljar;

    public Handleliste() {
        liste = new LinkedHashMap<>();
        teljar = new AtomicInteger(1);
    }

    public void leggTil(String objekt) {
        liste.put(teljar.getAndIncrement(), objekt);
    }

    public String fjern(int nokkel) {
        return liste.remove(nokkel);
    }

    public Iterator<Entry<Integer,String>> getIteratorOverPar() {
        return liste.entrySet().iterator();
    }

    public int getStorrelse() {
        return liste.size();
    }
}