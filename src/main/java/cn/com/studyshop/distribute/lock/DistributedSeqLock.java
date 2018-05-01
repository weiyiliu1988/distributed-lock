package cn.com.studyshop.distribute.lock;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.com.studyshop.zk.demo.MyZkSerializer;

/**
 * 临时顺节点锁
 * 
 * @author weiyiLiu
 *
 */
public class DistributedSeqLock implements Lock {

	private Logger logger = LoggerFactory.getLogger(DistributedSeqLock.class);

	private String lockPath;
	private ZkClient zkClient;
	private ThreadLocal<String> currentPath = new ThreadLocal<>();
	private ThreadLocal<String> beforePath = new ThreadLocal<>();

	public DistributedSeqLock(String lockPath, String services) {
		super();
		System.out.println("初始化锁处理开始...");
		this.lockPath = lockPath;
		this.zkClient = new ZkClient(services);
		zkClient.setZkSerializer(new MyZkSerializer());
		try {
			if (!this.zkClient.exists(this.lockPath)) {
				this.zkClient.createPersistent(this.lockPath);
			}
		} catch (Exception e) {
			logger.debug("[创建父节点] 父节点:{} 异常Exception:{}", lockPath, e);
		}
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
				// data被删除(即节点被删除)
				// System.out.println("删除节点--->" + dataPath);
				// cdl.countDown();

			}

			@Override
			public void handleDataDeleted(String dataPath) throws Exception {
				// doNothing
				cdl.countDown();
			}

		};

		this.zkClient.subscribeDataChanges(this.beforePath.get(), listener); // 所有线程节点加监听(侦听模式watchForData)

		// 如果没有判断则马上进入自旋（删除节点时 所有瞬时都被唤醒）
		/**
		 * 1. 当锁释放后，可能触发了cdl.countDown(),但是这些新的节点对监听还没初始化 2. 所有的节点都变成wait状态
		 * 
		 * 增加判断后,如果有锁才自旋
		 */
		if (this.zkClient.exists(this.beforePath.get())) {
			try {
				// System.out.println("进入自旋------");
				cdl.await(); // 阻塞
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		this.zkClient.unsubscribeDataChanges(this.beforePath.get(), listener); // 取消注册侦听
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

	/**
	 * 最小节点获取锁
	 * 
	 */
	@Override
	public boolean tryLock() {
		try {
			// 如果临时节点不存在，则创建临时顺序节点
			if (this.currentPath.get() == null) {
				this.currentPath.set(this.zkClient.createEphemeralSequential(this.lockPath + "/", "lockseq"));
			}
			// 获取所有的子节点
			List<String> children = this.zkClient.getChildren(this.lockPath);

			logger.info("children list-->{}", children);

			Collections.sort(children);

			if (this.currentPath.get().equals(this.lockPath + "/" + children.get(0))) {
				return true;// 获取锁
			} else {
				// 当前节点索引
				int currentIndex = children.indexOf(this.currentPath.get().substring(this.lockPath.length() + 1));

				// 前置path
				this.beforePath.set(this.lockPath + "/" + children.get(currentIndex - 1));

			}

			logger.info("[当前path] currentPath:{}", this.currentPath.get());
			logger.info("[前置path] beforePath:{}", this.beforePath.get());
		} catch (Exception e) {
			logger.debug("[获取锁  创建子节点] 异常Exception:{}", e);
		}
		return false;
	}

	@Override
	public boolean tryLock(long arg0, TimeUnit arg1) throws InterruptedException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void unlock() {
		// TODO Auto-generated method stub
		this.zkClient.delete(this.currentPath.get());
	}

}
