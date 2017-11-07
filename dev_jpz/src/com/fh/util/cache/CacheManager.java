package com.fh.util.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.fh.util.Logger;
import com.fh.wechat.gongzhong.GongZhongService;

public class CacheManager
{
	protected static Logger log = Logger.getLogger(CacheManager.class);
  static HashMap<String, Object> cacheMap = new HashMap();

  public static Cache getCacheInfo(String key)
  {
    if (hasCache(key)) {
      Cache cache = getCache(key);
      if (cacheExpired(cache)) {
        cache.setExpired(true);
      }

      return cache;
    }
    clearByKey(key);
    return null;
  }

  public static boolean getSimpleFlag(String key)
  {
    try
    {
      return ((Boolean)cacheMap.get(key)).booleanValue(); } catch (NullPointerException e) {
    }
    return false;
  }

  public static long getServerStartdt(String key)
  {
    try {
      return ((Long)cacheMap.get(key)).longValue(); } catch (Exception ex) {
    }
    return 0L;
  }

  public static synchronized boolean setSimpleFlag(String key, boolean flag)
  {
    if ((flag) && (getSimpleFlag(key)))
      return false;

    cacheMap.put(key, Boolean.valueOf(flag));
    return true;
  }

  public static synchronized boolean setSimpleFlag(String key, long serverbegrundt)
  {
    if (cacheMap.get(key) == null) {
      cacheMap.put(key, Long.valueOf(serverbegrundt));

      return true;
    }
    return false;
  }

  private static synchronized Cache getCache(String key)
  {
    return ((Cache)cacheMap.get(key));
  }

  private static synchronized boolean hasCache(String key)
  {
    return cacheMap.containsKey(key);
  }

  public static synchronized void clearAll()
  {
    cacheMap.clear();
  }

  public static synchronized void clearByKey(String key)
  {
    cacheMap.remove(key);
  }

  public static synchronized void clearStartsWithAll(String type)
  {
    Iterator i = cacheMap.entrySet().iterator();

    List arr = new ArrayList();
    try
    {
      Map.Entry entry = null;

      while (i.hasNext()) {
        entry = (Map.Entry)i.next();
        String key = (String)entry.getKey();

        if (key.startsWith(type)) {
          arr.add(key);
        }

        entry = null;
      }

      int listSize = arr.size();

      for (int k = 0; k < listSize; ++k)
        clearByKey((String)arr.get(k));
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  public static synchronized void putCache(String key, Cache obj)
  {
    cacheMap.put(key, obj);
  }

  public static void putCacheInfo(String key, Cache obj, long dt, boolean expired)
  {
    obj.setTimeOut(dt +System.currentTimeMillis());
    obj.setExpired(expired);
    cacheMap.put(key, obj);
  }

  public static void putCacheInfo(String key, Cache obj, long dt)
  {
    obj.setKey(key);
    long nowdt = System.currentTimeMillis();
    long TimeOut = nowdt+dt;
    obj.setTimeOut(TimeOut);
    obj.setExpired(false);
    cacheMap.put(key, obj);
  }

  public static boolean cacheExpired(Cache cache)
  {
	  if (cache == null) {
	      return false;
	    }
	    long nowDt = System.currentTimeMillis();
	    long cacheDt = cache.getTimeOut();
	    return ((cacheDt > 0L) && (cacheDt <= nowDt));
  }

  public static int getCacheSize()
  {
    return cacheMap.size();
  }

  public static int getCacheSize(String type)
  {
    int k = 0;
    Iterator i = cacheMap.entrySet().iterator();
    try
    {
      Map.Entry entry = null;

      while (i.hasNext()) {
        entry = (Map.Entry)i.next();
        String key = (String)entry.getKey();

        if (key.indexOf(type) != -1) {
          ++k;
        }

        entry = null;
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    }

    return k;
  }

  public static ArrayList<String> getCacheAllkey()
  {
    ArrayList a = new ArrayList();
    try
    {
      Iterator i = cacheMap.entrySet().iterator();
      Map.Entry entry = null;

      while (i.hasNext()) {
        entry = (Map.Entry)i.next();
        a.add((String)entry.getKey());
        entry = null;
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    }

    return a;
  }

  public static ArrayList<String> getCacheListkey(String type)
  {
    ArrayList a = new ArrayList();
    try
    {
      Iterator i = cacheMap.entrySet().iterator();
      Map.Entry entry = null;

      while (i.hasNext()) {
        entry = (Map.Entry)i.next();
        String key = (String)entry.getKey();

        if (key.indexOf(type) != -1) {
          a.add(key);
        }

        entry = null;
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    }

    return a;
  }
}
