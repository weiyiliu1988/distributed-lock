package utils;

import java.util.concurrent.CountDownLatch;

import org.junit.Assert;
import org.junit.Test;

import cn.com.studyshop.utils.UIDGenerator;

public class UIDGeneratorTest {

	@Test
	public void generatorStrTest() {
		System.out.println(UIDGenerator.getInstance("47.*:2181").generatorStr());
		System.out.println(UIDGenerator.getInstance("47.*:2181").generatorStr());
		System.out.println(UIDGenerator.getInstance("47.*:2181").generatorStr());
		System.out.println(UIDGenerator.getInstance("47.*:2181").generatorStr());
		System.out.println(UIDGenerator.getInstance("47.*:2181").generatorStr());
		System.out.println(UIDGenerator.getInstance("47.*:2181").generatorStr());
		System.out.println(UIDGenerator.getInstance("47.*:2181").generatorStr());
		System.out.println(UIDGenerator.getInstance("47.*:2181").generatorStr());
		System.out.println(UIDGenerator.getInstance("47.*:2181").generatorStr());
		System.out.println(UIDGenerator.getInstance("47.*:2181").generatorStr());
		System.out.println(UIDGenerator.getInstance("47.*:2181").generatorStr());
		System.out.println(UIDGenerator.getInstance("47.*:2181").generatorStr());
		System.out.println(UIDGenerator.getInstance("47.*:2181").generatorStr());
		System.out.println(UIDGenerator.getInstance("47.*:2181").generatorStr());
		System.out.println(UIDGenerator.getInstance("47.*:2181").generatorStr());
		System.out.println(UIDGenerator.getInstance("47.*:2181").generatorStr());

	}

	@Test
	public void generatorIntTest() {
		System.out.println(UIDGenerator.getInstance("47.*:2181").generatorInt());
		System.out.println(UIDGenerator.getInstance("47.*:2181").generatorInt());
	}

	@Test
	public void CountDownLatchTest() {
		CountDownLatch cdl = new CountDownLatch(100);
		// 100并发量
		for (int i = 0; i < 100; i++) {
			new Thread(new Runnable() {

				@Override
				public void run() {
					cdl.countDown();
					try {
						cdl.await();
					} catch (InterruptedException e) {
						e.printStackTrace();
					} finally {
						// doNothing
					}
					System.out.println(UIDGenerator.getInstance("47.*:2181").generatorInt());
				}

			}).start();
		}

		try {
			// 1 minute wait
			Thread.sleep(60000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void innerTest() {
		Assert.assertEquals("数值不一致", "aa", "cc");
	}
}
