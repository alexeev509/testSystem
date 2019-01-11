package testSystem.services;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import testSystem.entity.Student;
import testSystem.mappers.StudentMapper;

import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
@Slf4j
public class StudetsService {

    private DataSource driverManagerDataSource;
    private JdbcTemplate jdbcTemplate;
    @Autowired
    public StudetsService(DataSource driverManagerDataSource) {
        this.driverManagerDataSource = driverManagerDataSource;
        this.jdbcTemplate=new JdbcTemplate(driverManagerDataSource);
    }

    public String findAllStudents(){
        log.info("WE TRY TO FIND ALL STUDENTS");
        String GET_ALL_STUDENTS = "SELECT * FROM STUDENTS";
        System.out.println(jdbcTemplate.query(GET_ALL_STUDENTS, new StudentMapper()));
        List<Student> students = jdbcTemplate.query(GET_ALL_STUDENTS, new StudentMapper());
        Collections.sort(students, new Comparator<Student>() {
            @Override
            public int compare(Student o1, Student o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });
        return new Gson().toJson(students).toString();
    }

    public String saveStudent(String name, String surname, String patronymic, String email, String group, String password) {
        List<Student> studentByEmail = findStudentByEmail(email);

        if (studentByEmail.size() == 0) {
            String CREATE_NEW_STUDENT = "INSERT INTO STUDENTS (name_std,surname,lastname,group_num,email,password) VALUES (?,?,?,?,?,?)";
            jdbcTemplate.update(CREATE_NEW_STUDENT, name, surname, patronymic, group, email, password);
            return new Gson().toJson(findStudentByEmail(email).get(0)).toString();
        } else
            return "false";
    }

    public void deleteStudent(String id){
        String DELETE_STUDENT_BY_ID = "DELETE FROM STUDENTS WHERE STUDENTS.id=?";
        int rows = jdbcTemplate.update(DELETE_STUDENT_BY_ID, Integer.valueOf(id));
        System.out.println(rows);
    }

//    public String findStudent(String email, String password){
//        String FIND_STUDENT_IN_BD="SELECT * FROM STUDENTS WHERE email=? AND password=?";
//        System.out.println(jdbcTemplate.query(FIND_STUDENT_IN_BD,new Object[]{email,password}, new StudentMapper()));
//        return  new Gson().toJson(jdbcTemplate.query(FIND_STUDENT_IN_BD,new Object[]{email,password}, new StudentMapper()));
//    }

    public String findStudentById(String id){
        String FIND_STUDENT_BY_ID = "SELECT * FROM STUDENTS WHERE id=?";
        return new Gson().toJson(jdbcTemplate.query(FIND_STUDENT_BY_ID, new Object[]{Integer.valueOf(id)}, new StudentMapper()));
    }

    public List<Student> findStudentByEmail(String email) {
        log.info("I am here");
        String FIND_STUDENT_BY_EMAIL = "SELECT * FROM STUDENTS WHERE email=?";
        return jdbcTemplate.query(FIND_STUDENT_BY_EMAIL, new Object[]{email}, new StudentMapper());
    }


    public void updateSudent(String id, String name, String lastname, String surname, String email, String group) {
        String UPDATE_STUDENT_BY_ID = "UPDATE STUDENTS SET name_std=?, lastname=?, surname=?, email=?, group_num=? WHERE id=?";
        jdbcTemplate.update(UPDATE_STUDENT_BY_ID, name, lastname, surname, email, group, Integer.valueOf(id));
    }
}
