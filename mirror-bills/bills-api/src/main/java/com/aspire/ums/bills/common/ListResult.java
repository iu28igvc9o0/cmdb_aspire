package com.aspire.ums.bills.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ListResult<A> {

    private int count;

    private List<A> data;
}
