package com.aspire.mirror.alert.api.dto.alert;

import java.util.List;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 描述：
 *
 * @author
 * @date 2019-08-14 19:35:33
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class KpiData {

	 @NotBlank
	private String idc_type;

	private List<String> pod;
	private List<String> roomId;

	 @NotEmpty
	private List<String> keys;

	private Integer id;
	
	private Integer kpi_id;
}