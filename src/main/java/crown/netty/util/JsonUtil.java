package crown.netty.util;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * @author kuangjunlin
 */
public class JsonUtil {
    public static final Gson GSON = new Gson();
    public static String toJson(Object object) {
        return new Gson().toJson(object);
    }

    public static <T> T fromJson(String jsonStr, Class<T> Clazz) {
        return GSON.fromJson(jsonStr, Clazz);
    }

    public static JsonObject fromJson (String jsonStr) {
        return GSON.fromJson(jsonStr, JsonObject.class);
    }
}
