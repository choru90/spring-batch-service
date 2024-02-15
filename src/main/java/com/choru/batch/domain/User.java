package com.choru.batch.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;


@Getter
@Entity
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "BINARY(16)")
    private UUID uid;

    @Column(length = 30)
    private String name;

    @Column(length = 100)
    private String email;

    @Column(length = 20)
    private String phoneNumber;

    @Column(length = 30)
    private String nickname;

    @Column(length = 100)
    private String photo;

    @Column
    private LocalDate joinDate;


    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    private LocalDateTime deletedAt;

    private User(String name, String email, String phoneNumber, String nickname, String photo, LocalDate joinDate){
        this.name =  name;
        this.email= email;
        this.phoneNumber = phoneNumber;
        this.nickname = nickname;
        this.photo = photo;
        this.joinDate = joinDate;
    }

    public static User of(String name, String email, String phoneNumber, String nickname, String photo, LocalDate joinDate){
        return new User(name, email, phoneNumber, nickname, photo, joinDate);
    }

    public void delete(){
        this.deletedAt = LocalDateTime.now();
    }

    protected User(){

    }
}
