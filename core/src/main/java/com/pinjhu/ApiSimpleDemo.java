package com.pinjhu;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ApiSimpleDemo {

  public static void main(String[] args) throws IOException {

    ObjectMapper objectMapper = new ObjectMapper();

    // objectMapper序列化和反序列化时设置的一些属性
    /*******************************************************************************
     * // 序列化的结果格式化
     * objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
     * // 空对象不要抛出异常
     * objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
     * // 遇到时间不要以时间戳的形式序列化(也可以自己设置dateformat)
     * objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
     * // 反序列化时候遇到未知属性不抛错
     * objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
     * // 反序列化时空字符串对应的实例属性为null
     * objectMapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
     * // 允许C和C++样式注释
     * objectMapper.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
     * // 允许字段名没有引号（可以进一步减小json体积
     * objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
     * // 允许单引号
     * objectMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
     *************************************************************************************/

    System.out.println("以下是序列化操作");

    // 对象 -> 字符串
    TwitterEntry twitterEntry = new TwitterEntry();
    twitterEntry.setId(123456L);
    twitterEntry.setFromUserId(101);
    twitterEntry.setToUserId(102);
    twitterEntry.setText("this is a message for serializer test");
    twitterEntry.setLanguageCode("zh");

    String jsonValueStr = objectMapper.writeValueAsString(twitterEntry);
    System.out.println("序列化的字符串：" + jsonValueStr);
    // 对象 -> 文件
    objectMapper.writeValue(new File("twitter.json"), twitterEntry);
    // 对象 -> 字节数组
    byte[] array = objectMapper.writeValueAsBytes(twitterEntry);

    System.out.println("以下是反序列化操作");

    TwitterEntry deserializedEntry1 = objectMapper.readValue(jsonValueStr, TwitterEntry.class);
    System.out.println("反序列化结果1：" + deserializedEntry1);
    TwitterEntry deserializedEntry2 =
        objectMapper.readValue(new File("twitter.json"), TwitterEntry.class);
    System.out.println("反序列化结果2：" + deserializedEntry2);
    TwitterEntry deserializedEntry3 = objectMapper.readValue(array, TwitterEntry.class);
    System.out.println("反序列化结果3：" + deserializedEntry3);

    Map<String, Object> map = new HashMap<>();
    map.put("name", "tom");
    map.put("age", 11);

    Map<String, String> addr = new HashMap<>();
    addr.put("city", "深圳");
    addr.put("street", "粤海");

    map.put("addr", addr);
    String mapToString = objectMapper.writeValueAsString(map);
    System.out.println("HashMap序列化的字符串：" + mapToString);

    Map<String, Object> stringToMap =
        objectMapper.readValue(mapToString, new TypeReference<Map<String, Object>>() {});
    System.out.println("从字符串反序列化的HashMap对象：" + stringToMap);

    JsonNode jsonNode = objectMapper.readTree(mapToString);
    String name = jsonNode.get("name").asText();
    String city = jsonNode.get("addr").get("city").asText();
    System.out.println("name: " + name + ", city: " + city);

    // 时间序列化
    Map<String, Object> dateMap = new HashMap<>();
    dateMap.put("today", new Date());
    String defaultDate = objectMapper.writeValueAsString(dateMap);
    System.out.println("默认的时间序列化结果 " + defaultDate);
    objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"));
    String formattedDate = objectMapper.writeValueAsString(dateMap);
    System.out.println("格式化的时间序列化结果 " + formattedDate);

    // json数组
    String jsonArrayStr =
        "[{\n"
            + "  \"id\":1,\n"
            + "  \"text\":\"text1\",\n"
            + "  \"fromUserId\":11, \n"
            + "  \"toUserId\":111,\n"
            + "  \"languageCode\":\"en\"\n"
            + "},\n"
            + "{\n"
            + "  \"id\":2,\n"
            + "  \"text\":\"text2\",\n"
            + "  \"fromUserId\":22, \n"
            + "  \"toUserId\":222,\n"
            + "  \"languageCode\":\"zh\"\n"
            + "},\n"
            + "{\n"
            + "  \"id\":3,\n"
            + "  \"text\":\"text3\",\n"
            + "  \"fromUserId\":33, \n"
            + "  \"toUserId\":333,\n"
            + "  \"languageCode\":\"en\"\n"
            + "}]";

    TwitterEntry[] entryArray = objectMapper.readValue(jsonArrayStr, TwitterEntry[].class);
    System.out.println(("json数组反序列化成对象数组:" + Arrays.toString(entryArray)));
    String arrayObjectToStr = objectMapper.writeValueAsString(entryArray);
    System.out.println(("对象数组序列化成字符串:" + arrayObjectToStr));
  }
}
