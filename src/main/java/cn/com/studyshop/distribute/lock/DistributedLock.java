package cn.com.studyshop.distribute.lock;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;

import cn.com.studyshop.zk.demo.MyZkSerializer;

/**
 * 持久节点锁
 * 
 * 惊群效应引起性能巨大耗费
 * 
 * @author weiyiLiu
 *
 */
@Deprecated
public class DistributedLock implements Lock {

	private String lockPath;
	private ZkClient zkClient;

	public DistributedLock(String lockPath, String services) {
		super();
		System.out.println("初始化锁处理开始...");
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

		CountDownLatch cdl = new CountDownLatch(1); // 每次进行初始化
		IZkDataListener listener = new IZkDataListener() {

			@Override
			public void handleDataChange(String dataPath, Object data) throws Exception {

			}

			@Override
			public void handleDataDeleted(String dataPath) throws Exception {
				// data被删除(即节点被删除)
				// System.out.println("删除节点--->" + dataPath);
				cdl.countDown();// 惊群效应

			}

		};

		this.zkClient.subscribeDataChanges(this.lockPath, listener); // 所有线程节点加监听(侦听模式watchForData)

		// 如果没有判断则马上进入自旋（删除节点时 所有瞬时都被唤醒）
		/**
		 * 1. 当锁释放后，可能触发了cdl.countDown(),但是这些新的节点对监听还没初始化 2. 所有的节点都变成wait状态
		 * 
		 * 增加判断后,如果有锁才自旋
		 */
		if (this.zkClient.exists(this.lockPath)) {
			try {
				System.out.println("进入自旋------");
				cdl.await(); // 阻塞
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		this.zkClient.unsubscribeDataChanges(this.lockPath, listener); // 取消注册侦听
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
