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
import java.util.Arrays;

@Service
public class TestService {

    private final static String TEST_FILES_PATH="/Users/apathy/IdeaProjects/testSystem/src/main/resources/tests/";
    private final static String SAVE_TEST_RESULT = "INSERT INTO testResults (student_id,test_name,result) VALUES (?,?,?)";
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


    public void saveTestResults(String actuallyAnswers,String id,String testName){
        System.out.println(actuallyAnswers);
        String expectedAnswers=readTestFiles("Answers_"+testName);
        String resultOfTheTest=checkAnswers(actuallyAnswers,expectedAnswers);
        jdbcTemplate.update(SAVE_TEST_RESULT,id,testName,resultOfTheTest);

    }
    public String readTestFiles(String testName){
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

    public String checkAnswers(String actuallyAnswers, String expectedAnswers){
        String[] expAnswers=expectedAnswers.split(" ");
        String[] actAnswers=getActAnswerInNormalForm(actuallyAnswers,expAnswers.length);
        double rightAnswers=0;
        int step=expAnswers.length>10?3:2;

        for (int i = 0; i < actAnswers.length; i=i+step) {
            if(expAnswers.length>10){
                if(actAnswers[i].equals(expAnswers[i]) && actAnswers[i+1].equals(expAnswers[i+1]) && actAnswers[i+2].equals(expAnswers[i+2])){
                    ++rightAnswers;
                }
            }
            else {
                if(actAnswers[i].equals(expAnswers[i]) && actAnswers[i+1].equals(expAnswers[i+1])){
                    ++rightAnswers;
                }
            }
        }
        System.out.println(rightAnswers/5*100+"%");
        return rightAnswers/5*100+"%";
    }

    public String[]getActAnswerInNormalForm(String actuallyAnswers,int len){
        String[] massOfAnswers=actuallyAnswers.split("___");
        String[] resultMass=new String[len];
        int counter=0;
        for (int i = 0; i < massOfAnswers.length; i++) {
            String[] massOfAnswerOnQuestion=massOfAnswers[i].split(" ");
            //If we have 0.5 right of the question we think that this answer is wrong
            if(massOfAnswerOnQuestion.length<2){
                resultMass[counter++]="";
                resultMass[counter++]="";
                if(len>10){
                    resultMass[counter++]="";
                }
            }
            else {
                resultMass[counter++]=massOfAnswerOnQuestion[0];
                resultMass[counter++]=massOfAnswerOnQuestion[1];
                if(len>10){
                    resultMass[counter++]=massOfAnswerOnQuestion[2];
                }
            }
        }
        return resultMass;
    }


    public void saveTestResultsInFile(){

    }

    public String findAllTest (){
        String sql = "SELECT * FROM STUDENTS INNER JOIN TESTRESULTS ON TESTRESULTS.student_id=STUDENTS.id";
        System.out.println(jdbcTemplate.query(sql, new StudentMapper()));
        return new Gson().toJson(jdbcTemplate.query(sql, new StudentMapper())).toString();
    }
}
