import java.util.Scanner;
import java.net.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.LinkedHashMap;
/**
 * Write a description of class getData here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class getCasaHTTP
{
    // instance variables - replace the example below with your own
    public int grados;
    public int minutos;
    public int segundos;
    public String astro;
    
    /**
     * Constructor for objects of class getData
     */
    public getCasaHTTP(String fecha, String hora, String gmt, String lat, String latNS, String lon, String lonEW)
    {
        // initialise instance variables
        String content = null;
        URLConnection connection = null;
            try {
                
                    //String urlParameters  = "nombre=Diego&fecha=10-10-1993&hora=10:10:10&ST=1&coor=1&lat=10:10:10&NS=N&lon=020:20:20&EW=E&xh=+00:00";
                    URL url = new URL("https://carta-natal.es/carta.php");
                    Map<String,Object> params = new LinkedHashMap<>();
                    params.put("nombre", "Jhon");
                    params.put("fecha", fecha);
                    params.put("hora", hora);
                    params.put("ST", 1);
                    params.put("coor", "manual");
                    params.put("lat", lat);
                    params.put("NS", latNS);
                    params.put("lon", lon);
                    params.put("EW", lonEW);
                    params.put("xh", gmt);
                  //System.out.println("Conectando para " + url);
                  StringBuilder postData = new StringBuilder();
                    for (Map.Entry<String,Object> param : params.entrySet()) {
                        if (postData.length() != 0) postData.append('&');
                        postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                        postData.append('=');
                        postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
                    }
                    byte[] postDataBytes = postData.toString().getBytes("UTF-8");
        
                    HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
                    conn.setDoOutput(true);
                    conn.getOutputStream().write(postDataBytes);
            
                Reader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            
                    StringBuilder sb = new StringBuilder();
                    for (int c; (c = in.read()) >= 0;)
                        sb.append((char)c);
                    String response = sb.toString();
                    response = replaceAcutesHTML(response);
                    
                    /*
                    Debug de los parametros de la pagina de carta-natal.es 
                    int firstPr = response.indexOf("<strong>CARTA NATAL</strong>");
                    String resultPr = response.substring(firstPr, firstPr + 500);
                    System.out.println(resultPr);
                    */
                   
                    int first = response.indexOf("<td>Casa 1 (AC)</td>");
                    String result = response.substring(first, first + 500);
                    
                    int x = result.indexOf("title");
                    x+=7;
                    String astro = result.substring(x, x + 3);
                    //System.out.println(astro);
                    this.astro = astro;
                    
                    int second = result.indexOf("</div>");
                    second+=6;
                    
                    int grados = Integer.parseInt(result.substring(second, second + 2));
                    second+=7;
                    this.grados = grados;
                    
                    int minutos = Integer.parseInt(result.substring(second, second + 2));
                    second+=3;
                    this.minutos = minutos;
                    
                    int segundos = Integer.parseInt(result.substring(second, second + 2));
                    this.segundos = segundos;
                    
                } catch ( Exception ex ) {
                    ex.printStackTrace();
                }
    }
    public String replaceAcutesHTML(String str) {
        str = str.replaceAll("&aacute;","a");
        str = str.replaceAll("&eacute;","e");
        str = str.replaceAll("&iacute;","i");
        str = str.replaceAll("&oacute;","o");
        str = str.replaceAll("&uacute;","u");
        str = str.replaceAll("&Aacute;","A");
        str = str.replaceAll("&Eacute;","E");
        str = str.replaceAll("&Iacute;","I");
        str = str.replaceAll("&Oacute;","O");
        str = str.replaceAll("&Uacute;","U");
        str = str.replaceAll("&ntilde;","Ñ");
        str = str.replaceAll("&Ntilde;","N");
        
        return str;
    }
}
