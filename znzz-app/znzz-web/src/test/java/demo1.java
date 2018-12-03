import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class demo1 {
   /* public static void main(String[] args) {
        int i,j,k,n;
        Scanner scanner = new Scanner(System.in);
        System.out.print("请输入金字塔层数:");
        n = scanner.nextInt();
        //外层循环控制层数
        for (i = 1; i <= n; i++){
            //根据外层行号,输出星号个数
            for(j = 1; j <= n-i; j++)
                System.out.print(" ");
            //根据外层行号,输出星号个娄
            for (k = 1; k <= 2*i-1; k++)
                System.out.printf("*");
            //一行结束,换行
            System.out.printf("\n");
        }
    }*/

    public static void main(String[] args){
        List<String> list = new ArrayList<String>();
        list.add("aaa");
        list.add("bbb");
        list.add("ccc");
        list.add("ddd");
        list.add("aaa");
        list.add("aaaa");
        list.add("eee");
        list.add("bbb");
        list.add("ccc");
        StringBuilder builder = new StringBuilder();
        for(String str : list) {
            // 如果不存在返回 -1。
            if(builder.indexOf(","+str+",") > -1) {
                System.out.println("重复的有："+str);
            } else {
                builder.append(",").append(str).append(",");
            }
        }
    }

}
