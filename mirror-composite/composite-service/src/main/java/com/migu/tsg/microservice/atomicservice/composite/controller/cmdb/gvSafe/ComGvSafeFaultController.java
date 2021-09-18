package com.migu.tsg.microservice.atomicservice.composite.controller.cmdb.gvSafe;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.aspire.mirror.composite.service.cmdb.gvSafe.IComGvSafeFaultAPI;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.gvSafe.GvSafeFaultClient;
import com.migu.tsg.microservice.atomicservice.composite.controller.authcontext.RequestAuthContext;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class ComGvSafeFaultController implements IComGvSafeFaultAPI {

    @Autowired
    private GvSafeFaultClient gvSafeFaultClient;

	@Override
	//@ResAction(action = "view", resType = "gvSafeFault", loadResFilter=true)
	public Map<String,Object> saveGvSafeFault(@RequestBody Map<String,Object> request) {
		log.info("saveGvSafeFault-begin");
		RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
		String userName = authCtx.getUser().getUsername();
		return gvSafeFaultClient.saveGvSafeFault(request, userName);
	}

	

   

}
