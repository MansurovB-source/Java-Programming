package TypeAdaptor;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by IntelliJ IDEA.
 * Author: Behruz Mansurov
 */
public class ZonedDateTimeTypeAdaptor implements JsonSerializer<ZonedDateTime>, JsonDeserializer<ZonedDateTime> {

    @Override
    public ZonedDateTime deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        try {
            final String dateTimeString = jsonElement.getAsString();
            return ZonedDateTime.parse(dateTimeString);
        } catch (Exception e) {
            throw new JsonParseException("Failed to new instance", e);
        }

    }

    @Override
    public JsonElement serialize(ZonedDateTime zonedDateTime, Type type, JsonSerializationContext jsonSerializationContext) {
        return new JsonPrimitive(zonedDateTime.format(DateTimeFormatter.ISO_INSTANT));
    }

    public static GsonBuilder getGsonBuilder() {
        final GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(ZonedDateTime.class, new ZonedDateTimeTypeAdaptor());
        return builder;
    }
}
