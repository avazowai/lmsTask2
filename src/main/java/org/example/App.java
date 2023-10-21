package org.example;

import org.example.daoImpl.JobDaoImpl;
import org.example.model.Employee;
import org.example.model.Job;
import org.example.service.EmployeeService;
import org.example.service.JobService;
import org.example.serviceImpl.JobServiceImpl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class App {
    public static void main(String[] args) {
        Config.getConnection();

        /* JobService jobService = new JobServiceImpl();
        jobService.createJobTable();
        JobService jobService = new JobServiceImpl();
        jobService.addJob(new Job(1L, "Instructor", "Java", "Backend developer", 3));*/
        //JobService jobService1 = new JobServiceImpl();
        //jobService1.getJobById(1L);
        //JobService jobService = new JobServiceImpl();
        //jobService.sortByExperience("3");
        //JobService jobService = new JobServiceImpl();
        //jobService.getJobByEmployeeId(1L);
        JobService jobService = new JobServiceImpl();
        jobService.deleteDescriptionColumn();
    }
}