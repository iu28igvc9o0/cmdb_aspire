package com.migu.tsg.microservice.atomicservice.composite.common.sql;

import java.util.ArrayList;
import java.util.List;

public class CompCondition implements Condition {
    protected CompCondition parent;
    protected List<Condition> conds = new ArrayList<>();

    protected CompCondition getParent() {
        return parent;
    }

    protected void addCond(Condition cond) {
        if (cond instanceof CompCondition) {
            ((CompCondition) cond).parent = this;
        }

        conds.add(cond);
    }

    protected void removeLast() {
        conds.remove(conds.size() - 1);
    }

    protected List<Condition> getSubConditions() {
        return conds;
    }
}
