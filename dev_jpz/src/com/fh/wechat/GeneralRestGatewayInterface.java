package com.fh.wechat;

import java.util.Map;

public abstract interface GeneralRestGatewayInterface
{
  public abstract String delegateHandleRequest(Map<String, String> paramMap, StringBuilder paramStringBuilder)
    throws RuntimeException;
}
