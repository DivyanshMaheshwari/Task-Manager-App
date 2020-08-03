package com.example.task_manager_app;

public class ProListView {

    public String ProjectName;
    public String Projectdescription;
    // public int ProjectImage;
    public  ProListView(String proname,String prodes){
        this.ProjectName=proname;
        this.Projectdescription=prodes;
    }

    public String getProjectName() {

        return ProjectName;
    }

    public String getProjectdescription() {

        return Projectdescription;
    }


    public void setProjectName(String projectName) {

        ProjectName = projectName;
    }

    public void setProjectdescription(String projectdescription) {

        Projectdescription = projectdescription;
    }


}
