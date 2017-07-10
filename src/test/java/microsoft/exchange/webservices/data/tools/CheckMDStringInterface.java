package microsoft.exchange.webservices.data.tools;

/**
 * 校验及消息摘要成字符串的接口
 * Created by Koupu Alen on 2017/6/20.
 */
public interface CheckMDStringInterface {

	/**
	 * 功能描述: 使用字节数组 b 中从 off 开始 len 长度的字节数组 更新摘要
	 * @createTime: 2016年8月20日 下午5:27:31
	 * @author: <a href="mailto:676096658@qq.com">xiaochangyu</a>
	 * @param b
	 * @param off
	 * @param len
	 */
	void update(byte[] b, int off, int len);

	/**
	 * 功能描述: 重置, 将之前 update 的字节数组清空还原
	 * @createTime: 2016年8月20日 下午5:29:07
	 * @author: <a href="mailto:676096658@qq.com">xiaochangyu</a>
	 */
	void reset();

	/**
	 * 功能描述: 将之前 update 的字节数组计算校验和或者摘要并转换为 16 进制字符串. 调用后摘要会被重置
	 * @createTime: 2016年8月20日 下午5:40:12
	 * @author: <a href="mailto:676096658@qq.com">xiaochangyu</a>
	 * @return String
	 */
	String digestToHex();
}
