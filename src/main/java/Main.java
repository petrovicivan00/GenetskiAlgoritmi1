public class Main {

    static int vp = 10; // velicina populacije(10,50,90)
    static Double phigh = 3.0;
    static Double plow = -3.0;
    static int jzp = 3; //jedinke za parenje
    static double vMutacije = 0.15;

    public static void main(String[] args) {
        GA ga = new GA();
        ga.ga();
    }
}
