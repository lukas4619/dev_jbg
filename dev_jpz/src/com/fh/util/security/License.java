package com.fh.util.security;

import com.fh.util.Convert;
import com.fh.util.io.file.PropertyFile;
import com.fh.util.Utility;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;

public class License
{
  private static Boolean isRefreshed = Boolean.valueOf(false);
  private static Boolean domainNameAllow = Boolean.valueOf(false);
  private static Boolean webPagesAllow = Boolean.valueOf(false);
  private static Boolean adminPagesAllow = Boolean.valueOf(false);
  private static Boolean iOSAllow = Boolean.valueOf(false);
  private static Boolean andoridAllow = Boolean.valueOf(false);
  private static Boolean windowsPhoneAllow = Boolean.valueOf(false);
  private static final String key = "com.fh.security.License";

  public static void update(HttpServletRequest request, String license)
    throws UnsupportedEncodingException
  {
    PropertyFile pf = new PropertyFile();
    pf.write("com.fh.security.License", license);

    refresh(request, license);
  }

  private static void refresh(HttpServletRequest request) throws UnsupportedEncodingException {
    PropertyFile pf = new PropertyFile();
    refresh(request, pf.read("com.fh.security.License"));
  }

  public static void refresh(HttpServletRequest request, String license) {
    isRefreshed = Boolean.valueOf(true);

    domainNameAllow = Boolean.valueOf(false);
    webPagesAllow = Boolean.valueOf(false);
    adminPagesAllow = Boolean.valueOf(false);
    iOSAllow = Boolean.valueOf(false);
    andoridAllow = Boolean.valueOf(false);
    windowsPhoneAllow = Boolean.valueOf(false);
    try
    {
      license = Encrypt.decryptSES(license, "ZAQwsxCdeRFV1234");
    }
    catch (Exception e) {
      return;
    }

    if (!(license.isEmpty())) {
      String[] strs = license.split(";");

      if ((strs != null) && (strs.length >= 7)) {
        Date expiration = Convert.strToDate(strs[0], new Date());

        if (expiration.compareTo(new Date()) > 0) {
          String currentDomainName = Utility.getDomainName(request);
          domainNameAllow = Boolean.valueOf("," + strs[1] + ",".contains("," + currentDomainName + ","));

          webPagesAllow = Boolean.valueOf(strs[2].equals("1"));
          adminPagesAllow = Boolean.valueOf(strs[3].equals("1"));
          iOSAllow = Boolean.valueOf(strs[4].equals("1"));
          andoridAllow = Boolean.valueOf(strs[5].equals("1"));
          windowsPhoneAllow = Boolean.valueOf(strs[6].equals("1"));
        }
      }
    }
  }

  public static Boolean getDomainNameAllow(HttpServletRequest request) throws UnsupportedEncodingException {
    if (!(isRefreshed.booleanValue())) {
      refresh(request);
    }

    return domainNameAllow;
  }

  public static Boolean getWebPagesAllow(HttpServletRequest request) throws UnsupportedEncodingException {
    if (!(isRefreshed.booleanValue())) {
      refresh(request);
    }

    return webPagesAllow;
  }

  public static Boolean getAdminPagesAllow(HttpServletRequest request) throws UnsupportedEncodingException {
    if (!(isRefreshed.booleanValue())) {
      refresh(request);
    }

    return adminPagesAllow;
  }

  public static Boolean getiOSAllow(HttpServletRequest request) throws UnsupportedEncodingException {
    if (!(isRefreshed.booleanValue())) {
      refresh(request);
    }

    return iOSAllow;
  }

  public static Boolean getAndoridAllow(HttpServletRequest request) throws UnsupportedEncodingException {
    if (!(isRefreshed.booleanValue())) {
      refresh(request);
    }

    return andoridAllow;
  }

  public static Boolean getWindowsPhoneAllow(HttpServletRequest request) throws UnsupportedEncodingException {
    if (!(isRefreshed.booleanValue())) {
      refresh(request);
    }

    return windowsPhoneAllow;
  }
}
