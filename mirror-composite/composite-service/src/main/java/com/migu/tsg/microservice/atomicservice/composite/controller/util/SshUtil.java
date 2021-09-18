package com.migu.tsg.microservice.atomicservice.composite.controller.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.sshd.client.SshClient;
import org.apache.sshd.client.channel.ChannelShell;
import org.apache.sshd.client.channel.ClientChannel;
import org.apache.sshd.client.channel.ClientChannelEvent;
import org.apache.sshd.client.config.hosts.HostConfigEntryResolver;
import org.apache.sshd.client.keyverifier.AcceptAllServerKeyVerifier;
import org.apache.sshd.client.session.ClientSession;
import org.apache.sshd.client.subsystem.sftp.SftpClient;
import org.apache.sshd.client.subsystem.sftp.SftpClient.OpenMode;
import org.apache.sshd.client.subsystem.sftp.SftpClientFactory;
import org.apache.sshd.client.subsystem.sftp.SftpRemotePathChannel;
import org.apache.sshd.common.Factory;
import org.apache.sshd.common.channel.PtyMode;
import org.apache.sshd.common.io.nio2.Nio2ServiceFactoryFactory;
import org.apache.sshd.common.keyprovider.KeyIdentityProvider;
import org.apache.sshd.common.util.MapEntryUtils.EnumMapBuilder;
import org.apache.sshd.common.util.threads.CloseableExecutorService;
import org.apache.sshd.common.util.threads.NoCloseExecutor;
import org.apache.sshd.common.util.threads.ThreadUtils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

/** 
*
* 项目名称: ops-proxy 
* <p/>
* 
* 类名: SshUtil
* <p/>
*
* 类功能描述: 基于apache-sshd的SSH工具类
* <p/>
*
* @author	pengguihua
*
* @date	2019年10月26日  
*
* @version	V1.0 
* <br/>
*
* <b>Copyright(c)</b> 2020 卓望公司-版权所有 
*
*/
@Slf4j
public final class SshUtil {
	public static final int					GENERAL_TIMEOUT			= 10;
	public static final int					DEFAULT_CONNECT_TIMEOUT	= GENERAL_TIMEOUT;
	public static final int					DEFAULT_LOGIN_TIMEOUT	= GENERAL_TIMEOUT;

	private static final ExecutorService	THREAD_POOL				= new ThreadPoolExecutor(8, 32, 1, TimeUnit.MINUTES,
			new ArrayBlockingQueue<Runnable>(800));
	
	private static final Object				INSTANCE_LOCK			= new Object();
	private static final SshClient			GLOBAL_SSH_CLIENT		= getGlobalSshClient();

			
	private static SshClient getGlobalSshClient() {
		if (GLOBAL_SSH_CLIENT != null && !GLOBAL_SSH_CLIENT.isClosed()) {
			return GLOBAL_SSH_CLIENT;
		}
		
		synchronized (INSTANCE_LOCK) {
			if (GLOBAL_SSH_CLIENT != null && !GLOBAL_SSH_CLIENT.isClosed()) {
				return GLOBAL_SSH_CLIENT;
			}
			
			SshClient client = SshClient.setUpDefaultClient();
			client.setServerKeyVerifier(AcceptAllServerKeyVerifier.INSTANCE);
			client.setHostConfigEntryResolver(HostConfigEntryResolver.EMPTY);
			client.setKeyIdentityProvider(KeyIdentityProvider.EMPTY_KEYS_PROVIDER);
//			client.setNioWorkers(16);
			Factory<CloseableExecutorService> executors = new Factory<CloseableExecutorService>() {
				@Override
				public CloseableExecutorService create() {
					return new NoCloseExecutor(ThreadUtils.newFixedThreadPool("SshUtil", 16));
				}
			};
			client.setIoServiceFactoryFactory(new Nio2ServiceFactoryFactory(executors));
			client.start();
			return client;
		}
	}
	
