package com.aspire.fileCheck.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class RemoteFile {
    private String fileName;
    private double fileSize;
    private Date modifyDate;
}
