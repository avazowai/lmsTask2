package org.example.daoImpl;

import org.example.Config;
import org.example.dao.JobDao;
import org.example.model.Job;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JobDaoImpl implements JobDao {
    Connection connection = Config.getConnection();
    public void createJobTable() {
        String sql = "create table if not exists job(" +
                "id serial primary key," +
                "position varchar," +
                "profession varchar," +
                "description varchar," +
                "experience int)";
        try (Connection connection = Config.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute(sql);
            System.out.println("Table successfully created");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addJob(Job job) {
        String sql = "insert into job(" +
                "position," +
                "profession," +
                "description," +
                "experience" +
                ")values(?,?,?,?)";
        try (Connection connection = Config.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, job.getPosition());
            preparedStatement.setString(2, job.getProfession());
            preparedStatement.setString(3, job.getDescription());
            preparedStatement.setInt(4, job.getExperience());
            preparedStatement.executeUpdate();
            System.out.println("Saved");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public Job getJobById(Long jobId) {
        Job job = null;
        String sql = "select * from job where id = ?";
        try (Connection connection = Config.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, jobId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                job = new Job();
                job.setId(resultSet.getLong("id"));
                job.setPosition(resultSet.getString("position"));
                job.setProfession(resultSet.getString("profession"));
                job.setDescription(resultSet.getString("description"));
                job.setExperience(resultSet.getInt("experience"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return job;
    }

    public List<Job> sortByExperience(String ascOrDesc) {
        List<Job> jobList = new ArrayList<>();
        String sql = "select * from job order by experience  ";
        try (Connection connection = Config.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Job job = new Job();
                job.setId(resultSet.getLong("id"));
                job.setPosition(resultSet.getString("position"));
                job.setProfession(resultSet.getString("profession"));
                job.setDescription(resultSet.getString("description"));
                job.setExperience(resultSet.getInt("experience"));
                jobList.add(job);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return jobList;
    }

    public Job getJobByEmployeeId(Long employeeId) {
        String sql = "select j.* from job j join employee e on e.job_id = j.id where e.id = ?";
        Job job = null;

        try (Connection connection = Config.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, employeeId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                job = new Job();
                job.setId(resultSet.getLong("id"));
                job.setPosition(resultSet.getString("position"));
                job.setProfession(resultSet.getString("profession"));
                job.setExperience(resultSet.getInt("experience"));
            }

            resultSet.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return job;
    }

    public void deleteDescriptionColumn() {
        String sql = "alter table job drop column description";
        try (Connection connection = Config.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.executeUpdate();
            System.out.println("Is deleted");
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }

    }
}
