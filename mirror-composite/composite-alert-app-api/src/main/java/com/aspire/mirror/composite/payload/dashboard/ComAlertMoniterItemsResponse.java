package com.aspire.mirror.composite.payload.dashboard;

import java.io.Serializable;
import java.util.Set;

import lombok.Data;
@Data
public class ComAlertMoniterItemsResponse  implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private String name;
	private Set<ComItemIndexDto> moniterItem;
}
