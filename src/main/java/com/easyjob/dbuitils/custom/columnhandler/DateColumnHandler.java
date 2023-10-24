package com.easyjob.dbuitils.custom.columnhandler;

import org.apache.commons.dbutils.ColumnHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

public class DateColumnHandler implements ColumnHandler<Date> {

    @Override
    public Date apply(ResultSet resultSet, int columnIndex) throws SQLException {
        //java.time.LocalDateTime to Date;
        LocalDateTime dateTime = (LocalDateTime) resultSet.getObject(columnIndex);
        ZonedDateTime zonedDateTime = dateTime.atZone(ZoneId.systemDefault());
        Instant instant = zonedDateTime.toInstant();
        return Date.from(instant);
    }

    @Override
    public boolean match(Class<?> propType) {
        return Date.class.isAssignableFrom(propType);
    }
}
