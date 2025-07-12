package com.pm.campaign_service.util;

import com.pm.campaign_service.exception.InvalidUuidException;

import java.util.UUID;
import java.util.regex.Pattern;

public class UuidUtil {
    private static final Pattern UUID_REGEX =
            Pattern.compile("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$");

    public static boolean isValidUuid(String value) {
        return value != null && UUID_REGEX.matcher(value).matches();
    }

    public static UUID parseUuidOrThrow(String value) {
        if (!isValidUuid(value)) {
            throw new InvalidUuidException("Invalid UUID format: " + value);
        }
        return UUID.fromString(value);
    }
}
