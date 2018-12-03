package com.znzz.app.instrument.modules.service;

import java.util.List;

import com.znzz.app.instrument.modules.models.Ins_ProjectInfo;
import com.znzz.framework.base.service.BaseService;

public interface InsProjectService extends BaseService<Ins_ProjectInfo>{
	
	/**
	 * 获取所有项目编号
	 * @param code
	 * @param string 
	 * @return
	 * */
	List<String> getCodeList(String code, String string);

	/**
	 * 在资产表里查询是否存在当前项目下是否存在资产
	 * @param parseInt
	 * @return
	 */
	boolean ExistInAsset(Integer id);

	/**
	 * 查询当前项目是否存在分配时长
	 * @param id
	 * @return
	 */
	boolean ExistInDevice(Integer id);

	/**
	 * 修改时对编号校验
	 * @param id
	 * @param code
	 * @return
	 */
	List<String> checkID(String id, String code);

	List<String> getNameList(String name, String string);

	/**
	 * 批量插入项目信息
	 * @param projectList
	 */
	void insertList(List<Ins_ProjectInfo> projectList);
	/**
	 * 检查该项目是否存在表中
	 * @param code
	 * @return
	 */
	int isExist(String code) ;
	
	
	void updateProjectInfo(List<Ins_ProjectInfo> projectList);

	void saveOrUpdate(List<Ins_ProjectInfo> projectList);

	/**
	 * 获取技改项目
	 * @return
	 */
    List<Ins_ProjectInfo> getCodeAndName();

	void updateList(List<Ins_ProjectInfo> projectList);
}
