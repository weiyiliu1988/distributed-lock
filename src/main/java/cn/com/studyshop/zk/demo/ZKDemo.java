package cn.com.studyshop.zk.demo;

import java.security.NoSuchAlgorithmException;
import java.util.List;

import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;
import org.apache.zookeeper.server.auth.DigestAuthenticationProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

/**
 * 
 * @author LIU
 *
 */
public class ZKDemo {

	private static Logger logger = LoggerFactory.getLogger(ZKDemo.class);

	public static void main(String[] args) {
		ZkClient client = new ZkClient("*:2181");
		client.setZkSerializer(new MyZkSerializer());

		client.subscribeDataChanges("/bbc", new IZkDataListener() {

			@Override
			public void handleDataChange(String dataPath, Object data) throws Exception {
				// TODO Auto-generated method stub
				System.out.println(dataPath + "||handleDataChange--->" + data.toString());

			}

			@Override
			public void handleDataDeleted(String dataPath) throws Exception {
				// TODO Auto-generated method stub
				System.out.println("handleDataDeleted--->" + dataPath);

			}

		});

		client.subscribeChildChanges("/edward", new IZkChildListener() {

			@Override
			public void handleChildChange(String parentPath, List<String> currentChilds) throws Exception { // TODO
				// stub
				System.out.println("handleChildChange-->" + parentPath + "   size:" + currentChilds.size());

				System.out.println("bbq->" + currentChilds.get(0));
				System.out.println("::" + currentChilds);
			}

		});

		/*
		 * _
		 * 
		 * [31,s{'world,'anyone} ]
		 * 
		 */
		List<ACL> aclList = client.getAcl("/edward").getKey();
		aclList.forEach(o -> {
			System.out.println("-----------");
			System.out.println(o);
		});

		List<ACL> seqAclList = Lists.newArrayList();
		Id id1 = null;
		try {
			id1 = new Id("digest", DigestAuthenticationProvider.generateDigest("fish:fishpw"));
		} catch (NoSuchAlgorithmException e1) {
			e1.printStackTrace();
		}
		seqAclList.add(new ACL(ZooDefs.Perms.ALL, id1));

		// 创建持久顺序节点
		String path = client.createPersistentSequential("/edward/seq/", "seq", seqAclList);
		System.out.println("------------");
		logger.debug("path is :{}", path);
		try {
			Thread.sleep(600000); // 10minutes
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
