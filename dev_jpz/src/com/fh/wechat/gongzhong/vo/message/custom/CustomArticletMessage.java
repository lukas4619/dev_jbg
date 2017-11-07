package com.fh.wechat.gongzhong.vo.message.custom;

import java.util.ArrayList;
import java.util.List;

import com.fh.wechat.gongzhong.GongZhongObject;
import com.fh.wechat.gongzhong.vo.message.Article;

/**
 * @describe 客服回复图文消息实体类
 * @author lukas Lukas 414024003@qq.com
 * @date 2016年8月13日 10:56:14
 * @version 1.0
 */
public class CustomArticletMessage extends GongZhongObject {
	private String toUser;
	public String msgType = "news";
	private List<Article> articles = new ArrayList();

	public String getToUser() {
		return this.toUser;
	}

	public void setToUser(String toUser) {
		this.toUser = toUser;
	}

	public List<Article> getArticles() {
		return this.articles;
	}

	public void addArticles(Article article) {
		if (this.articles.size() > 10)
			throw new RuntimeException("图文消息个数，限制为10条以内");

		this.articles.add(article);
	}
}
