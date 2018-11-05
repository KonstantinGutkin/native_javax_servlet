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

    public List<Part> getParts(PartFilterDto partFilterDto) throws SQLException {
        String query = "SELECT * FROM part\n";
        QueryCondition queryCondition = getConditions(partFilterDto);
        String conditions = queryCondition.createConjuctionConditions();
        if (!conditions.isEmpty()) {
            query += "WHERE " + conditions;
        }
        query += getOrderClause(partFilterDto);
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        queryCondition.addConditionValues(preparedStatement);
        return mapToParts(preparedStatement.executeQuery());
    }

    private String getOrderClause(SortDto sortDto) {
        if (sortDto.getSortField() != null) {
            // fixme SQLi
            return "order by " + sortDto.getSortField() + " " + sortDto.getSortOrder();
        }
        return "";
    }


    private QueryCondition getConditions(PartFilterDto partFilterDto) {
        QueryCondition queryCondition = new QueryCondition();
        queryCondition.add("name LIKE ?", partFilterDto.getName());
        queryCondition.add("number LIKE ?", partFilterDto.getNumber());
        queryCondition.add("vendor LIKE ?", partFilterDto.getVendor());
        queryCondition.add("qty >= ?", partFilterDto.getQty());
        queryCondition.add("received > ?", partFilterDto.getReceivedAfter());
        queryCondition.add("received < ?", partFilterDto.getReceivedBefore());
        queryCondition.add("shipped > ?", partFilterDto.getShippedAfter());
        queryCondition.add("shipped < ?", partFilterDto.getShippedBefore());
        return queryCondition;
    }

    private class QueryCondition {

        private List<String> params = new ArrayList<>();
        private List<Object> values = new ArrayList<>();

        public void add(String param, Object value) {
            if (param != null && value != null) {
                params.add(param);
                values.add(value);
            }
        }

        public String createConjuctionConditions() {
            return String.join(" AND ", params);
        }

        public PreparedStatement addConditionValues(PreparedStatement preparedStatement) throws SQLException {
            int i = 1;
            // fixme SQLi
            for (Object value : values) {
                if (value instanceof String) {
                    preparedStatement.setString(i++, "%" + value + "%");
                } else if (value instanceof Integer) {
                    preparedStatement.setInt(i++, (Integer) value);
                } else if (value instanceof Date) {
                    preparedStatement.setDate(i++, new java.sql.Date(((Date) value).getTime()));
                }
            }
            return preparedStatement;
        }

    }
}
