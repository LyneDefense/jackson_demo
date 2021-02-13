package com.pinjhu;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

public class StreamingDemo {

  JsonFactory factory = new JsonFactory();

  /** 反序列化测试(JSON -> Object)，入参是JSON字符串 */
  public TwitterEntry deserializeJSONStr(String json) throws IOException {

    JsonParser jsonParser = factory.createParser(json);

    if (jsonParser.nextToken() != JsonToken.START_OBJECT) {
      jsonParser.close();
      System.out.println("起始位置没有大括号");
      throw new IOException("起始位置没有大括号");
    }

    TwitterEntry result = new TwitterEntry();

    try {
      while (jsonParser.nextToken() != JsonToken.END_OBJECT) {
        // 字段名
        String fieldName = jsonParser.getCurrentName();
        System.out.println("正在解析字段 [{}]" + jsonParser.getCurrentName());
        // 解析下一个
        jsonParser.nextToken();

        switch (fieldName) {
          case "id":
            result.setId(jsonParser.getLongValue());
            break;
          case "text":
            result.setText(jsonParser.getText());
            break;
          case "fromUserId":
            result.setFromUserId(jsonParser.getIntValue());
            break;
          case "toUserId":
            result.setToUserId(jsonParser.getIntValue());
            break;
          case "languageCode":
            result.setLanguageCode(jsonParser.getText());
            break;
          default:
            System.out.println("未知字段 '" + fieldName + "'");
            throw new IOException("未知字段 '" + fieldName + "'");
        }
      }
    } catch (IOException e) {
      System.out.println("反序列化出现异常 :" + e.getMessage());
    } finally {
      jsonParser.close(); // important to close both parser and underlying File reader
    }
    return result;
  }

  public String serialize(TwitterEntry twitterEntry) throws IOException {

    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    JsonGenerator jsonGenerator = factory.createGenerator(byteArrayOutputStream, JsonEncoding.UTF8);

    try {
      jsonGenerator.useDefaultPrettyPrinter();

      jsonGenerator.writeStartObject();
      jsonGenerator.writeNumberField("id", twitterEntry.getId());
      jsonGenerator.writeStringField("text", twitterEntry.getText());
      jsonGenerator.writeNumberField("fromUserId", twitterEntry.getFromUserId());
      jsonGenerator.writeNumberField("toUserId", twitterEntry.getToUserId());
      jsonGenerator.writeStringField("languageCode", twitterEntry.getLanguageCode());
      jsonGenerator.writeEndObject();
    } catch (IOException e) {
      System.out.println("序列化出现异常 :" + e.getMessage());
    } finally {
      jsonGenerator.close();
    }

    // 一定要在

    return byteArrayOutputStream.toString();
  }

  public static void main(String[] args) throws Exception {

    StreamingDemo streamingDemo = new StreamingDemo();

    // 执行一次对象转JSON操作
    System.out.println("********************执行一次对象转JSON操作********************");
    String serializeResult = streamingDemo.serialize(Constant.TEST_OBJECT);
    System.out.println("********************执行一次对象转JSON操作********************");
    System.out.println(serializeResult);

    // 用本地字符串执行一次JSON转对象操作
    System.out.println("********************执行一次本地JSON反序列化操作********************");
    TwitterEntry deserializeResult = streamingDemo.deserializeJSONStr(Constant.TEST_JSON_STR);
    String deserializeStr = String.format("\n本地JSON反序列化结果是个java实例 : \n%s\n\n", deserializeResult.toString());
    System.out.println(deserializeStr);
  }
}
