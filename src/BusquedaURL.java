/*
Autores:    Catherine Neira Saavedra
            Fernando Sánchez Espinoza
            José Pedro Unda Montecinos
*/

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Queue;

public class BusquedaURL {

    static Nodo root;
    static Queue<Nodo> cola = new LinkedList<Nodo>();
    static String url2;

    public static void main(String[] args) {
        String url1 = "https://adecca.ubiobio.cl/";
        url2 = "https://intranet.ubiobio.cl/8af8cf98daf113077701c8d80d870364/intranet/?";
        int profundidad = 4;

        root = new Nodo(url1, 0, null);
        cola.add(root);
        if (comparar(root, url2)) {
            // Comparación para ver si el url objetivo es el url inicial
            System.out.println("\nEl link ha sido encontrado: " + cola.poll().toString());

            System.exit(1);
        }
        buscaLinks(root);

        while (!cola.isEmpty()) {
            // Bucle para recorrer y revisar si los nodos "hijos" contienen el url objetivo
            if (comparar(cola.element(), url2)) {
                System.out.println("\nEl link ha sido encontrado: " + cola.poll().toString());
                System.exit(1);
            }
            if (cola.element().getDepth() == profundidad) {
                System.out.println("No se encontró el url en la profundidad entregada: " + profundidad);
                System.exit(1);
            }
            buscaLinks(cola.element());

        }
    }

    public static void buscaLinks(Nodo link) {
        try {

            URL url = new URL(link.getUrl());
            URLConnection connection = url.openConnection();
            BufferedReader buff = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;

            // Mientras queden líneas por leer, revisará si éstas contienen un href (un
            // hipervínculo)
            while ((inputLine = buff.readLine()) != null) {
                // TODO: Implementar caso en que una línea posea más de 1 href
                // TODO: Implementar que los links que partan de un slash se lean (considerando
                // que lo que va antes del slash es el url padre)
                if (inputLine.contains("href")) {
                    // Se ubica en la posición posterior a href=" y crea un substring con el
                    // contenido entre las comillas del href
                    String linkEncontrado;
                    int inputLineSegment1 = inputLine.indexOf("href") + 6;
                    int inputLineSegment2;
                    if (inputLine.length() < inputLineSegment1) {
                        inputLineSegment2 = inputLineSegment1 - 1;
                    } else {
                        inputLineSegment2 = inputLine.substring(inputLineSegment1).indexOf("\"") + inputLineSegment1;
                    }

                    if ((inputLineSegment1 > inputLineSegment2) ||
                            (inputLineSegment1 >= inputLine.length()) || (inputLineSegment2 > inputLine.length())) {
                        linkEncontrado = "";
                    } else {
                        linkEncontrado = inputLine.substring(inputLineSegment1, inputLineSegment2);
                    }

                    if (!linkEncontrado.isEmpty()) {
                        if (linkEncontrado.charAt(0) != '#' &&
                                !linkEncontrado.contains(".css") && !linkEncontrado.contains(".png")) {
                            // Crea el nuevo nodo del árbol
                            Nodo hijo = new Nodo(linkEncontrado, link.getDepth() + 1, link);
                            // Lo asocia como hijo del nodo revisado
                            link.addHijos(hijo);
                            // Lo agrega a la cola para establecerlo como link pendiente por revisar
                            cola.add(hijo);
                        }
                    }

                }
            }
            if (link != cola.remove()) {
                System.out.println("El nodo revisado difiere del que salió por la cola");
                System.exit(-1);
            }
            buff.close();
        } catch (IOException e) {
            // El link a revisar no corresponde a una URL con la que se establezca una
            // conexión, por lo que lo quita de la cola

            System.out.println("\nHipervínculo ignorado: " + cola.poll().getUrl());
            System.out.println("Seguimos buscando...");
        } catch (NoSuchElementException e) {
            // La cola se encontraba vacía pero se estaba revisando un nodo
            System.err.println("La cola quedó vacía pero habían nodos por recorrer");
        }
    }

    public static boolean comparar(Nodo urlRevisar, String urlObjetivo) {
        // Metodo el cual revisa si el url objetivo esta contenido en el nodo que se
        // esta revisando, viceversa y que el url a revisar no sea vacío.
        System.out.println("\nRevisando Link: " + urlRevisar.getUrl());
        return ((urlRevisar.getUrl().contains(urlObjetivo) || urlObjetivo.contains(urlRevisar.getUrl()))
                && urlRevisar.getUrl().contains("."));
    }
}