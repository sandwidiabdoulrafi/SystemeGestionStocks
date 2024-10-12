package util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class UtilitaireDate {
    private static final DateTimeFormatter FORMAT_DATE_HEURE = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public static LocalDateTime stringVersLocalDateTime(String dateHeureString) throws DateTimeParseException {
        return LocalDateTime.parse(dateHeureString, FORMAT_DATE_HEURE);
    }

    public static String localDateTimeVersString(LocalDateTime dateHeure) {
        return dateHeure.format(FORMAT_DATE_HEURE);
    }
}
