package hellojpa;

public class ValueMain {

    public static void main(String[] args) {

        Integer a = new Integer(10);
        Integer b = a;

        a = new Integer(20);
        System.out.println("a = " + a);
        System.out.println("b = " + b);
    }
}
