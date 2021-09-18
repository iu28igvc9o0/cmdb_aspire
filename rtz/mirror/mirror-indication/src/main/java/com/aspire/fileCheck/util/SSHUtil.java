package com.aspire.fileCheck.util;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import com.aspire.common.FactoryUtils;
import com.aspire.fileCheck.entity.RemoteFile;
import com.aspire.mirror.entity.IndicationProvinceEntity;
import com.aspire.mirror.service.IIndicationProvinceService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.Charsets;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class SSHUtil {

    private static Map<String, Connection> CONNECTION_MAP = new HashMap<String, Connection>();
    private static Map<String, IndicationProvinceEntity> PROVINCE_MAP = new HashMap<String, IndicationProvinceEntity>();

    public static Session openSession(String server, String userName, String passWord) {
        Session session = null;
        try {
            Connection connection;
            if (CONNECTION_MAP.containsKey(server)) {
                connection = CONNECTION_MAP.get(server);
            } else {
                connection = new Connection(server);
                CONNECTION_MAP.put(server, connection);
            }
            if (!connection.isAuthenticationComplete()) {
                connection.connect();
                boolean isAuthenticate = connection.authenticateWithPassword(userName, passWord);
                if (!isAuthenticate) {
                    log.error("Login server fail. server {} user {} pwd {}", server, userName, passWord);
                    return null;
                }
            }
            session = connection.openSession();
        } catch (IOException e) {
            log.error("Open session error.", e);
            closeSession(session);
        }
        return session;
    }

    public static void closeSession(Session session) {
        if (session != null) {
            session.close();
            session = null;
        }
    }

    public static List<RemoteFile> getRemoteFiles(String provinceCode, String fileDirectory) {
        IndicationProvinceEntity entity;
        if (PROVINCE_MAP.containsKey(provinceCode)) {
            entity = PROVINCE_MAP.get(provinceCode);
        } else {
            List<IndicationProvinceEntity> list = FactoryUtils.getBean(IIndicationProvinceService.class).getAllProvince();
            for (IndicationProvinceEntity provinceEntity : list) {
                PROVINCE_MAP.put(provinceEntity.getProvinceCode(), provinceEntity);
            }
            entity = PROVINCE_MAP.get(provinceCode);
        }
        return getRemoteFiles(entity, fileDirectory);
    }

    public static List<RemoteFile> getRemoteFiles(IndicationProvinceEntity provinceEntity, String fileDirectory) {
        List<RemoteFile> remoteFileList = new ArrayList<RemoteFile>();
        Session session = openSession(provinceEntity.getProvinceServerIp(), provinceEntity.getProvinceServerUser(),
                provinceEntity.getProvinceServerPwd());
        if (session != null) {
            fileDirectory = provinceEntity.getProvinceFileDir() + fileDirectory;
            StringBuilder cmdBuilder = new StringBuilder();
            cmdBuilder.append("ls ").append(fileDirectory);
            cmdBuilder.append(" --full-time | awk '{print $5\" \"$6\" \"$7\" \"$9\" \"}';");
            try {
                log.info("Execute linux command -> {}", cmdBuilder.toString());
                session.execCommand(cmdBuilder.toString(), Charsets.UTF_8.name());
                formatCmdResult(remoteFileList, session.getStdout());
                log.info("Find directory {} file size {}.", fileDirectory, remoteFileList.size());
            } catch (IOException e) {
                log.error("execute command {} error.", cmdBuilder.toString(), e);
            } finally {
                closeSession(session);
            }
        } else {
            log.error("Open session error. server {} user {} pwd {}", provinceEntity.getProvinceServerIp(),
                    provinceEntity.getProvinceServerUser(), provinceEntity.getProvinceServerPwd());
        }
        return remoteFileList;
    }

    private static void formatCmdResult(List<RemoteFile> remoteFileList, InputStream stdout) {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(stdout));
            while (true) {
                String line = br.readLine();
                if (line == null) {
                    break;
                }
                if (StringUtils.isBlank(line.trim())) {
                    continue;
                }
                String[] lineArray = line.split(" ");
                String yyyyMMdd = lineArray[1];
                String hhmmss = lineArray[2].split("\\.")[0];
                RemoteFile remoteFile = new RemoteFile();
                BigDecimal b1 = new BigDecimal(lineArray[0]);
                BigDecimal b2 = new BigDecimal(1024 * 1024);
                double fileSize = b1.divide(b2, 2, BigDecimal.ROUND_UP).doubleValue();
                remoteFile.setFileSize(fileSize);
                remoteFile.setModifyDate(DateUtils.parseDate(yyyyMMdd + " " + hhmmss, new String[]{"yyyy-MM-dd HH:mm:ss"}));
                remoteFile.setFileName(lineArray[3]);
                remoteFileList.add(remoteFile);
            }
        } catch (IOException e) {
            log.error("Read command return line error.", e);
        } catch (ParseException e) {
            log.error("Parse date error.", e);
        }
    }
}
