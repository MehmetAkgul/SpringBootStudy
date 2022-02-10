package com.TechPro.SpringBootStudy.basic_authentication;

import net.bytebuddy.implementation.bytecode.Throw;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Optional;

@Service
public class StudenBean05Service {
    private StudentBean05Repository studentRepo;//Repository layer'a ulaşmak için data type'nan obj create edildi.

    //obj degerini cons'dan alacak
    @Autowired
    public StudenBean05Service(StudentBean05Repository studentRepo) {
        this.studentRepo = studentRepo;
    }




    //Bu method id ile ögrc return edecek
    public StudentBean05 selectStudentById(Long id) {
        // return studentRepo.findById(id).get();--> olmayan id hata verir code kırlrı bununiçin kontrol if çalışmalı
        if (studentRepo.findById(id).isPresent()) {

            return studentRepo.findById(id).get();
        }
        return new StudentBean05();//isteen id yoksa bos cons run edilecek
    }//service layer'de repository'den alınan datalar methodda çalıştırıldı. bu metthod controlle layer'da call edilmeli


    public List<StudentBean05> selectAllStudent(){
        return studentRepo.findAll();
    }


    public StudentBean05 updateFullyStudentById(Long id, StudentBean05 fullupdatedStudent) {

        StudentBean05 oldStudent = studentRepo
                .findById(id)
                .orElseThrow(() -> new IllegalStateException("Aradığınız id'ye ait öğrenci yok "));

        ///name update edilecek


        if (fullupdatedStudent.getName() == null) {//kullanıcı yeni bir isim girmezse
            oldStudent.setName("");//eski ogrc ismini bos bırak
        } else if (!oldStudent.getName().equals(fullupdatedStudent.getName())) {//kullanıcının ogrc isimi ile eski ogrc ismi farklı ise
            oldStudent.setName(fullupdatedStudent.getName());//kullanıcı ogrc ismi eski ogrc ismine ata
        }


        //email aupdate edilecek

        Optional<StudentBean05> studentEmail = studentRepo.findStudentBean04ByEmail(fullupdatedStudent.getEmail());
        if (studentEmail.isPresent()) {
            throw new IllegalStateException("bu mail kayıtlı ");
        } else if (!fullupdatedStudent.getEmail().contains("@")) {
            throw new IllegalStateException("mail geçerli değil==>  '@'  karakterini kullanmalısınız!!!  ");
        } else if (fullupdatedStudent.getEmail() == null) {
            throw new IllegalStateException("mail adresi girmek zorunlu ");
        } else if (!fullupdatedStudent.getEmail().equals(oldStudent.getEmail())) {
            oldStudent.setEmail(fullupdatedStudent.getEmail());
        } else {
            throw new IllegalStateException("aynı e mail update edilmez");
        }


        //dob aupdate edilecek
//dob aupdate edilecek
/*
1) girilen dob gelecekten olmamalı hayatın akısına ters oldg için excp
2) girilen dop yanı olmamalı gereksia işelem için excp
 */
        if (Period.between(fullupdatedStudent.getDob(), LocalDate.now()).isNegative()) {//1. sart kontrol edildi
            throw new IllegalStateException("hatalı dob giridiniz");

        } else if (!fullupdatedStudent.getDob().equals(oldStudent.getDob())) {//2. sart kontrol edildi
            oldStudent.setDob(oldStudent.getDob());

        }

        return studentRepo.save(oldStudent);
    }
}
