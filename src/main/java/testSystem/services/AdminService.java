package testSystem.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import testSystem.entity.Admin;
import testSystem.entity.Student;
import testSystem.mappers.AdminMapper;

import javax.sql.DataSource;
import java.util.List;

@Service
public class AdminService {
    private DataSource driverManagerDataSource;
    private JdbcTemplate jdbcTemplate;
    private final StudetsService studetsService;

    @Autowired
    public AdminService(DataSource driverManagerDataSource, StudetsService studetsService) {
        this.driverManagerDataSource = driverManagerDataSource;
        this.jdbcTemplate = new JdbcTemplate(driverManagerDataSource);
        this.studetsService = studetsService;
    }

    public List<Admin> findAdminByEmail(String email) {
        String FIND_ADMIN_IN_BD = "SELECT * FROM ADMINS WHERE email=?";
        return jdbcTemplate.query(FIND_ADMIN_IN_BD, new Object[]{email}, new AdminMapper());
    }

    public String saveAdmin(String email, String password) {
        String SAVE_ADMIN = "INSERT INTO ADMINS (email,password) VALUES (?,?)";
        List<Admin> adminByEmail = findAdminByEmail(email);
        List<Student> studentByEmail = studetsService.findStudentByEmail(email);
        if (adminByEmail.size() == 0 && studentByEmail.size() == 0) {
            jdbcTemplate.update(SAVE_ADMIN, email, password);
            return "true";
        } else {
            return "false";
        }
    }
}
