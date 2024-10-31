package co.edu.uptc.utilities;

public class UtilThread {

    public static void sleep(int miliseconds){
        try {
            Thread.sleep(miliseconds);
        } catch (InterruptedException e) {
        }
    }
    
}
