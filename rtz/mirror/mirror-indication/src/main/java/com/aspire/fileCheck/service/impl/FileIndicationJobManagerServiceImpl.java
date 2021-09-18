package com.aspire.fileCheck.service.impl;

import com.aspire.fileCheck.dao.IFileIndicationJobManagerDAO;
import com.aspire.fileCheck.entity.FileIndicationJobManagerEntity;
import com.aspire.fileCheck.service.IFileIndicationJobManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FileIndicationJobManagerServiceImpl implements IFileIndicationJobManagerService {

    @Autowired
    private IFileIndicationJobManagerDAO jobManagerDAO;

    @Override
    public List<FileIndicationJobManagerEntity> getJobManagerByJobName(String jobName) {
        return jobManagerDAO.getJobManagerByJobName(jobName);
    }

    @Override
    public FileIndicationJobManagerEntity getLatestJobManagerByJobName(String jobName) {
        return jobManagerDAO.getLatestJobManagerByJobName(jobName);
    }

    @Override
    public void insertEntity(FileIndicationJobManagerEntity entity) {
        jobManagerDAO.insertEntity(entity);
    }

    @Override
    public void updateEntity(FileIndicationJobManagerEntity entity) {
        jobManagerDAO.updateEntity(entity);
    }
}
