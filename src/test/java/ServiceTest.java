import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import testSystem.services.StudetsService;

public class ServiceTest {
//    private StudetsService studetsService =new StudetsService();
//    @Autowired
//    public ServiceTest(StudetsService studetsService) {
//        this.studetsService=studetsService;
//    }

    @Test
    public void saveStudentTest(){
        String name="Anton";
        String surname="Baribin";
        String patronymic="Petrovich";
        String email="Baribin@gmail.com";
        String group="23543/3";
       // studetsService.saveStudent(name,surname,patronymic,email,group);
    }
}
