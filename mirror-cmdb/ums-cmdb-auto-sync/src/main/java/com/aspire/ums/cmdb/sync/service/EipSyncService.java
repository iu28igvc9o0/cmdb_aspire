package com.aspire.ums.cmdb.sync.service;

import java.util.List;

public interface EipSyncService<T> {

    void add(List<T> t);
    void modify(List<T> t);
    void delete(List<T> t);
}
