package testSystem.services;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import testSystem.entity.TestResult;
import testSystem.mappers.StudentMapper;
import testSystem.mappers.TestMapper;

import javax.servlet.ServletContext;
import javax.sql.DataSource;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class TestService {

    private final static String TEST_FILES_PATH = "/Users/apathy/Desktop/tomcat/apache-tomcat-9.0.1/webapps/ROOT/classes/tests/";
    private final static String SAVE_TEST_RESULT = "INSERT INTO test_results (student_id,test_name,result) VALUES (?,?,?)";
    private DataSource driverManagerDataSource;
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public TestService(DataSource driverManagerDataSource) {
        this.driverManagerDataSource = driverManagerDataSource;
        this.jdbcTemplate=new JdbcTemplate(driverManagerDataSource);
    }

    //this code must be refactor!!
    public ResponseEntity<String> readTest(String testName) throws IOException {
        String textOfTest=readTestFiles(testName);
       //this logic in another class
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "text/html; charset=UTF-8");

        return new ResponseEntity<String>(textOfTest, headers, HttpStatus.OK);
    }


    public String saveTestResults(String actuallyAnswers, String id, String testName) throws IOException {
//        String expectedAnswers=readTestFiles("Answers_"+testName);
//        String resultOfTheTest=checkAnswers(actuallyAnswers,expectedAnswers);
//        jdbcTemplate.update(SAVE_TEST_RESULT, Long.valueOf(id), testName, resultOfTheTest);
        System.out.println("WE TRY TO SAVE" + id + " " + actuallyAnswers + " " + testName);
        List<String> expectedAnswers = readAnswers("Answers_" + testName);
        List<String> actAnswers=Arrays.asList(actuallyAnswers.split("___"));
        String resultOfTheTest=checkAnswers(actAnswers,expectedAnswers);
        jdbcTemplate.update(SAVE_TEST_RESULT, Long.valueOf(id), testName, resultOfTheTest);
        System.out.println("WE SAVE ALL TEST INFO");
        return resultOfTheTest;
    }
    private List<String> readAnswers(String fileName) throws IOException {
        ClassLoader cl = this.getClass().getClassLoader();
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver(cl);
        Resource resource = resolver.getResource("classpath:/tests/" + fileName + ".txt");
        System.out.println(resource.getFile().getPath());


        List<String> massOfAnswers=new ArrayList<String>();
        // FileInputStream fstream = new FileInputStream(TEST_FILES_PATH+fileName+".txt");
        FileInputStream fstream = new FileInputStream(resource.getFile().getPath());
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
        return rightAnswers / expectedAnswers.size() * 100 + "%";
    }


    private String readTestFiles(String testName) throws IOException {
        ClassLoader cl = this.getClass().getClassLoader();
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver(cl);
        Resource resource = resolver.getResource("classpath:/tests/" + testName + ".txt");
        System.out.println(resource.getFile().getPath());

        String textOfTest="";
        InputStreamReader reader = null;
        try {
            reader = new InputStreamReader(new FileInputStream(resource.getFile().getPath()), "UTF-8");
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

    public void saveNewTest(String testName, String questions, String answers) throws IOException {

        ClassLoader cl = this.getClass().getClassLoader();
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver(cl);
        Resource resource = resolver.getResource("classpath:/tests/");
        System.out.println(resource.getFile().getPath());

        // String path = TEST_FILES_PATH + testName + ".txt";
        String path = resource.getFile().getPath();
        File f = new File(path + "/" + testName + ".txt");

        try {
            FileWriter writer = new FileWriter(path + "/" + testName + ".txt", false);
            // запись всей строки
            writer.write(questions);
            // запись по символам
            writer.flush();

            //path = TEST_FILES_PATH + "Answers_" + testName + ".txt";
            f = new File(path + "/" + "Answers_" + testName + ".txt");
            writer = new FileWriter(path + "/" + "Answers_" + testName + ".txt", false);
            writer.write(answers);
            writer.flush();

        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public String getAllTestNames(String id) throws IOException {
        // String dir=context.getRealPath("/");
        //String configFilePath = getServletContext().getRealPath("/");
        List<String> testNamesByStudentId = getTestNamesByStudentId(id);
        ClassLoader cl = this.getClass().getClassLoader();
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver(cl);
        Resource[] resources = resolver.getResources("classpath:/tests/*.txt");
        System.out.println(resources[0].getFilename());
        File[] folderEntries = new File[resources.length];
        for (int i = 0; i < resources.length; i++) {
            folderEntries[i] = resources[i].getFile();
        }

        String namesOfFiles = "";
//        File[] folderEntries = new File(TEST_FILES_PATH).listFiles();
        for (int i = 0; i < folderEntries.length - 1; i++) {
            if (!folderEntries[i].getName().contains("Answers_") && !testNamesByStudentId.contains(folderEntries[i].getName().split("\\.")[0]))
                namesOfFiles += folderEntries[i].getName().split("\\.")[0] + " ";
        }
        if (!folderEntries[folderEntries.length - 1].getName().contains("Answers_") && !testNamesByStudentId.contains(folderEntries[folderEntries.length - 1].getName().split("\\.")[0]))
            namesOfFiles += folderEntries[folderEntries.length - 1].getName().split("\\.")[0];

        namesOfFiles = namesOfFiles.trim();


        System.out.println(namesOfFiles);
        return namesOfFiles;
    }

    public String getTestsByStudentId(String id) {
        String GET_STUDENT_TEST_RESULTS_BY_STUDENT_ID = "SELECT * FROM TEST_RESULTS WHERE student_id=?";
        System.out.println(jdbcTemplate.query(GET_STUDENT_TEST_RESULTS_BY_STUDENT_ID, new Object[]{Integer.valueOf(id)}, new TestMapper()));
        return new Gson().toJson(jdbcTemplate.query(GET_STUDENT_TEST_RESULTS_BY_STUDENT_ID, new Object[]{Integer.valueOf(id)}, new TestMapper())).toString();
    }

    public List<String> getTestNamesByStudentId(String id) {
        String GET_STUDENT_TEST_RESULTS_BY_STUDENT_ID = "SELECT test_name FROM TEST_RESULTS WHERE student_id=?";
        return jdbcTemplate.queryForList(GET_STUDENT_TEST_RESULTS_BY_STUDENT_ID, new Object[]{Integer.valueOf(id)}, String.class);
    }

    public void deleteTestByNameAndId(String id, String test_name) {
        String DELETE_TEST_RESULT_BY_NAME_AND_STUDENT_ID = "DELETE FROM TEST_RESULTS WHERE student_id=? AND test_name=?";
        jdbcTemplate.update(DELETE_TEST_RESULT_BY_NAME_AND_STUDENT_ID, Integer.valueOf(id), test_name);
    }

    public void updateTestByNameAndId(String id, String test_name, String result) {
        String UPDATE_TEST_RESULT_BY_NAME_AND_STUDENT_ID = "UPDATE TEST_RESULTS SET result=? WHERE student_id=? AND test_name=?";
        jdbcTemplate.update(UPDATE_TEST_RESULT_BY_NAME_AND_STUDENT_ID, result, Integer.valueOf(id), test_name);
    }


    //very bad
    public String getAllTestNames() throws IOException {
        ClassLoader cl = this.getClass().getClassLoader();
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver(cl);
        Resource[] resources = resolver.getResources("classpath:/tests/*.txt");
        System.out.println(resources[0].getFilename());
        File[] folderEntries = new File[resources.length];
        for (int i = 0; i < resources.length; i++) {
            folderEntries[i] = resources[i].getFile();
        }

        String namesOfFiles = "";
//        File[] folderEntries = new File(TEST_FILES_PATH).listFiles();
        for (int i = 0; i < folderEntries.length - 1; i++) {
            if (!folderEntries[i].getName().contains("Answers_"))
                namesOfFiles += folderEntries[i].getName().split("\\.")[0] + " ";
        }
        if (!folderEntries[folderEntries.length - 1].getName().contains("Answers_"))
            namesOfFiles += folderEntries[folderEntries.length - 1].getName().split("\\.")[0];

        namesOfFiles = namesOfFiles.trim();


        System.out.println(namesOfFiles);
        return namesOfFiles;
    }

    public void deleteTestByNameEveryWhere(String testName) throws IOException {
        ClassLoader cl = this.getClass().getClassLoader();
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver(cl);
        Resource resources = resolver.getResource("classpath:/tests/" + testName + ".txt");
        resources.getFile().delete();
        resources = resolver.getResource("classpath:/tests/Answers_" + testName + ".txt");
        resources.getFile().delete();
    }
}
