package com.fh.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * 字符串相关方法
 *
 */
public class StringUtil {

	/**
	 * 将以逗号分隔的字符串转换成字符串数组
	 * @param valStr
	 * @return String[]
	 */
	public static String[] StrList(String valStr){
	    int i = 0;
	    String TempStr = valStr;
	    String[] returnStr = new String[valStr.length() + 1 - TempStr.replace(",", "").length()];
	    valStr = valStr + ",";
	    while (valStr.indexOf(',') > 0)
	    {
	        returnStr[i] = valStr.substring(0, valStr.indexOf(','));
	        valStr = valStr.substring(valStr.indexOf(',')+1 , valStr.length());
	        
	        i++;
	    }
	    return returnStr;
	}
	
	/**
	 * 将以逗号分隔的字符串过滤重复
	 * @param valStr
	 * @return String[]
	 */
	public static String StrLists(String valStr){
		if(valStr.endsWith(",")){
			valStr=valStr.substring(0,valStr.length()-1);
		}
		String[] array = valStr.split(",");
		List<String> list = new ArrayList<>();
		list.add(array[0]);
		for(int i=1;i<array.length;i++){
			if(list.toString().indexOf(array[i]) == -1){
					list.add(array[i]);
			}
		}
		String[] arrayResult = (String[]) list.toArray(new String[list.size()]);
		String result ="";
		if(arrayResult.length>0){
			for (String S : arrayResult) {
				result+=S+",";
			}
		}
		if(result.endsWith(",")){
			result=result.substring(0,result.length()-1);
		}
	    return result;
	}
	
	/**获取字符串编码
	 * @param str
	 * @return
	 */
	public static String getEncoding(String str) {      
	       String encode = "GB2312";      
	      try {      
	          if (str.equals(new String(str.getBytes(encode), encode))) {      
	               String s = encode;      
	              return s;      
	           }      
	       } catch (Exception exception) {      
	       }      
	       encode = "ISO-8859-1";      
	      try {      
	          if (str.equals(new String(str.getBytes(encode), encode))) {      
	               String s1 = encode;      
	              return s1;      
	           }      
	       } catch (Exception exception1) {      
	       }      
	       encode = "UTF-8";      
	      try {      
	          if (str.equals(new String(str.getBytes(encode), encode))) {      
	               String s2 = encode;      
	              return s2;      
	           }      
	       } catch (Exception exception2) {      
	       }      
	       encode = "GBK";      
	      try {      
	          if (str.equals(new String(str.getBytes(encode), encode))) {      
	               String s3 = encode;      
	              return s3;      
	           }      
	       } catch (Exception exception3) {      
	       }      
	      return "";      
	   } 
	/**
	 * 判断object
	 * @param o
	 * @return
	 */
	 public static Boolean isEmpty(Object o ){
		 if(o==null){
			 return true;
		 }
		 String s = o+"";
		 if(s==null || s.length()<=0){
			 return true;
		 }
		 return false;
	 }
	 
	 /**
	  * 验证List<PageData> 是否为空
	  * @param pdList
	  * @return null 为true
	  */
	 public static boolean isPdList(List<PageData> pdList){
		 if(pdList==null || pdList.size()<=0){
			 return true;
		 }
		 return false;
	 }
	 
	 /**
	  * 判断List中是map的列是否包含指定的值
	  * @param lpd 集合
	  * @param k 指定map中的key
	  * @param v 指定的值
	  * @return true：存在 false:不存在
	  */
	 public static boolean isListHas(List<PageData> lpd,Object k,Object v){
		 boolean result = false;
		 for (Iterator iterator = lpd.iterator(); iterator.hasNext();) {
			PageData pageData = (PageData) iterator.next();
			if(pageData.get(k).equals(v)){
				result = true;
				break;
			}
		}
		 return result;
	 }
	 
	 
}
