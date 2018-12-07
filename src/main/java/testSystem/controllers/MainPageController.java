package testSystem.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import testSystem.services.StudetsService;
import testSystem.services.TestService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Created by apathy on 20.09.18.
 */

@Controller
@RequestMapping(value = "/")
public class MainPageController {


    private final StudetsService studetsService;
    private final TestService testService;

    @Autowired
    public MainPageController(StudetsService studetsService, TestService testService) {
        this.studetsService = studetsService;
        this.testService = testService;
    }

    @RequestMapping("/register")
    public String start() {
        return "RegisterPage";
    }

    @RequestMapping(value = "/addNewStudent", method = RequestMethod.POST)
    public @ResponseBody
    void addNewStudent(@RequestParam(value = "name") String name,
                       @RequestParam(value = "lastname") String lastname,
                       @RequestParam(value = "patronymic") String patronymic,
                       @RequestParam(value = "email") String email,
                       @RequestParam(value = "group") String group,
                       @RequestParam(value = "password") String password
    ) {
        System.out.println("name=" + name + " lastname=" + lastname + " patronymic=" + patronymic + " email=" + email + " group=" + group + " password=" + password);
        studetsService.saveStudent(name, lastname, patronymic, email, group, password);
        studetsService.findAllStudents();
    }

    @RequestMapping(value = "/admin", method = RequestMethod.GET)
    public String adminPage() {
        return "AdminPanel";
    }

    @RequestMapping(value = "/getallstudents", method = RequestMethod.GET)
    public @ResponseBody
    String getallstudents() {
        return studetsService.findAllStudents();
    }

    @RequestMapping(value = "/deleteStudent", method = RequestMethod.POST)
    public @ResponseBody
    void deleteStudent(@RequestParam(value = "id") String id) {
        System.out.println("id=" + id);
        studetsService.deleteStudent(id);
        //i must create response if all is ok
    }

    @RequestMapping(value = "/autorisation", method = RequestMethod.GET)
    public String autorisationPage() {
        return "AutorisationPage";
    }

//    //change post to get
//    @RequestMapping(value = "/autoCheck", method = RequestMethod.POST)
//    public @ResponseBody
//    String autorisationCheck(@RequestParam(value = "email") String email,
//                             @RequestParam(value = "password") String password) {
//        System.out.println("hello");
//        return studetsService.findStudent(email, password);
//    }

    @RequestMapping(value = "/testPage", method = RequestMethod.GET)
    public String testPage(@RequestParam(value = "id") String id) {
        return "TestPage";
    }

    @RequestMapping(value = "/getStudentById", method = RequestMethod.GET)
    public @ResponseBody
    String getStudentById(@RequestParam(value = "id") String id) {
        System.out.println(id);
        return studetsService.findStudentById(id);
    }

    @RequestMapping(value = "/getTest", method = RequestMethod.GET)
    public ResponseEntity<String> getTest(@RequestParam(value = "testName") String testName) {
        return testService.readTest(testName);
    }


    @RequestMapping(value = "/saveTestResults", method = RequestMethod.POST)
    public @ResponseBody
    void saveTestResults(@RequestParam(value = "answers") String answers,
                         @RequestParam(value = "id") String id,
                         @RequestParam(value = "testName") String testName) throws IOException {
        testService.saveTestResults(answers, id, testName);
    }


    @RequestMapping(value = "/saveAllTestResultsInFile", method = RequestMethod.POST)
    public @ResponseBody
    void saveAllTestResultsInFile() {
        testService.saveTestResultsInFile();
    }


    @RequestMapping(value = "/addTest", method = RequestMethod.GET)
    public String addTest() {
        return "AddNewTest";
    }

    @RequestMapping(value = "/saveNewTest", method = RequestMethod.POST)
    public @ResponseBody
    void saveNewTest(@RequestParam(value = "questions") String questions,
                     @RequestParam(value = "answers") String answers,
                     @RequestParam(value = "testName") String testName) throws IOException {
        System.out.println(questions + "\n" + answers + "\n" + testName);
        testService.saveNewTest(testName, questions, answers);
    }

}


//@ResponseBody i must read about this annotation
