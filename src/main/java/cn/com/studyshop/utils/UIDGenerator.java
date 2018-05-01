package cn.com.studyshop.utils;

import java.math.BigInteger;

import org.I0Itec.zkclient.ZkClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.com.studyshop.api.Generator;

/**
 * 分布式系统获取UID 利用模式（Singleton etc.)
 * 
 * @author weiyiLiu
 *
 */
public class UIDGenerator implements Generator {

	private Logger logger = LoggerFactory.getLogger(UIDGenerator.class);

	private static UIDGenerator generator = null;

	private static ZkClient zkClient = null;

	private static final String UIDGeneratorPath = "/uid-gen-path-uuid";

	private static volatile Object monitor = new Object();

	private UIDGenerator() {
		super();
		// TODO Auto-generated constructor stub
	}

	public static final UIDGenerator getInstance(String zkServiers) {

		if (null == generator) {
			synchronized (monitor) {
				if (null != generator && null != zkClient) {
					return generator;
				}
				// 组合
				zkClient = new ZkClient(zkServiers);
				generator = new UIDGenerator();
			}
		}

		return generator;
	}

	@Override
	public String generatorStr() {
		String seqPath = "";
		try {

			// 创建
			if (!zkClient.exists(UIDGeneratorPath)) {
				zkClient.createPersistent(UIDGeneratorPath);
			}

			seqPath = zkClient.createEphemeralSequential(UIDGeneratorPath + "/", "uid");
			logger.debug("[子节点] {}", seqPath);
		} catch (Exception e) {
			logger.error("[获取ID失败] 严重异常Exception:{}", e);
		}
		return seqPath.replaceAll(UIDGeneratorPath + "/", "");
	}

	@Override
	public Number generatorInt() {
		String seqPath = "";
		try {

			// 创建
			if (!zkClient.exists(UIDGeneratorPath)) {
				zkClient.createPersistent(UIDGeneratorPath);
			}

			seqPath = zkClient.createEphemeralSequential(UIDGeneratorPath + "/", "uid");
			logger.debug("[子节点] {}", seqPath);
		} catch (Exception e) {
			logger.error("[获取ID失败] 严重异常Exception:{}", e);
		}
		return new BigInteger(seqPath.replaceAll(UIDGeneratorPath + "/", ""));
	}

}
