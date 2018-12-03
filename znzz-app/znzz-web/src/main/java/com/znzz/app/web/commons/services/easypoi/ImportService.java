package com.znzz.app.web.commons.services.easypoi;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.ioc.Ioc;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;

import com.znzz.app.sys.modules.models.Sys_import_column;
import com.znzz.app.sys.modules.models.Sys_import_table;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.params.ExcelCollectionParams;
import cn.afterturn.easypoi.excel.entity.params.ExcelImportEntity;
import cn.afterturn.easypoi.excel.entity.result.ExcelImportResult;
import cn.afterturn.easypoi.handler.inter.IExcelDataHandler;
import cn.afterturn.easypoi.handler.inter.IExcelDictHandler;
import cn.afterturn.easypoi.handler.inter.IExcelVerifyHandler;
import cn.afterturn.easypoi.util.PoiReflectorUtil;

@IocBean
public class ImportService {
	@Inject("refer:$ioc")
    protected Ioc ioc;
	@Inject
    private ExcelDictHandlerImpl excelDicHandlerImpl ;



	public <T> List<ExcelImportResult<T>> importFile(HttpServletRequest request ,File file , String reportCode) 
			throws Exception{
		
		Dao dao = ioc.get(Dao.class);
		Cnd cnd = Cnd.NEW();
		cnd.where().and("code", "=", reportCode);
		List<Sys_import_table> listTablePO = dao.query(Sys_import_table.class, cnd);
		if(isEmpty(listTablePO)){
			return null;
		}
		
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for(Sys_import_table tablePO : listTablePO) {
            Class<?> pojoClass;
            Map<String, ExcelImportEntity> excelParams;
            List<ExcelCollectionParams> excelCollection = new ArrayList<ExcelCollectionParams>();
            ImportParams params = getExcelExportParams(tablePO);
            if(isEmpty(tablePO.getType())) {
            	pojoClass = Map.class;
            }
            else {
            	pojoClass = Class.forName(tablePO.getType());
            }
            Cnd cnd2 = Cnd.NEW();
            cnd2.where().and("table_id","=",tablePO.getId());
			
			//获取报表列配置参数
			List<Sys_import_column> listColumnPO = dao.query(Sys_import_column.class, cnd2);
			if(!Map.class.equals(pojoClass)  && listColumnPO==null) {
				return null;
			}
			else if(listColumnPO==null) {
				listColumnPO = new ArrayList<Sys_import_column>();
			}
			
            excelParams = getExcelImportEntityList(listColumnPO, pojoClass);
            
        	Map<String, Object> importMap = new HashMap<String, Object>();
        	importMap.put("class", pojoClass);
        	importMap.put("entity", excelParams);
        	importMap.put("entityCollection", excelCollection);
        	importMap.put("title", params);
        	list.add(importMap);
           
		}
		InputStream is = new FileInputStream(file);
		List<ExcelImportResult<T>> importExcelEntityListMore = ExcelImportUtil.importExcelEntityListMore(is,list);
		is.close();
		return importExcelEntityListMore ;
	}
	
	
	private ImportParams getExcelExportParams(Sys_import_table importPO) {
		ImportParams ep = new ImportParams();
		//titleRows
		if( !isEmpty(importPO.getTitlerows())) {
			ep.setTitleRows(importPO.getTitlerows().intValue());
		}
		//headRows
		if( !isEmpty(importPO.getHeadrows())) {
			ep.setHeadRows(importPO.getHeadrows().intValue());
		}
		//startRows
		if( !isEmpty(importPO.getStartrows())) {
			ep.setStartRows(importPO.getStartrows().intValue());
		}
		//keyIndex
		if( !isEmpty(importPO.getKeyindex())) {
			ep.setKeyIndex(importPO.getKeyindex());
		}
		//startSheetIndex
		if(!isEmpty(importPO.getStartsheetindex() )) {
			ep.setStartSheetIndex(importPO.getStartsheetindex().intValue());
		}
		//sheetNum
		if( !isEmpty(importPO.getSheetnum())) {
			ep.setSheetNum(importPO.getSheetnum().intValue());
		}
		//needSave
		if(!isEmpty(importPO.getNeedsave())) {
			if(importPO.getNeedsave().equals("1")) {
				ep.setNeedSave(true);
			}
			else if(importPO.getNeedsave().equals("0")) {
				ep.setNeedSave(false);
			}					
		}
		//verfiyGroup
		if(!isEmpty(importPO.getVerifygroup())) {
			String[] verifyGroup = importPO.getVerifygroup().split(",");
			List<Class> verifyClass = new ArrayList<Class>();
			try {
				for(String verify : verifyGroup) {
					verifyClass.add(Class.forName(verify));
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			ep.setVerfiyGroup((Class[])verifyClass.toArray());
		}
		//needVerfiy
		if(!isEmpty(importPO.getNeedverify())) {
			if(importPO.getNeedverify().equals("1")) {
				ep.setNeedVerfiy(true);
			}
			else if(importPO.getNeedverify().equals("0")) {
				ep.setNeedVerfiy(false);
			}					
		}
		//verifyHandler
		if(!isEmpty(importPO.getVerifyhandler())) {
			try {
				Object object = null;
				Class<?> forName = Class.forName(importPO.getVerifyhandler());
				object = ioc.get(forName);
				if (object != null) {
					ep.setVerifyHandler((IExcelVerifyHandler<?>)object);
				}
				else{
					Class<?> cl = Class.forName(importPO.getVerifyhandler());
					object = cl.newInstance();
					if(object instanceof IExcelVerifyHandler) {
						ep.setVerifyHandler((IExcelVerifyHandler<?>)object);
					}
				}
				
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		//saveUrl
		if(!isEmpty(importPO.getSaveurl())) {
			ep.setSaveUrl(importPO.getSaveurl());
		}
		//lastOfInvalidRow
		if(!isEmpty(importPO.getLastofinvalirow())) {
			ep.setLastOfInvalidRow(importPO.getLastofinvalirow().intValue());
		}
		//readRows
		if(!isEmpty(importPO.getReadrows())) {
			ep.setReadRows(importPO.getReadrows().intValue());
		}
		//importFields
		if(!isEmpty(importPO.getImportfields())) {
			String[] importField = importPO.getImportfields().split(",");
			ep.setImportFields(importField);
		}
		//needCheckOrder
		if(!isEmpty(importPO.getNeedcheckorder())) {
			if(importPO.getNeedcheckorder().equals("1")) {
				ep.setNeedCheckOrder(true);
			}
			else if(importPO.getNeedcheckorder().equals("0")) {
				ep.setNeedCheckOrder(false);
			}					
		}
		//keyMark
		if(!isEmpty(importPO.getKeymark())) {
			ep.setKeyMark(importPO.getKeymark());
		}
		//readSingleCell
		if(!isEmpty(importPO.getReadsinglecell())) {
			if(importPO.getReadsinglecell().equals("1")) {
				ep.setReadSingleCell(true);
			}
			else if(importPO.getReadsinglecell().equals("0")) {
				ep.setReadSingleCell(false);
			}					
		}
		//data handle
		if(!isEmpty(importPO.getData_handler())) {
			try {
				Object object = null;
				Class<?> forName = Class.forName(importPO.getData_handler());
				object = ioc.get(forName);
				if (object != null) {
					ep.setDataHandler((IExcelDataHandler<?>)object);
				}else{
					Class<?> cl = Class.forName(importPO.getData_handler());
					object = cl.newInstance();
					if(object instanceof IExcelDataHandler) {
						ep.setDataHandler((IExcelDataHandler<?>)object);
					}
				}
				
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		//dic
		if(!isEmpty(importPO.getDic_handler())) {
			try {
				Object object = null;
				Class<?> forName = Class.forName(importPO.getDic_handler());
				object = ioc.get(forName);
				if (object != null) {
					ep.setDictHandler((IExcelDictHandler)object);
				}else{
					Class<?> cl = Class.forName(importPO.getDic_handler());
					object = cl.newInstance();
					if(object instanceof IExcelDictHandler) {
						ep.setDictHandler((IExcelDictHandler)object);
					}
				}
				
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		else {
			ep.setDictHandler(excelDicHandlerImpl);
		}
		return ep;
	}
	
private Map<String, ExcelImportEntity> getExcelImportEntityList(List<Sys_import_column> listColumnPO, Class<?> entityClass) {
		
		Map<String, ExcelImportEntity> listEntity = new HashMap<String, ExcelImportEntity>();
		//生成列配置信息
		for(Sys_import_column columnPO : listColumnPO) {
			ExcelImportEntity entity = new ExcelImportEntity();
			//key
			if(!isEmpty(columnPO.getMap_key())) {
				if(entityClass!=null && !Map.class.equals(entityClass)) {
					entity.setMethod(PoiReflectorUtil.fromCache(entityClass).getSetMethod(columnPO.getMap_key()));					
				}
				else if(Map.class.equals(entityClass)) {
					entity.setCollectionName(columnPO.getMap_key());
				}
			}
			//saveUrl
			if(!isEmpty(columnPO.getSaveurl())) {
				entity.setSaveUrl(columnPO.getSaveurl());
			}
			//saveType
			if(!isEmpty(columnPO.getSavetype())) {
				entity.setSaveType(Integer.parseInt(columnPO.getSavetype()));
			}
			//suffix
			if(!isEmpty(columnPO.getSuffix())) {
				entity.setSuffix(columnPO.getSuffix());
			}
			//importField
			if(!isEmpty(columnPO.getImportfield())) {
				if(columnPO.getImportfield().equals("1")) {
					entity.setImportField(true);
				}
				else if(columnPO.getImportfield().equals("0")) {
					entity.setImportField(false);
				}
			}
			//enumImportMethod
			if(!isEmpty(columnPO.getEnumimportmethod())) {
				entity.setEnumImportMethod(columnPO.getEnumimportmethod());
			}
			//column name
			if(!isEmpty(columnPO.getGroup_name()) && 
					!isEmpty(columnPO.getCol_name())) {
				entity.setName(columnPO.getGroup_name() + "_" + columnPO.getCol_name());
			}
			//column name
			else if( !isEmpty(columnPO.getCol_name())) {
				entity.setName(columnPO.getCol_name());
			}
			//column type
			if(!isEmpty(columnPO.getType())) {
				entity.setType(Integer.parseInt(columnPO.getType()));
			}
			//database date format
			if(!isEmpty(columnPO.getDatabase_date_format())) {
				entity.setDatabaseFormat(columnPO.getDatabase_date_format());
			}
			//date format
			if(!isEmpty(columnPO.getDate_format())) {
				entity.setFormat(columnPO.getDate_format());
			}
			//replace
			if(!isEmpty(columnPO.getExport_replace())) {
				entity.setReplace(columnPO.getExport_replace().split(","));
			}
			//dic
			if(!isEmpty(columnPO.getDict())) {
				entity.setDict(columnPO.getDict());
			}
			//hyper link
			if(!isEmpty(columnPO.getHyper_link())) {
				if(columnPO.getHyper_link().equals("1")) {
					entity.setHyperlink(true);
				}
				else if(columnPO.getHyper_link().equals("0")) {
					entity.setHyperlink(false);
				}
			}
			//fixedIndex
			if(!isEmpty(columnPO.getFixed_index())) {
				entity.setFixedIndex(columnPO.getFixed_index());
			}
	        if(!isEmpty(columnPO.getFixed_index()) && entity.getFixedIndex() != -1){
	        	listEntity.put("FIXED_"+entity.getFixedIndex() , entity);
	        } else {
	        	listEntity.put(entity.getName(), entity);
	        }
		}
		return listEntity;
	}
	
public static boolean isEmpty(Object pObj) {
	if (pObj == null)
		return true;
	if (pObj == "")
		return true;
	if (pObj instanceof String) {
		if (((String) pObj).length() == 0) {
			return true;
		}
	} else if (pObj instanceof Collection) {
		if (((Collection) pObj).size() == 0) {
			return true;
		}
	} else if (pObj instanceof Map) {
		if (((Map) pObj).size() == 0) {
			return true;
		}
	}
	return false;
}
}
