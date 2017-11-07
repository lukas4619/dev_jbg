package com.fh.util;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class _String
{
  public static String[] Split(String input, int partLength)
  {
    return split(input, partLength, 0);
  }

  public static String[] split(String input, int partLength, int maxPartNum)
  {
    if ((input == null) || (input.isEmpty())) {
      return null;
    }

    List list = new ArrayList();
    List list_length = new ArrayList();

    list.add("");
    list_length.add(Integer.valueOf(0));

    int locate = 0;

    for (int i = 0; i < input.length(); ++i) {
      String ch = input.substring(i, i + 1);
      int len = ch.getBytes(Charset.defaultCharset()).length;

      if (((Integer)list_length.get(locate)).intValue() + len > partLength) {
        ++locate;

        if ((maxPartNum > 0) && (locate >= maxPartNum)) {
          break;
        }

        list.add("");
        list_length.add(Integer.valueOf(0));
      }

      list.set(locate, ((String)list.get(locate)) + ch);
      list_length.set(locate, Integer.valueOf(((Integer)list_length.get(locate)).intValue() + len));
    }

    String[] result = new String[list.size()];

    for (int i = 0; i < list.size(); ++i) {
      result[i] = ((String)list.get(i));
    }

    return result;
  }

  public static int getLength(String input)
  {
    byte[] bytes = input.getBytes(Charset.forName("ASCII"));
    int result = 0;

    for (int i = 0; i <= bytes.length - 1; ++i) {
      if (bytes[i] == 63)
      {
        ++result;
      }

      ++result;
    }

    return result;
  }

  public static boolean isChineseCharacters(char ch)
  {
    int unicode = ch - 19968;
    return ((unicode >= 0) && (unicode <= 20900));
  }

  public static boolean isDBCCharacters(char ch)
  {
    int unicode = ch;
    return ((unicode == 12288) || ((unicode > 65280) && (unicode < 65375)));
  }

  public static String escape(String src)
  {
    StringBuffer tmp = new StringBuffer();
    tmp.ensureCapacity(src.length() * 6);

    for (int i = 0; i < src.length(); ++i) {
      char j = src.charAt(i);

      if ((Character.isDigit(j)) || (Character.isLowerCase(j)) || (Character.isUpperCase(j))) {
        tmp.append(j);
      }
      else if (j < 256) {
        tmp.append("%");

        if (j < '\16') {
          tmp.append("0");
        }

        tmp.append(Integer.toString(j, 16));
      } else {
        tmp.append("%u");
        tmp.append(Integer.toString(j, 16));
      }
    }

    return tmp.toString();
  }

  public static String unescape(String src)
  {
    StringBuffer tmp = new StringBuffer();
    tmp.ensureCapacity(src.length());
    int lastPos = 0; int pos = 0;

    while (lastPos < src.length()) {
      pos = src.indexOf("%", lastPos);

      if (pos == lastPos) {
        char ch;
        if (src.charAt(pos + 1) == 'u') {
          ch = (char)Integer.parseInt(src.substring(pos + 2, pos + 6), 16);
          tmp.append(ch);
          lastPos = pos + 6;
        } else {
          ch = (char)Integer.parseInt(src.substring(pos + 1, pos + 3), 16);
          tmp.append(ch);
          lastPos = pos + 3;
        }
      }
      else if (pos == -1) {
        tmp.append(src.substring(lastPos));
        lastPos = src.length();
      } else {
        tmp.append(src.substring(lastPos, pos));
        lastPos = pos;
      }

    }

    return tmp.toString();
  }
}
