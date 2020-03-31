package zbi.trunkstrop.core.network

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.format.DateTimeFormatter.ISO_LOCAL_DATE
import org.threeten.bp.format.DateTimeFormatter.ISO_LOCAL_TIME
import org.threeten.bp.format.DateTimeFormatterBuilder
import org.threeten.bp.format.DateTimeParseException
import java.util.*

private val PRIMARY_FORMAT = DateTimeFormatter.ISO_OFFSET_DATE_TIME

private val WP_FORMAT = DateTimeFormatterBuilder()
    .parseCaseInsensitive()
    .append(ISO_LOCAL_DATE)
    .appendLiteral(' ')
    .append(ISO_LOCAL_TIME)
    .toFormatter(Locale.US)

private val LOCAL_DATE_TIME_FORMAT = DateTimeFormatterBuilder()
    .appendOptional(WP_FORMAT)
    .appendOptional(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
    .toFormatter()

class ZonedDateTimeAdapter {

    @ToJson
    fun toJson(dateTime: ZonedDateTime): String {
        return PRIMARY_FORMAT.format(dateTime)
    }

    @FromJson
    fun fromJson(dateTime: String): ZonedDateTime {
        return try {
            ZonedDateTime.parse(dateTime, PRIMARY_FORMAT)
        } catch (e1: DateTimeParseException) {
            try {
                parseLocalDateTime(dateTime)
            } catch (e2: DateTimeParseException) {
                ZonedDateTime.now()
            }
        }
    }

    private fun parseLocalDateTime(dateTime: String): ZonedDateTime {
        return ZonedDateTime.ofLocal(
            LocalDateTime.parse(dateTime, LOCAL_DATE_TIME_FORMAT),
            ZoneId.systemDefault(),
            null
        )
    }
}