package com.fan.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class User implements Serializable {
    private int id;
    private String name;
    private String pwd;
    private String perms;
    private Date sign;
}
