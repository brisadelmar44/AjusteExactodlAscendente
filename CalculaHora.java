import java.util.Date;
import java.time.LocalDate;

import java.text.SimpleDateFormat;
import java.text.ParseException;
/**
 * Main del programa
 * 
 * @author MandarinaWebs 
 * @v0.9.5
 */
public class CalculaHora
{

    public CalculaHora()
    {
        //CalculaHora2 c = new CalculaHora2("18-08-1983", "10:00:00", "15-11-1990", "10:00:00", "27-08-2002", "10:20:10", "-04", "04:37:00", "N", "061:08:00", "W");
        
        //  1. Fecha de nacimiento de la madre
        //  2. Hora de nacimiento de la madre
        //  3. Fecha de cuando empezó a trabajar
        //  4. Hora de cuando empezó a trabajar
        //  5. Fecha de cuando tubo el primer hijo
        //  6. Hora de cuando tubo el primer hijo
        //  7. GMT
        //  8. Latitud
        //  9. Norte o Sud
        //  10. Longitud
        //  11. Este u Oeste
        
        CalculaHora c = new CalculaHora(
        "18-08-1983", 
        "10:00:00", 
        "15-11-1990", 
        "10:00:00", 
        "", 
        "", 
        "-04", 
        "04:37:00", 
        "N", 
        "061:08:00", 
        "W");
    }
    
    public CalculaHora(String fechaPadre, String horaPadre, String fechaTrabajar, String horaTrabajar, String fechaHijo, String horaHijo, String gmt, String latitud, String latNS, String longitud, String lonEW)
    {
        //15-5-1993 12:59:59
        //SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        
        try {
            if(fechaHijo.equals("")) {
                Date f = formatter.parse(fechaPadre + " " + horaPadre);
                //La luna debe ser del hijo
                getSolLuna Efemiredes = new getSolLuna(f.getDate(), f.getMonth() + 1, f.getYear() + 1900, f.getHours() +  Integer.parseInt(gmt), f.getMinutes(), f.getSeconds());
                
                 Date ftrabajo = formatter.parse(fechaTrabajar + " " + horaTrabajar);
                 getSolLuna EfemiredesSol = new getSolLuna(ftrabajo.getDate(), ftrabajo.getMonth() + 1, ftrabajo.getYear() + 1900, ftrabajo.getHours() +  Integer.parseInt(gmt), ftrabajo.getMinutes(), ftrabajo.getSeconds());
                int daysTrabajo = (int)( (ftrabajo.getTime() - f.getTime()) / (1000 * 60 * 60 * 24));
                double edadTrabajo = daysTrabajo / 365.4;
                System.out.println("Empezó a trabajar a los " + edadTrabajo + " años.");
                double fechacteTrabajo = edadTrabajo * 5.789;
                //System.out.println("Edad pto: " + fechacteTrabajo);
                String gmtStr = gmt + ":00"; 
                getCasaHTTP casa1 = new getCasaHTTP(fechaPadre, horaPadre, gmtStr, latitud, latNS, longitud, lonEW);
                int gradosTotales = casa1.grados + getGrados(casa1.astro);
                int solTrabajo = (int) fechacteTrabajo + gradosTotales;
                //System.out.println("SOL: " + solTrabajo);
                double puntodbl = fechacteTrabajo + gradosTotales;
                int punto = (int) puntodbl;
                //System.out.println("Casa 1: " + casa1.grados + "º de " + casa1.astro + ": " + gradosTotales + "º totales");
                int gradosTotalSol =  Efemiredes.minutosSol + getGrados(Efemiredes.astroSol); 
                //System.out.println("Grados totales del sol: " + gradosTotalSol);
                int cteMultiplicar = (int) (fechacteTrabajo / 30);
                int sol1 = gradosTotalSol + cteMultiplicar*30;
            
                int nuevoResultado = (Math.abs(solTrabajo - sol1));
                //System.out.println("¡nuevo resultado! " + nuevoResultado);
                rectifica(f, nuevoResultado, gradosTotales);
            } else {
                Date f = formatter.parse(fechaPadre + " " + horaPadre);
                //La luna debe ser del hijo
                getSolLuna Efemiredes = new getSolLuna(f.getDate(), f.getMonth() + 1, f.getYear() + 1900, f.getHours() +  Integer.parseInt(gmt), f.getMinutes(), f.getSeconds());
                 Date f2 = formatter.parse(fechaHijo + " " + horaHijo);
    
                //Calculamos la cte y la edad del hijo
                LocalDate birthDate = LocalDate.of(f2.getYear() + 1900, f2.getMonth() + 1, f.getDate());
                Date ahora = new Date();
                //int days = (int)( (ahora.getTime() - f2.getTime()) / (1000 * 60 * 60 * 24));
                
                int days = (int)( (f2.getTime() - f.getTime()) / (1000 * 60 * 60 * 24));
                
                double edad = days / 365.4;
                double fechacte = edad * 5.789;
                String gmtStr = gmt + ":00"; 
                getCasaHTTP casa1 = new getCasaHTTP(fechaPadre, horaPadre, gmtStr, latitud, latNS, longitud, lonEW);
            
                System.out.println("Edad: " + edad);
                int gradosTotales = casa1.grados + getGrados(casa1.astro);
                double puntodbl = fechacte + gradosTotales;
                int punto = (int) puntodbl;
                //System.out.println("Casa 1: " + casa1.grados + "º de " + casa1.astro + ": " + gradosTotales + "º totales");
            
                int desfase = punto%30;
                int signo = punto/30;
                int gradosTotalLuna = Efemiredes.minutosLuna + getGrados(Efemiredes.astroLuna);
                int gradosACorregir = (int) Math.abs(puntodbl - gradosTotalLuna);
                rectifica(f, gradosACorregir, gradosTotales);
            }            
        } catch (ParseException e) {
            System.out.println("¡Coincide!");
            e.printStackTrace();
        }
    }
    
    
    private void rectifica(Date f, int gradosACorregir, int gradosTotales){
            int minutosACorregir = gradosACorregir * 4;
            int minutosHora = (f.getMinutes() + minutosACorregir);

            int horaBuena =  f.getHours() + minutosHora/60;
            int minutosHoraBuena =  minutosHora%60;
            
            System.out.println("La hora buena es " + horaBuena + ":" + minutosHoraBuena + ":" + f.getSeconds());
            
            int resultadoFinal = gradosACorregir + gradosTotales;
            System.out.println("ASCENDENTE EXACTO: " +  resultadoFinal);   
    }
    
    private int getGrados(String astro)
    {
        if(astro.equals("Ari"))
            return 0;
        else if(astro.equals("Tau"))
            return 30;
        else if(astro.equals("Gem"))
            return 60;
        else if(astro.equals("Can"))
            return 90;
        else if(astro.equals("Leo"))
            return 120;
        else if(astro.equals("Vir"))
            return 150;
        else if(astro.equals("Lib"))
            return 180;
        else if(astro.equals("Esc"))
            return 210;
        else if(astro.equals("Sag"))
            return 240;
        else if(astro.equals("Cap"))
            return 270;
        else if(astro.equals("Acu"))
            return 300;
        else if(astro.equals("Pis"))
            return 330;
        return 0;
    }
}
