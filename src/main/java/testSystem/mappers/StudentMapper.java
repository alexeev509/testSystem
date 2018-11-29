package testSystem.mappers;

import org.springframework.jdbc.core.RowMapper;
import testSystem.entity.Student;


import java.sql.ResultSet;
import java.sql.SQLException;

public class StudentMapper implements RowMapper {

    public Student mapRow(ResultSet rs,int rowNum) throws SQLException {
        Student student = new Student();
        student.setId(rs.getInt("id"));
        student.setEmail(rs.getString("email"));
        student.setName(rs.getString("name_std"));
        student.setSurname(rs.getString("surname"));
        student.setPatronymic(rs.getString("lastname"));
        student.setGroup(rs.getString("group_num"));
        return student;
    }
}
