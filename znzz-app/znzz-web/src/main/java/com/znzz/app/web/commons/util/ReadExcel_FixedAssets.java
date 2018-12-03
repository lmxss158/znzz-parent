package com.znzz.app.web.commons.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.ObjectUtils.Null;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.shiro.SecurityUtils;
import org.beetl.ext.fn.GetValueFunction;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.Sqls;
import org.nutz.dao.entity.Entity;
import org.nutz.dao.sql.Sql;
import org.nutz.ioc.Ioc;
import org.nutz.lang.Strings;
import org.nutz.mvc.Mvcs;

import com.znzz.app.ledger.modules.models.InsFixedAssetsLedger;

public class ReadExcel_FixedAssets {

	private static Ioc ioc_my = Mvcs.ctx().getDefaultIoc();
	Dao my_dao = ioc_my.get(Dao.class);

	/**
	 * list ExcelToList(将excel表格中的数据转换成List<T>)
	 * 
	 * @throws Exception
	 */
	public Object readExcel(String path) throws Exception {
		if (path == null || "".equals(path)) {
			return null;
		} else {
			String postfix = CommonExcelUtil.getPostfix(path);
			if (!CommonExcelUtil.EMPTY.equals(postfix)) {
				// xls格式的
				if (CommonExcelUtil.OFFICE_EXCEL_2003_POSTFIX.equals(postfix)) {
					return readXls(path);
				} else if (CommonExcelUtil.OFFICE_EXCEL_2010_POSTFIX.equals(postfix)) {
					// xlsx格式的
					return readXlsx(path);
				}
			} else {
				// 前端已经作了校验
				System.out.println(path + CommonExcelUtil.NOT_EXCEL_FILE);
			}
		}
		return null;
	}

