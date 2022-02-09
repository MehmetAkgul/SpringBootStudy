package com.TechPro.SpringBootStudy.basic_authentication;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StudentBean05controller {
    private StudenBean05Service stdSrvc;//service layer' ulaşmak için obj create edildi
    public StudentBean05controller(StudenBean05Service stdSrvc){
        this.stdSrvc = stdSrvc;
    }

    //bu method id ile ogrc returnn eden service methodu call edecek
    @GetMapping(path = "/selectStudentById/{id}")
    public StudentBean05 selectStudentById(@PathVariable Long id){

        return stdSrvc.selectStudentById(id);
    }
}
