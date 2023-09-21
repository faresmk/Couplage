import java.io.*;
import java.util.Arrays;
public class EssaiGraphe {
    public static void main(String [] args) {
        FileInputStream f;
        Graphe g;
        boolean b =true;
        Arc a[];
       // int [] b;
       // int b;

        try {
            f = new FileInputStream(args[0]);
            g = new Graphe(f);
             b = g.estBiparti();
           // a = g.getArcsIndependants();


            System.out.println(b);
        } catch (Exception e) {
            System.out.println(e);
        }

    }
}
