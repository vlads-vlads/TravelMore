package com.example.TravelMore.UserAccount;

import jakarta.persistence.*;


    @Entity
    @Table
    public class User {

        @Id
        @SequenceGenerator(
                name = "user_sequence",
                sequenceName = "user_sequence",
                allocationSize = 1
        )
        @GeneratedValue(
                strategy = GenerationType.SEQUENCE,
                generator = "user_sequence"
        )
        private int user_id;
        private String userName;
        private String userEmail;

        private String userPassword;

        public User() {
        }
        public User(String name, String email) {
            this.userName = name;
            this.userEmail = email;
        }

        public User(String name, String email, String password) {
            this.userName = name;
            this.userEmail = email;
            this.userPassword = password;
        }

        public int getUser_id() {
            return user_id;
        }

        public String getUser_name() {
            return userName;
        }

        public String getUser_email() {
            return userEmail;
        }

        public String getUser_password() {
            return userPassword;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public void setUserEmail(String userEmail) {
            this.userEmail = userEmail;
        }

        public void setUserPassword(String userPassword) {
            this.userPassword = userPassword;
        }
    }
