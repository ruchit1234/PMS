package com.kitelytech.pmsapp.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Taskmsg {
    @SerializedName("Project_Name")
    @Expose
    private String projectName;
    @SerializedName("Managers")
    @Expose
    private String managers;
    @SerializedName("Task_Id")
    @Expose
    private Integer taskId;
    @SerializedName("Task_Name")
    @Expose
    private String taskName;

    public String getComment() {
        return comment;
    }

    @SerializedName("Comment")
    @Expose
    private String comment;
    @SerializedName("Task_Description")
    @Expose
    private String taskDescription;
    @SerializedName("Dev_Estimated_Hours")
    @Expose
    private String devEstimatedHours;
    @SerializedName("QA_Estimated_Hours")
    @Expose
    private String qAEstimatedHours;
    @SerializedName("Task_Created_By")
    @Expose
    private String taskCreatedBy;

    public String getEmp_Email() {
        return Emp_Email;
    }

    @SerializedName("Emp_Email")
    @Expose
    private String Emp_Email;
    @SerializedName("Task_Created_time")
    @Expose
    private String taskCreatedTime;
    @SerializedName("Task_Assign_To")
    @Expose
    private Integer taskAssignTo;
    @SerializedName("Task_Assign_time")
    @Expose
    private String taskAssignTime;
    @SerializedName("Project_Id")
    @Expose
    private Integer projectId;
    @SerializedName("Task_Status")
    @Expose
    private Integer taskStatus;
    @SerializedName("Task_Level")
    @Expose
    private Integer taskLevel;
    @SerializedName("Chargeables_Hours")
    @Expose
    private Integer chargeablesHours;
    @SerializedName("Emp_First_Name")
    @Expose
    private String empFirstName;
    @SerializedName("Emp_Last_Name")
    @Expose
    private String empLastName;
    @SerializedName("Task_Type")
    @Expose
    private Integer taskType;
    @SerializedName("Hours_Exceed_Flag")
    @Expose
    private Integer hoursExceedFlag;
    @SerializedName("Total_Hours")
    @Expose
    private String totalHours;

    public String getFilename() {
        return filename;
    }

    @SerializedName("File_Name")
    @Expose
    private String filename;

    public String getEmpid() {
        return empid;
    }

    @SerializedName("Emp_Id")
    @Expose
    private String empid;

    public String getqAEstimatedHours() {
        return qAEstimatedHours;
    }

    public String getMessage() {
        return message;
    }

    public String getTBD_Id() {
        return TBD_Id;
    }

    @SerializedName("TBD_Id")
    private String TBD_Id;

    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("Working_Total_Hours")
    @Expose
    private String workingTotalHours;
    @SerializedName("Start_Date_time")
    @Expose
    private Object startDateTime;

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getManagers() {
        return managers;
    }

    public void setManagers(String managers) {
        this.managers = managers;
    }

    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public String getDevEstimatedHours() {
        return devEstimatedHours;
    }

    public void setDevEstimatedHours(String devEstimatedHours) {
        this.devEstimatedHours = devEstimatedHours;
    }

    public String getQAEstimatedHours() {
        return qAEstimatedHours;
    }

    public void setQAEstimatedHours(String qAEstimatedHours) {
        this.qAEstimatedHours = qAEstimatedHours;
    }

    public String getTaskCreatedBy() {
        return taskCreatedBy;
    }

    public void setTaskCreatedBy(String taskCreatedBy) {
        this.taskCreatedBy = taskCreatedBy;
    }

    public String getTaskCreatedTime() {
        return taskCreatedTime;
    }

    public void setTaskCreatedTime(String taskCreatedTime) {
        this.taskCreatedTime = taskCreatedTime;
    }

    public Integer getTaskAssignTo() {
        return taskAssignTo;
    }

    public void setTaskAssignTo(Integer taskAssignTo) {
        this.taskAssignTo = taskAssignTo;
    }

    public String getTaskAssignTime() {
        return taskAssignTime;
    }

    public void setTaskAssignTime(String taskAssignTime) {
        this.taskAssignTime = taskAssignTime;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public Integer getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(Integer taskStatus) {
        this.taskStatus = taskStatus;
    }

    public Integer getTaskLevel() {
        return taskLevel;
    }

    public void setTaskLevel(Integer taskLevel) {
        this.taskLevel = taskLevel;
    }

    public Integer getChargeablesHours() {
        return chargeablesHours;
    }

    public void setChargeablesHours(Integer chargeablesHours) {
        this.chargeablesHours = chargeablesHours;
    }

    public String getEmpFirstName() {
        return empFirstName;
    }

    public void setEmpFirstName(String empFirstName) {
        this.empFirstName = empFirstName;
    }

    public String getEmpLastName() {
        return empLastName;
    }

    public void setEmpLastName(String empLastName) {
        this.empLastName = empLastName;
    }

    public Integer getTaskType() {
        return taskType;
    }

    public void setTaskType(Integer taskType) {
        this.taskType = taskType;
    }

    public Integer getHoursExceedFlag() {
        return hoursExceedFlag;
    }

    public void setHoursExceedFlag(Integer hoursExceedFlag) {
        this.hoursExceedFlag = hoursExceedFlag;
    }

    public String getTotalHours() {
        return totalHours;
    }

    public void setTotalHours(String totalHours) {
        this.totalHours = totalHours;
    }

    public String getWorkingTotalHours() {
        return workingTotalHours;
    }

    public void setWorkingTotalHours(String workingTotalHours) {
        this.workingTotalHours = workingTotalHours;
    }

    public Object getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(Object startDateTime) {
        this.startDateTime = startDateTime;
    }

}
