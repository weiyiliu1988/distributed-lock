package cn.com.studyshop.distribute.lock;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;

import cn.com.studyshop.zk.demo.MyZkSerializer;

public class DistributedLock implements Lock {

	private String lockPath;
	private ZkClient zkClient;

	public DistributedLock(String lockPath, String services) {
		super();
		this.lockPath = lockPath;
		this.zkClient = new ZkClient(services);
		zkClient.setZkSerializer(new MyZkSerializer());
	}

	@Override
	public void lock() {
		// TODO Auto-generated method stub
		if (!tryLock()) {
			waitForLock();
			this.lock(); // 再加锁
		}

	}

	private void waitForLock() {
		// 自旋处理

		CountDownLatch cdl = new CountDownLatch(1);
		IZkDataListener listener = new IZkDataListener() {

			@Override
			public void handleDataChange(String dataPath, Object data) throws Exception {
				// data被删除(即节点被删除)
				System.out.println("删除节点--->" + dataPath);
				cdl.countDown();

			}

			@Override
			public void handleDataDeleted(String dataPath) throws Exception {
				// TODO Auto-generated method stub

			}

		};

		this.zkClient.subscribeDataChanges(this.lockPath, listener); // 所有线程节点加监听(惊群效应)

		if (this.zkClient.exists(this.lockPath)) {
			try {
				cdl.await(); // 阻塞
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		this.zkClient.unsubscribeDataChanges(this.lockPath, listener);
	}

	@Override
	public void lockInterruptibly() throws InterruptedException {
		// TODO Auto-generated method stub

	}

	@Override
	public Condition newCondition() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean tryLock() {
		try {
			// 创建持久节点
			this.zkClient.createPersistent(this.lockPath);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	@Override
	public boolean tryLock(long arg0, TimeUnit arg1) throws InterruptedException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void unlock() {
		// TODO Auto-generated method stub
		this.zkClient.delete(this.lockPath);
	}

}
