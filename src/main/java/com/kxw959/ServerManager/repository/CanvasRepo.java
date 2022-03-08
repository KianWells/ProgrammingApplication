package com.kxw959.ServerManager.repository;

import com.kxw959.ServerManager.config.CanvasConfig;
import com.kxw959.ServerManager.entity.Student;
import com.kxw959.ServerManager.entity.Students;
import edu.ksu.canvas.interfaces.AssignmentReader;
import edu.ksu.canvas.interfaces.CourseReader;
import edu.ksu.canvas.interfaces.UserReader;
import edu.ksu.canvas.model.Course;
import edu.ksu.canvas.model.User;
import edu.ksu.canvas.model.assignment.Assignment;
import edu.ksu.canvas.requestOptions.GetSingleCourseOptions;
import edu.ksu.canvas.requestOptions.GetUsersInCourseOptions;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Repository
public class CanvasRepo {
    CanvasConfig canvasConfig = new CanvasConfig();

}
