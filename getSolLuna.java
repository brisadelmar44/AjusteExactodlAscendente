import java.util.Scanner;
import java.net.*;
import java.io.*;
/**
 * Write a description of class getData here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class getSolLuna
{
    // instance variables - replace the example below with your own
    public int minutosSol;
    public String astroSol;
    public int minutosLuna;
    public String astroLuna;

    /**
     * Constructor for objects of class getData
     */
    public getSolLuna(int dia, int mes, int anyo, int hora, int minutos, int segundos)
    {
        // initialise instance variables
        String content = null;
        URLConnection connection = null;
            try {
                  String URLS = "http://www.astropampa.com/efemerides?inday=" + dia + "&inmonth=" + mes + "&inyear=" + anyo + "&idc=efemerides&inhours=" + hora + "&inmins=" + minutos + "&insecs=" + segundos + "&bot_buscar=Buscar";
                  //System.out.println("Conectando para " + URLS);
                  connection =  new URL(URLS).openConnection();
                  URL u = new URL(URLS);
                  HttpURLConnection huc =  ( HttpURLConnection )  u.openConnection ();
                  huc.setRequestMethod ("GET");  //OR  huc.setRequestMethod ("HEAD"); 
                  huc.connect () ; 
                  int code = huc.getResponseCode();
                  //System.out.print("Codigo: " + code + "");
                  if(code == 200) {
                    //System.out.println("OK Escribiendo...");
                    Scanner scanner = new Scanner(connection.getInputStream(), "UTF-8");
                    scanner.useDelimiter("\\Z");
                    content = scanner.next();
                    //System.out.println(content);
                    if(content.indexOf("HTTP Error 404.0") >= 0) {
                        System.out.println("No se encontró");
                    } else {
                        //Tenemos la web lista
                                               
                        //Sacamos SOL
                        int first = content.indexOf("hspace");
                        int second = content.indexOf("listado", first + 1);
                        second += 13;
                        String result = content.substring(second, second + 100);

                        int solMinutos = Integer.parseInt(result.substring(0, 2));
                        String astroSol = result.substring(3, 6);
                        
                        //Sacamos Luna
                        int third = content.indexOf("hspace",second + 1);
                        int forth = content.indexOf("listado", third + 1);
                        forth+= 13;
                        String resultLuna = content.substring(forth, forth + 100);
                        //System.out.println(resultLuna);
                        int lunaMinutos = Integer.parseInt(resultLuna.substring(0, 2));
                        String astroLuna = resultLuna.substring(3, 6);
                        //System.out.println("Los minutos de la luna son: " + lunaMinutos + " y los segundos: " + lunaSegundos);
                        
                        this.astroSol = astroSol;
                        minutosLuna = lunaMinutos;
                        minutosSol = solMinutos;
                        this.astroLuna = astroLuna;
                    }
                  }
              }catch ( Exception ex ) {
                ex.printStackTrace();
            }
    }
}
