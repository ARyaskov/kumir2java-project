
public class RTL {

    public static void ku_print(int ivalue) {
        System.out.print(ivalue);
    }

    public static void ku_print(double dvalue) {
        System.out.print(dvalue);
    }

    public static void ku_print(char cvalue) {
        System.out.print(cvalue);
    }

    public static void ku_print(boolean bvalue) {
        if (bvalue == true) {
            System.out.print("да");
        } else {
            System.out.print("нет");
        }
    }

    public static void ku_print(String svalue) {
        System.out.print(svalue);
    }

    public static void ku_println() {
        System.out.println();
    }

    public static int ku_pow(int base, int exp) {
        return (int) Math.pow((double) base, (double) exp);
    }

    public static double ku_pow(double base, double exp) {
        return Math.pow(base, exp);
    }
}
