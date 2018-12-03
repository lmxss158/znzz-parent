public class demo2 {
    public static void main(String[] args) {
        //外层控制行数
        for (int i = 1; i < 10; i++){
            //内层控制表达式个数
            for(int j = 1; j <= i; j++){
                System.out.print(" " +i+ "*"+j+"="+(i*j));
            }
            System.out.println();
        }
    }
}
