package io.proj3ct.tegridycoffeebot.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;

@Getter
@Setter
@Entity(name = "userDataTable")
public class User {
    @Id
    private Long chatId;
    private String userName;
    private Boolean banFlag;

}
