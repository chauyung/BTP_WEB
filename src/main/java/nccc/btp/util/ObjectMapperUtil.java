package nccc.btp.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ObjectMapperUtil {

  private static final ObjectMapper objectMapper = new ObjectMapper();

  // 將對象轉換為JSON字串
  public static String toJson(Object object) {
      try {
          return objectMapper.writeValueAsString(object);
      } catch (JsonProcessingException e) {
          throw new RuntimeException("Failed to convert object to JSON", e);
      }
  }

  // 將JSON字串轉換為對象
  public static <T> T fromJson(String json, Class<T> clazz) {
      try {
          return objectMapper.readValue(json, clazz);
      } catch (JsonProcessingException e) {
          throw new RuntimeException("Failed to convert JSON to object", e);
      }
  }
}
