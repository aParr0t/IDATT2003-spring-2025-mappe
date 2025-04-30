package edu.ntnu.iir.bidata.filehandling;

import com.google.gson.*;
import edu.ntnu.iir.bidata.model.tileaction.*;

import java.lang.reflect.Type;

/**
 * Gson adapter for serializing and deserializing TileAction objects.
 */
public class TileActionAdapter implements JsonSerializer<TileAction>, JsonDeserializer<TileAction> {
    private static final String TYPE_FIELD = "type";
    private static final String MOVE_ACTION = "MoveAction";
    private static final String GO_TO_JAIL_ACTION = "GoToJailAction";
    private static final String JAIL_ACTION = "JailAction";
    private static final String CHANCE_ACTION = "ChanceAction";
    private static final String INCOME_TAX_ACTION = "IncomeTaxAction";
    private static final String TREASURY_ACTION = "TreasuryAction";

    @Override
    public JsonElement serialize(TileAction src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject result = new JsonObject();
        
        if (src instanceof MoveAction) {
            result.addProperty(TYPE_FIELD, MOVE_ACTION);
            result.addProperty("start", ((MoveAction) src).getStart());
            result.addProperty("end", ((MoveAction) src).getEnd());
        } else if (src instanceof GoToJailAction) {
            result.addProperty(TYPE_FIELD, GO_TO_JAIL_ACTION);
        } else if (src instanceof JailAction) {
            result.addProperty(TYPE_FIELD, JAIL_ACTION);
        } else if (src instanceof ChanceAction) {
            result.addProperty(TYPE_FIELD, CHANCE_ACTION);
        } else if (src instanceof IncomeTaxAction) {
            result.addProperty(TYPE_FIELD, INCOME_TAX_ACTION);
        } else if (src instanceof TreasuryAction) {
            result.addProperty(TYPE_FIELD, TREASURY_ACTION);
        } else {
            throw new JsonParseException("Unknown TileAction type: " + src.getClass().getName());
        }
        
        return result;
    }

    @Override
    public TileAction deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        
        // Check if the type field exists
        if (!jsonObject.has(TYPE_FIELD)) {
            // If no type field, try to determine the type based on available fields
            if (jsonObject.has("start") && jsonObject.has("end")) {
                int start = jsonObject.get("start").getAsInt();
                int end = jsonObject.get("end").getAsInt();
                return new MoveAction(start, end);
            }
            throw new JsonParseException("Missing type field and could not determine TileAction type");
        }
        
        String type = jsonObject.get(TYPE_FIELD).getAsString();
        
        switch (type) {
            case MOVE_ACTION:
                int start = jsonObject.get("start").getAsInt();
                int end = jsonObject.get("end").getAsInt();
                return new MoveAction(start, end);
            case GO_TO_JAIL_ACTION:
                return new GoToJailAction();
            case JAIL_ACTION:
                return new JailAction();
            case CHANCE_ACTION:
                return new ChanceAction();
            case INCOME_TAX_ACTION:
                return new IncomeTaxAction();
            case TREASURY_ACTION:
                return new TreasuryAction();
            default:
                throw new JsonParseException("Unknown TileAction type: " + type);
        }
    }
}
