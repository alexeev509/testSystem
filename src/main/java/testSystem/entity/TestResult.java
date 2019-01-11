package testSystem.entity;

public class TestResult {
    private int student_id;
    private String test_name;
    private String result;

    public TestResult() {
    }

    public TestResult(int student_id, String test_name, String result) {
        this.student_id = student_id;
        this.test_name = test_name;
        this.result = result;
    }

    public int getStudent_id() {
        return student_id;
    }

    public void setStudent_id(int student_id) {
        this.student_id = student_id;
    }

    public String getTest_name() {
        return test_name;
    }

    public void setTest_name(String test_name) {
        this.test_name = test_name;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "TestResult{" +
                "student_id=" + student_id +
                ", test_name='" + test_name + '\'' +
                ", result='" + result + '\'' +
                '}';
    }
}
