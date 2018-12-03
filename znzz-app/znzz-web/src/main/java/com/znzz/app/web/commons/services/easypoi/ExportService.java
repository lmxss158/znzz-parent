package com.znzz.app.web.commons.services.easypoi;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.ioc.Ioc;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;

import com.beust.jcommander.internal.Lists;
import com.itextpdf.text.Document;
import com.znzz.app.sys.modules.models.Sys_export_column;
import com.znzz.app.sys.modules.models.Sys_export_table;
import com.znzz.app.web.commons.util.Configer;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.TemplateExportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.afterturn.easypoi.excel.entity.params.ExcelExportEntity;
import cn.afterturn.easypoi.handler.inter.IExcelDataHandler;
import cn.afterturn.easypoi.handler.inter.IExcelDictHandler;
import cn.afterturn.easypoi.pdf.PdfExportUtil;
import cn.afterturn.easypoi.pdf.entity.PdfExportParams;
import cn.afterturn.easypoi.pdf.styler.IPdfExportStyler;
import cn.afterturn.easypoi.util.PoiMergeCellUtil;
import cn.afterturn.easypoi.util.PoiReflectorUtil;
import cn.afterturn.easypoi.word.WordExportUtil;

@IocBean
public class ExportService {
	@Inject("refer:$ioc")
	protected Ioc ioc;
	@Inject
	private PdfExportStylerImpl pdfExportStylerImpl  ;
	@Inject
    private ExcelDictHandlerImpl excelDicHandlerImpl ;

	public String exportFile(HttpServletRequest request, HttpServletResponse response, String reportCode,
			List<?>... reportData) {
		return exportFile(request, response, null, reportCode, reportData);
	}

	public String exportFile(HttpServletRequest request, HttpServletResponse response, List<Class<?>> entityClass,
			 String reportCode, List<?>... reportData) {
		return exportFile(request, response, reportCode, entityClass, null, reportData);
	}

	public String exportFile(HttpServletRequest request, HttpServletResponse response, String reportCode,
			List<Class<?>> entityClass, Map<String, String> exclusions, List<?>... reportData) {
		String url = "";
		Dao dao = ioc.get(Dao.class);
		Cnd cnd = Cnd.NEW();
		cnd.where().and("code", "=", reportCode);
		// 获取报表配置参数
		List<Sys_export_table> listTablePO = dao.query(Sys_export_table.class, cnd);
		if (isEmpty(listTablePO)) {
			return "error";
		}
		// 设置排除列
		if (!isEmpty(exclusions)) {
			for (Sys_export_table tablePO : listTablePO) {
				String exCol = exclusions.get(tablePO.getSheet_name());
				if (!isEmpty(exCol)) {
					tablePO.setExclusions(exCol);
				}
			}
		}
		
		if((reportData != null) && (reportData.length == 1) && (reportData[0].size() > 3000)){
			url = exportBigFile(listTablePO,  entityClass, reportData);
		}else{
			url = exportFile(listTablePO,  entityClass, reportData);
		}	
		

		return url;
	}

	public String exportFileTemplate(HttpServletRequest request, HttpServletResponse response, String reportCode,
			Map<String, Object>... reportData) {
		return exportFileTemplate(request, response, reportCode, null, reportData);
	}

	public String exportFileTemplate(HttpServletRequest request, HttpServletResponse response, String reportCode,
			List<MergeExcelEntity> mergeList, Map<String, Object>... reportData) {
		String ret = "";
		Dao dao = ioc.get(Dao.class);
		Cnd cnd = Cnd.NEW();
		cnd.where().and("code", "=", reportCode);
		// 获取报表配置参数
		List<Sys_export_table> listTablePO = dao.query(Sys_export_table.class, cnd);
		if (isEmpty(listTablePO)) {
			return "error";
		}

		ret = exportFile(listTablePO,  mergeList, reportData);

		return ret;
	}

