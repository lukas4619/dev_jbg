package com.fh.wechat.gongzhong.vo.user;

import org.apache.commons.lang.StringUtils;

import com.fh.util._String;
import com.fh.wechat.gongzhong.GongZhongObject;


/**
 * @describe 微信用户群组实体类
 * @author lukas Lukas 414024003@qq.com
 * @date 2016年8月13日 10:56:14
 * @version 1.0
 */
public class UserGroup extends GongZhongObject {
	private String id;
	private String name;
	private String count;

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		if (StringUtils.isBlank(name)) {
			throw new RuntimeException("分租名称不能为空");
		}
		if (_String.getLength(name) > 30) {
			throw new RuntimeException("分组名称不能大于30个字符");
		}

		this.name = name;
	}

	public String getCount() {
		return this.count;
	}

	public void setCount(String count) {
		this.count = count;
	}
}