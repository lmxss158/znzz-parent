import com.znzz.app.web.commons.util.Configer;

import java.util.*;
import java.util.concurrent.Callable;

public class Test {
	public static void main(String[] args) {
//		List<Date> list = new ArrayList<>();
//		list.add(new Date());
//		System.out.println(list);

		/*Map<String, Integer> map =new HashMap<String, Integer>();

		map.put("a",1);

		System.out.println(map.get("b"));*/

		for (int j = 0; j <= 9; j++) {
			for (int i = 1; i <= j; i++) {
				System.out.print(i + "*" + j + "=" + i * j + "\t");
			}
			System.out.println();
		}
	}

	/*@Test
	public void test(){
		Boolean flag = false;
		flag = Boolean.parseBoolean(Configer.getInstance().get("uploadToCloud").toString());

		System.out.println(flag instanceof Boolean);
	}*/
}
