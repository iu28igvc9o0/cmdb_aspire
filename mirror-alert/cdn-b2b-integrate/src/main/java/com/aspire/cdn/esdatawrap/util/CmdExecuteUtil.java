package com.aspire.cdn.esdatawrap.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Map;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecuteResultHandler;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;
import org.apache.commons.exec.PumpStreamHandler;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.Maps;

import lombok.extern.slf4j.Slf4j;

/**
 * 命令执行工具类   <br/>
 * Project Name:spectre_agent
 * File Name:CmdExecuteUtil.java
 * Package Name:com.migu.tsg.microservice.atomicservice.spectre.util
 * ClassName: CmdExecuteUtil <br/>
 * date: 2018年12月20日 下午7:59:14 <br/>
 *
 * @author pengguihua
 * @since JDK 1.6
 */
@Slf4j
public final class CmdExecuteUtil {
	private static final int	NORMAL_EXIT		= 0;
	private static final int	EXCEPTION_EXIT	= 1;
	public static final String	DEFAULT_CHARSET	= "GBK";


    public static boolean executeCommand(String command) throws Exception {
        return executeCommand(CommandLine.parse(command), null);
    }

    public static boolean executeCommand(String command, File workDir) throws Exception {
        return executeCommand(CommandLine.parse(command), workDir);
    }

    public static boolean executeCommand(String[] cmdWithArgs) throws Exception {
        return executeCommand(buildFromCmdAndArgs(cmdWithArgs), null);
    }

    public static boolean executeCommand(String[] cmdWithArgs, File workDir) throws Exception {
        return executeCommand(buildFromCmdAndArgs(cmdWithArgs), workDir);
    }

    /**
     * 功能描述: 根据命令和参数构建  CommandLine 对象
     * <p>
     *
     * @param cmdWithArgs
     * @return
     */
    private static CommandLine buildFromCmdAndArgs(String[] cmdWithArgs) {
        CommandLine cmdLine = new CommandLine(cmdWithArgs[0]);
        if (cmdWithArgs.length > 1) {
            for (int i = 1; i < cmdWithArgs.length; i++) {
                cmdLine.addArgument(cmdWithArgs[i], false);
            }
        }
        return cmdLine;
    }

    /**
     * 执行命令，返回是否成功. <br/>
     * <p>
     * 作者： pengguihua
     *
     * @param cmdLine
     * @param workDir
     * @return
     * @throws Exception
     */
    public static boolean executeCommand(CommandLine cmdLine, File workDir) throws Exception {
        DefaultExecutor exec = new DefaultExecutor();
        exec.setExitValue(NORMAL_EXIT);
        if (workDir != null) {
            exec.setWorkingDirectory(workDir);
        }
        try {
            return exec.execute(cmdLine, System.getenv()) == NORMAL_EXIT;
        } catch (ExecuteException e) {
            return false;
        }
    }


    public static Pair<Boolean, String> executeCommandWithFeedback(String command) throws Exception {
        return executeCommandWithFeedback(CommandLine.parse(command), null, null);
    }

    public static Pair<Boolean, String> executeCommandWithFeedback(String command, File workDir) throws Exception {
        return executeCommandWithFeedback(CommandLine.parse(command), workDir, null);
    }

    public static Pair<Boolean, String> executeCommandNonBlockingWithFeedback(String command, File workDir, Map<String, String> env) throws Exception {
        return executeCommandNonBlockingWithFeedback(CommandLine.parse(command), workDir, env);
    }

    public static Pair<Boolean, String> executeCommandWithFeedback(String command, File workDir, Map<String, String> env) throws Exception {
        return executeCommandWithFeedback(CommandLine.parse(command), workDir, env);
    }
    public static Pair<Boolean, String> executeCommandWithFeedback(String[] cmdWithArgs) throws Exception {
        return executeCommandWithFeedback(buildFromCmdAndArgs(cmdWithArgs), null, null);
    }

    public static Pair<Boolean, String> executeCommandWithFeedback(String[] cmdWithArgs, File workDir) throws Exception {
        return executeCommandWithFeedback(buildFromCmdAndArgs(cmdWithArgs), workDir, null);
    }

