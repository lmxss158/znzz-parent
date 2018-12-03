package com.znzz.app.web.commons.util;

import com.znzz.framework.page.datatable.DataTableColumn;
import com.znzz.framework.page.datatable.DataTableOrder;
import org.nutz.dao.Cnd;
import org.nutz.dao.Sqls;

import java.util.List;

public class OrderByUtil {

    /**
     * 中文排序，用于抽样检定和周期检定
     *
     */
    public static Cnd orderByWithChinese(Cnd cnd, List<DataTableOrder> order, List<DataTableColumn> columns) {
        for (DataTableOrder or : order) {
            DataTableColumn col = columns.get(or.getColumn());
            //获取排序的字段
            String name = col.getData();
            if ("assetname".equals(name)) {
                name = " CONVERT( `assetname` USING gbk)";
            } else if ("testdepartment".equals(name)) {
                name = " CONVERT( `testdepartment` USING gbk)";
            } else if ("usedepartment".equals(name)) {
                name = " CONVERT( `usedepartment` USING gbk)";
            } else if ("userman".equals(name)) {
                name = " CONVERT( `userman` USING gbk)";
            } else if ("respman".equals(name)) {
                name = " CONVERT( `respman` USING gbk)";
            } else if ("testresult".equals(name)) {
                name = " CONVERT( `testresult` USING gbk)";
            } else if ("remark".equals(name)) {
                name = " CONVERT( `remark` USING gbk)";
            }
            cnd.orderBy(Sqls.escapeSqlFieldValue(name).toString(), or.getDir());
        }
        return cnd;
    }
}
