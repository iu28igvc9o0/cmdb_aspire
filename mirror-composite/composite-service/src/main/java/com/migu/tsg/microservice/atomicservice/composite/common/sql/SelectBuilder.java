package com.migu.tsg.microservice.atomicservice.composite.common.sql;

import java.util.ArrayList;
import java.util.List;

public class SelectBuilder implements SqlStatementBuilder {
    private WhereBuilder<SelectBuilder> whereBuild;
    // private String orderBy;

    public WhereBuilder<SelectBuilder> where(boolean and) {
        whereBuild = new WhereBuilder<SelectBuilder>(this);
        return whereBuild;
    }

    public WhereBuilder<SelectBuilder> where() {
        whereBuild = new WhereBuilder<SelectBuilder>(this);
        return whereBuild;
    }

    public List<ConditionPiece> build() {
        if (whereBuild == null) {
            return new ArrayList<ConditionPiece>();
        }
        return whereBuild.build();
    }
}
