package com.kxw959.programmingapplication.user;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class User {
    public static boolean isTeacher;
    public static String username;
    public static URL url;
    public static List<Task> taskList = new ArrayList<>();
    public static Task selectedTask;
    public static List<File> uploadFiles;
}