	private String exportFile(List<Sys_export_table> listTablePO,  List<MergeExcelEntity> mergeList,
			Map<String, Object>... reportData) {
		String exportPath = Configer.getInstance().getProperty("assetExportPath");
		String url = exportPath + "/" + listTablePO.get(0).getFile_name();
		File file =new File(exportPath);
		if(!file.exists()  && !file.isDirectory()){
			file.mkdir();
		}
		// excel导出
		if (listTablePO.get(0).getType().equals("1") || listTablePO.get(0).getType().equals("2")) {
			Workbook workbook = null;
			try {
				// EXCEL模板导出
				if (listTablePO.get(0).getIs_template().equals("1")) {
					TemplateExportParams params = new TemplateExportParams(listTablePO.get(0).getTemplate_url());
				
					params.setStyle(ExcelExportStylerColorBorderImpl.class);
					if (!isEmpty(listTablePO.get(0).getIs_column_foreach() )) {
						if (listTablePO.get(0).getIs_column_foreach().equals("1")) {
							params.setColForEach(true);
						} else if (listTablePO.get(0).getIs_column_foreach().equals("0")) {
							params.setColForEach(false);
						}
					}
					Map<Integer, Map<String, Object>> sheetMap = new HashMap<>();
					String[] sheetName = new String[listTablePO.size()];
					Integer[] sheetNum = new Integer[listTablePO.size()];
					int i = 0;
					for (Sys_export_table tablePO : listTablePO) {
						sheetName[i] = tablePO.getSheet_name();
						sheetNum[i] = tablePO.getSheet_num();
						sheetMap.put(i, reportData[i]);
						i++;
					}
					params.setSheetName(sheetName);
					params.setSheetNum(sheetNum);
					// params.setScanAllsheet(true);

					workbook = ExcelExportUtil.exportExcel(sheetMap, params);
					mergeCell(mergeList, workbook);
					workbook.write(new FileOutputStream(file));
					
					return url;
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if (workbook != null) {
						workbook.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		// WORD模板导出
		else if (listTablePO.get(0).getType().equals("4")) {
			XWPFDocument doc = null;
			try {
				int i = 0;
				for (Sys_export_table tablePO : listTablePO) {
					if (i < reportData.length) {
						doc = WordExportUtil.exportWord07(tablePO.getTemplate_url(), reportData[i]);
						doc.write(new FileOutputStream(file));
					}
					i++;
					// WORD导出只有一个SHEET
					break;
				}
				return url;

			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if (doc != null) {
						doc.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return "error";
	}

	private String exportFile(List<Sys_export_table> listTablePO,  List<Class<?>> entityClass,
			List<?>... reportData) {
		
		String exportPath = Configer.getInstance().getProperty("assetExportPath");
		//String filename = Configer.getInstance().getProperty("export_asset_name");
		String url = exportPath + "/" + listTablePO.get(0).getFile_name();
		File file =new File(exportPath);
		if(!file.exists()  && !file.isDirectory()){
			file.mkdir();
		}
		// excel导出
		if (listTablePO.get(0).getType().equals("1") || listTablePO.get(0).getType().equals("2")) {
			Workbook workbook = null;
			try {
				// excel非模板导出
				if (!"1".equals(listTablePO.get(0).getIs_template())) {
					List<Map<String, Object>> list = new ArrayList<>();
					int i = 0;
					for (Sys_export_table tablePO : listTablePO) {
						Dao dao = ioc.get(Dao.class);
						Cnd cnd = Cnd.NEW();
						cnd.where().and("table_id", "=", tablePO.getId());
						List<Sys_export_column> listColumnPO = dao.query(Sys_export_column.class, cnd);
						if (isEmpty(listColumnPO)) {
							return "error";
						}
						//排除列
						List<Sys_export_column> removeCol = Lists.newArrayList();
						if(!isEmpty(tablePO.getExclusions())){
							String[] exclusion = tablePO.getExclusions().split(",");
							for (String ex : exclusion) {
								for(Sys_export_column col : listColumnPO){
									if(ex.equalsIgnoreCase(col.getMap_key())){
										removeCol.add(col);
									}
								}
							}
						}
						listColumnPO.removeAll(removeCol);
						
						
						Map<String, Object> exportMap = new HashMap<>();
						exportMap.put("title", getExcelExportParams(tablePO));
						if (entityClass != null && i < entityClass.size()) {
							exportMap.put("entity", getExcelExportEntityList(listColumnPO, entityClass.get(i)));
						} else {
							exportMap.put("entity", getExcelExportEntityList(listColumnPO, null));
						}
						if (i < reportData.length) {
							exportMap.put("data", reportData[i]);
						}
						list.add(exportMap);
						i++;
					}
					workbook = ExcelExportUtil.exportExcelMap(list,
							listTablePO.get(0).getType().equals("1") ? ExcelType.XSSF
									: ExcelType.HSSF);
					
					workbook.write(new FileOutputStream(new File(url)));
					
					return url;
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if (workbook != null) {
						workbook.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		// pdf导出
		else if (listTablePO.get(0).getType().equals("3")) {
			Document workbook = null;
			try {
				int i = 0;
				for (Sys_export_table tablePO : listTablePO) {
					Dao dao = ioc.get(Dao.class);
					Cnd cnd = Cnd.NEW();
					cnd.where().and("table_id", "=", tablePO.getId());
					List<Sys_export_column> listColumnPO = dao.query(Sys_export_column.class, cnd);
					if (isEmpty(listColumnPO)) {
						return "error";
					}
					List<ExcelExportEntity> entityList = null;
					if (entityClass != null && i < entityClass.size()) {
						entityList = getExcelExportEntityList(listColumnPO, entityClass.get(i));
					} else {
						entityList = getExcelExportEntityList(listColumnPO, null);
					}
					if (i < reportData.length) {
						PdfExportUtil.exportPdf(getPDFExportParams(tablePO), entityList, reportData[i], new FileOutputStream(new File(url)));
					}
					i++;
					// 目前PDF导出只支持一个表格
					break;
				}
				//stream.flush();
				return url;

			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (workbook != null) {
					workbook.close();
				}
			}
		}

		return "error";
	}
	
	private String exportBigFile(List<Sys_export_table> listTablePO,  List<Class<?>> entityClass,
			List<?>... reportData) {
		Workbook workbook = null;
		List<Map<String, Object>> list = new ArrayList<>();
		
		Sys_export_table tablePO = listTablePO.get(0);
		Dao dao = ioc.get(Dao.class);
		Cnd cnd = Cnd.NEW();
		cnd.where().and("table_id", "=", tablePO.getId());
		List<Sys_export_column> listColumnPO = dao.query(Sys_export_column.class, cnd);
		if (isEmpty(listColumnPO)) {
			return "error";
		}
		//排除列
		List<Sys_export_column> removeCol = Lists.newArrayList();
		if(!isEmpty(tablePO.getExclusions())){
			String[] exclusion = tablePO.getExclusions().split(",");
			for (String ex : exclusion) {
				for(Sys_export_column col : listColumnPO){
					if(ex.equalsIgnoreCase(col.getMap_key())){
						removeCol.add(col);
					}
				}
			}
		}
		listColumnPO.removeAll(removeCol);
		
		List<ExcelExportEntity> excelParams = getExcelExportEntityList(listColumnPO, entityClass.get(0));
		
		ExportParams params = new ExportParams();
		params.setTitle(tablePO.getTitle());
		params.setSheetName(tablePO.getSheet_name());
		
		workbook = ExcelExportUtil.exportBigExcel(params, excelParams, reportData[0]);
		reportData[0].clear();
		
		ExcelExportUtil.closeExportBigExcel();
		String exportPath = Configer.getInstance().getProperty("assetExportPath");
		String url = exportPath + "/" + listTablePO.get(0).getFile_name();
        try{
			File file =new File(exportPath);
			if(!file.exists()  && !file.isDirectory()){
				file.mkdir();
			}
	        String fielPathUrl = exportPath + "/" + listTablePO.get(0).getFile_name();
	        FileOutputStream fos = new FileOutputStream(fielPathUrl);
	
	        workbook.write(fos);
	        fos.close();
	        return url;
        }catch(Exception e){
        	e.printStackTrace();
        }
        
        return "error";
		
	}

	private List<ExcelExportEntity> getExcelExportEntityList(List<Sys_export_column> listColumnPO,
			Class<?> entityClass) {

		List<ExcelExportEntity> listEntity = new ArrayList<>();
		// 生成列配置信息
		for (Sys_export_column columnPO : listColumnPO) {
			ExcelExportEntity entity = new ExcelExportEntity();
			// key
			if (!isEmpty(columnPO.getMap_key())) {
				entity.setKey(columnPO.getMap_key());
				if (entityClass != null) {
					entity.setMethod(PoiReflectorUtil.fromCache(entityClass).getGetMethod(columnPO.getMap_key()));
				}
			}
			// width
			if (!isEmpty(columnPO.getWidth())) {
				entity.setWidth(columnPO.getWidth().doubleValue());
			}
			// image type
			if (!isEmpty(columnPO.getImage_type())) {
				entity.setExportImageType(Integer.parseInt(columnPO.getImage_type()));
			}
			// order num
			if (!isEmpty(columnPO.getOrder_num())) {
				entity.setOrderNum(columnPO.getOrder_num().intValue());
			}
			// wrap
			if (!isEmpty(columnPO.getIs_wrap())) {
				if (columnPO.getIs_wrap().equals("1")) {
					entity.setWrap(true);
				} else if (columnPO.getIs_wrap().equals("0")) {
					entity.setWrap(false);
				}
			}
			// need merge
			if ( !isEmpty(columnPO.getNeed_merge()) ) {
				if (columnPO.getNeed_merge().equals("1")) {
					entity.setNeedMerge(true);
				} else if (columnPO.getNeed_merge().equals("0")) {
					entity.setNeedMerge(false);
				}
			}
			// merge vertical
			if ( !isEmpty(columnPO.getMerge_vertical())) {
				if (columnPO.getMerge_vertical().equals("1")) {
					entity.setMergeVertical(true);
				} else if (columnPO.getMerge_vertical().equals("0")) {
					entity.setMergeVertical(false);
				}
			}
			// merge rely
			if ( !isEmpty(columnPO.getMerge_rely())) {
				String[] rely = columnPO.getMerge_rely().split(",");
				int[] intRely = new int[rely.length];
				int i = 0;
				for (String rl : rely) {
					intRely[i] = Integer.parseInt(rl);
					i++;
				}
				entity.setMergeRely(intRely);
			}
			// suffix
			if (!isEmpty(columnPO.getSuffix())) {
				entity.setSuffix(columnPO.getSuffix());
			}
			// statistics
			if (!isEmpty(columnPO.getIs_statistics() )) {
				if (columnPO.getIs_statistics().equals("1")) {
					entity.setStatistics(true);
				} else if (columnPO.getIs_statistics().equals("0")) {
					entity.setStatistics(false);
				}
			}
			// num format
			if ( !isEmpty(columnPO.getNum_format())) {
				entity.setNumFormat(columnPO.getNum_format());
			}
			// column hide
			if ( !isEmpty(columnPO.getIs_column_hidden())) {
				if (columnPO.getIs_column_hidden().equals("1")) {
					entity.setColumnHidden(true);
				} else if (columnPO.getIs_column_hidden().equals("0")) {
					entity.setColumnHidden(false);
				}
			}
			// enum field
			if ( !isEmpty(columnPO.getEnum_field())) {
				entity.setEnumExportField(columnPO.getEnum_field());
			}
			// column name
			if (!isEmpty(columnPO.getCol_name())) {
				entity.setName(columnPO.getCol_name());
			}
			// group name
			if (!isEmpty(columnPO.getGroup_name())) {
				entity.setGroupName(columnPO.getGroup_name());
			}
			// column type
			if (!isEmpty(columnPO.getType())) {
				entity.setType(Integer.parseInt(columnPO.getType()));
			}
			// database date format
			if (!isEmpty(columnPO.getDatabase_date_format())) {
				entity.setDatabaseFormat(columnPO.getDatabase_date_format());
			}
			// date format
			if (!isEmpty(columnPO.getDate_format())) {
				entity.setFormat(columnPO.getDate_format());
			}
			// replace
			if (!isEmpty(columnPO.getExport_replace())) {
				entity.setReplace(columnPO.getExport_replace().split(","));
			}
			// dic
			if (!isEmpty(columnPO.getDict())) {
				entity.setDict(columnPO.getDict());
			}
			// hyper link
			if (!isEmpty(columnPO.getHyper_link())) {
				if (columnPO.getHyper_link().equals("1")) {
					entity.setHyperlink(true);
				} else if (columnPO.getHyper_link().equals("0")) {
					entity.setHyperlink(false);
				}
			}
			listEntity.add(entity);
		}
		return listEntity;
	}

	private PdfExportParams getPDFExportParams(Sys_export_table exportPO) {
		PdfExportParams ep = new PdfExportParams();

		// title名
		if ( !isEmpty(exportPO.getTitle())) {
			ep.setTitle(exportPO.getTitle());
		}
		if ( !isEmpty(exportPO.getTitle_height())) {
			ep.setTitleHeight(exportPO.getTitle_height().shortValue());
		}
		// 第二title名
		if ( !isEmpty(exportPO.getSecond_title())) {
			ep.setSecondTitle(exportPO.getSecond_title());
		}
		if ( !isEmpty(exportPO.getSecond_title_height())) {
			ep.setSecondTitleHeight(exportPO.getSecond_title_height().shortValue());
		}
		// exclusions
		if ( !isEmpty(exportPO.getExclusions())) {
			ep.setExclusions(exportPO.getExclusions().split(","));
		}
		// index
		if ( !isEmpty(exportPO.getAddindex())) {
			if (exportPO.getAddindex().equals("1")) {
				ep.setAddIndex(true);
			} else if (exportPO.getAddindex().equals("0")) {
				ep.setAddIndex(false);
			}
		}
		// index name
		if ( !isEmpty(exportPO.getIndexname())) {
			ep.setIndexName(exportPO.getIndexname());
		}

		// height
		if ( !isEmpty(exportPO.getHeight())) {
			ep.setHeight(exportPO.getHeight().shortValue());
		}
		// header height
		if ( !isEmpty(exportPO.getHeader_height())) {
			ep.setHeaderHeight(exportPO.getHeader_height().doubleValue());
		}

		// style
		if ( !isEmpty(exportPO.getStyle() )) {
			try {
				Object object = null;
				String[] str = exportPO.getStyle().split("\\.");
				if (str.length > 0) {
					String bean = str[str.length - 1];
					object = ioc.get(Class.forName(bean));
					if (object != null) {
						ep.setStyler((IPdfExportStyler) object);
					} else {
						Class<?> cl = Class.forName(exportPO.getStyle());
						object = cl.newInstance();
						if (object instanceof IPdfExportStyler) {
							ep.setStyler((IPdfExportStyler) object);
						}
					}
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		} else {
			ep.setStyler(pdfExportStylerImpl);
		}
		return ep;
	}

private ExportParams getExcelExportParams(Sys_export_table exportPO) {
	ExportParams ep = new ExportParams();
	if(exportPO.getType().equals("1")) {
		ep.setType(ExcelType.XSSF);
	}
	//sheet名
	if(!isEmpty(exportPO.getSheet_name())) {
		ep.setSheetName(exportPO.getSheet_name());
	}
	//title名
	if(!isEmpty(exportPO.getTitle())) {
		ep.setTitle(exportPO.getTitle());
	}
	if(!isEmpty(exportPO.getTitle_height())) {
		ep.setTitleHeight(exportPO.getTitle_height().shortValue());
	}
	//第二title名
	if(!isEmpty(exportPO.getSecond_title())) {
		ep.setSecondTitle(exportPO.getSecond_title());
	}
	if(!isEmpty(exportPO.getSecond_title_height())) {
		ep.setSecondTitleHeight(exportPO.getSecond_title_height().shortValue());
	}
	//exclusions
	if(!isEmpty(exportPO.getExclusions())) {
		ep.setExclusions(exportPO.getExclusions().split(","));
	}
	//index
	if(!isEmpty(exportPO.getAddindex())) {
		if(exportPO.getAddindex().equals("1")) {
			ep.setAddIndex(true);
		}
		else if(exportPO.getAddindex().equals("0")) {
			ep.setAddIndex(false);
		}					
	}
	//index name
	if(!isEmpty(exportPO.getIndexname())) {
		ep.setIndexName(exportPO.getIndexname());
	}
	//freeze col
	if(!isEmpty(exportPO.getFreezecol())) {
		ep.setFreezeCol(exportPO.getFreezecol().intValue());
	}
	//color
	if(!isEmpty(exportPO.getColor())) {
		ep.setColor(exportPO.getColor().shortValue());
	}
	//second color
	if(!isEmpty(exportPO.getSecond_color())) {
		ep.setSecondColor(exportPO.getSecond_color().shortValue());
	}
	//header color
	if(!isEmpty(exportPO.getHeader_color())) {
		ep.setHeaderColor(exportPO.getHeader_color().shortValue());
	}
	if(!isEmpty(exportPO.getHeader_height())) {
		ep.setHeaderHeight(exportPO.getHeader_height().doubleValue());
	}
	//create header
	if(!isEmpty(exportPO.getIs_create_header())) {
		if(exportPO.getIs_create_header().equals("1")) {
			ep.setCreateHeadRows(true);
		}
		else if(exportPO.getIs_create_header().equals("0")) {
			ep.setCreateHeadRows(false);
		}					
	}
	//height
	if(!isEmpty(exportPO.getHeight())) {
		ep.setHeight(exportPO.getHeight().shortValue());
	}
	//style
	if(!isEmpty(exportPO.getStyle())) {
		try {
			ep.setStyle(Class.forName(exportPO.getStyle()));
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	else {
		ep.setStyle(ExcelExportStylerColorBorderImpl.class);
	}
	//data handle
	if(!isEmpty(exportPO.getData_handler())) {
		try {
			Object object = null;
			String[] str = exportPO.getData_handler().split("\\.");
			if(str.length > 0) {
				String bean = str[str.length - 1];
				object = ioc.get(Class.forName(bean));	
				if (object != null) {
					ep.setDataHandler((IExcelDataHandler<?>)object);
				}
				else{
					Class<?> cl = Class.forName(exportPO.getData_handler());
					object = cl.newInstance();
					if(object instanceof IExcelDataHandler) {
						ep.setDataHandler((IExcelDataHandler<?>)object);
					}
				}
			}
			//String[] dataHandler = exportPO.getData_handler().split(",");
			//ep.setDataHandler(getDataHandler(Class.forName(dataHandler[0]), Class.forName(dataHandler[1])));
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}
	//dic
	if(!isEmpty(exportPO.getDic_handler())) {
		try {
			Object object = null;
			String[] str = exportPO.getDic_handler().split("\\.");
			if(str.length > 0) {
				String bean = str[str.length - 1];
				object = ioc.get(Class.forName(bean));
				if (object != null) {
					ep.setDictHandler((IExcelDictHandler)object);
				}else{
					Class<?> cl = Class.forName(exportPO.getDic_handler());
					object = cl.newInstance();
					if(object instanceof IExcelDictHandler) {
						ep.setDictHandler((IExcelDictHandler)object);
					}
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

	private void mergeCell(List<MergeExcelEntity> mergeList, Workbook workbook) {
		if (mergeList==null || mergeList.size()==0) {
			return;
		}
		for (MergeExcelEntity merge : mergeList) {
			Sheet sheet = workbook.getSheetAt(merge.getSheetNo());
			Integer startRow = merge.getStartRow();
			Integer endRow = merge.getEndRow();

			if (endRow !=null) {
				endRow = sheet.getLastRowNum();
			}
			if (merge.getMergeType().equals("colMerge")) {
				// 纵向合并单元格
				int[] cols = merge.getCols();
				Map<Integer, int[]> mergeMap = new HashMap<Integer, int[]>();
				for (int i : cols) {
					mergeMap.put(i, null);
				}
				PoiMergeCellUtil.mergeCells(sheet, mergeMap, startRow, endRow);
			} else {
				// 横向合并rowMerge
				Row row;
				Integer startCol = merge.getStartCol(); // 起始列
				Integer endCol = merge.getEndCol(); // 结束列
				for (int i = startRow; i < endRow; i++) {
					row = sheet.getRow(i);
					if (null != row && row.getCell(startCol) != null && row.getCell(endCol) != null) {
						String startValue = row.getCell(startCol).getStringCellValue();
						String endValue = row.getCell(endCol).getStringCellValue();
						if (endValue==null || endValue=="" || endValue.equals(startValue)) {
							sheet.addMergedRegion(new CellRangeAddress(i, i, startCol, endCol));
						}

					}

				}

			}

		}

	}

	/**
	 * 对文件流输出下载的中文文件名进行编码 屏蔽各种浏览器版本的差异性<br>
	 * 
	 * @param agent
	 *            request.getHeader("USER-AGENT");
	 */
/*	public static String encodeChineseDownloadFileName(String agent, String pFileName) {
		try {
			if (null != agent && -1 != agent.indexOf("MSIE")) {
				pFileName = URLEncoder.encode(pFileName, "utf-8");
			} else {
				pFileName = new String(pFileName.getBytes("utf-8"), "iso8859-1");
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return pFileName;
	}*/
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
