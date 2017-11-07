package com.fh.util.io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class File
{
  public static String read(String fileName)
    throws IOException
  {
    java.io.File file = new java.io.File(fileName);
    StringBuilder sb = new StringBuilder();
    String line = "";
    BufferedReader br = new BufferedReader(new FileReader(file));

    while ((line = br.readLine()) != null) {
      sb.append(line + "\r\n");
    }

    br.close();

    return sb.toString();
  }

  public static void write(String fileName, String content)
    throws IOException
  {
    java.io.File file = new java.io.File(fileName);
    BufferedWriter br = new BufferedWriter(new FileWriter(file));
    br.write(content);
    br.close();
  }

  public static String getExtensionName(String fileName)
  {
    if ((fileName == null) || (fileName.length() == 0)) {
      return "";
    }

    int path1 = fileName.lastIndexOf(47);
    int path2 = fileName.lastIndexOf(92);
    int path = (path1 > path2) ? path1 : path2;

    fileName = fileName.substring(path + 1);

    if ((fileName == null) || (fileName.length() == 0)) {
      return "";
    }

    int dot = fileName.lastIndexOf(46);

    if ((dot > -1) && (dot < fileName.length() - 1))
    {
      return fileName.substring(dot);
    }

    return "";
  }

  public static String getFileNameWithoutExt(String fileName)
  {
    if ((fileName == null) || (fileName.length() == 0)) {
      return "";
    }

    int path1 = fileName.lastIndexOf(47);
    int path2 = fileName.lastIndexOf(92);
    int path = (path1 > path2) ? path1 : path2;

    fileName = fileName.substring(path + 1);

    if ((fileName == null) || (fileName.length() == 0)) {
      return "";
    }

    int dot = fileName.lastIndexOf(46);

    if ((dot > -1) && (dot < fileName.length())) {
      return fileName.substring(0, dot);
    }

    return "";
  }
}
