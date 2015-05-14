package org.springframework.social.salesforce.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;



/**
 * @author Umut Utkan
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class SalesforceProfile implements Serializable {


    private String id;

    private String email;

    private String firstName;

    private String lastName;


//    @JsonCreator()
//    public SalesforceProfile(String id, String email, String firstName, String lastName) {
//        this.id = id;
//        this.email = email;
//        this.firstName = firstName;
//        this.lastName = lastName;
//    }




    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFullName() {
        return this.firstName + " " + this.lastName;
    }

    public String getUsername() {
        return this.id;
    }

}
