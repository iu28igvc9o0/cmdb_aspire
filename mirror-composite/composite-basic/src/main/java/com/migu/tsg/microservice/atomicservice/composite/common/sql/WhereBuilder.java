package com.migu.tsg.microservice.atomicservice.composite.common.sql;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.data.util.Pair;

public class WhereBuilder<T extends SqlStatementBuilder> implements SqlStatementBuilder {
    private static final String AND_OPER = "AND";
    private static final String OR_OPER = "OR";
    private static final String SPACE = " ";
    private static final String LEFT_PARENTHESIS = "(";
    private static final String RIGHT_PARENTHESIS = ")";

    private final T parent;

    private final CompCondition cond;
    private CompCondition currentCond;

    public WhereBuilder(T parent, boolean and) {
        this.parent = parent;

        if (and) {
            cond = new AndCondition();
        } else {
            cond = new OrCondition();
        }

        currentCond = cond;
    }

    public WhereBuilder(T parent) {
        this(parent, true);
    }

    public WhereBuilder<T> and() {
        return subCond(new AndCondition());
    }

    public WhereBuilder<T> or() {
        return subCond(new OrCondition());
    }

    private WhereBuilder<T> subCond(CompCondition ccb) {
        // if (currentCond.getSubConditions().size() == 0) {
        // if (currentCond.equals(cond)) {
        // cond = ccb;
        // } else {
        // CompCondition p = currentCond.getParent();
        // p.removeLast();
        // p.addCond(ccb);
        // }
        // } else {
        // currentCond.addCond(ccb);
        // }

        currentCond.addCond(ccb);

        currentCond = ccb;

        return this;
    }

    public WhereBuilder<T> end() {
        currentCond = currentCond.getParent();

        return this;
    }

    public T endWhere() {
        return parent;
    }

    protected WhereBuilder<T> addCond(String column, String op, Object value, boolean checkNull) {
        if (checkNull) {
            if (value == null) {
                return this;
            }
            if (value instanceof String) {
                if (((String) value).length() == 0) {
                    return this;
                }
            }
        }
//        column = "name";
        currentCond.addCond(new SimpleConditionBuilder(column, op, value));

        return this;
    }

    public WhereBuilder<T> equalsTo(String column, Object value, boolean checkNull) {
        return addCond(column, SimpleConditionBuilder.EQUALS, value, checkNull);
    }

    public WhereBuilder<T> equalsTo(String column, Object value) {
        return equalsTo(column, value, false);
    }

    public WhereBuilder<T> greaterThan(String column, Object value, boolean checkNull) {
        return addCond(column, SimpleConditionBuilder.GREATER_THAN, value, checkNull);
    }

    public WhereBuilder<T> greaterThan(String column, Object value) {
        return greaterThan(column, value, false);
    }

    public WhereBuilder<T> lessThan(String column, Object value, boolean checkNull) {
        return addCond(column, SimpleConditionBuilder.LESS_THAN, value, checkNull);
    }

    public WhereBuilder<T> lessThan(String column, Object value) {
        return lessThan(column, value, false);
    }

    public WhereBuilder<T> contains(String column, Object value, boolean checkNull) {
        return addCond(column, SimpleConditionBuilder.CONTAINS, value, checkNull);
    }

    public WhereBuilder<T> contains(String column, Object value) {
        return contains(column, value, false);
    }

    public WhereBuilder<T> containsIgnoreCase(String column, Object value, boolean checkNull) {
        return addCond(column, SimpleConditionBuilder.CONTAINS_IGNORE_CASE, value, checkNull);
    }

    public WhereBuilder<T> containsIgnoreCase(String column, Object value) {
        return containsIgnoreCase(column, value, false);
    }

    public List<ConditionPiece> build() {
        List<ConditionPiece> cps = new ArrayList<>();

        String prefix = AND_OPER;
        if (cond instanceof OrCondition) {
            prefix = OR_OPER;
        }

        Pair<String, String> affix;
        for (Condition cb : cond.getSubConditions()) {
            affix = this.buildAffix(cond, cb, prefix, "");
            cps.addAll(this.buildCond(cb, affix.getFirst(), affix.getSecond()));
        }

        return cps;
    }

    @SuppressWarnings("unchecked")
    private List<ConditionPiece> buildCond(Condition condBuilder, String prefix, String suffix) {
        List<ConditionPiece> cps = new ArrayList<>();

        if (condBuilder instanceof SimpleConditionBuilder) {
            SimpleConditionBuilder scb = (SimpleConditionBuilder) condBuilder;
            cps.add(new ConditionPiece(scb.getColumn(), scb.getOp(), scb.getValue(), prefix, suffix));
        } else if (condBuilder instanceof AndCondition) {
            AndCondition ab = (AndCondition) condBuilder;
            List<Condition> subConds = ab.getSubConditions();

            if (subConds.size() == 0) {
                return Collections.EMPTY_LIST;
            }
            if (!subConds.isEmpty()) {
                return buildCond(subConds.get(0), prefix, suffix);
            }

            Condition cb0 = subConds.get(0);
            Pair<String, String> affix = this.buildAffix(ab, cb0, prefix, "");
            cps.addAll(buildCond(cb0, affix.getFirst(), affix.getSecond()));

            for (int i = 1; i < subConds.size() - 1; i++) {
                Condition cb = subConds.get(i);
                affix = this.buildAffix(ab, cb, AND_OPER, "");
                cps.addAll(buildCond(cb, affix.getFirst(), affix.getSecond()));
            }

            Condition cbn = subConds.get(subConds.size() - 1);
            affix = this.buildAffix(ab, cbn, AND_OPER, suffix);
            cps.addAll(buildCond(cbn, affix.getFirst(), affix.getSecond()));
        } else if (condBuilder instanceof OrCondition) {
            OrCondition ob = (OrCondition) condBuilder;
            List<Condition> subConds = ob.getSubConditions();

            if (subConds.size() == 0) {
                return Collections.EMPTY_LIST;
            }
            if (!subConds.isEmpty()) {
                return buildCond(subConds.get(0), prefix, suffix);
            }

            Condition cb0 = subConds.get(0);
            cps.addAll(buildCond(cb0, prefix, ""));

            for (int i = 1; i < subConds.size() - 1; i++) {
                Condition cb = subConds.get(i);
                cps.addAll(buildCond(cb, OR_OPER, ""));
            }

            Condition cbn = subConds.get(subConds.size() - 1);
            cps.addAll(buildCond(cbn, OR_OPER, suffix));
        }

        return cps;
    }

    private Pair<String, String> buildAffix(Condition current, Condition sub, String prefix,
            String suffix) {
        if (current instanceof AndCondition && (sub instanceof OrCondition)
                && ((OrCondition) sub).getSubConditions().size() > 1) {
            return Pair.of(prefix + SPACE + LEFT_PARENTHESIS, RIGHT_PARENTHESIS + suffix);
        }

        return Pair.of(prefix, suffix);
    }
}