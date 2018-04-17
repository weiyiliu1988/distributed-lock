package cn.com.studyshop.zk.demo;

import java.io.UnsupportedEncodingException;

import org.I0Itec.zkclient.exception.ZkMarshallingError;
import org.I0Itec.zkclient.serialize.ZkSerializer;

/**
 * 序列化(存储用) 自带利用可
 * 
 * @author LIU
 *
 */
public class MyZkSerializer implements ZkSerializer {

	String charSet = "utf-8";

	@Override
	public Object deserialize(byte[] arg0) throws ZkMarshallingError {
		// TODO Auto-generated method stub
		try {
			return new String(arg0, charSet);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new ZkMarshallingError(e);
		}
	}

	@Override
	public byte[] serialize(Object arg0) throws ZkMarshallingError {
		// TODO Auto-generated method stub
		try {
			return String.valueOf(arg0).getBytes(charSet);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			throw new ZkMarshallingError(e);
		}
	}

}
