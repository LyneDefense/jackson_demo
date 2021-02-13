package com.pinjhu;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class TwitterEntry {

  /**
   * 推特消息id
   */
  long id;
  /**
   * 消息内容
   */
  String text;    /**
   * 消息创建者
   */
  int fromUserId;
  /**
   * 消息接收者
   */
  int toUserId;
  /**
   * 语言类型
   */
  String languageCode;
}
