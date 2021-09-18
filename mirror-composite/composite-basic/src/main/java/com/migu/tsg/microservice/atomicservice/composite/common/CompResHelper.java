package com.migu.tsg.microservice.atomicservice.composite.common;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;

import com.migu.tsg.microservice.atomicservice.composite.Constants;
import com.migu.tsg.microservice.atomicservice.composite.dao.po.CompositeResource;

public class CompResHelper {
    public static final char FIELD_SEP = ':';

    private static final int SOURCE = 0;
    private static final int TARGET = 1;
    private static final int QUERYSIZE = 100;

    public static List<String> getUuids(List<CompositeResource> ress, String separator) {
		int size = ress.size();
		int count = 0;
//		if (ress == null || size == 0) {
		if (size == 0) {
			return new ArrayList<String>();
		}
		List<String> result = new ArrayList<String>();
		if (size > QUERYSIZE) {
			count = size / QUERYSIZE;
		}
		for (int x = 0; x < count; x++) {
			StringBuffer sb = new StringBuffer();
			for (int i = QUERYSIZE * x; i < QUERYSIZE * (x + 1); i++) {
				sb.append(ress.get(i).getUuid());
				sb.append(separator);
			}
			result.add(sb.substring(0, sb.length() - 1));
		}
		if (count * QUERYSIZE != size) {
			StringBuffer sbLatest = new StringBuffer();
			for (int i = count * QUERYSIZE; i < size; i++) {
				sbLatest.append(ress.get(i).getUuid());
				sbLatest.append(separator);
			}
			result.add(sbLatest.substring(0, sbLatest.length() - 1));
		}
		return result;
	}

    public static List<String> getUuids(List<CompositeResource> ress) {
        return getUuids(ress, ",");
    }

    public static void copyProps(List<?> sourceList, List<?> targetList, String[] copyFields,
            String[] pkFields) {
        if (sourceList == null || sourceList.isEmpty() || targetList == null || targetList.isEmpty()) {
            return;
        }

        Class<?> clazz = sourceList.get(0).getClass();
        PropertyDescriptor[][] sourceProps = getPropDescs(clazz, getFields(copyFields, SOURCE));
        PropertyDescriptor[][] sourcePks = getPropDescs(clazz, getFields(pkFields, SOURCE));

        clazz = targetList.get(0).getClass();
        PropertyDescriptor[][] targetProps = getPropDescs(clazz, getFields(copyFields, TARGET));
        PropertyDescriptor[][] targetPks = getPropDescs(clazz, getFields(pkFields, TARGET));

        Object[] sps;
        Object[] tps;

        try {
            for (Object target : targetList) {
                tps = getPropVals(target, targetPks);

                for (Object source : sourceList) {
                    sps = getPropVals(source, sourcePks);
                    if (Arrays.equals(sps, tps)) {
                        copyProps(source, sourceProps, target, targetProps);
                        break;
                    }
                }
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Copy bean properties error", e);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Copy bean properties error", e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException("Copy bean properties error", e);
        } catch (Exception e) {
            throw new RuntimeException("Copy bean properties error", e);
        }
    }

    public static Object getProperty(Object obj, String propertyName) {
        PropertyDescriptor pd = BeanUtils.getPropertyDescriptor(obj.getClass(), propertyName);

        if (pd != null) {
            try {
                return pd.getReadMethod().invoke(obj);
            } catch (Exception e) {
                throw new RuntimeException("Get bean property error", e);
            }
        }
        return null;
    }

    public static void setProperty(Object obj, String propertyName, Object value) {
        PropertyDescriptor pd = BeanUtils.getPropertyDescriptor(obj.getClass(), propertyName);

        if (pd != null) {
            try {
                pd.getWriteMethod().invoke(obj, value);
            } catch (Exception e) {
                throw new RuntimeException("Set bean property error", e);
            }
        }
    }

    public static boolean equalsTo(CompositeResource res, Map<String, Object> m, boolean strict) {
        Object val;
        PropertyDescriptor pd;
        for (Map.Entry<String, Object> entry : m.entrySet()) {
            pd = BeanUtils.getPropertyDescriptor(CompositeResource.class, entry.getKey());
            if (strict && pd == null) {
                return false;
            }

            val = getProperty(res, entry.getKey());
            if (val != null ? !val.equals(entry.getValue()) : entry.getValue() != null) {
                return false;
            }
        }

        return true;
    }

    public static boolean equalsTo(CompositeResource res, Map<String, Object> m) {
        return equalsTo(res, m, true);
    }

    public static CompositeResource findResource(List<CompositeResource> ress, Map<String, Object> m) {
        for (CompositeResource res : ress) {
            if (equalsTo(res, m)) {
                return res;
            }
        }

        return null;
    }

    public static CompositeResource findResource(List<CompositeResource> ress, String type, String name) {
        Map<String, Object> m = new HashMap<>(2);
        m.put(Constants.ResourceColumn.TYPE, type);
        m.put(Constants.ResourceColumn.NAME, name);

        return findResource(ress, m);
    }

    public static CompositeResource findResourceByUuid(List<CompositeResource> ress, String type, String uuid) {
        Map<String, Object> m = new HashMap<>(2);
        m.put(Constants.ResourceColumn.TYPE, type);
        m.put(Constants.ResourceColumn.UUID, uuid);

        return findResource(ress, m);
    }

    private static String[] getFields(String[] fs, int index) {
        String[] nfs = new String[fs.length];

        String f;
        int n;
        for (int i = 0; i < fs.length; i++) {
            f = fs[i];

            n = f.indexOf(FIELD_SEP);
            if (n < 0) {
                nfs[i] = f;
            } else if (index == SOURCE) {
                nfs[i] = f.substring(0, n);
            } else {
                nfs[i] = f.substring(n + 1);
            }
        }

        return nfs;
    }

    private static PropertyDescriptor[][] getPropDescs(Class<?> clazz, String[] fields) {
        PropertyDescriptor[][] props = new PropertyDescriptor[fields.length][];

        String[] fs;
        PropertyDescriptor[] ps;
        Class<?> c;
        PropertyDescriptor p;
        for (int i = 0; i < fields.length; i++) {
            fs = fields[i].split("\\.");
            ps = new PropertyDescriptor[fs.length];
            c = clazz;
            for (int j = 0; j < fs.length; j++) {
                p = BeanUtils.getPropertyDescriptor(c, fs[j]);
                ps[j] = p;
                c = p.getReadMethod().getReturnType();
            }

            props[i] = ps;
        }

        return props;
    }

    private static Object[] getPropVals(Object obj, PropertyDescriptor[][] props)
            throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Object[] sps = new Object[props.length];

        for (int i = 0; i < props.length; i++) {
            sps[i] = getPropVal(obj, props[i]);
        }

        return sps;
    }

    private static Object getPropVal(Object obj, PropertyDescriptor[] prop)
            throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {

        Object val = obj;
        for (int i = 0; i < prop.length; i++) {
            val = prop[i].getReadMethod().invoke(val);
        }

        return val;
    }

    private static void setPropVal(Object target, PropertyDescriptor[] prop, Object val)
            throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {

        Object obj = target;
        for (int i = 0; i < prop.length - 1; i++) {
            obj = prop[i].getReadMethod().invoke(obj);
        }

        prop[prop.length - 1].getWriteMethod().invoke(obj, val);
    }

    private static void copyProps(Object source, PropertyDescriptor[][] sps, Object target, PropertyDescriptor[][] tps)
            throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Object val;

        for (int i = 0; i < sps.length; i++) {
            val = getPropVal(source, sps[i]);
            setPropVal(target, tps[i], val);
        }
    }
}