package org.example.daoImpl;

import org.example.Config;
import org.example.dao.EmployeeDao;
import org.example.model.Employee;
import org.example.model.Job;

import javax.swing.text.Position;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EmployeeDaoImpl implements EmployeeDao {
    public void createEmployee() {
        String sql = "create table if not exists Employee(" +
                "id serial primary key," +
                "firstName varchar," +
                "lastName varchar," +
                "age int," +
                "email varchar," +
                "jobId INT references job(id))";
        try (Connection connection = Config.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute(sql);
            System.out.println("Table successfully created");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addEmployee(Employee employee) {
        String sql = "insert into Employee(" +
                "firstName," +
                "lastName," +
                "age," +
                "email" +
                ")values(?,?,?,?)";
        try (Connection connection = Config.getConnection();) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, employee.getFirstName());
            preparedStatement.setString(2, employee.getLastName());
            preparedStatement.setInt(1, employee.getAge());
            preparedStatement.setString(1, employee.getEmail());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public void dropTable() {
        String sql = ("Drop table ");
        try (Connection connection = Config.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.executeUpdate();
            System.out.println("id dropped");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void cleanTable() {
        String sql = ("DELETE from employee");
        try (Connection connection = Config.getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(sql);
            System.out.println("is cleaned");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void updateEmployee(Long id, Employee employee) {
        String sql = "UPDATE  employee set firstName =? where id = ?";
        try (Connection connection = Config.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, "FirstName");
            preparedStatement.setLong(2, 2L);
            preparedStatement.executeUpdate();
            System.out.println(employee.getFirstName() + " is update");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public List<Employee> getAllEmployees() {
        List<Employee> employees = new ArrayList<>();
        String sql = "SELECT * FROM Employee";
        try (Connection connection = Config.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                Employee employee = new Employee();
                employee.setId(resultSet.getLong("id"));
                employee.setFirstName(resultSet.getString("FirstName"));
                employee.setLastName(resultSet.getString("LastName"));
                employee.setAge(resultSet.getInt("Age"));
                employee.setEmail(resultSet.getString("Email"));
                employees.add(employee);
            }
        } catch (SQLException e) {
            System.out.println("Ошибка при выполнении SQL-запроса: " + e.getMessage());
        }
        return employees;
    }

    public Employee findByEmail(String email) {
        String sql = "select * from employee where email = ?";
        Employee employee = null;

        try (Connection connection = Config.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                employee = new Employee();
                employee.setId(resultSet.getLong("id"));
                employee.setFirstName(resultSet.getString("firstName"));
                employee.setLastName(resultSet.getString("lastName"));
                employee.setAge(resultSet.getInt("age"));
                employee.setEmail(resultSet.getString("email"));
                employee.setJobId(resultSet.getInt("job_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage(), e);
        }

        return employee;
    }

    public Map<Employee, Job> getEmployeeById(Long employeeId) {
        Map<Employee, Job> employeeJobMap = new HashMap<>();

        String sql = "SELECT e.*, j.* FROM Employee e LEFT JOIN Job j ON e.job_id = j.job_id WHERE e.id = ?";

        try (Connection connection = Config.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, employeeId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Employee employee = new Employee();
                    employee.setId(resultSet.getLong("id"));
                    employee.setFirstName(resultSet.getString("FirstName"));
                    employee.setLastName(resultSet.getString("LastName"));
                    employee.setAge(resultSet.getInt("Age"));
                    employee.setEmail(resultSet.getString("Email"));

                    Job job = new Job();
                    job.setId(resultSet.getLong("job_id"));
                    job.setPosition(resultSet.getString("position"));
                    job.setProfession(resultSet.getString("profession"));
                    job.setExperience(resultSet.getInt("experience"));

                    employeeJobMap.put(employee, job);
                }
            }
        } catch (SQLException e) {
            System.out.println("Ошибка при выполнении SQL-запроса: " + e.getMessage());
        }

        return employeeJobMap;
    }

    @Override
    public List<Employee> getEmployeeByPosition(String position) {
        String sql = "SELECT * FROM employee WHERE position = ?";
        try (Connection connection = Config.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, position);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Employee> employees = new ArrayList<>();

            while (resultSet.next()) {
                Employee employee = new Employee();
                employee.setId(resultSet.getLong("id"));
                employee.setFirstName(resultSet.getString("first_name"));
                employee.setLastName(resultSet.getString("last_name"));
                employee.setAge(resultSet.getInt("age"));
                employee.setEmail(resultSet.getString("email"));
                employees.add(employee);
            }

            return employees;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Ошибка при выполнении SQL-запроса", e);
        }
    }
}