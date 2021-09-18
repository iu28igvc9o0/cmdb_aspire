package com.aspire.mirror.service.impl;

import com.aspire.mirror.dao.IProcessDAO;
import com.aspire.mirror.entity.ProcessEntity;
import com.aspire.mirror.service.IProcessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IProcessServiceImpl implements IProcessService {

    @Autowired
    private IProcessDAO processDAO;

    @Override
    public List<ProcessEntity> getAllProcess() {
        return processDAO.getAllProcess();
    }

    @Override
    public ProcessEntity getProcessByIndicationId(Integer indicationId) {
        return processDAO.getProcessByIndicationId(indicationId);
    }

    @Override
    public void updateProcess(ProcessEntity processEntity) {
        processDAO.updateProcess(processEntity);
    }
}