	/** 
	 * 功能描述: SFPT文件下载
	 * <p>
	 * @param sftpServer
	 * @param remoteFilePath
	 * @param localDirectory
	 * @return
	 */
	public static Pair<SshResultWrap, Path> sftpDownload(SshdServer sftpServer, String remoteFilePath, String localDirectory) {
		SshClient client = getGlobalSshClient();
        try (ClientSession session = client.connect(sftpServer.getLoginUser(), 
        		new InetSocketAddress(sftpServer.getIpAddress(), sftpServer.getPort())).verify(
        				DEFAULT_CONNECT_TIMEOUT, TimeUnit.SECONDS).getSession()) {
	        session.addPasswordIdentity(sftpServer.getLoginPass());
	        session.auth().verify(DEFAULT_LOGIN_TIMEOUT, TimeUnit.SECONDS);
	        
	        try (SftpClient sftp = SftpClientFactory.instance().createSftpClient(session);
	        		SftpRemotePathChannel remoteChannel = sftp.openRemoteFileChannel(remoteFilePath, OpenMode.Read)) {

	        	String fileName = Paths.get(remoteFilePath).getFileName().toFile().getName();
	        	Files.createDirectories(Paths.get(localDirectory));
	        	Path localeDown = Paths.get(localDirectory, fileName);
	        	Files.deleteIfExists(localeDown);
	        	
	        	byte[] buff = new byte[1024*1024*2];
	        	ByteBuffer byteBuff = ByteBuffer.wrap(buff);
	        	
	        	while (remoteChannel.read(byteBuff) != -1) {
	        		byte[] copy = buff;
	        		if (byteBuff.position() < byteBuff.capacity()) {
	        			copy = Arrays.copyOfRange(buff, 0, byteBuff.position());
	        		}
	        		Files.write(localeDown, copy, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
	        		byteBuff.rewind();
	        	}
	        	return Pair.of(SshResultWrap.ofSuccess(ResultFlagEnum.FLAG_9), localeDown); 
	        }
        } catch (Throwable e) {
        	log.error("Exception when invoke sftpDownload().", e);
        	return Pair.of(SshResultWrap.ofException(e, ResultFlagEnum.FLAG_101), null);
        }
	}
	
	public static Pair<SshResultWrap, String> sftpUpload(SshdServer sftpServer, Path localFilePath, String remoteDir) {
		SshClient client = getGlobalSshClient();
        try (ClientSession session = client.connect(sftpServer.getLoginUser(), 
        		new InetSocketAddress(sftpServer.getIpAddress(), sftpServer.getPort())).verify(
        				DEFAULT_CONNECT_TIMEOUT, TimeUnit.SECONDS).getSession()) {
	        session.addPasswordIdentity(sftpServer.getLoginPass());
	        session.auth().verify(DEFAULT_LOGIN_TIMEOUT, TimeUnit.SECONDS);
	        
	        String fileName = localFilePath.toFile().getName();
	        String remoteFileFullPath = remoteDir + "/" + fileName;
	        try (SftpClient sftp = SftpClientFactory.instance().createSftpClient(session);
	        		ClientChannel sftpChannel = sftp.getClientChannel();
	        		OutputStream uploadOut = sftp.write(remoteFileFullPath, OpenMode.Write, OpenMode.Create, OpenMode.Truncate);
	        		InputStream fileIs = new FileInputStream(localFilePath.toFile())) {
	        	
	        	int readCount = 0;
	        	byte[] buff = new byte[1024*1024*2];
	        	while ((readCount = fileIs.read(buff)) != -1) {
	        		uploadOut.write(buff, 0, readCount);
	        	}
	        	uploadOut.flush();
	        	return Pair.of(SshResultWrap.ofSuccess(ResultFlagEnum.FLAG_9), remoteFileFullPath); 
	        } catch (Exception e) {
	        	log.error("Exception when invoke sftpUpload().", e);
	        	return Pair.of(SshResultWrap.ofException(e, ResultFlagEnum.FLAG_101), null);
	        } 
        } catch (Throwable e) {
        	log.error("Exception when invoke sftpUpload().", e);
        	return Pair.of(SshResultWrap.ofException(e, ResultFlagEnum.FLAG_101), null);
        } 
	}
	
	public static void main(String[] args) {
		SshdServer sftpServer = new SshdServer();
		sftpServer.setIpAddress("10.12.70.39");
		sftpServer.setPort(2022);
		sftpServer.setLoginUser("root");
		sftpServer.setLoginPass("root123");
		
		Path localFilePath = Paths.get("D:\\work\\categories\\agent自动化软件部署\\stage01\\testSftpUpload.zip");
		String remoteDir = "/home/sudoroot/pgh";
		
		System.out.println(SshUtil.sftpUpload(sftpServer, localFilePath, remoteDir));
	}
	
	public static SshResultWrap executeShellCmd(SshdServer sshd, Integer timeout, String ... cmdArr) {
		SshClient client = getGlobalSshClient();
        try (ClientSession session = client.connect(sshd.getLoginUser(), 
        		new InetSocketAddress(sshd.getIpAddress(), sshd.getPort())).verify(
        				DEFAULT_CONNECT_TIMEOUT, TimeUnit.SECONDS).getSession()) {
	        session.addPasswordIdentity(sshd.getLoginPass());
	        session.auth().verify(DEFAULT_LOGIN_TIMEOUT, TimeUnit.SECONDS);
	        
	        Future<String> outRead = null;
	        Future<String> errorRead = null;
	        try (ChannelShell shellChannel = session.createShellChannel();
                	PipedOutputStream outOs = new PipedOutputStream();
            		PipedInputStream pipeOutIs = new PipedInputStream(outOs);
	        		PipedOutputStream errorOs = new PipedOutputStream();
            		PipedInputStream pipeErrorIs = new PipedInputStream(errorOs)) {
	        	
	        	// 异步线程读取响应, 因为当响应的行较多时，会造成线程阻塞, 异步读取可以解决这个问题
	        	final StringBuilder outHolder = new StringBuilder();
	        	final StringBuilder errorHolder = new StringBuilder();
	        	outRead = asycReadResponse(pipeOutIs, outHolder);
	        	errorRead = asycReadResponse(pipeErrorIs, errorHolder);
	        	
	        	// append \r at the end is essential for SUSE distribution
	        	String joinCmd = StringUtils.join(cmdArr, "\r") + " \r";
            	InputStream cmdInput = new ByteArrayInputStream(joinCmd.getBytes());
            	shellChannel.setIn(cmdInput);
            	
	        	shellChannel.setOut(outOs);
	        	shellChannel.setErr(errorOs);
	        	shellChannel.setPtyType("xterm");
	        	shellChannel.setPtyLines(100);
	        	Map<PtyMode, Integer> ptyModes = EnumMapBuilder.<PtyMode, Integer>builder(PtyMode.class)
        	            .put(PtyMode.ISIG, 1).put(PtyMode.ICANON, 1)
        	            .put(PtyMode.ECHO, 0).put(PtyMode.ECHOE, 1)
        	            .put(PtyMode.ECHOK, 1).put(PtyMode.ECHONL, 0)
        	            .put(PtyMode.NOFLSH, 0).immutable();
	        	shellChannel.setPtyModes(ptyModes);
	        	shellChannel.setAgentForwarding(true);
	        	
	        	shellChannel.open();
	        	int executeTimeout = timeout == null ? GENERAL_TIMEOUT : timeout;
	        	Set<ClientChannelEvent> events = shellChannel.waitFor(
	        			Arrays.asList(ClientChannelEvent.EXIT_STATUS), TimeUnit.SECONDS.toMillis(executeTimeout));
	        	
	        	// 执行超时
	        	if (events.contains(ClientChannelEvent.TIMEOUT)) {
	        		return SshResultWrap.ofException("Time out: \n" + outHolder.toString(), ResultFlagEnum.FLAG_102);
	        	}
	        	
	        	Integer exitCode = shellChannel.getExitStatus();
	        	if (exitCode == null) {
	        		return SshResultWrap.ofException(outHolder.toString(), exitCode, ResultFlagEnum.FLAG_101);
	        	}
	        	if (exitCode != 0) {
	        		return SshResultWrap.ofException(errorRead.get(), exitCode, ResultFlagEnum.FLAG_101);
	        	}
        		return SshResultWrap.ofSuccess(outRead.get(), exitCode, ResultFlagEnum.FLAG_9);
	        } finally {
	        	if (outRead != null && !outRead.isDone()) {
	        		outRead.cancel(true);
	        	}
	        	if (errorRead != null && !errorRead.isDone()) {
	        		errorRead.cancel(true);
	        	}
	        }
        } catch (Throwable e) {
        	log.error("Exception when invoke executeShellCmd().", e);
        	return SshResultWrap.ofException(e, ResultFlagEnum.FLAG_101);
        }
	}
	
	private static Future<String> asycReadResponse(final InputStream input, final StringBuilder holder) throws IOException {
		return THREAD_POOL.submit(new Callable<String>() {
			@Override
			public String call() throws Exception {
				BufferedReader buffReader = new BufferedReader(new InputStreamReader(input));
				String line;
				while ((line = buffReader.readLine()) != null) {
					holder.append(line).append('\n');
				}
				return holder.toString();
			}
		});
	}
	
	@Setter
	@Getter
	@ToString(exclude="loginPass")
	@EqualsAndHashCode(of= {"ipAddress", "port"})
	public static final class SshdServer {
		private String	ipAddress;
		private String	loginUser;
		private String	loginPass;
		private int		port	= 22;
		private String 	osType;
	}
	
	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public static final class SshResultWrap {
		public static final int	EXIT_ERROR	= -1;
		public static final int	EXIT_OK		= 0;

		private String			bizLog;
		private Integer			exitCode;			// 返回码
		private ResultFlagEnum	resultFlag;			// 结果flag
		private boolean			success;
		
		public static SshResultWrap ofSuccess(String bizLog, Integer exitCode, ResultFlagEnum resultFlag) {
			SshResultWrap result = new SshResultWrap();
			result.setSuccess(true);
			result.setExitCode(exitCode);
			result.setResultFlag(resultFlag);
			result.setBizLog(bizLog);
			return result;
		}
		
		public static SshResultWrap ofSuccess(String bizLog, ResultFlagEnum resultFlag) {
			return ofSuccess(bizLog, null, resultFlag);
		}
		
		public static SshResultWrap ofSuccess(ResultFlagEnum resultFlag) {
			return ofSuccess(null, resultFlag);
		}
		
		public static SshResultWrap ofException(String bizLog, Integer exitCode, ResultFlagEnum resultFlag) {
			SshResultWrap result = new SshResultWrap();
			result.setBizLog(bizLog);
			result.setSuccess(false);
			result.setExitCode(exitCode);
			result.setResultFlag(resultFlag);
			return result;
		}
		
		public static SshResultWrap ofException(String bizLog, ResultFlagEnum resultFlag) {
			return ofException(bizLog, null, resultFlag);
		}
		
		public static SshResultWrap ofException(Throwable e, ResultFlagEnum resultFlag) {
			return ofException(e.toString(), resultFlag);
		}
		
		public static SshResultWrap ofException(String bizTip) {
			return ofException(bizTip, ResultFlagEnum.FLAG_101);
		}
	}
	
	@Getter
	public enum ResultFlagEnum {
		FLAG_101(101, "执行失败"), FLAG_102(102, "执行超时"), FLAG_9(9, "执行成功");

		ResultFlagEnum(int flag, String label) {
			this.flag = flag;
			this.label = label;
		}

		private final int		flag;
		private final String	label;
	}
}