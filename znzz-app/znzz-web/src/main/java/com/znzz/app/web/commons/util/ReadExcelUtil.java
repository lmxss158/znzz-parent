package com.znzz.app.web.commons.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.apache.commons.lang3.StringUtils;
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
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.Sqls;
import org.nutz.dao.entity.Entity;
import org.nutz.dao.sql.Sql;
import org.nutz.ioc.Ioc;
import org.nutz.lang.Strings;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.Mvcs;
import com.znzz.app.asset.moudules.models.Ins_Asset_Rule;
import com.znzz.app.asset.moudules.models.Ins_Asset_Unit;
import com.znzz.app.asset.moudules.models.Ins_Asset_lend_record;
import com.znzz.app.asset.moudules.models.Ins_Assets;
import com.znzz.app.asset.moudules.services.InsAssetsService;
import com.znzz.app.asset.moudules.services.impl.InsAssetsServiceImpl;
import com.znzz.app.instrument.modules.models.Ins_Collect;
import com.znzz.app.instrument.modules.models.Ins_DeviceInfo;
import com.znzz.app.instrument.modules.models.Ins_ProjectInfo;
import com.znzz.app.sys.modules.models.Sys_unit;
import com.znzz.app.sys.modules.models.Sys_user;
import com.znzz.app.web.modules.controllers.platform.asset.vo.AssetsForm;
import com.znzz.app.web.modules.controllers.platform.asset.vo.ExcelForm;
import com.znzz.app.web.modules.controllers.platform.instruments.formBean.CollectBindDeviceBean;
import com.znzz.framework.util.DateUtil;

public class ReadExcelUtil {

	private static final Log LOG = Logs.get();

	public static final String OFFICE_EXCEL_2003_POSTFIX = "xls";
	public static final String OFFICE_EXCEL_2010_POSTFIX = "xlsx";

	public static final String EMPTY = "";
	public static final String POINT = ".";
	public static final String NOT_EXCEL_FILE = " : 没有此excel文件!";
	public static final String PROCESSING = "Processing...";

	private static Ioc ioc_my = Mvcs.ctx().getDefaultIoc();
	Dao my_dao = ioc_my.get(Dao.class);
	InsAssetsService assetsService = ioc_my.get(InsAssetsServiceImpl.class);

	/**
	 * 采集器绑定设备的list ExcelToList(将excel表格中的数据转换成List<T>)
	 *
	 * @throws Exception
	 */
	public Object readExcel(String path) throws Exception {
		if (path == null || "".equals(path)) {
			return null;
		} else {
			String postfix = ReadExcelUtil.getPostfix(path);
			if (!ReadExcelUtil.EMPTY.equals(postfix)) {
				// xls格式的
				if (ReadExcelUtil.OFFICE_EXCEL_2003_POSTFIX.equals(postfix)) {
					return readXls(path);
				} else if (ReadExcelUtil.OFFICE_EXCEL_2010_POSTFIX.equals(postfix)) {
					// xlsx格式的
					return readXlsx(path);
				}
			} else {
				// 前端已经作了校验
				System.out.println(path + ReadExcelUtil.NOT_EXCEL_FILE);
			}
		}
		return null;
	}

	// 获取文件后缀
	public static String getPostfix(String path) {
		if (path == null || "".equals(path.trim())) {
			return ReadExcelUtil.EMPTY;
		}
		if (path.contains(ReadExcelUtil.POINT)) {
			return path.substring(path.lastIndexOf(ReadExcelUtil.POINT) + 1, path.length());
		}
		return ReadExcelUtil.EMPTY;
	}

