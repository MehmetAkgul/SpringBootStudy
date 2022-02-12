package com.TechPro.SpringBootStudy.basic_authentication;

public enum ApplicationUserPermission {

    STUDENT_READ("studenr:read"), STUDENT_WRITE("studenr:write");

    private final String permission; // final variable olduğu için initial edilmeli bunu için consç ile ilişkilendirilmeli

    ApplicationUserPermission(String permission) {

        this.permission = permission;


    }
    public String getPermission() {//final permission veriable obj ile degerini okumak için getter method create edildi
        return permission;
    }

}
