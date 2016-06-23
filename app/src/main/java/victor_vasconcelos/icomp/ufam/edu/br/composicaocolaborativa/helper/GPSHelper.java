package victor_vasconcelos.icomp.ufam.edu.br.composicaocolaborativa.helper;

import android.util.Log;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

public class GPSHelper {

    public static boolean setArea(double xFixo, double yFixo, double longiVar, double latVar, double raio){

        DecimalFormat df = new DecimalFormat("#.00000");
        xFixo = Double.parseDouble(df.format(xFixo).replace(',','.'));
        yFixo = Double.parseDouble(df.format(yFixo).replace(',','.'));
        longiVar = Double.parseDouble(df.format(longiVar).replace(',','.'));
        latVar = Double.parseDouble(df.format(latVar).replace(',','.'));
        double paramX = longiVar - xFixo;
        double paramY = latVar - yFixo;
        paramX = pow(paramX,2);
        Log.i("Get", "PARAMX DFORMAT: " + paramX);

        //noinspection SuspiciousNameCombination
        paramY = pow(paramY,2);
        Log.i("Get", "PARAMY DFORMAT: " + paramY);

        double dist = (sqrt(paramX + paramY));
        Log.i("Get", "DIST DFORMAT: " + dist);



        //Valores muito pequenos!!!!!!!!!!!!!!!
        /*
        *
        * */


        /*BigDecimal bigDist = new BigDecimal("" + dist).setScale(10, BigDecimal.ROUND_HALF_EVEN);
        BigDecimal bigRaio = new BigDecimal("" + raio);
        Log.i("GetAmbienteAll", "BIGDIST: " + bigRaio.compareTo(bigDist));
        Log.i("GetAmbienteAll", "BIGDIST VALOR: " + bigDist);
        Log.i("GetAmbienteAll", "DIST: " + dist);*/
        return dist <= raio;
    }
    public static BigDecimal getDist(double xFixo, double yFixo, double longiVar, double latVar){

        double paramX = longiVar - xFixo;
        double paramY = latVar - yFixo;
        paramX = pow(paramX,2);

        //noinspection SuspiciousNameCombination
        paramY = pow(paramY,2);

        double testeDist = (sqrt(paramX + paramY));
        BigDecimal dist = new BigDecimal(""+testeDist).setScale(7,BigDecimal.ROUND_HALF_EVEN);

        return dist;
    }



}
