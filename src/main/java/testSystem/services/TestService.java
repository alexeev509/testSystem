package testSystem.services;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import testSystem.mappers.StudentMapper;

import javax.sql.DataSource;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class TestService {

    private final static String TEST_FILES_PATH="/Users/apathy/IdeaProjects/testSystem/src/main/resources/tests/";
    private final static String SAVE_TEST_RESULT = "INSERT INTO test_results (student_id,test_name,result) VALUES (?,?,?)";
    private DataSource driverManagerDataSource;
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public TestService(DataSource driverManagerDataSource) {
        this.driverManagerDataSource = driverManagerDataSource;
        this.jdbcTemplate=new JdbcTemplate(driverManagerDataSource);
    }

    //this code must be refactor!!
    public ResponseEntity<String> readTest(String testName){
        String textOfTest=readTestFiles(testName);
       //this logic in another class
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "text/html; charset=UTF-8");

        return new ResponseEntity<String>(textOfTest, headers, HttpStatus.OK);
    }


    public void saveTestResults(String actuallyAnswers,String id,String testName) throws IOException {
//        String expectedAnswers=readTestFiles("Answers_"+testName);
//        String resultOfTheTest=checkAnswers(actuallyAnswers,expectedAnswers);
//        jdbcTemplate.update(SAVE_TEST_RESULT, Long.valueOf(id), testName, resultOfTheTest);
        System.out.println("i am here"+testName);
        List<String> expectedAnswers = readAnswers("Answers_" + testName);
        List<String> actAnswers=Arrays.asList(actuallyAnswers.split("___"));
        String resultOfTheTest=checkAnswers(actAnswers,expectedAnswers);


    }
    private List<String> readAnswers(String fileName) throws IOException {
        List<String> massOfAnswers=new ArrayList<String>();
        FileInputStream fstream = new FileInputStream(TEST_FILES_PATH+fileName+".txt");
        BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
        String strLine;
        while ((strLine = br.readLine()) != null){
            massOfAnswers.add(strLine);
        }
        return massOfAnswers;
    }

    private String checkAnswers(List<String> actuallyAnswers, List<String> expectedAnswers){

        double rightAnswers=0;
        for (int i = 0; i < expectedAnswers.size(); i++) {
            String[] expectedAnswerOnQuestion=expectedAnswers.get(i).split(" ");
            String[] actuallyAnswerOnAQuestion=actuallyAnswers.get(i).split(" ");
            for (int j = 0; j < expectedAnswerOnQuestion.length && actuallyAnswerOnAQuestion.length==expectedAnswerOnQuestion.length; j++) {
                if (!actuallyAnswerOnAQuestion[j].equals(expectedAnswerOnQuestion[j])) {
                    break;
                }
                if(j==expectedAnswerOnQuestion.length-1){
                    ++rightAnswers;
                }
            }
        }

        System.out.println(rightAnswers/expectedAnswers.size()*100+"%");
        return rightAnswers/5*100+"%";
    }



    private String readTestFiles(String testName){
        String textOfTest="";
        InputStreamReader reader = null;
        try {
            reader=new InputStreamReader(new FileInputStream(TEST_FILES_PATH+testName+".txt"),"UTF-8");
            char[] buf = new char[256];
            int c;
            while((c = reader.read(buf))>0){

                if(c < 256){
                    buf = Arrays.copyOf(buf, c);
                }
                textOfTest+=String.valueOf(buf);
            }
        }  catch (IOException e) {
            e.printStackTrace();
        }
        return textOfTest;
    }

    public void saveTestResultsInFile(){

    }

    public String findAllTest (){
        String sql = "SELECT * FROM STUDENTS INNER JOIN TESTRESULTS ON TESTRESULTS.student_id=STUDENTS.id";
        System.out.println(jdbcTemplate.query(sql, new StudentMapper()));
        return new Gson().toJson(jdbcTemplate.query(sql, new StudentMapper())).toString();
    }
}
