package cn.com.studyshop.zk.demo;

import java.util.List;

import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;

/**
 * 
 * @author LIU
 *
 */
public class ZKDemo {

	public static void main(String[] args) {
		ZkClient client = new ZkClient("47.93.*:2181");
		client.setZkSerializer(new MyZkSerializer());

		client.subscribeDataChanges("/edward", new IZkDataListener() {

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

		try {
			Thread.sleep(600000); // 10minutes
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
