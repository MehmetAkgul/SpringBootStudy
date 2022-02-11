package com.TechPro.SpringBootStudy.basic_authentication;

import net.bytebuddy.implementation.bytecode.Throw;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.*;
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

        Optional<StudentBean05> studentEmail = studentRepo.findStudentBean05ByEmail(fullupdatedStudent.getEmail());
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

    //Bu met id ile data(student obj) delete edecek
    public String deletStudentById(Long id){
        if (!studentRepo.existsById(id)){//id'si verilen obj'nin DB'de varlıgını kontrol eder-->id'li ogrc yoksa code excute stop App stop
            throw new IllegalStateException("AGAM niddin "+id+" li ogrc araziii");
        }
        studentRepo.deleteById(id);//id'Li oğrs repodan call edip delet eder
        return  "AGAM "+id+"li ogrc sizlere omur...";//action hakkında bilgi verir
    }//bu method run için controller da call edilmeli


    public StudentBean05 updatePatchStudentById(Long id, StudentBean05 newStudent){

        StudentBean05 existingStudentById= studentRepo.
                findById(id).orElseThrow(()-> new IllegalStateException(id+"'li ogrenci yok")); //Lambda expression

        // student email update edilecek BRD
        /*
        brd:

        1) email tekararlı olmaz uniq-->EXCEPTION
        2) email gecerli (@ içermeli) olmalı-->EXCEPTION
        3) email null olamaz -->EXCEPTION
        4) email eski ve yeni aynı ise gereksiz işlem için update etmemeli
         */
        if (newStudent.getEmail()==null){
            newStudent.setEmail("");
        }
        Optional<StudentBean05> emailOLaneskiOgrc = studentRepo.findStudentBean05ByEmail(newStudent.getEmail());
        if (emailOLaneskiOgrc.isPresent()) {//1. sart kontrol edildi eger emailolanogc containerde varsa
            throw new IllegalStateException("daha once bu email kullanıldı");
        }else if (!newStudent.getEmail().contains("@")&& newStudent.getEmail()!=""){//2 . sart kontrol edilecek
            throw new IllegalStateException("@ karakteri kullanmalısınız");
        }else if(newStudent.getEmail()==null){//3. sart kontrol edilecek
            throw new IllegalStateException("mutlaka bir email girmelisiniz");
        }else if(!newStudent.getEmail().equals(existingStudentById.getEmail())){
            existingStudentById.setEmail(newStudent.getEmail());
        }else {
            throw new IllegalStateException("aynı e mail update edilmez");
        }

        return studentRepo.save(existingStudentById); // update edilecek ogrc action sonrasi save edilerek return edilir
    }


    public StudentBean05 addStudent1(StudentBean05 newStudent) throws ClassNotFoundException, SQLException {
        //ogrc email datası girilecek
        //e mail tekrarsız olmalı BRD
        Optional<StudentBean05> existingStudenById= studentRepo.findStudentBean05ByEmail(newStudent.getEmail());
        if (existingStudenById.isPresent()){//eski ogrc email varsa exc
            throw new IllegalStateException("AGAM bu "+newStudent.getEmail()+" 2. el sana ajente bir imeyıl lazım");

        }
        //ogrc name datası giriliecek
        if (newStudent.getName()==null) {//yeni ogrc henus name girmemis-->excp
            throw new IllegalStateException("AGAM adın yoksa sen de yoksun  :-( ");
        }

        //her yeni ogrc için app uniq id  cretae etmeli...
    /*
    LOGİC : DB'de varolan max id get edip +1 hali yeni id assaign edilmeli
     */
        //DB'ye JDBC connection ...
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/sys?serverTimezone=UTC");
        Statement st = con.createStatement();

        //max id get içinSQL query komutla
        String sqlQueryForMaxId="select max(id) from students";//birden cok (id tek olacagı içim bizm sorguda tek verir)sonuc  satır verir
        ResultSet result= st.executeQuery(sqlQueryForMaxId);//query sonrası satırlerı return eder loop ile istene  satır alınır
        Long maxId=0l;
        while(result.next()){//next() pointer bir sonraki satıra gider onceki satur return eder
            result.getLong(1);
        }
        newStudent.setId(maxId+1);
        newStudent.setAge(newStudent.getAge());
        newStudent.setErrMsg("AGAM müjde nur topu gibi ogrencin oldii");

        return studentRepo.save(newStudent);
    }

}