	// 2007以上版本(采集器绑定模块)
	public Object readXlsx(String path) throws Exception {
		System.out.println(ReadExcelUtil.PROCESSING + path);
		List<CollectBindDeviceBean> list = new ArrayList<CollectBindDeviceBean>();
		InputStream is = null;
		// 用来存放错误信息的map
		Map<String, String> map = new HashMap<>();
		try {
			is = new FileInputStream(path);
			XSSFWorkbook xssfWorkbook = new XSSFWorkbook(is);
			CollectBindDeviceBean collect = null;
			// Read the Sheet
			for (int numSheet = 0; numSheet < xssfWorkbook.getNumberOfSheets(); numSheet++) {
				XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(numSheet);
				if (xssfSheet == null) {
					continue;
				}
				// Read the Row
				for (int rowNum = 2; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
					XSSFRow xssfRow = xssfSheet.getRow(rowNum);
					String val = "";

					XSSFCell XcollectCode = xssfRow.getCell(0);
					XSSFCell XdeviceCode = xssfRow.getCell(1);
					XSSFCell createTime = xssfRow.getCell(2);
					if (XcollectCode == null || "".equals(XcollectCode)
							|| XcollectCode.getCellType() == XSSFCell.CELL_TYPE_BLANK) {
						map.put("error", "数据校验出错:第" + (rowNum + 1) + "行,采集器编号不能为空");
						return map;
					}
					if (XdeviceCode == null || "".equals(XdeviceCode)
							|| XdeviceCode.getCellType() == XSSFCell.CELL_TYPE_BLANK) {
						map.put("error", "数据校验出错:第" + (rowNum + 1) + "行,统一编号不能为空");
						return map;
					}
					if (createTime == null || "".equals(createTime)
							|| createTime.getCellType() == XSSFCell.CELL_TYPE_BLANK) {
						map.put("error", "数据校验出错:第" + (rowNum + 1) + "行,时间不能为空");
						return map;
					}

					if (xssfRow != null) {
						collect = new CollectBindDeviceBean();
						/*
						 * student.setNo(getValue(no));
						 * student.setName(getValue(name));
						 * student.setAge(getValue(age));
						 * student.setScore(Float.valueOf(getValue(score)));
						 */

						String collectCode = CommonExcelUtil.getValue(XcollectCode);
						// String collectName = getValue(XcollectName);
						String deviceCode = CommonExcelUtil.getValue(XdeviceCode);
						// String deviceName = getValue(XdeviceName);

						collect.setCollectCode(collectCode.trim());
						// collect.setCollectName(collectName);
						collect.setDeviceCode(deviceCode.trim());
						// collect.setDeviceName(deviceName);
						SimpleDateFormat dff = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						try {
							if (org.apache.poi.ss.usermodel.DateUtil.isCellDateFormatted(createTime)) {
								Date theDate = createTime.getDateCellValue();
								if (theDate != null) {
									val = dff.format(theDate);
								} else {
									val = dff.format(new Date());
								}
							} else {
								DecimalFormat df = new DecimalFormat("0");
								val = df.format(createTime.getNumericCellValue());
							}

							collect.setCreateTime(dff.parse(val));
						} catch (Exception e) {
							map.put("error", "数据校验出错:第" + (rowNum + 1) + "行,时间格式不正确,请按模板输入" + val);
							return map;
						}

						// 对单元格里的数据进行校验
						// 1.Excel表中采集器编号不允许重复
						if (exitsCollectCode(list, collectCode)) {
							is.close();
							// throw new Exception("数据校验出错:第" + (rowNum + 1) +
							// "行,Excel中采集器编号重复，重复的采集器编号是：" + collectCode);
							map.put("error", "数据校验出错:第" + (rowNum + 1) + "行,Excel中采集器编号重复，重复的采集器编号是：" + collectCode);
							return map;
						}
						// 2.Excel表统一编号不能重复
						if (exitsDeviceCode(list, deviceCode)) {
							// throw new Exception("数据校验出错:第" + (rowNum + 1) +
							// "行,Excel中统一编号重复，重复的统一编号是：" + deviceCode);
							map.put("error", "数据校验出错:第" + (rowNum + 1) + "行,Excel中统一编号重复，重复的统一编号是：" + deviceCode);
							return map;
						}

						// 3.校验数据库中采集器编号是否存在
						/*
						 * if(duplicateCheckCollectCode(collectCode)){
						 * is.close(); //采集器编号在设备绑定列表已存在，存在的编号为： //throw new
						 * Exception("数据校验出错:第" + (rowNum + 1) +
						 * "行,数据库采集器表中已经存在相同编号，重复的采集器编号是：" + deviceCode);
						 * map.put("error", "数据校验出错:第" + (rowNum + 1) +
						 * "行,采集器编号在设备绑定列表已存在，存在的编号为：" + collectCode); return
						 * map; }
						 */

						// 4.校验数据库中统一编号是否存在
						/*
						 * if(duplicateCheckDeviceCode(deviceCode)){ is.close();
						 * //采集器编号在设备绑定列表已存在，存在的编号为： //throw new
						 * Exception("数据校验出错:第" + (rowNum + 1) +
						 * "行,数据库采集器表中已经存在相同编号，重复的采集器编号是：" + deviceCode);
						 * map.put("error", "数据校验出错:第" + (rowNum + 1) +
						 * "行,统一编号在设备绑定列表已存在，存在的编号为：" + deviceCode); return map;
						 * }
						 */
						// 先校验后放入 (数据库中采集器编号不存在 数据库中统一编号不存在) 进行插入
						if (!duplicateCheckCollectCode(collectCode) && !duplicateCheckDeviceCode(deviceCode)) {
							list.add(collect);
						}
					}
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		/*
		 * if(list.size()==0){ map.put("error", "数据校验出错:请确保excel填了合格的数据");
		 * return map; }
		 */

		return list;
	}

	private boolean duplicateCheckDeviceCode(String deviceCode) {
		List<Ins_Collect> query = my_dao.query(Ins_Collect.class, Cnd.where("deviceCode", "=", deviceCode));
		if (query.size() == 0) {
			return false;
		}
		return true;
	}

	// 2003-2007版本
	public Object readXls(String path) throws Exception {
		System.out.println(ReadExcelUtil.PROCESSING + path);
		List<CollectBindDeviceBean> list = new ArrayList<CollectBindDeviceBean>();
		InputStream is = null;
		try {
			is = new FileInputStream(path);
			HSSFWorkbook hssfWorkbook = new HSSFWorkbook(is);
			CollectBindDeviceBean collect = null;
			String val = null;
			// Read the Sheet
			for (int numSheet = 0; numSheet < hssfWorkbook.getNumberOfSheets(); numSheet++) {
				HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(numSheet);
				if (hssfSheet == null) {
					continue;
				}
				// Read the Row
				for (int rowNum = 1; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
					HSSFRow hssfRow = hssfSheet.getRow(rowNum);
					if (hssfRow != null) {
						collect = new CollectBindDeviceBean();
						HSSFCell HcollectCode = hssfRow.getCell(0);
						HSSFCell HcollectName = hssfRow.getCell(1);
						HSSFCell HdeviceCode = hssfRow.getCell(2);
						HSSFCell HdeviceName = hssfRow.getCell(3);
						HSSFCell createTime = hssfRow.getCell(4);
						/*
						 * student.setNo(getValue(no));
						 * student.setName(getValue(name));
						 * student.setAge(getValue(age));
						 * student.setScore(Float.valueOf(getValue(score)));
						 */
						String collectCode = getValue(HcollectCode);
						String collectName = getValue(HcollectName);
						String deviceCode = getValue(HdeviceCode);
						String deviceName = getValue(HdeviceName);

						collect.setCollectCode(collectCode);
						collect.setCollectName(collectName);
						collect.setDeviceCode(deviceCode);
						collect.setDeviceName(deviceName);

						if (org.apache.poi.ss.usermodel.DateUtil.isCellDateFormatted(createTime)) {
							Date theDate = createTime.getDateCellValue();
							SimpleDateFormat dff = new SimpleDateFormat("yyyy/MM/dd");
							val = dff.format(theDate);
						} else {
							DecimalFormat df = new DecimalFormat("0");
							val = df.format(createTime.getNumericCellValue());
						}
						collect.setCreateTime(DateUtil.getDateFromString(val));

						// 对单元格里的数据进行校验
						// 采集器编号不允许重复
						if (exitsCollectCode(list, collectCode)) {
							throw new Exception("数据校验出错:第" + (rowNum + 1) + "行,Excel中采集器编号重复，重复的采集器编号是：" + collectCode);
						}

						list.add(collect);
					}
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			is.close();
		}
		return list;
	}

	private String getValue(HSSFCell cell) {

		SimpleDateFormat sFormat = new SimpleDateFormat("yyyy/MM/dd");
		DecimalFormat decimalFormat = new DecimalFormat("#.#");
		String cellValue = "";
		if (cell == null) {
			return cellValue;
		} else if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
			cellValue = cell.getStringCellValue();
		}

		else if (cell.getCellType() == XSSFCell.CELL_TYPE_NUMERIC) {
			if (HSSFDateUtil.isCellDateFormatted(cell)) {
				double d = cell.getNumericCellValue();
				Date date = HSSFDateUtil.getJavaDate(d);
				cellValue = sFormat.format(date);
			} else {
				cellValue = decimalFormat.format((cell.getNumericCellValue()));
			}
		} else if (cell.getCellType() == Cell.CELL_TYPE_BLANK) {
			cellValue = "";
		} else if (cell.getCellType() == Cell.CELL_TYPE_BOOLEAN) {
			cellValue = String.valueOf(cell.getBooleanCellValue());
		} else if (cell.getCellType() == Cell.CELL_TYPE_ERROR) {
			cellValue = "";
		} else if (cell.getCellType() == Cell.CELL_TYPE_FORMULA) {
			cellValue = cell.getCellFormula().toString();
		}
		return cellValue;
	}

	/**
	 * Excel表格中校验采集器编号是否重复
	 *
	 * @param list
	 * @param collectCode
	 * @return
	 */
	private boolean exitsCollectCode(List<CollectBindDeviceBean> list, String collectCode) {
		for (CollectBindDeviceBean domain : list) {
			if (domain.getCollectCode().equals(collectCode)) {
				return true;
			}
		}
		return false;
	}

	/**
	 *
	 * Excel表格中校验统一编号是否重复
	 * @param list
	 * @param deviceCode
	 * @return
	 */
	private boolean exitsDeviceCode(List<CollectBindDeviceBean> list, String deviceCode) {
		for (CollectBindDeviceBean domain : list) {
			if (domain.getDeviceCode().equals(deviceCode)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 校验采集器表中是否有重复
	 *
	 * @param collectCode
	 * @return
	 */
	private boolean duplicateCheckCollectCode(String collectCode) {
		Ioc ioc = Mvcs.ctx().getDefaultIoc();
		Dao dao = ioc.get(Dao.class);
		List<Ins_Collect> query = dao.query(Ins_Collect.class, Cnd.where("collectCode", "=", collectCode));
		if (query.size() == 0) {
			return false;
		}
		return true;
	}

	// 2007版本(导入资产信息)
	@SuppressWarnings({ "unused" })
	public Object readAssetExcel(String path) {
		Ioc ioc = Mvcs.ctx().getDefaultIoc();
		Dao dao = ioc.get(Dao.class);
		// 是否是第一次导入
		// String flag = (String) Configer.getInstance().get("is_first");
		Map<String, Object> resultMap = new HashMap<>();
		// 封装的实体类
		List<Ins_Assets> assetList = new ArrayList<Ins_Assets>();
		List<Ins_Asset_Rule> rulesList = new ArrayList<Ins_Asset_Rule>();
		List<Ins_DeviceInfo> deviceInfoList = new ArrayList<Ins_DeviceInfo>();
		List<Ins_ProjectInfo> projectInfoList = new ArrayList<Ins_ProjectInfo>();
		List<Ins_Asset_lend_record> lendRecordList = new ArrayList<Ins_Asset_lend_record>();
		List<Ins_Asset_Unit> assetUnitList = new ArrayList<Ins_Asset_Unit>();
		// 封装错误信息
		StringBuilder sb = new StringBuilder();
		InputStream is = null;
		try {
			// 将文件放入流中
			is = new FileInputStream(path);
			XSSFWorkbook xssfWorkbook = new XSSFWorkbook(is);
			// 创建要封装的对象
			Ins_Assets assets = null;
			Ins_Asset_Rule rules = null;
			Ins_DeviceInfo deviceInfo = null;
			Ins_ProjectInfo projectInfo = null;
			Ins_Asset_lend_record lendRecord = null;
			Ins_Asset_Unit assetUnit = null;
			Map<String, String> projectMap = new HashMap<String, String>();
			Map<String, String> rulesMap = new HashMap<String, String>();
			// 开始读Sheet
			for (int numSheet = 0; numSheet < xssfWorkbook.getNumberOfSheets(); numSheet++) {
				// 获取每一个sheet
				XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(numSheet);
				if (xssfSheet == null) {
					continue;
				}
				// Read the Row(遍历每一行)
				for (int rowNum = 2; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
					XSSFRow xssfRow = xssfSheet.getRow(rowNum);

					XSSFCell fisrtCell = xssfRow.getCell(0);
					XSSFCell senCell = xssfRow.getCell(1);
					if (fisrtCell == null && senCell == null){
						break;
					}
					// 此行不等于空 并且第二个单元格不等于空
					if (xssfRow.getCell(1) != null
							|| !"".equals(CommonExcelUtil.getValue(xssfRow.getCell(1)))) {
						// 实例化对象
						assets = new Ins_Assets(); // 资产对象asset_info
						rules = new Ins_Asset_Rule(); // 型号对象
						deviceInfo = new Ins_DeviceInfo(); // 资产对象device_info
						projectInfo = new Ins_ProjectInfo(); // 项目对象
						lendRecord = new Ins_Asset_lend_record(); // 借入借出对象
						assetUnit = new Ins_Asset_Unit(); // 资产单位对象

						// 获取单行excel数据
						String assetType = CommonExcelUtil.getValue(xssfRow.getCell(0)).trim(); // 仪器类别
						String assetCode = CommonExcelUtil.getValue(xssfRow.getCell(1)).trim(); // 统一编号
						String assetName = CommonExcelUtil.getValue(xssfRow.getCell(2)).trim(); // 名称
						String deviceVersion = CommonExcelUtil.getValue(xssfRow.getCell(3)).trim(); // 型号
						String originalValue = CommonExcelUtil.getValue(xssfRow.getCell(4)).trim(); // 原值
						String country = CommonExcelUtil.getValue(xssfRow.getCell(5)).trim(); // 国别
						String manufactor = CommonExcelUtil.getValue(xssfRow.getCell(6)).trim(); // 厂家
						XSSFCell factoryTime = xssfRow.getCell(7); // 出厂日期
						XSSFCell enableTime = xssfRow.getCell(8); // 启用日期
						String serialNumber = CommonExcelUtil.getValue(xssfRow.getCell(9)).trim(); // 出厂编号
						String borrowDepart = CommonExcelUtil.getValue(xssfRow.getCell(10)).trim(); // 使用单位
						String chargePerson = CommonExcelUtil.getValue(xssfRow.getCell(11)).trim(); // 责任人
						XSSFCell ReturnDate = xssfRow.getCell(12); // 归还日期
						String manageState = CommonExcelUtil.getValue(xssfRow.getCell(13)).trim(); // 管理状态
						String fundSource = CommonExcelUtil.getValue(xssfRow.getCell(14)).trim(); // 资金来源
						String projectName = CommonExcelUtil.getValue(xssfRow.getCell(15)).trim(); // 项目名称
						String contractCode = CommonExcelUtil.getValue(xssfRow.getCell(16)).trim(); // 合同号
						String batchCode = CommonExcelUtil.getValue(xssfRow.getCell(17)).trim(); // 批件序号
						String locationInfo = CommonExcelUtil.getValue(xssfRow.getCell(18)).trim(); // 存放位置
						String scrapState = CommonExcelUtil.getValue(xssfRow.getCell(19)).trim(); // 报废状态
						String assetCategory = CommonExcelUtil.getValue(xssfRow.getCell(20)).trim(); // 资产类别
						String is_military = CommonExcelUtil.getValue(xssfRow.getCell(21)).trim(); // 是否军工
						XSSFCell examineDate = xssfRow.getCell(22); // 检定日期
						XSSFCell dueDate = xssfRow.getCell(23); // 到期检定日期
						String ggName = CommonExcelUtil.getValue(xssfRow.getCell(24)).trim(); // 规格
						XSSFCell lendDate = xssfRow.getCell(25); // 借用日期
						String manageLevel = CommonExcelUtil.getValue(xssfRow.getCell(26)).trim(); // 管理级别
						String completeStatus = CommonExcelUtil.getValue(xssfRow.getCell(27)).trim(); // 完好状态
						String insCatergory = CommonExcelUtil.getValue(xssfRow.getCell(28)).trim(); // 仪器类别
						String power = CommonExcelUtil.getValue(xssfRow.getCell(29)).trim(); // 功率
						String period = CommonExcelUtil.getValue(xssfRow.getCell(30)).trim(); // 保修期
						String repairState = CommonExcelUtil.getValue(xssfRow.getCell(31)).trim(); // 维修状态
						String useType = CommonExcelUtil.getValue(xssfRow.getCell(32)).trim(); // 用途
						String remark = CommonExcelUtil.getValue(xssfRow.getCell(33)).trim(); // 备注

						// 判断类别是否为空
						if (Strings.isBlank(assetType)) {
							sb.append("第" + (rowNum + 1) + "行,类别不能为空<br/>");
						} else {
							// 封装到asset当中
							if ("设备".equals(assetType)) {
								assets.setAssetType(1);
							} else if ("仪器".equals(assetType)) {
								assets.setAssetType(2);
							} else if ("工量具".equals(assetType)) {
								assets.setAssetType(3);
							} else {// 输入其他值
								sb.append("第" + (rowNum + 1) + "行,Excel中类别输入有误,请确定是设备、仪器、工量具其中之一<br/>");
							}

						}
						// 判断统一编号是否为空
						if (Strings.isBlank(assetCode)) {
							sb.append("第" + (rowNum + 1) + "行,Excel中统一编号不能为空<br/>");
						} else {
							// 设置统一编号
							assets.setAssetCode(assetCode);
						}
						String deviceVersionORG = null;
						// 判断型号是否为空
						if (Strings.isBlank(deviceVersion)) {
							// 如果资产编号以H开头
							if (assetCode.startsWith("H")) {
								// 型号名为资产名称（资产编号）
								deviceVersionORG = assetName + "(" + assetCode + ")";
								assets.setDeviceVersion(deviceVersionORG);

							} else {// 其他不以H开头的空型号
								deviceVersionORG = assetCode;//UUID.randomUUID().toString();
								assets.setDeviceVersion(deviceVersionORG);
							}
							// sb.append("第" + (rowNum + 1) +
							// "行,Excel中型号不能为空<br/>");
						} else {
							// 设置型号(型号不为空)
							deviceVersionORG = deviceVersion;
							assets.setDeviceVersion(deviceVersion);
						}

						// 型号得处理
						String newDeviceVersion = null;
						Ins_Asset_Rule new_rule = null;
						try {
							// 根据型号查询看数据库当中是否存在
							new_rule = dao.fetch(Ins_Asset_Rule.class,
									Cnd.where("deviceVersionOrg", "=", deviceVersionORG));
							// 数据库没有的话，那么型号进行插入
							if (new_rule == null) {
								// 表格中没有重复的
								if (!exitsDeviceVersion2(rulesList, deviceVersionORG, rulesMap)) {
									// assets.setDeviceVersion(deviceVersionORG);
									rules.setDeviceVersion(deviceVersion);
									rules.setDeviceVersionOrg(deviceVersionORG);
									rules.setAssetName(assetName);

									rules.setCreateTime(new Date());
									rulesMap.put(deviceVersionORG, deviceVersionORG);
								} else {// 表格中有重复的
									assets.setDeviceVersion(rulesMap.get(deviceVersionORG));
									rules.setAssetName(assetName);
									rules.setCreateTime(new Date());
								}
							}
						} catch (Exception e2) {
							e2.printStackTrace();
						}

						// 判断原值的正确性
						if (Strings.isNotBlank(originalValue)) {
							boolean isNumber = CommonExcelUtil.isNumber(originalValue);
							// 是数字
							if (isNumber) {
								Double num = null;
								try {
									num = Double.parseDouble(originalValue);
								} catch (NumberFormatException e) {
									LOG.error("第" + (rowNum + 1) + "行, 原值输入有误");
									sb.append("第" + (rowNum + 1) + "行, 原值输入有误");
								}
								if (num != null) {
									assets.setOriginalValue(num);
								}
							}
						}
						assets.setCountry(country); // 国别
						assets.setManufactor(manufactor); // 厂家
						assets.setFactoryTime(CommonExcelUtil.getDate(factoryTime)); // 日期需要进行转换
						assets.setEnableTime(CommonExcelUtil.getDate(enableTime));
						assets.setSerialNumber(serialNumber); // 出厂编号
						// 查找使用单位和责任人的id
						String newBorrowDepart = null;
						//
						if (Strings.isNotBlank(borrowDepart)) {
							try {
								// 根据名称查找单位
								Sys_unit findUnit = dao.fetch(Sys_unit.class, Cnd.where("name", "=", borrowDepart));
								if (findUnit == null) {
									sb.append("使用单位错误:第" + (rowNum + 1) + "行, 没有" + borrowDepart + "此单位<br/>");
								} else {
									newBorrowDepart = findUnit.getId();
								}

								assets.setBorrowDepart(newBorrowDepart);
							} catch (Exception e1) {
								sb.append("使用单位错误:第" + (rowNum + 1) + "行");
								e1.printStackTrace();
							}
						} else {
							Sys_unit unit = dao.fetch(Sys_unit.class, Cnd.where("name", "=", "库房"));
							if (unit != null) {
								assets.setBorrowDepart(unit.getId()); // 默认是库房
							}
						}

						// 责任人id
						String newChargePerson = null;
						if (!"".equals(chargePerson) && chargePerson != null && !"".equals(borrowDepart)) {
							try {
								Sql sql2 = Sqls
										.create("select b.id from sys_user b WHERE b.username = @data AND b.unitid = "
												+ "( SELECT a.id from sys_unit a WHERE a.`name` = @data2)");
								sql2.setParam("data", chargePerson);
								sql2.setParam("data2", borrowDepart);
								Entity<ExcelForm> entity2 = dao.getEntity(ExcelForm.class);
								sql2.setEntity(entity2);
								sql2.setCallback(Sqls.callback.entities());
								dao.execute(sql2);
								List<ExcelForm> list = sql2.getList(ExcelForm.class);
								if (list.size() > 0 && list != null) { // 有这个员工
									newChargePerson = list.get(0).getId();
									assets.setChargePerson(newChargePerson);
								} else if (Strings.isBlank(newChargePerson)) {// 当前单位没有该职工,查询所有职工
									Sql sql5 = Sqls.create("select b.* from sys_user b WHERE b.username = @data ");
									sql5.setParam("data", chargePerson);
									Entity<Sys_user> entity5 = dao.getEntity(Sys_user.class);
									sql5.setEntity(entity5);
									sql5.setCallback(Sqls.callback.entities());
									dao.execute(sql5);
									List<Sys_user> list5 = sql5.getList(Sys_user.class);
									if (list5 == null || list5.size() == 0) {
										Sys_user user = new Sys_user();
										user.setId(UUID.randomUUID().toString());
										user.setUserStatus(5);// 未知状态
										user.setUsername(chargePerson);
										user.setUnitid(newBorrowDepart);
										dao.insert(user);
										assets.setChargePerson(user.getId());
										assets.setBorrowDepart(user.getUnitid());
									} else if (list5.size() == 1) {
										// Sys_user sys_user = list5.get(0);
										// assets.setBorrowDepart(sys_user.getUnitid());
										// assets.setChargePerson(sys_user.getId());
										sb.append("第" + (rowNum + 1) + "行,有"+ chargePerson +"此责任人,但不在"+ borrowDepart +"部门下,请确认!" + "<br/>");
									} else {
										sb.append("第" + (rowNum + 1) + "行,有"+ chargePerson+ "此责任人,但不在"+ borrowDepart +"部门下,且这个人在职工表里有重名的情况,请确认!" + "<br/>");
									}
								}
							} catch (Exception e) {
								sb.append("责任人错误:第" + (rowNum + 1) + "行<br/>");
								e.printStackTrace();
							}
						}

						if (!"".equals(chargePerson) && chargePerson != null && "".equals(borrowDepart)) {
							try {
								Sql sql2 = Sqls
										.create("select b.id from sys_user b WHERE b.username = @data AND b.unitid = "
												+ "( SELECT a.id from sys_unit a WHERE a.`name` = '库房')");
								sql2.setParam("data", chargePerson);
								sql2.setParam("data2", "库房");
								Entity<ExcelForm> entity2 = dao.getEntity(ExcelForm.class);
								sql2.setEntity(entity2);
								sql2.setCallback(Sqls.callback.entities());
								dao.execute(sql2);
								List<ExcelForm> list = sql2.getList(ExcelForm.class);
								if (list.size() > 0 && list != null) { // 有这个员工
									newChargePerson = list.get(0).getId();
									assets.setChargePerson(newChargePerson);
								} else if (Strings.isBlank(newChargePerson)) {// 库房单位下没有该职工,查询所有职工
									Sql sql5 = Sqls.create("select b.* from sys_user b WHERE b.username = @data ");
									sql5.setParam("data", chargePerson);
									Entity<Sys_user> entity5 = dao.getEntity(Sys_user.class);
									sql5.setEntity(entity5);
									sql5.setCallback(Sqls.callback.entities());
									dao.execute(sql5);
									List<Sys_user> list5 = sql5.getList(Sys_user.class);
									if (list5 == null || list5.size() == 0) {
										sb.append("第" + (rowNum + 1) + "行," + "所有单位下无此" + chargePerson + "责任人,请检查!" + "<br/>");
										/*Sys_user user = new Sys_user();
										user.setId(UUID.randomUUID().toString());
										user.setUserStatus(5);// 未知状态
										user.setUsername(chargePerson);
										user.setUnitid(newBorrowDepart);
										dao.insert(user);
										assets.setChargePerson(user.getId());
										assets.setBorrowDepart(user.getUnitid());*/

									} else if (list5.size() == 1) {
										sb.append("第" + (rowNum + 1) + "行," + chargePerson + "责任人,不在库房单位下请检查!" + "<br/>");
									} else if (list5.size() > 1) {
										sb.append("第" + (rowNum + 1) + "行," + chargePerson + "责任人不在库房单位下,且这个人在职工表里有重名的情况,请确认!" + "<br/>");
									}
								}
							} catch (Exception e) {
								sb.append("责任人错误:第" + (rowNum + 1) + "行<br/>");
								e.printStackTrace();
							}
						}
						// 归还日期
						Date date = CommonExcelUtil.getDate(ReturnDate);
						// System.out.println(date);
						assets.setReturnDate(CommonExcelUtil.getDate(ReturnDate));
						// 管理状态
						if (Strings.isBlank(manageState)) {
							assets.setManageStatus(null);
						} else if (manageState.trim().equals("在用")) {
							assets.setManageStatus(0);
						} else if (manageState.trim().equals("封存")) {
							assets.setManageStatus(1);
						} else {
							assets.setManageStatus(null);
						}

						if (Strings.isBlank(fundSource)) {
							assets.setFundSources(null);
						} else if (fundSource.trim().equals("拨款")) {
							assets.setFundSources(0);
						} else if (fundSource.trim().equals("自购")) {
							assets.setFundSources(1);
						} else {
							assets.setFundSources(null);
						}

						// 根据统一编号查询自残
						Ins_Assets proAsset = dao.fetch(Ins_Assets.class, Cnd.where("assetCode", "=", assetCode));
						String projectFromDB = null;
						// 获取项目名称
						if (proAsset != null) {
							projectFromDB = proAsset.getProjectName();
						}
						if (Strings.isNotBlank(projectName)) {
							Sql sql3 = Sqls.create("select b.* from ins_project_info b where b.`name` = @projectName");
							sql3.setParam("projectName", projectName.trim());
							Entity<Ins_ProjectInfo> entity3 = dao.getEntity(Ins_ProjectInfo.class);
							sql3.setEntity(entity3);
							sql3.setCallback(Sqls.callback.entities());
							dao.execute(sql3);
							List<Ins_ProjectInfo> list = sql3.getList(Ins_ProjectInfo.class);
							if (list != null && list.size() > 0) { // 数据库当中有
								Ins_ProjectInfo findInfo = list.get(0);
								String projectCode = findInfo.getCode();
								assets.setProjectName(projectCode);
							} else {// 没有进行插入
									// excel表中是否有重复
								if (!exitsProjectName(projectInfoList, projectName)) {
									String code = UUID.randomUUID().toString();
									projectMap.put(projectName, code);
									projectInfo.setCode(code); // 项目code
									projectInfo.setName(projectName); // 项目名称
									projectInfo.setType("0"); // 项目类型(技改)
									projectInfo.setCreateTime(new Date()); // 项目时间
									assets.setProjectName(code);
								} else {
									String code = projectMap.get(projectName);
									assets.setProjectName(code);
								}
							}
						} else {
							assets.setProjectName(null);
						}

						assets.setContractCode(contractCode);
						assets.setBatchCode(batchCode);

						if (scrapState == null) {
							assets.setScrapState(null);
						} else if (scrapState.trim().equals("待报废")) {
							assets.setScrapState(0);
						} else if (scrapState.trim().equals("报废")) {
							assets.setScrapState(1);
						} else {
							assets.setScrapState(null);
						}

						if (assetCategory == null) {
							assets.setAssetCategory(null);
						} else if (assetCategory.trim().equals("固定资产")) {
							assets.setAssetCategory(0);
						} else if (assetCategory.trim().equals("低值")) {
							assets.setAssetCategory(1);
						} else if (assetCategory.trim().equals("在建")) {
							assets.setAssetCategory(2);
						} else if (assetCategory.trim().equals("附件")) {
							assets.setAssetCategory(3);
						} else if (assetCategory.trim().equals("零值")) {
							assets.setAssetCategory(4);
						} else {
							assets.setAssetCategory(null);
						}

						if (is_military == null) {
							assets.setIsMilitary(null);
						} else if (is_military.trim().equals("是")) {
							assets.setIsMilitary(0);
						} else if (is_military.trim().equals("否")) {
							assets.setIsMilitary(1);
						} else {
							assets.setIsMilitary(null);
						}
						assets.setExamineDate(CommonExcelUtil.getDate(examineDate));
						assets.setDueDate(CommonExcelUtil.getDate(dueDate));
						assets.setGgName(ggName); // 规格

						// 借用日期需要特殊处理
						Date lendDateForm = CommonExcelUtil.getDate(lendDate);

						// 根据统一编号查单位资产
						Ins_Asset_Unit fetchAssetUnit = dao.fetch(Ins_Asset_Unit.class,
								Cnd.where("assetCode", "=", assetCode));

						// 判断数据库中是否存在此资产,并判断借出状态
						Ins_Assets lendAsset = dao.fetch(Ins_Assets.class, Cnd.where("assetCode", "=", assetCode));
						if (lendAsset == null) { // 资产不存在
							if (lendDateForm == null) { // 借用日期为空
								if ("库房".equals(borrowDepart) || "KF_001".equals(assets.getBorrowDepart())) { // 是库房
									if(lendDateForm != null || date != null){
										sb.append("数据校验出错: 初次导入时不能填写借用和归还日期,请检查!" + "<br/>");
									}
									// 是否过期(0是/1否)
									assets.setIsOverdue(1);
									// 是否链接云网(0是/1否)
									// assets.setIsConnectCloud(1);
									// 是否借出(0是/1否)
									assets.setIsLend(1);
									// 是否在外场(0否/1是)
									deviceInfo.setOutField(0);
									// 使用单位默认是库房，已经在上面赋值
								} else { // 不是库房（借用日期为空，但是不为库房）isLend =
											// 0,插入借还记录，科室借调
									// 是否过期(0是/1否)
									assets.setIsOverdue(1);
									// 是否借出(0是/1否)
									assets.setIsLend(0);
									// 是否在外场(0否/1是)
									deviceInfo.setOutField(1);
									// 插入借还记录(借出时间为：当前系统时间)
									setLendRecord(dao, assets, lendRecord, assetCode, newBorrowDepart, newChargePerson,
											new Date());
									// 插入科室借调记录
									insertUntiLend(assetUnit, assetCode, locationInfo, newBorrowDepart, newChargePerson,
											fetchAssetUnit);
								}
							} else { // 借用日期有值（资产不存在）
								if ("库房".equals(borrowDepart) || "KF_001".equals(assets.getBorrowDepart())) {
									if(lendDateForm != null || ReturnDate != null){
										sb.append("数据校验出错: 初次导入时不能填写借用日期和归还日期,请检查!" + "<br/>");
									}
									// 是否过期(0是/1否)
									assets.setIsOverdue(1);
									// 是否链接云网(0是/1否)
									// assets.setIsConnectCloud(1);
									// 是否借出(0是/1否)
									assets.setIsLend(1);
									// 是否在外场(0否/1是)
									deviceInfo.setOutField(0);
									// 使用单位默认是库房，已经在上面赋值
								} else { // 借出日期不为空，责任单位不是库房
									// 是否过期(0是/1否)
									assets.setIsOverdue(1);
									// 是否借出(0是/1否)
									assets.setIsLend(0);
									// 是否在外场(0否/1是)
									deviceInfo.setOutField(1);
									// 插入借还记录
									setLendRecord(dao, assets, lendRecord, assetCode, newBorrowDepart, newChargePerson,
											lendDateForm);
									// 插入科室借调
									insertUntiLend(assetUnit, assetCode, locationInfo, newBorrowDepart, newChargePerson,
											fetchAssetUnit);
								}
							}
						} else { // 查到资产

							if (lendDateForm != null && lendAsset.getIsLend() != null && lendAsset.getIsLend() == 1) { // 归还状态
								setLendRecord(dao, assets, lendRecord, assetCode, newBorrowDepart, newChargePerson,
										lendDateForm);
							}
							// 插入科室借调
							insertUntiLend(assetUnit, assetCode, locationInfo, newBorrowDepart, newChargePerson,
									fetchAssetUnit);
						}
						// 采集器表中有无此条记录
						Ins_Collect findCollect = dao.fetch(Ins_Collect.class, Cnd.where("deviceCode", "=", assetCode));
						if (findCollect != null && Strings.isNotBlank(findCollect.getDeviceCode())) {
							// 连接云网
							assets.setIsConnectCloud(0);
						} else {
							// 没有连接云网
							assets.setIsConnectCloud(1);
						}

						// 管理级别
						if (manageLevel == null || "".equals(manageLevel.trim())) {
							assets.setManageLevel(null);
						} else if ("厂(所)管".equals(manageLevel)) {
							assets.setManageLevel(0);
						} else if ("厂（所）管".equals(manageLevel)) {
							assets.setManageLevel(0);
						} else if ("院管".equals(manageLevel)) {
							assets.setManageLevel(1);
						} else if ("总公司部管".equals(manageLevel)) {
							assets.setManageLevel(2);
						} else {
							assets.setManageLevel(null);
						}
						if (completeStatus == null) {
							assets.setCompleteStatus(null);
						} else if ("完好".equals(completeStatus)) {
							assets.setCompleteStatus(0);
						} else if ("不完好".equals(completeStatus)) {
							assets.setCompleteStatus(1);
						} else {
							assets.setCompleteStatus(null);
						}
						if (insCatergory == null) {
							assets.setInstrumentCategory(null);
						} else if ("普通".equals(insCatergory)) {
							assets.setInstrumentCategory(0);
						} else if ("精大贵稀".equals(insCatergory)) {
							assets.setInstrumentCategory(1);
						} else if ("进口普通".equals(insCatergory)) {// 进口普通
							assets.setInstrumentCategory(2);
						} else if ("进口精大贵稀".equals(insCatergory)) {
							assets.setInstrumentCategory(3);
						} else {
							assets.setInstrumentCategory(null);
						}
						
						//功率校验
						if(Strings.isNotBlank(power) && !power.matches("^0\\.([1-9]|\\d[1-9])$|^[1-9]\\d{0,8}\\.\\d{0,2}$|^[1-9]\\d{0,8}$")){
							sb.append("数据校验出错:第" + (rowNum + 1) + "行, 功率输入有误! 请输入大于0的小数或整数,最多2位小数!" + "<br/>");
						}
						assets.setPower(power);
						assets.setWarrantyPeriod(period);
						if (repairState == null) {
							assets.setRepairState(null);
						} else if ("维修中".equals(repairState)) {
							assets.setRepairState(0);
						} else if ("已完成".equals(repairState)) {
							assets.setRepairState(1);
						} else {
							assets.setRepairState(null);
						}
						if (useType == null) {
							assets.setUseType(null);
						} else if ("科研类".equals(useType)) {
							assets.setUseType(0);
						} else if ("生产类".equals(useType)) {
							assets.setUseType(1);
						} else if ("测量类".equals(useType)) {
							assets.setUseType(2);
						} else {
							assets.setUseType(null);
						}
						assets.setRemark(remark);
						// 设置资产统一编号
						deviceInfo.setDeviceCode(assets.getAssetCode());
						// 设置设备名称
						if (new_rule != null) {
							deviceInfo.setDeviceName(new_rule.getAssetName());
						} else {
							deviceInfo.setDeviceName(rules.getAssetName());
						}
						// 设置型号
						deviceInfo.setDeviceVersion(assets.getDeviceVersion());
						// 使用单位和责任人
						deviceInfo.setBorrowDepart(assets.getBorrowDepart());
						deviceInfo.setChargePerson(assets.getChargePerson());
						// 资产类型
						deviceInfo.setAssetType(assets.getAssetType());

						// 对单元格里的数据进行校验
						// 1.Excel表中统一编号不允许重复
						if (exitsAssetCode(assetList, assetCode)) {
							sb.append("数据校验出错: 第" + (rowNum + 1) + "行,Excel中统一编号重复，重复的统一编号是：" + assetCode + "<br/>");
						}
						// 判断数据库中是否存在此统一编号,如果存在则赋值Id
						if (duplicateCheckAssetCode(assetCode)) {
							Ins_Assets findAsset = dao.fetch(Ins_Assets.class, Cnd.where("assetCode", "=", assetCode));
							Ins_DeviceInfo fetchDevice = dao.fetch(Ins_DeviceInfo.class,
									Cnd.where("deviceCode", "=", assetCode));
							assets.setId(findAsset.getId());
							assets.setLocationInfo(locationInfo);
							deviceInfo.setId(fetchDevice.getId());
						} else {
							assets.setLocationInfo(locationInfo);
						}
						// 检验数据库中型号是否重复
						if (duplicateCheckDeviceVersion(deviceVersion)) {
							Ins_Asset_Rule findRule = dao.fetch(Ins_Asset_Rule.class,
									Cnd.where("deviceVersion", "=", deviceVersion));
							rules.setId(findRule.getId());
						}
						// 项目是否重复
						if (duplicateCheckProjectCode(projectName)) {
							Ins_ProjectInfo findProject = dao.fetch(Ins_ProjectInfo.class,
									Cnd.where("name", "=", projectName.trim()));
							projectInfo.setId(findProject.getId());
						}
						// 先校验后放入
						assetList.add(assets);
						if (Strings.isNotBlank(rules.getDeviceVersionOrg())) {
							rulesList.add(rules);
						}
						if (Strings.isNotBlank(deviceInfo.getDeviceCode())) {
							deviceInfoList.add(deviceInfo);
						}
						if (Strings.isNotBlank(projectInfo.getCode())) {
							projectInfoList.add(projectInfo);
						}
						if (Strings.isNotBlank(lendRecord.getAssetCode())) {
							lendRecordList.add(lendRecord);
						}
						if (Strings.isNotBlank(assetUnit.getAssetCode())) {
							assetUnitList.add(assetUnit);
						}

					}
				}
			}
		} catch (FileNotFoundException e) {
			sb.append("导入文件找不到<br/>");
			e.printStackTrace();
		} catch (IOException e) {
			sb.append("导入失败<br/>");
			e.printStackTrace();
		}

		// 存在错误信息直接返回错误信息
		String newSb = sb.toString();
		if (Strings.isNotBlank(newSb)) {
			resultMap.put("error", newSb);
			return resultMap;
		} else {// 没有错误，放回数据集合
			resultMap.put("assetInfo", assetList);
			resultMap.put("ruleInfo", rulesList);
			resultMap.put("deviceInfo", deviceInfoList);
			resultMap.put("projectInfo", projectInfoList);
			resultMap.put("lendRecordInfo", lendRecordList);
			resultMap.put("assetUnitInfo", assetUnitList);
			return resultMap;
		}

	}

	private void insertUntiLend(Ins_Asset_Unit assetUnit, String assetCode, String locationInfo, String newBorrowDepart,
			String newChargePerson, Ins_Asset_Unit fetchAssetUnit) {
		if (fetchAssetUnit == null) { // 数据库当中没有,初始化导入
			assetUnit.setAssetCode(assetCode);
			assetUnit.setChargeDepart(newBorrowDepart);
			assetUnit.setChargeMan(newChargePerson);
			assetUnit.setUserDepart(newBorrowDepart);
			assetUnit.setUserMan(newChargePerson);
			assetUnit.setRate("--");
			assetUnit.setStatus(0);
			assetUnit.setOperateTime(new Date());
			assetUnit.setPosition(locationInfo);
		} else {
			assetUnit.setId(fetchAssetUnit.getId());
			assetUnit.setAssetCode(assetCode);
			// 可以变更使用人和责任单位
			assetUnit.setChargeDepart(newBorrowDepart);
			assetUnit.setChargeMan(newChargePerson);
		}
	}

	/**
	 * 插入借还记录
	 *
	 * @param dao
	 * @param assets
	 * @param lendRecord
	 * @param assetCode
	 * @param newBorrowDepart
	 * @param newChargePerson
	 * @param lendDateForm
	 */
	private void setLendRecord(Dao dao, Ins_Assets assets, Ins_Asset_lend_record lendRecord, String assetCode,
			String newBorrowDepart, String newChargePerson, Date lendDateForm) {
		// 获取当前登录用户
		Sys_user currentUser = (Sys_user) SecurityUtils.getSubject().getPrincipal();
		String username = currentUser.getUsername();
		String unitid = currentUser.getUnitid();
		// 根据单位id查出单位名称
		Sys_unit fetch = dao.fetch(Sys_unit.class, Cnd.where("id", "=", unitid));
		lendRecord.setOriginalDepart(fetch.getName());
		// 为资产借出记录赋值
		lendRecord.setAssetCode(assetCode);
		Sys_unit unit = dao.fetch(Sys_unit.class, Cnd.where("id", "=", newBorrowDepart));
		if (unit != null) {
			String departName = unit.getName();
			lendRecord.setApplyDepart(departName);
		}
		Sys_user user = dao.fetch(Sys_user.class, Cnd.where("id", "=", newChargePerson));
		if (user != null) {
			String personName = user.getUsername();
			lendRecord.setApplyPerson(personName);
		}

		lendRecord.setHandlePerson(username);
		lendRecord.setActionType(0); // 事件类型，0借出1归还(仪器室角度)
		lendRecord.setOprateTime(lendDateForm);
		assets.setIsLend(0); // 借出状态
		assets.setLendDate(lendDateForm);
	}

	private boolean exitsDeviceVersion2(List<Ins_Asset_Rule> rulesList, String deviceVersion,
			Map<String, String> rulesMap) {
		for (Ins_Asset_Rule rules : rulesList) {
			if (deviceVersion.equals(rules.getDeviceVersion())) {
				// rulesMap.put(deviceVersion, deviceVersion);
				return true;
			}
		}
		return false;

	}

	// 检测项目名称是否有重复
	private boolean exitsProjectName(List<Ins_ProjectInfo> projectInfoList, String projectName) {
		for (Ins_ProjectInfo projectInfo : projectInfoList) {
			if (projectName.equals(projectInfo.getName())) {
				return true;
			}
		}
		return false;
	}

	// 检测型号是否重复(数据库)
	private boolean duplicateCheckDeviceVersion(String deviceVersion) {
		Ioc ioc = Mvcs.ctx().getDefaultIoc();
		Dao dao = ioc.get(Dao.class);
		List<Ins_Asset_Rule> query = dao.query(Ins_Asset_Rule.class, Cnd.where("deviceVersion", "=", deviceVersion));
		if (query.size() == 0) {
			return false;
		}
		List<Ins_Assets> query2 = dao.query(Ins_Assets.class, Cnd.where("deviceVersion", "=", deviceVersion));
		if (query2.size() == 0) {
			return false;
		}
		return true;
	}

	// 检测统一编号是否重复(数据库)
	private boolean duplicateCheckAssetCode(String assetCode) {
		Ioc ioc = Mvcs.ctx().getDefaultIoc();
		Dao dao = ioc.get(Dao.class);
		List<Ins_Assets> query = dao.query(Ins_Assets.class, Cnd.where("assetCode", "=", assetCode));
		if (query.size() == 0) {
			return false;
		}
		return true;
	}

	// 检测统一编号是否重复(数据库)
	private boolean duplicateCheckProjectCode(String projectName) {
		if (projectName == null || "".equals(projectName)) {
			return false;
		}
		Ioc ioc = Mvcs.ctx().getDefaultIoc();
		Dao dao = ioc.get(Dao.class);
		List<Ins_ProjectInfo> query = dao.query(Ins_ProjectInfo.class, Cnd.where("name", "=", projectName));
		if (query.size() == 0) {
			return false;
		}
		return true;
	}

	// Excel表格中检测型号是否重复
	/*
	 * private boolean exitsDeviceVersion(List<Ins_Assets> assetList, String
	 * deviceVersion) { for (Ins_Assets domain : assetList) { if(
	 * domain.getDeviceVersion().equals(deviceVersion) ){ return true; } }
	 * return false; }
	 */
	// Excel表格中检测统一编号是否重复
	private boolean exitsAssetCode(List<Ins_Assets> assetList, String assetCode) {
		if (assetList.size() > 0) {
			for (Ins_Assets domain : assetList) {
				if (Strings.isNotBlank(domain.getAssetCode())) {
					if (domain.getAssetCode().equals(assetCode)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	/*
	 * 以流的形式把错误信息显示到本地.
	 */
	/*
	 * public static void writeErrormessageToLocal(String content){ // 创建输出流
	 * FileOutputStream fos = null; File file = null;
	 * 
	 * try { file = new File("D://error_new.txt"); fos = new
	 * FileOutputStream(file,true); // 如果不存在文件,create it if(!file.exists()){
	 * file.createNewFile(); } byte[] contentInByte = content.getBytes();
	 * fos.write(contentInByte); fos.flush(); fos.close(); } catch (Exception e)
	 * { e.printStackTrace(); } finally { if(fos != null){ try { fos.close(); }
	 * catch (IOException e) { e.printStackTrace(); } } }
	 * 
	 * }
	 */

	/**
	 * 履历导入,完成填充返回
	 * 
	 * @param path
	 */
	public Object readResumeExcelAndFillIt(String path) {
		long startTime = new Date().getTime();
		// 封装错误信息
		Map<String, Object> resultMap = new LinkedHashMap<>();
		InputStream is = null;
		OutputStream out = null;
		StringBuilder build = new StringBuilder();
		// 错误信息的可变字符串
		StringBuilder sb = new StringBuilder();
		try {
			// 将文件放入流中
			is = new FileInputStream(path);
			XSSFWorkbook xssfWorkbook = new XSSFWorkbook(is);
			// Read the Sheet
			for (int numSheet = 0; numSheet < xssfWorkbook.getNumberOfSheets(); numSheet++) {
				XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(numSheet);
				if (xssfSheet == null) {
					continue;
				}
				// Read the Row(遍历每一行)
				for (int rowNum = 3; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
					XSSFRow xssfRow = xssfSheet.getRow(rowNum);
					// 此行不等于空 并且第二个单元格不等于空
					if (xssfRow != null && xssfRow.getCell(0) != null
							&& !"".equals(CommonExcelUtil.getValue(xssfRow.getCell(0)))) {
						String assetCode = CommonExcelUtil.getValue(xssfRow.getCell(0));
						if (StringUtils.isNotBlank(assetCode)) {
							build.append("'").append(StringUtils.trim(assetCode)).append("'").append(",");
						}

						/*
						 * AssetsForm assets =
						 * assetsService.getAssetAndRuleInfoByAssetCode(
						 * assetCode) ; if(assets!=null){ XSSFCell cell =
						 * xssfRow.createCell(1);
						 * cell.setCellValue(isNull(assets.getDeviceVersion()));
						 * XSSFCell cell2 = xssfRow.createCell(2) ;
						 * cell2.setCellValue(isNull(assets.getGgName()));
						 * XSSFCell cell3 = xssfRow.createCell(3) ;
						 * cell3.setCellValue(isNull(assets.getAccuracyClass()))
						 * ; XSSFCell cell4 = xssfRow.createCell(4) ;
						 * cell4.setCellValue(DateUtils.converteToDay(assets.
						 * getFactoryTime(),4)); }
						 */

					}
				}

				if (StringUtils.isNotBlank(build.toString())) {
					Map<String, Ins_Assets> map = assetsService
							.getAssetAndRuleInfoByAssetCodes(StringUtils.strip(build.toString(), ","));
					for (int rowNum = 3; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
						XSSFRow xssfRow = xssfSheet.getRow(rowNum);
						// 此行不等于空 并且第二个单元格不等于空
						if (xssfRow != null && xssfRow.getCell(0) != null
								&& !"".equals(CommonExcelUtil.getValue(xssfRow.getCell(0)))) {
							String assetCode = CommonExcelUtil.getValue(xssfRow.getCell(0));// 取出统一编号
							assetCode = StringUtils.trim(assetCode);// 去除两端空格
							Ins_Assets assets = map.get(assetCode);
							if (assets != null) {
								XSSFCell cell = xssfRow.createCell(1);// 名称
								cell.setCellValue(isNull(assets.getAssetName()));
								XSSFCell cell2 = xssfRow.createCell(2);// 型号
								cell2.setCellValue(isNull(assets.getDeviceVersion()));
								XSSFCell cell3 = xssfRow.createCell(3);// 技术指标
								cell3.setCellValue(isNull(assets.getTechnicalIndex()));
								XSSFCell cell4 = xssfRow.createCell(4);// 出厂编号
								cell4.setCellValue(isNull(assets.getSerialNumber()));
								XSSFCell cell5 = xssfRow.createCell(5);// 出厂日期
								cell5.setCellValue(DateUtils.converteToDay(assets.getFactoryTime(), 1));
								XSSFCell cell6 = xssfRow.createCell(6);// 启用年月
								cell6.setCellValue(DateUtils.converteToDay(assets.getEnableTime(), 1));
								XSSFCell cell7 = xssfRow.createCell(7);// 原值
								cell7.setCellValue(isNull(assets.getOriginalValue()));
								XSSFCell cell8 = xssfRow.createCell(8);// 使用单位
								cell8.setCellValue(isNull(assets.getBorrowDepart()));
								XSSFCell cell9 = xssfRow.createCell(9);// 使用人
								cell9.setCellValue(isNull(assets.getChargePerson()));
								XSSFCell cell10 = xssfRow.createCell(10);// 国别厂家
								cell10.setCellValue(isNull(assets.getCountry()) + isNull(assets.getManufactor()));
								XSSFCell cell11 = xssfRow.createCell(11);// 供货单位
								cell11.setCellValue(isNull(assets.getSupplier()));
								XSSFCell cell12 = xssfRow.createCell(12);// 开箱日期
								cell12.setCellValue(DateUtils.converteToDay(assets.getUnpackingDate(), 1));
								XSSFCell cell13 = xssfRow.createCell(13);// 折扣年限
								cell13.setCellValue(isNull(assets.getDepreciationYear()));
								XSSFCell cell14 = xssfRow.createCell(14);// 年折旧率
								cell14.setCellValue(isNull(assets.getDepreciationRate()));
								// 15 年折旧额没有
								XSSFCell cell16 = xssfRow.createCell(16);// 经费来源
								String fundSources = isNull(assets.getFundSources());
								if (StringUtils.isNotBlank(fundSources)) {
									fundSources = "0".equals(fundSources) ? "拨款" : "自购";
								}
								cell16.setCellValue(fundSources);
								// 17 属性没有
								XSSFCell cell18 = xssfRow.createCell(18);// 管理级别
								String mangeLevel = isNull(assets.getManageLevel());
								if (StringUtils.isNotBlank(mangeLevel)) {
									if ("0".equals(mangeLevel)) {
										mangeLevel = "厂(所)管";
									} else if ("1".equals(mangeLevel)) {
										mangeLevel = "院管";
									} else {
										mangeLevel = "总公司部管";
									}
								}
								cell18.setCellValue(mangeLevel);

								XSSFCell cell19 = xssfRow.createCell(19);// 验收人
								cell19.setCellValue(isNull(assets.getChecker()));
								XSSFCell cell20 = xssfRow.createCell(20);// 备注-附件
								cell20.setCellValue(isNull(assets.getRemark()));
							} else {
								// 不存在的统一资产
								resultMap.put("error", sb.append("第" + (rowNum + 1) + "行,该资产统一编号不存在，请删除后使用" + "<br/>"));
								continue;
							}

						}
					}
				}
			}
			out = new FileOutputStream(path);
			xssfWorkbook.write(out);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (null != out) {
					out.close();
				}
				if (null != is) {
					is.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		long endTime = new Date().getTime();
		System.out.println("用时:======" + (endTime - startTime));
		return resultMap;
	}

	public String isNull(Object value) {
		String str = "";
		if (null != value) {
			str += value;
		}
		return str;

	}
}
