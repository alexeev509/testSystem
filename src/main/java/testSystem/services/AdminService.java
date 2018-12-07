package testSystem.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import testSystem.entity.Admin;
import testSystem.mappers.AdminMapper;

import javax.sql.DataSource;
import java.util.List;

@Service
public class AdminService {
    private DataSource driverManagerDataSource;
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public AdminService(DataSource driverManagerDataSource) {
        this.driverManagerDataSource = driverManagerDataSource;
        this.jdbcTemplate = new JdbcTemplate(driverManagerDataSource);
    }

    public List<Admin> findAdminByEmail(String email) {
        String FIND_ADMIN_IN_BD = "SELECT * FROM ADMINS WHERE email=?";
        return jdbcTemplate.query(FIND_ADMIN_IN_BD, new Object[]{email}, new AdminMapper());
    }
}