    /**
     * 执行命令, 并返回响应内容, 如果执行成功，返回正常响应，否则返回错误响应. <br/>
     * <p>
     * 作者： pengguihua
     *
     * @param command
     * @return
     * @throws Exception
     */
    public static Pair<Boolean, String> executeCommandWithFeedback(CommandLine cmdLine, File workDir, Map<String, String> env) throws Exception {
        DefaultExecutor exec = new DefaultExecutor();
        if (workDir != null) {
            exec.setWorkingDirectory(workDir);
        }
        exec.setExitValue(NORMAL_EXIT);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PumpStreamHandler streamHandler = new PumpStreamHandler(outputStream, outputStream);
        exec.setStreamHandler(streamHandler);

        String cmdJoin = StringUtils.join(cmdLine.toStrings(), " ");
        String cmdLog = (workDir != null ? cmdJoin + "(" + workDir.getCanonicalPath() + ")" : cmdJoin);

        int exitCode;
        try {
            if (env == null) {
                env = System.getenv();
            }
            exitCode = exec.execute(cmdLine, env);
        } catch (Exception e) {
            exitCode = EXCEPTION_EXIT;
            log.error("Error when execute [" + cmdLog + "]", e);
        }
        String feedBack = outputStream.toString(DEFAULT_CHARSET);
        if (log.isInfoEnabled()) {
            log.info("Execute shell command: '{}', with exitCode:{}, feedback: {} ", cmdLog, exitCode, feedBack);
        }
        return Pair.of(NORMAL_EXIT == exitCode, feedBack);
    }
    
    public static Pair<Boolean, String> executeCommandNonBlockingWithFeedback(CommandLine cmdLine, File workDir, Map<String, String> env) throws Exception {
        DefaultExecutor exec = new DefaultExecutor();
        if (workDir != null) {
            exec.setWorkingDirectory(workDir);
        }
        exec.setExitValue(NORMAL_EXIT);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PumpStreamHandler streamHandler = new PumpStreamHandler(outputStream, outputStream);
        exec.setStreamHandler(streamHandler);

        final DefaultExecuteResultHandler resultHandler = new DefaultExecuteResultHandler();

        String cmdJoin = StringUtils.join(cmdLine.toStrings(), " ");
        String cmdLog = (workDir != null ? cmdJoin + "(" + workDir.getCanonicalPath() + ")" : cmdJoin);

        int exitCode;
        try {
            if (env == null) {
                env = System.getenv();
            }
            exec.execute(cmdLine, env, resultHandler);
            resultHandler.waitFor(5000);
            if (resultHandler.hasResult()) {
                exitCode = resultHandler.getExitValue();
            } else {
                exitCode = 1;
            }
        } catch (Exception e) {
            exitCode = EXCEPTION_EXIT;
            log.error("Error when execute [" + cmdLog + "]", e);
        }
        String feedBack = outputStream.toString(DEFAULT_CHARSET);
        if (log.isInfoEnabled()) {
            log.info("Execute shell command: '{}', with exitCode:{}, feedback: {} ", cmdLog, exitCode, feedBack);
        }
        return Pair.of(NORMAL_EXIT == exitCode, feedBack);
    }
    
    public static void main(String[] args) {
//        Map<String, String> env = Maps.newHashMap();
//        env.put("SPECTRE_DATA_ROOT", "D:\\opsagent\\agent_host_root");
//        env.put("JAVA_HOME", "D:\\opsagent\\agent_host_root\\jdk\\jdk1.8.0_231");
////        String cmd = "cmd.exe /C chcp 65001 & tasklist /FI \"PID eq 4816\"";
//        String cmd = "cmd.exe /C chcp 65001 & tasklist /FI \"PID eq 4736\"";
//        String cmd = "cmd.exe /c D:\\test\\start.bat";
        try {
//            CmdExecuteUtil.executeCommandWithFeedback(cmd, FileUtils.getFile("D:\\opsagent\\agent_host_root\\spectre\\standalone\\3.0\\"));
        	Pair<Boolean, String> feedback = CmdExecuteUtil.executeCommandNonBlockingWithFeedback("ping -n 2 10.12.70.39", null, null);
        	System.out.println(JsonUtil.toJacksonJson(feedback));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
