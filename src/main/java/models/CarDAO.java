package models;

import daos.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.function.BiFunction;

public class CarDAO extends DAO<Car> {

    Map<String, BiFunction<Integer, ResultSet, ?>> funcyionMap;
    List<String> columnNames;
    public CarDAO() {
        super("cars", ConnectionType.MYSQL);

        columnNames = new ArrayList<>(Arrays.asList(getIdColumnName()));
        columnNames.addAll(getColumnNames());

        BiFunction<Integer, ResultSet, String> getStringFn = (intVal, resultSet) -> {
            String value = "";
            try {
                value =  resultSet.getString(intVal);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return value;
        };
        BiFunction<Integer, ResultSet, Integer> getIntegerFn = (intVal, resultSet) -> {
            Integer value = 0;
            try {
                value =  resultSet.getInt(intVal);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return value;
        };
        List<BiFunction<Integer, ResultSet, ?>> functionList =
                Arrays.asList(getIntegerFn, getStringFn, getStringFn, getIntegerFn, getStringFn, getStringFn);

        funcyionMap = new HashMap<>();
        for (int i = 0; i < columnNames.size(); i++) {
            funcyionMap.put(columnNames.get(i), functionList.get(i));
        }
    }

    public String getIdColumnName() {
        return "id";
    }

    public List<String> getColumnNames() {
        return Arrays.asList("make","model","year","color", "vin");
    }

    public void prepare(PreparedStatement preparedStmt, Car car) throws SQLException {
        preparedStmt.setString(1, car.getMake());
        preparedStmt.setString(2, car.getModel());
        preparedStmt.setInt(3, car.getYear());
        preparedStmt.setString(4, car.getColor());
        preparedStmt.setString(5, car.getVin());
    }

    @Override
    protected <R> R getColumnData(ResultSet resultSet, String columnName, int columnIdx) {

        return (R)funcyionMap.get(columnName).apply(columnIdx,resultSet);
    }

    @Override
    public Car prepareRecord(ResultSet resultSet) throws SQLException {
        Car car = new Car(resultSet.getInt(1));
        car.setMake(resultSet.getString(2));
        car.setModel(resultSet.getString(3));
        car.setYear(resultSet.getInt(4));
        car.setColor(resultSet.getString(5));
        car.setVin(resultSet.getString(6));
        return car;
    }
}
