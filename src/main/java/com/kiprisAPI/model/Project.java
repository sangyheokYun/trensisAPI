package com.kiprisAPI.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class Project {
    public String projectName;
    public String author;
    public Date createdDate;

}