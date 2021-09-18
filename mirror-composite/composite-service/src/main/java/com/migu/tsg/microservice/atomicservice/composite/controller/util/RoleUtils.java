package com.migu.tsg.microservice.atomicservice.composite.controller.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.migu.tsg.microservice.atomicservice.composite.Constants;
import com.migu.tsg.microservice.atomicservice.composite.common.CompResHelper;
import com.migu.tsg.microservice.atomicservice.composite.controller.authcontext.RequestConstraintEnum;
import com.migu.tsg.microservice.atomicservice.composite.dao.CompositeResourceDao;
import com.migu.tsg.microservice.atomicservice.composite.dao.po.CompositeResource;

@Component
public class RoleUtils {
    @Autowired
    protected CompositeResourceDao resDao;

    @SuppressWarnings("unchecked")
    public Map<String, String> getConstraints(Pair<String, String>... ps) {
        Map<String, String> m = new HashMap<>();

        RequestConstraintEnum c;
        for (Pair<String, String> p : ps) {
            c = RequestConstraintEnum.getEnumByName(p.getFirst());
            if (c != null) {
                m.put(c.name(), p.getSecond());
            }
        }

        return m;
    }

    public Map<String, String> getConstraints(String name, String value) {
        Map<String, String> constraints = new HashMap<>();

        RequestConstraintEnum c = RequestConstraintEnum.getEnumByName(name);
        if (c != null) {
            constraints.put(name, value);
        }

        return constraints;
    }

    public Map<String, String> addConstraints(Map<String, String> constraints, String name, String value) {
        if (constraints == null) {
            constraints = new HashMap<>();
        }

        RequestConstraintEnum c = RequestConstraintEnum.getEnumByName(name);
        if (c != null) {
            constraints.put(name, value);
        }

        return constraints;
    }

    public Map<String, Object> buildRbacResource(Map<String, Object> data, String action,
            Map<String, String> constraints) {
        String resType = Constants.RESOURCE_TYPE_MAP.get(data.get("type"));
        if (resType == null) {
            throw new RuntimeException("Requested type is not registered");
        }
        
        Map<String, Object> result = new HashMap<>(data);
        String resAction = Constants.RESOURCE_ACTION_MAP.get(resType).get(action);
        if (resAction == null) {
            throw new RuntimeException("Required resource action does not exist");
        }

        result.put("action", resAction);

        result.putAll(constraintConvert(constraints));

        return result;
    }

    public Map<String, Object> buildRbacResource(Map<String, Object> data, String action) {
        return buildRbacResource(data, action, null);
    }

    public void addBulkResourcesUuidToList(List<Map<String, Object>> rbacRess, List<CompositeResource> ress) {
        for (CompositeResource res : ress) {
            addResourceUuidToList(rbacRess, res);
        }
    }

    public void addResourceUuidToList(List<Map<String, Object>> rbacRess, CompositeResource res) {
        Map<String, String> constraints;
        Map<String, String> consts;
        for(Map<String, Object> rbacRes: rbacRess) {
            if (CompResHelper.equalsTo(res, rbacRes, false) && StringUtils.isEmpty(rbacRes.get("uuid"))) {
                rbacRes.put("uuid", res.getUuid());

                constraints = getConstraintsFromResource(res);
                consts = (Map<String, String>) rbacRes.get("constraints");
                if (consts != null) {
                    constraints.putAll(consts);
                }
                rbacRes.put("constraints", constraints);
            }
        }
    }
/////////////////////////////////////////////////////////////////////////////////////
    public Map<String, String> getConstraintsFromResource(CompositeResource res) {
        Map<String, String> cons = new HashMap<>();

        String s = res.getRegionId();
        if (s != null && s.length() > 0) {
            cons.putAll(getConstraints("cluster_name", getResName(s)));
        }

        s = res.getSpaceUuid();
        if (s != null && s.length() > 0) {
            cons.putAll(getConstraints("space_name", getResName(s)));
        }

        s = res.getProjectUuid();
        if (s != null && s.length() > 0) {
            cons.putAll(getConstraints("project_name", getResName(s)));
        }

        return cons;
    }

    public String getResName(String uuid) {
        CompositeResource res = resDao.queryByUuid(uuid);

        if (res != null) {
            return res.getName();
        }

        return null;
    }
 /////////////////////////////////////////////////////////////////////////////////////////////////////// 
    public Map<String, String> getConstraintsByResource(CompositeResource res) {
        Map<String, String> cons = new HashMap<>();

        String s = res.getRegionId();
        if (s != null && s.length() > 0) {
            cons.putAll(getConstraints("cluster_name", getResourceName(s, res.getNamespace())));
        }

        s = res.getSpaceUuid();
        if (s != null && s.length() > 0) {
            cons.putAll(getConstraints("space_name", getResourceName(s, res.getNamespace())));
        }

        s = res.getProjectUuid();
        if (s != null && s.length() > 0) {
            cons.putAll(getConstraints("project_name", getResourceName(s, res.getNamespace())));
        }

        return cons;
    }
    public String getResourceName(String uuid, String namespace) {
        CompositeResource res = resDao.queryByUuidAndNamespace(uuid, namespace);

        if (res != null) {
            return res.getName();
        }

        return null;
    }
    
/////////////////////////////////////////////////////////////////////////////////////////////////////////
    private static Map<String, String> constraintConvert(Map<String, String> constraints) {
        if (constraints == null || constraints.size() == 0) {
            return Collections.EMPTY_MAP;
        }

        Map<String, String> m = new HashMap<>();

        for (Map.Entry<String, String> entry : constraints.entrySet()) {
            m.put(Constants.CONSTRAINT_KEY_MAP.get(entry.getKey()), entry.getValue());
        }

        return m;
    }
}