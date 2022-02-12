package com.TechPro.SpringBootStudy.basic_authentication;


import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
public class StudentBean05controller {
    private StudenBean05Service stdSrvc;//service layer' ulaşmak için obj create edildi

    public StudentBean05controller(StudenBean05Service stdSrvc) {
        this.stdSrvc = stdSrvc;
    }

    //bu method id ile ogrc returnn eden service methodu call edecek
    @GetMapping(path = "/selectStudentById/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_STUDENT')")
    public StudentBean05 selectStudentById(@PathVariable Long id) {

        return stdSrvc.selectStudentById(id);
    }

    @GetMapping(path = "/selectAllStudents")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_STUDENT')")
    public List<StudentBean05> selectAllStudents() {
        return stdSrvc.selectAllStudent();
    }

    @PutMapping(path = "/updateFullyStudentById/{id}")
    @PreAuthorize("hasAnyRole('student.write')")
    public StudentBean05 updateFullyStudentById(@PathVariable Long id, @RequestBody StudentBean05 newStd) {
        return stdSrvc.updateFullyStudentById(id, newStd);
    }


    @DeleteMapping(path = "/deleteStudentById/{id}")
    @PreAuthorize("hasAnyRole('student.write')")
    public String deleteStudentById(@PathVariable Long id) {
        return stdSrvc.deletStudentById(id);
    }


    @PatchMapping(path = "/updatePatchStudentById/{id}")
    @PreAuthorize("hasAnyRole('student.write')")
    public StudentBean05 updatePatchStudentById(@PathVariable Long id, @RequestBody StudentBean05 newStd) {
        return stdSrvc.updatePatchStudentById(id, newStd);
    }

    @PostMapping(path = "/addStudent")
    @PreAuthorize("hasAnyRole('student.write')")
    public StudentBean05 addStndt(@RequestBody StudentBean05 newStd)throws ClassNotFoundException, SQLException {
        return stdSrvc.addStudent1(newStd);
    }


}
