package daos;


import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

public abstract class DAO<T extends DTO> {
    protected Connection connection = null;
    protected PreparedStatement statement = null;
    protected ResultSet resultSet = null;
    protected ConnectionType connectionType = null;
    protected String tableName;


    public DAO(String tableName, ConnectionType connectionType)  {
        this.tableName = tableName;
        this.connectionType = connectionType;
    }

    protected void establishConnection(){
        connection = DBUtils.getConnection(ConnectionType.MYSQL);
    }

    protected void close(){
        try{
            if(resultSet != null)
                resultSet.close();
            if(statement != null)
                statement.close();
            if(connection != null)
                connection.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }finally {
            clearAllresourses();
        }
    }

    private void clearAllresourses(){
            resultSet = null;
            statement = null;
            connection = null;
    }

    public void delete(T record)  {

        String sql = "DELETE FROM " + tableName +" WHERE " + getIdColumnName() +" = ?";
        try {
            establishConnection();
            statement = connection.prepareStatement(sql);
            statement.setInt(1, record.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            close();
        }
    }

    public void update(T record)  {

        List<String> columnNameSet = getColumnNames().stream()
                .map(columnName-> columnName + " = ? ")
                .collect(Collectors.toList());

        String ColumnListSQlString = String.join(",", columnNameSet.toArray(new String[columnNameSet.size()]));
        ColumnListSQlString = ColumnListSQlString.substring(0, ColumnListSQlString.length()-1);

        String sql = "UPDATE " + tableName +" SET " + ColumnListSQlString +
                "WHERE " + getIdColumnName()+ " = "+ record.getId();
        try {
            establishConnection();
            statement = connection.prepareStatement(sql);
            prepare(statement, record);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            close();
        }
    }

    public T create(T record)  {
        List<String> columnNames = getColumnNames();
        String query = " insert into "+tableName+ " ("+ String.join(",", columnNames) +")"
                + " values ("+ String.join(",", Collections.nCopies(columnNames.size(), "?"))+")";
        try {
            establishConnection();
            statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            prepare(statement, record);
            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating user failed, no rows affected.");
            }

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    record.setId(generatedKeys.getInt(1));
                }
                else {
                    throw new SQLException("Creating user failed, no ID obtained.");
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            close();
        }
        return record;
    }

    public T findById(int id)  {
        String query = "select * from "+ tableName +" where " + getIdColumnName() + " = ?" ;
        T record = null;
        try {
            establishConnection();
            statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                record = prepareRecord(resultSet);
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            close();
        }
        return record;
    }

    public int getTotalRowCount()  {
        String query = "select count(*) from "+ tableName ;
        int count = 0;
        try {
            establishConnection();
            statement = connection.prepareStatement(query);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                count = resultSet.getInt(1);
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            close();
        }
        return count;
    }

    public List<T> findAll()  {
        String query = "select * from "+ tableName ;
        List<T> recordList = new ArrayList<>();
        try {
            establishConnection();
            statement = connection.prepareStatement(query);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                T record = prepareRecord(resultSet);
                recordList.add(record);
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            close();
        }
        return recordList;
    }

    protected <R> List<R> getDataListForCloumn(String columnName){
        establishConnection();
        List<R> dataList = new ArrayList<R>();
        try {
            statement = connection.prepareStatement("SELECT "+columnName+" FROM " + tableName);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                R data = getColumnData(resultSet, columnName, 1);
                dataList.add(data);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close();
        }
        return dataList;
    }


    protected abstract T prepareRecord(ResultSet resultSet) throws SQLException;
    protected abstract String getIdColumnName();
    protected abstract List<String> getColumnNames();
    protected abstract void prepare(PreparedStatement preparedStmt, T record) throws SQLException;
    protected abstract <R> R getColumnData(ResultSet resultSet, String columnName, int columnIdx);

}
