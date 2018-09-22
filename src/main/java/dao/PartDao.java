package dao;

import config.JdbcDataSource;
import domain.Part;
import dto.PartFilterDto;

import dto.SortDto;
import lombok.Getter;
import lombok.SneakyThrows;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PartDao {

    private Connection connection;

    public PartDao() {
        connection = JdbcDataSource.getInstance().getConnection();
    }

    @SneakyThrows
    public List<Part> getParts() {
        return mapToParts(connection.prepareStatement("SELECT * FROM part").executeQuery());
    }

    private List<Part> mapToParts(ResultSet resultSet) throws SQLException {
        List<Part> parts = new ArrayList<>();
        while (resultSet.next()) {
            Part part = new Part();
            part.setId(resultSet.getLong("id"));
            part.setName(resultSet.getString("name"));
            part.setNumber(resultSet.getString("number"));
            part.setVendor(resultSet.getString("vendor"));
            part.setQty(resultSet.getInt("qty"));
            part.setShipped(resultSet.getDate("shipped"));
            part.setReceived(resultSet.getDate("received"));
            parts.add(part);
        }
        return parts;
    }

    public List<Part> getParts(PartFilterDto partFilterDto, SortDto sortDto) throws SQLException {
        String query = "SELECT * FROM part\n";
        ConditionQueryParams conditions = getConditions(partFilterDto);
        if (!conditions.getQuery().isEmpty()) {
            query += "WHERE " + conditions.getQuery();
        }
        query += getOrderClause(sortDto);
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        addParams(preparedStatement, conditions.getParams());
        return mapToParts(preparedStatement.executeQuery());
    }

    private String getOrderClause(SortDto sortDto) {
        if (sortDto.getField() != null) {
            return "order by " + sortDto.getField() + " " + sortDto.getOrder();
        }
        return "";
    }
    private PreparedStatement addParams(PreparedStatement preparedStatement, Object[] params) throws SQLException {
        int i = 1;
        for (Object param : params) {
            if (param instanceof String) {
                preparedStatement.setString(i++, "%" + param + "%");
            } else if (param instanceof Integer) {
                preparedStatement.setInt(i++, (Integer) param);
            } else if (param instanceof Date) {
                preparedStatement.setDate(i++, new java.sql.Date(((Date) param).getTime()));
            }
        }
        return preparedStatement;
    }

    private ConditionQueryParams getConditions(PartFilterDto partFilterDto) {
        int length = partFilterDto.getClass().getDeclaredFields().length;
        List<String> conditions = new ArrayList<>(length);
        Object[] params = new Object[length];
        int i = 0;

        if (partFilterDto.getName() != null) {
            conditions.add("name LIKE ?");
            params[i++] = partFilterDto.getName();
        }
        if (partFilterDto.getNumber() != null) {
            conditions.add("number LIKE ?");
            params[i++] = partFilterDto.getNumber();
        }
        if (partFilterDto.getVendor() != null) {
            conditions.add("vendor LIKE ?");
            params[i++] = partFilterDto.getVendor();
        }
        if (partFilterDto.getQty() != null) {
            conditions.add("qty >= ?");
            params[i++] = partFilterDto.getQty();
        }
        if (partFilterDto.getReceivedAfter() != null) {
            conditions.add("received > ?");
            params[i++] = partFilterDto.getReceivedAfter();
        }
        if (partFilterDto.getReceivedBefore() != null) {
            conditions.add("received < ?");
            params[i++] = partFilterDto.getReceivedBefore();
        }
        if (partFilterDto.getShippedAfter() != null) {
            conditions.add("shipped > ?");
            params[i++] = partFilterDto.getShippedAfter();
        }
        if (partFilterDto.getShippedBefore() != null) {
            conditions.add("shipped < ?");
            params[i] = partFilterDto.getShippedBefore();
        }
        return ConditionQueryParams.of(String.join(" AND ", conditions), params);
    }


    @Getter
    private static class ConditionQueryParams {

        private String query;
        private Object[] params;

        private ConditionQueryParams(String query, Object[] params) {
            this.query = query;
            this.params = params;
        }

        public static ConditionQueryParams of(String query, Object[] params) {
            return new ConditionQueryParams(query, params);
        }

    }
}
