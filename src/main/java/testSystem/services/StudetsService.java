package testSystem.services;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import testSystem.mappers.StudentMapper;

import javax.sql.DataSource;

@Service
public class StudetsService {

    private DataSource driverManagerDataSource;
    private JdbcTemplate jdbcTemplate;
    @Autowired
    public StudetsService(DataSource driverManagerDataSource) {
        this.driverManagerDataSource = driverManagerDataSource;
        this.jdbcTemplate=new JdbcTemplate(driverManagerDataSource);
    }

    public String findAllStudents(){
        String sql = "SELECT * FROM STUDENTS";
        System.out.println(jdbcTemplate.query(sql, new StudentMapper()));
        return new Gson().toJson(jdbcTemplate.query(sql, new StudentMapper())).toString();
    }

    public void saveStudent(String name, String surname,String patronymic,String email, String group,String password){
        String createNewStudent = "INSERT INTO STUDENTS (nameStd,surname,lastname,groupNum,email,password) VALUES (?,?,?,?,?,?)";
        jdbcTemplate.update(createNewStudent,name,surname,patronymic,group,email,password);
    }

    public void deleteStudent(String id){
        String deleteStudentSql="DELETE FROM STUDENTS WHERE id=?";
        int rows=jdbcTemplate.update(deleteStudentSql,id);
        System.out.println(rows);
    }

    public String findStudent(String email, String password){
        String FIND_STUDENT_IN_BD="SELECT * FROM STUDENTS WHERE email=? AND password=?";
        System.out.println(jdbcTemplate.query(FIND_STUDENT_IN_BD,new Object[]{email,password}, new StudentMapper()));
        return  new Gson().toJson(jdbcTemplate.query(FIND_STUDENT_IN_BD,new Object[]{email,password}, new StudentMapper()));
    }

    public String findStudentById(String id){
        String FIND_STUDENT_IN_BD="SELECT * FROM STUDENTS WHERE id=?";
        System.out.println(jdbcTemplate.query(FIND_STUDENT_IN_BD,new Object[]{id}, new StudentMapper()));
        return  new Gson().toJson(jdbcTemplate.query(FIND_STUDENT_IN_BD,new Object[]{id}, new StudentMapper()));
    }


}
