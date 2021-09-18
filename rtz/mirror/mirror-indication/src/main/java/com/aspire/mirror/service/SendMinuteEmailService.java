package com.aspire.mirror.service;

public interface SendMinuteEmailService {

	String autoSendMinuteEmail(String indicationOwner, String indicationCatalog,
			String indicationCycle, String indicationFrequency,
			String dateTime);

}
