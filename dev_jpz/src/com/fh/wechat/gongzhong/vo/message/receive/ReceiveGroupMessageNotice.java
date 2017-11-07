package com.fh.wechat.gongzhong.vo.message.receive;

import com.fh.wechat.gongzhong.vo.message.Message;


/**
 * @describe 接收按微信用户群组推送消息后实体类
 * @author lukas Lukas 414024003@qq.com
 * @date 2016年8月13日 10:56:14
 * @version 1.0
 */
public class ReceiveGroupMessageNotice extends Message
{
	  public static final String EVENT_GROUPMESSAGE = "MASSSENDJOBFINISH";
	  private String event;
	  private String status;
	  private long totalCount;
	  private long filterCount;
	  private long sentCount;
	  private long errorCount;

	  public String getEvent()
	  {
	    return this.event;
	  }

	  public void setEvent(String event) {
	    this.event = event;
	  }

	  public String getStatus() {
	    return this.status;
	  }

	  public void setStatus(String status) {
	    this.status = status;
	  }

	  public long getTotalCount() {
	    return this.totalCount;
	  }

	  public void setTotalCount(long totalCount) {
	    this.totalCount = totalCount;
	  }

	  public long getFilterCount() {
	    return this.filterCount;
	  }

	  public void setFilterCount(long filterCount) {
	    this.filterCount = filterCount;
	  }

	  public long getSentCount() {
	    return this.sentCount;
	  }

	  public void setSentCount(long sentCount) {
	    this.sentCount = sentCount;
	  }

	  public long getErrorCount() {
	    return this.errorCount;
	  }

	  public void setErrorCount(long errorCount) {
	    this.errorCount = errorCount;
	  }
	}
