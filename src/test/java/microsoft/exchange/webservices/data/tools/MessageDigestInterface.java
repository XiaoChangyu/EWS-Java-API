package microsoft.exchange.webservices.data.tools;

import java.security.MessageDigest;
import java.util.zip.Adler32;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

/**
 * 功能描述: 消息摘要算法的统一接口, 和 {@link Checksum} 是 {@link CRC32} 和 {@link Adler32} 统一接口类似
 * @createTime: 2016年8月20日 下午5:25:02
 * @author: <a href="mailto:676096658@qq.com">xiaochangyu</a>
 * @version: 0.1
 * @lastVersion: 0.1
 * @updateTime: 2016年8月20日 下午5:25:02
 * @updateAuthor: <a href="mailto:676096658@qq.com">xiaochangyu</a>
 * @changesSum:
 */
public abstract class MessageDigestInterface implements CheckMDStringInterface {

	/** 实现类摘要算法变量 */
	protected MessageDigest md;

	/**
	 * 功能描述: 使用字节数组 b 中的字节更新摘要
	 * @createTime: 2016年8月20日 下午5:27:11
	 * @author: <a href="mailto:676096658@qq.com">xiaochangyu</a>
	 * @param b
	 * @return 消息摘要算法对象自己, 方便级联操作
	 */
	public CheckMDStringInterface update(byte[] b) {
		update(b, 0, b.length);
		return this;
	}

	/**
	 * 功能描述: 将之前 update 的字节数组计算摘要. 调用后摘要会被重置
	 * @createTime: 2016年8月20日 下午5:28:41
	 * @author: <a href="mailto:676096658@qq.com">xiaochangyu</a>
	 * @return byte[]
	 */
	public abstract byte[] digest();

	/**
	 * 功能描述: 将之前 update 的字节数组计算摘要并转换为 16 进制字符串. 调用后摘要会被重置
	 * @createTime: 2016年8月20日 下午5:40:12
	 * @author: <a href="mailto:676096658@qq.com">xiaochangyu</a>
	 * @return String
	 */
	@Override
	public String digestToHex() {
		return convertDigestToHex(digest());
	}


	private final static byte[] digits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

	/**
	 * 功能描述: 将摘要字节数组转换成 16 进制字符串
	 * @createTime: 2016年8月20日 下午5:35:05
	 * @author: <a href="mailto:676096658@qq.com">xiaochangyu</a>
	 * @param digest
	 * @return String
	 */
	public static String convertDigestToHex(byte[] digest) {
		byte[] strBytes = new byte[digest.length * 2];
		for (int i = 0; i < digest.length; i++) {
			strBytes[i * 2] = digits[(digest[i] >>> 4) & 0x0F];
			strBytes[i * 2 + 1] = digits[digest[i] & 0x0F];
		}
		return new String(strBytes);
	}
}
