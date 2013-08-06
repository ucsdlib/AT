package org.archiviststoolkit.exceptions;

public class Report {
    private String message="";
    private String name = "";
    private String email  = "";
    private String description = "";
    private String stacktrace = "";
    private String environment = "";
    
    public String  getMessage(){
        return message;
    }

    public void setMessage(String message){
        this.message = message;
        
    }

    public String  getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
        
    }
    
    public String  getEmail(){
        return email;
    }

    public void setEmail(String email){
        this.email = email;
        
    }
    
    public String  getDescription(){
        return description;
    }

    public void setDescription(String description){
        this.description = description;
        
    }
    
    public String  getStacktrace(){
        return stacktrace;
    }

    public void setStacktrace(String stacktrace){
        this.stacktrace = stacktrace;        
    }
    
    public String  getEnvironment(){
        return environment;
    }

    public void setEnvironment(String environment){
        this.environment = environment;
        
    }
    
    
}
