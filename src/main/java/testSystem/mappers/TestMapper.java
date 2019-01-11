package testSystem.mappers;

import org.springframework.jdbc.core.RowMapper;
import testSystem.entity.TestResult;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TestMapper implements RowMapper {
    @Override
    public TestResult mapRow(ResultSet rs, int rowNum) throws SQLException {
        TestResult testResult = new TestResult();
        testResult.setStudent_id(rs.getInt("student_id"));
        testResult.setResult(rs.getString("result"));
        testResult.setTest_name(rs.getString("test_name"));
        return testResult;
    }
}
