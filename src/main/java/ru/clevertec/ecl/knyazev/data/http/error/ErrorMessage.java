package ru.clevertec.ecl.knyazev.data.http.error;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorMessage {
    private static final String UNKNOWN_ERROR = "UNKNOWN ERROR";

    private int statusCode;
    private Date timestamp;
    private String message;

    public static String defaultError() {
        return UNKNOWN_ERROR;
    }
}
