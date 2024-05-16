package design.fiti.cool_do.data.local

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.google.gson.reflect.TypeToken
import design.fiti.cool_do.data.local.entity.TaskState
import design.fiti.cool_do.data.util.JsonParser

@ProvidedTypeConverter
class Converters(
    private val jsonParser: JsonParser
) {
    @TypeConverter
    fun fromAnyListJson(json: String): List<Any> {
        return jsonParser.fromJson<ArrayList<Any>>(
            json, object : TypeToken<ArrayList<Any>>() {}.type
        ) ?: emptyList()
    }

    @TypeConverter
    fun toAnyListJson(messages: List<Any>): String {
        return jsonParser.toJson<List<Any>>(
            messages, object : TypeToken<ArrayList<Any>>() {}.type
        ) ?: "[]"
    }

    @TypeConverter
    fun fromTaskStateJson(json: String): TaskState {
        return jsonParser.fromJson<TaskState>(
            json, object : TypeToken<TaskState>() {}.type
        ) ?: TaskState.TODO
    }

    @TypeConverter
    fun toTaskState(messages: TaskState): String {
        return jsonParser.toJson(
            messages, object : TypeToken<TaskState>() {}.type
        ) ?: ""
    }
}