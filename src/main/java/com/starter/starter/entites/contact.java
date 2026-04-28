package com.starter.starter.entites;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "contact")
public class contact {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int cId;
    private String name;
    private String secondname;
    private String work;
    private String email;
    private String phone;
    private String image;
    @Column(length = 5000)
    private String description;

    @ManyToOne
    private user user; 
    // here is many to one relationship as one user can have many contacts but one contact can belong to only one user therefore we are using many to one relationship(see the e-r diagram)

    public user getUser() {
        return user;
    }

    public void setUser(user user) {
        this.user = user;
    }
    

        public contact() {
            super(); // default constructor
        }

    public int getcId() {
        return cId;
    }
    public void setcId(int cId) {
        this.cId = cId;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getSecondname() {
        return secondname;
    }
    public void setSecondname(String secondname) {
        this.secondname = secondname;
    }
    public String getWork() {
        return work;
    }
    public void setWork(String work) {
        this.work = work;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }


    //this override method is used to compare if the contact we have to delete is same as the contact present in the list
    @Override
    public boolean equals(Object obj) {
        return this.cId==((contact)obj).getcId();
    }

   
    
}
