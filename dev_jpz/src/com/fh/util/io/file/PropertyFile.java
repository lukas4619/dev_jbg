package com.fh.util.io.file;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Properties;

/**
 * @describe 读取properties文件
 * @author lukas Lukas 414024003@qq.com
 * @date 2016年8月13日 10:56:14
 * @version 1.0
 */
public class PropertyFile
{
  private Properties property;
  private String filePath;

  public PropertyFile()
    throws UnsupportedEncodingException
  {
    this("config.properties");
  }

  /**
   * 构造器
   * @param propertyFileName properties文件名称，需要带后缀
   * @throws UnsupportedEncodingException
   */
  public PropertyFile(String propertyFileName)
    throws UnsupportedEncodingException
  {
    InputStream is = null;
    this.property = new Properties();
    this.filePath = URLDecoder.decode(super.getClass().getResource("/").getPath() + propertyFileName, "UTF-8");
    try
    {
      is = new FileInputStream(this.filePath);
      this.property.load(is);
    }
    catch (IOException e) {
      e.printStackTrace();
      try
      {
        is.close();
      }
      catch (IOException ex) {
        ex.printStackTrace();
      }
    }
    finally
    {
      try
      {
        is.close();
      }
      catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * 构造器
   * @param propertyFilePath properties文件路径
   * @param propertyFileName properties文件需要后缀
   * @throws UnsupportedEncodingException
   */
  public PropertyFile(String propertyFilePath, String propertyFileName)
    throws UnsupportedEncodingException
  {
    InputStream is = null;
    this.property = new Properties();
    this.filePath = URLDecoder.decode(propertyFilePath + propertyFileName, "UTF-8");
    try
    {
      is = new FileInputStream(this.filePath);
      this.property.load(is);
    }
    catch (IOException e) {
      e.printStackTrace();
      try
      {
        is.close();
      }
      catch (IOException ex) {
    	  ex.printStackTrace();
      }
    }
    finally
    {
      try
      {
        is.close();
      }
      catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * 读取指定键内容
   * @param key
   * @return
   */
  public String read(String key)
  {
    return this.property.getProperty(key);
  }

  /**
   * properties文件写入指定键值
   * @param key
   * @param value
   */
  public void write(String key, String value)
  {
    InputStream is = null;
    try
    {
      is = new FileInputStream(this.filePath);
      this.property.clear();
      this.property.load(is);
    }
    catch (IOException e) {
      e.printStackTrace();
      try
      {
        is.close();
      }
      catch (IOException ex) {
    	  ex.printStackTrace();
      }
      return;
    } finally {
      try {
        is.close();
      }
      catch (IOException e) {
        e.printStackTrace();
      }
    }

    OutputStream os = null;
    this.property.setProperty(key, value);
    try
    {
      os = new FileOutputStream(this.filePath);
      this.property.store(os, "");
      os.flush();
    } catch (Exception ex) {
    	ex.printStackTrace();
      try
      {
        os.close();
      }
      catch (IOException e) {
        e.printStackTrace();
      }
    }
    finally
    {
      try
      {
        os.close();
      }
      catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
}