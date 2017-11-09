package com.fh.test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import sun.security.krb5.internal.PAEncTSEnc;

import com.fh.util.PageData;
import com.fh.wechat.gongzhong.vo.user.UserInfo;

public class test {
	 public static void main(String[] args) {
//		 PageData pd = new PageData();
//		 pd.put("SUBSCRIBE", 0);
//		 UserInfo userInfo = new UserInfo();
//		 userInfo.setSubscribe("1");
//		 System.err.println(pd.get("SUBSCRIBE").toString().equals("0"));
//		 System.err.println(userInfo.getSubscribe().equals("1"));
//		 if(pd.get("SUBSCRIBE")+""=="0" && userInfo.getSubscribe()=="1"){
//				System.err.println("23123123123");
//		 }
		 int STOREID =1;
		 System.err.println(com.fh.util.security.Encrypt.MD5ByKey(1+""));
		 String[] keys = {"first","keyword1","keyword2", "keyword3", "keyword4","remark"};  
		 System.err.println(keys[0].toString());
	    }
	 
	 
	 public enum keys {
	     
			first, keyword1, keyword2, keyword3,keyword4,remark 
		 
		}
}
