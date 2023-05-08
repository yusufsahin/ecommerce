package net.provera.orderserv.event;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;

public enum OrderEventType {
    @JsonEnumDefaultValue
    CREATED,
    UPDATED,
    DELETED
}
