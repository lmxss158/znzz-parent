public class demo7 {
    static  int count = 0 ;
    public static void main(String[] args) {

        method1();
    }

    private static void method1() {
        System.out.println("哈哈跑起来....."+count++);
        method1();
    }


}
