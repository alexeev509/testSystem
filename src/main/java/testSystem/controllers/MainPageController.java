package testSystem.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import testSystem.services.AdminService;
import testSystem.services.StudetsService;
import testSystem.services.TestService;

import java.io.IOException;

/**
 * Created by apathy on 20.09.18.
 */

@Controller
@RequestMapping(value = "/")
@Slf4j
public class MainPageController {


    private final StudetsService studetsService;
    private final TestService testService;
    private final AdminService adminService;

    @Autowired
    public MainPageController(StudetsService studetsService, TestService testService, AdminService adminService) {
        this.studetsService = studetsService;
        this.testService = testService;
        this.adminService = adminService;
    }

    @RequestMapping("/register")
    public String start() {
        return "RegisterPage";
    }

    @RequestMapping(value = "/addNewStudent", method = RequestMethod.POST)
    public @ResponseBody
    String addNewStudent(@RequestParam(value = "name") String name,
                         @RequestParam(value = "lastname") String lastname,
                         @RequestParam(value = "patronymic") String patronymic,
                         @RequestParam(value = "email") String email,
                         @RequestParam(value = "group") String group,
                         @RequestParam(value = "password") String password
    ) {
        System.out.println("name=" + name + " lastname=" + lastname + " patronymic=" + patronymic + " email=" + email + " group=" + group + " password=" + password);
        return studetsService.saveStudent(name, lastname, patronymic, email, group, password);
        // studetsService.findAllStudents();
    }

    @RequestMapping(value = "/admin", method = RequestMethod.GET)
    public String adminPage() {
        return "AdminPanel";
    }

    @RequestMapping(value = "/getallstudents", method = RequestMethod.GET)
    public @ResponseBody
    String getallstudents() {
        log.info("WE ARE IN CONTROLLER");
        return studetsService.findAllStudents();
    }

    @RequestMapping(value = "/deleteStudent", method = RequestMethod.POST)
    public @ResponseBody
    void deleteStudent(@RequestParam(value = "id") String id) {
        studetsService.deleteStudent(id);
        //i must create response if all is ok
    }

    @RequestMapping(value = "/autorisation", method = RequestMethod.GET)
    public String autorisationPage() {
        return "AutorisationPage";
    }


    @RequestMapping(value = "/testPage", method = RequestMethod.GET)
    public String testPage(@RequestParam(value = "id") String id) {
        System.out.println("we are trying to get test" + id);
        return "TestPage";
    }

    @RequestMapping(value = "/getStudentById", method = RequestMethod.GET)
    public @ResponseBody
    String getStudentById(@RequestParam(value = "id") String id) {
        return studetsService.findStudentById(id);
    }

    @RequestMapping(value = "/getTest", method = RequestMethod.GET)
    public ResponseEntity<String> getTest(@RequestParam(value = "testName") String testName) throws IOException {
        return testService.readTest(testName);
    }


    @RequestMapping(value = "/saveTestResults", method = RequestMethod.POST)
    public @ResponseBody
    String saveTestResults(@RequestParam(value = "answers") String answers,
                           @RequestParam(value = "id") String id,
                           @RequestParam(value = "testName") String testName) throws IOException {
        return testService.saveTestResults(answers, id, testName);
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


    @RequestMapping(value = "/getAllTestNames", method = RequestMethod.GET)
    public @ResponseBody
    String getAllTestNames(@RequestParam(value = "id") String id) throws IOException {
        return testService.getAllTestNames(id);
    }


    @RequestMapping(value = "/updateStudent", method = RequestMethod.POST)
    public @ResponseBody
    void updateStudent(@RequestParam(value = "id") String id,
                       @RequestParam(value = "name") String name,
                       @RequestParam(value = "lastname") String lastname,
                       @RequestParam(value = "surname") String surname,
                       @RequestParam(value = "email") String email,
                       @RequestParam(value = "group") String group
    ) {
        System.out.println("name=" + name + " lastname=" + lastname + " surname=" + surname + " email=" + email + " group=" + group + " id=" + id);
        studetsService.updateSudent(id, name, lastname, surname, email, group);
    }

    @RequestMapping(value = "/getStudentTests", method = RequestMethod.GET)
    public @ResponseBody
    String getStudentTests(@RequestParam(value = "id") String id) throws IOException {
        return testService.getTestsByStudentId(id);
    }

    @RequestMapping(value = "/deleteStudentsTest", method = RequestMethod.POST)
    public @ResponseBody
    void deleteStudentsTest(@RequestParam(value = "id") String id,
                            @RequestParam(value = "test_name") String test_name) {
        System.out.println(id + " " + test_name);
        testService.deleteTestByNameAndId(id, test_name);
        //i must create response if all is ok
    }

    //updateStudentTest
    @RequestMapping(value = "/updateStudentTest", method = RequestMethod.POST)
    public @ResponseBody
    void updateStudentTest(@RequestParam(value = "id") String id,
                           @RequestParam(value = "test_name") String test_name,
                           @RequestParam(value = "result") String result) {
        System.out.println(id + " " + test_name + " res" + result);
        testService.updateTestByNameAndId(id, test_name, result);
        //i must create response if all is ok
    }

    @RequestMapping(value = "/getTestNames", method = RequestMethod.GET)
    public @ResponseBody
    String getTestNames() throws IOException {
        return testService.getAllTestNames();
    }

    //updateStudentTest
    @RequestMapping(value = "/deleteTestByName", method = RequestMethod.POST)
    public @ResponseBody
    void deleteTestByName(@RequestParam(value = "testName") String testName) throws IOException {
        System.out.println(testName);
        testService.deleteTestByNameEveryWhere(testName);
        //i must create response if all is ok
    }


    //updateStudentTest
    @RequestMapping(value = "/addNewAdmin", method = RequestMethod.POST)
    public @ResponseBody
    String addNewAdmin(@RequestParam(value = "email") String email,
                       @RequestParam(value = "password") String password) {
        System.out.println(email + " " + password);
        return adminService.saveAdmin(email, password);
    }
}


//@ResponseBody i must read about this annotation
