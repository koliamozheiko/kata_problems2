package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl extends Util implements UserDao {
    private Connection connection = getConnection();
    private static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS users("
            + "id INT AUTO_INCREMENT PRIMARY KEY,"
            + "name VARCHAR(45) NOT NULL,"
            + "last_name VARCHAR(45) NOT NULL,"
            + "age INT(3) NOT NULL)";
    private static final String SQL_SAVE_REQUEST = "INSERT INTO users(name, last_name, age) VALUES(?, ?, ?)";
    private static final String SQL_GET_USERS = "SELECT * FROM users";
    private static final String SQL_REMOVE_STATEMENT = "DELETE FROM users WHERE id = ?";
    private static final String SQL_DROP_STATEMENT = "DROP TABLE IF EXISTS users";
    private static final String SQL_DELETE_STATEMENT = "DELETE FROM users";

    public UserDaoJDBCImpl() {

    }
    @Override
    public void createUsersTable() {
        try(Statement statement = connection.createStatement()) {
            statement.execute(CREATE_TABLE);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void dropUsersTable() {
        try(Statement statement = connection.createStatement()) {
            statement.executeUpdate(SQL_DROP_STATEMENT);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void saveUser(String name, String lastName, byte age) {
        try(PreparedStatement preparedStatement = connection.prepareStatement(SQL_SAVE_REQUEST)) {
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, lastName);
            preparedStatement.setByte(3, age);
            preparedStatement.executeUpdate();
            System.out.printf("User с именем – %s добавлен в базу данных\n", name);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void removeUserById(long id) {
        try(PreparedStatement preparedStatement = connection.prepareStatement(SQL_REMOVE_STATEMENT)) {
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @Override
    public List<User> getAllUsers() {
        List<User> listOfUsers = new ArrayList<>();
        try(PreparedStatement preparedStatement = connection.prepareStatement(SQL_GET_USERS)) {
            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next()) {
                String name = resultSet.getString("name");
                String last_name = resultSet.getString("last_name");
                int age = resultSet.getInt("age");
                User user = new User(name, last_name, (byte) age);
                int id = resultSet.getInt("id");
                user.setId((long) id);
                listOfUsers.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listOfUsers;
    }
    @Override
    public void cleanUsersTable() {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(SQL_DELETE_STATEMENT);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
