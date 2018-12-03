import com.znzz.app.web.commons.util.CommonExcelUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class domo5 {
    public static void main(String[] args) {
        //文件
        File file = new File("D:\\Personal\\Desktop\\云网检索\\Find2018-10-24.xlsx");
        InputStream is = null;
        try {
            //将文件放入流中
            is =  new FileInputStream(file);
            XSSFWorkbook xssfWorkbook = new XSSFWorkbook(is);
            //创建要封装的对象/集合
            BObject bObject = null;
            CloudObject cloudObject = null;
            DObject dObject = null;
            List<BObject> bList = new ArrayList<>();
            List<CloudObject> cList = new ArrayList<>();
            List<DObject> dList = new ArrayList<>();

            Sheet b = xssfWorkbook.getSheet("B卡");
            Sheet c = xssfWorkbook.getSheet("C卡");
            Sheet d = xssfWorkbook.getSheet("D");

            for (int rowNum = 1; rowNum < b.getLastRowNum(); rowNum++) {
                Row bRow = b.getRow(rowNum);
                bObject = new BObject();
                bObject.setbId(CommonExcelUtil.getValue(((Row) bRow).getCell(0)).trim());
                bObject.setbAssetCode(CommonExcelUtil.getValue(bRow.getCell(1)).trim());
            }

        }catch (Exception e){

        }
    }
}