	// 2003-2007版本
	public Object readXls(String path) throws Exception {
		// 封装结果集
				Map<String, Object> resultMap = new HashMap<>();
				List<InsFixedAssetsLedger> fixedAssetsList = new ArrayList<>();
				// 封装错误信息
				StringBuilder sb = new StringBuilder();
				InputStream is = null;
				try {
					// 将文件放入流中
					is = new FileInputStream(path);
					// 获取excel-workbook
					HSSFWorkbook hssfWorkbook = new HSSFWorkbook(is);
					// 创建要封装的对象
					InsFixedAssetsLedger fixedAssets = null;
					// 读取单元簿Sheet
					HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(0);
					// (遍历每一行)
					for (int rowNum = 2; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
						HSSFRow hssfRow = hssfSheet.getRow(rowNum);
						
						// 此行不等于空
						if (hssfRow != null && hssfRow.getCell(0) != null && !"".equals(CommonExcelUtil.getValue(hssfRow.getCell(0)))) {
							// 实例化对象
							fixedAssets = new InsFixedAssetsLedger();
							// 获取单行excel数据{ }
							String sequence = CommonExcelUtil.getValue(hssfRow.getCell(0)).trim(); // 序号
							/*if(Strings.isBlank(sequence)){
								resultMap.put("error", sb.append("数据校验出错：第"+(rowNum + 1)+"行序号不能为空。。。"));
							}*/
							fixedAssets.setSequence(sequence);
							// 遍历行的每一个单元格
							for (Cell c : hssfRow) {
								int index = c.getColumnIndex();
								//判断是否是合并单元格
								 boolean isMerge = CommonExcelUtil.isMergedRegion(hssfSheet, rowNum, index); 
								 if (isMerge) {	//如果是合并单元格
									 //获取合并单元格里的值
									 Date time = null;
									 Double num = null;
									 String value = CommonExcelUtil.getMergedRegionValue(hssfSheet, hssfRow.getRowNum(), index).trim();
									 
									 if (19 == index || 20 == index) {
										 time = CommonExcelUtil.getDateWithYearMonth(value);
									 }else if (25 == index || 26 == index || 27 == index){ //double类型
										 String money = CommonExcelUtil.getValue(c);
										 num = Double.parseDouble(money);
									 }
									 caseSituation(fixedAssets, index, time, num, value);
								}else {	//不是合并单元格
									Date time = null;
									Double num = null;
									String value = CommonExcelUtil.getValue(c);
									if (19 == index || 20 == index) {
										 time = CommonExcelUtil.getDateWithYearMonth(value);
									 }else if (25 == index || 26 == index || 27 == index){ //double类型
										 if(Strings.isNotBlank(value)){
											 num = Double.parseDouble(value);
										 }
									 }
									caseSituation(fixedAssets, index, time, num, value);
								}
							}
							//校验Excel表格的序号是否重复
							if(existSequence(fixedAssetsList,sequence)){
								resultMap.put("error", sb.append("数据校验出错：Excel表格中第"+(rowNum + 1)+"行有重复的序号，重复的序号为："+sequence + "\n"));
							}
							//校验数据库当中是否有存在的序号
							if (duplicateCheckSequence(sequence)) {
								resultMap.put("error", sb.append("数据校验出错：第"+(rowNum + 1)+"行在数据库当中已经存在相同序号，重复的序号为："+sequence + "\n"));
							}
							
							if(fixedAssets != null && Strings.isNotBlank(fixedAssets.getSequence())){
								fixedAssetsList.add(fixedAssets);
							}
						}
						
					}
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				//关流
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				//放到resultMap当中
				resultMap.put("fixedAssetsList", fixedAssetsList);
				
				return resultMap;
	}

	// 2007版本
	@SuppressWarnings("null")
	public Object readXlsx(String path) {
		// 封装结果集
		Map<String, Object> resultMap = new HashMap<>();
		List<InsFixedAssetsLedger> fixedAssetsList = new ArrayList<>();
		// 封装错误信息
		StringBuilder sb = new StringBuilder();
		InputStream is = null;
		try {
			// 将文件放入流中
			is = new FileInputStream(path);
			// 获取excel-workbook
			XSSFWorkbook xssfWorkbook = new XSSFWorkbook(is);
			// 创建要封装的对象
			InsFixedAssetsLedger fixedAssets = null;
			// 读取单元簿Sheet
			XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(0);
			// (遍历每一行)
			for (int rowNum = 2; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
				XSSFRow xssfRow = xssfSheet.getRow(rowNum);
				
				// 此行不等于空
				if (xssfRow != null && xssfRow.getCell(0) != null && !"".equals(CommonExcelUtil.getValue(xssfRow.getCell(0)))) {
					// 实例化对象
					fixedAssets = new InsFixedAssetsLedger();
					// 获取单行excel数据{ }
					String sequence = CommonExcelUtil.getValue(xssfRow.getCell(0)).trim(); // 序号
					/*if(Strings.isBlank(sequence)){
						resultMap.put("error", sb.append("数据校验出错：第"+(rowNum + 1)+"行序号不能为空。。。"));
					}*/
					fixedAssets.setSequence(sequence);
					// 遍历行的每一个单元格
					for (Cell c : xssfRow) {
						int index = c.getColumnIndex();
						//判断是否是合并单元格
						 boolean isMerge = CommonExcelUtil.isMergedRegion(xssfSheet, rowNum, index); 
						 if (isMerge) {	//如果是合并单元格
							 //获取合并单元格里的值
							 Date time = null;
							 Double num = null;
							 String value = CommonExcelUtil.getMergedRegionValue(xssfSheet, xssfRow.getRowNum(), index).trim();
							 
							 if (19 == index || 20 == index) {
								 time = CommonExcelUtil.getDateWithYearMonth(value);
							 }else if (25 == index || 26 == index || 27 == index){ //double类型
								 if(Strings.isNotBlank(value)){
									 num = Double.parseDouble(value);
								 }
							 }
							 caseSituation(fixedAssets, index, time, num, value);
						}else {	//不是合并单元格
							Date time = null;
							Double num = null;
							String value = CommonExcelUtil.getValue(c);
							if (19 == index || 20 == index) {
								 time = CommonExcelUtil.getDateWithYearMonth(value);
							 }else if (25 == index || 26 == index || 27 == index){ //double类型
								 if(Strings.isNotBlank(value)){
									 num = Double.parseDouble(value);
								 }
							 }
							caseSituation(fixedAssets, index, time, num, value);
						}
					}
/*
					String costSource = CommonExcelUtil.getValue(xssfRow.getCell(9)).trim(); // 费用来源
					String projectName = CommonExcelUtil.getValue(xssfRow.getCell(10)).trim(); // 项目名称
					String insurance_2017 = CommonExcelUtil.getValue(xssfRow.getCell(11)).trim(); // 2017保险
					String testDevice_2017 = CommonExcelUtil.getValue(xssfRow.getCell(12)).trim(); // 2017测调设备
					String fixedAssetsName = CommonExcelUtil.getValue(xssfRow.getCell(13)).trim(); // 固定资产名称

					String ruleVersion = CommonExcelUtil.getValue(xssfRow.getCell(14)).trim(); // 规格型号
					String technicalIndicator = CommonExcelUtil.getValue(xssfRow.getCell(15)).trim(); // 技术指标
					String factory = CommonExcelUtil.getValue(xssfRow.getCell(16)).trim(); // 制造厂
					String factoryNumber = CommonExcelUtil.getValue(xssfRow.getCell(17)).trim(); // 出厂编号
					String salePerson = CommonExcelUtil.getValue(xssfRow.getCell(18)).trim(); // 销售商
					String purchaseDate = CommonExcelUtil.getValue(xssfRow.getCell(19)).trim(); // 购进日期
					String useDate = CommonExcelUtil.getValue(xssfRow.getCell(20)).trim(); // 使用日期
					String purchaseYear = CommonExcelUtil.getValue(xssfRow.getCell(21)).trim(); // 购置年代
					String borrowDepart = CommonExcelUtil.getValue(xssfRow.getCell(22)).trim(); // 使用单位
					String chargePerson = CommonExcelUtil.getValue(xssfRow.getCell(23)).trim(); // 使用人
					String installationSite = CommonExcelUtil.getValue(xssfRow.getCell(24)).trim(); // 安装地点

					String buyOriginal = CommonExcelUtil.getValue(xssfRow.getCell(24)).trim(); // 购入原值
					String increasePrice = CommonExcelUtil.getValue(xssfRow.getCell(25)).trim(); // 增值
					String accountOriginal = CommonExcelUtil.getValue(xssfRow.getCell(26)).trim(); // 财务原值

					String remark = CommonExcelUtil.getValue(xssfRow.getCell(27)).trim(); // 备注
					String returnTreasuryOrOthers = CommonExcelUtil.getValue(xssfRow.getCell(28)).trim(); // 退库或其他
					String returnTreasuryForDestory = CommonExcelUtil.getValue(xssfRow.getCell(29)).trim(); // 退库（销毁）
*/
					//校验Excel表格的序号是否重复
					if(existSequence(fixedAssetsList,sequence)){
						resultMap.put("error", sb.append("数据校验出错：Excel表格中第"+(rowNum + 1)+"行有重复的序号，重复的序号为："+sequence + "\n"));
					}
					//校验数据库当中是否有存在的序号
					if (duplicateCheckSequence(sequence)) {
						//resultMap.put("error", sb.append("数据校验出错：第"+(rowNum + 1)+"行在数据库当中已经存在相同序号，重复的序号为："+sequence + "\n"));
						InsFixedAssetsLedger fetchAssets = my_dao.fetch(InsFixedAssetsLedger.class, Cnd.where("sequence", "=", sequence));
						fixedAssets.setId(fetchAssets.getId());
					}
					
					if(fixedAssets != null && Strings.isNotBlank(fixedAssets.getSequence())){
						fixedAssetsList.add(fixedAssets);
					}
				}
				
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		//关流
		try {
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//放到resultMap当中
		resultMap.put("fixedAssetsList", fixedAssetsList);
		
		return resultMap;
	}

	/**
	 * 校验数据库当中是否有存在的序号
	 * @param sequence
	 * @return
	 */
	private boolean duplicateCheckSequence(String sequence) {
		List<InsFixedAssetsLedger> list = my_dao.query(InsFixedAssetsLedger.class, Cnd.where("sequence", "=", sequence));
		if(list != null && list.size() > 0){
			return true;
		}
		return false;
	}

	/**
	 * 校验Excel中是否有重复的序号
	 * @param fixedAssetsList
	 * @param sequence
	 * @return
	 */
	private boolean existSequence(List<InsFixedAssetsLedger> fixedAssetsList, String sequence) {
		if(fixedAssetsList.size() > 0){
			for (InsFixedAssetsLedger fixedAssets : fixedAssetsList) {
				String fseq = fixedAssets.getSequence();
				if(Strings.isNotBlank(fseq)){
					if (fseq.equals(sequence)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * 根据索引进行赋值(封装数据进入对象当中)
	 * @param fixedAssets
	 * @param index
	 * @param time
	 * @param num
	 * @param value
	 */
	private void caseSituation(InsFixedAssetsLedger fixedAssets, int index, Date time, Double num, String value) {
		switch (index) {
			case 1:
				fixedAssets.setAssetCode(value);				// 统一编码
				break;
			case 2:
				fixedAssets.setLargeCategory(value);			// 大类别
				break;	
			case 3:
				fixedAssets.setMediumCategory(value);			// 中类别
				break;
			case 4:
				fixedAssets.setSmallCategory(value);			// 小类别
				break;
			case 5:
				fixedAssets.setCheckDevice(value);				// 检测设备
				break;
			case 6:
				fixedAssets.setMilitarySpecial(value);			// 军工专用
				break;
			case 7:
				fixedAssets.setNoHandOver(value); 				// 未移交
				break;
			case 8:
				fixedAssets.setHandleUnit(value);				// 办理单位
				break;
			case 9:
				fixedAssets.setCostSource(value);				// 费用来源
				break;
			case 10:
				fixedAssets.setProjectName(value);
				break;
			case 11:
				fixedAssets.setInsurance_2017(value);
				break;
			case 12:
				fixedAssets.setTestDevice_2017(value);
				break;
			case 13:
				fixedAssets.setFixedAssetsName(value);
				break;
			case 14:
				fixedAssets.setRuleVersion(value);
				break;
			case 15:
				fixedAssets.setTechnicalIndicator(value);
				break;
			case 16:
				fixedAssets.setFactory(value);
				break;
			case 17:
				fixedAssets.setFactoryNumber(value);
				break;
			case 18:
				fixedAssets.setSalePerson(value);
				break;
			case 19:
				fixedAssets.setPurchaseDate(time);
				break;
			case 20:
				fixedAssets.setUseDate(time);
				break;
			case 21:
				fixedAssets.setPurchaseYear(value);
				break;
			case 22:
				fixedAssets.setBorrowDepart(value);
				break;
			case 23:
				fixedAssets.setChargePerson(value);
				break;
			case 24:
				fixedAssets.setInstallationSite(value);
				break;
			case 25:
				fixedAssets.setBuyOriginal(num);
				break;
			case 26:
				fixedAssets.setIncreasePrice(num);
				break;
			case 27:
				fixedAssets.setAccountOriginal(num);
				break;
			case 28:
				fixedAssets.setRemark(value);
				break;
			case 29:
				fixedAssets.setReturnTreasuryOrOthers(value);
				break;
			case 30:
				fixedAssets.setReturnTreasuryForDestory(value);
				break;
			default:
				break;
			}
	}
	

}
