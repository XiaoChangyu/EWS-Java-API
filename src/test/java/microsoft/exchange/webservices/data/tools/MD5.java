package microsoft.exchange.webservices.data.tools;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.zip.CRC32;

/**
 * 功能描述: {@link MD5} 摘要算法, 使用方式和 {@link CRC32} 类似 <br/>
 * {@link MD5} 返回的摘要是 16 位的字节数组, 可转换为 32 位的 16进制字符串 <br/>
 * @createTime: 2016年8月20日 下午5:32:47
 * @author: <a href="mailto:676096658@qq.com">xiaochangyu</a>
 * @version: 0.1
 * @lastVersion: 0.1
 * @updateTime: 2016年8月20日 下午5:32:47
 * @updateAuthor: <a href="mailto:676096658@qq.com">xiaochangyu</a>
 * @changesSum:
 */
public class MD5 extends MessageDigestInterface {

	private final static String _algorithm_ = "MD5";

	/**
	 * 创建一个新的实例 {@link MD5}
	 */
	public MD5() {
		try {
			this.md = MessageDigest.getInstance(_algorithm_);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void update(byte[] b, int off, int len) {
		md.update(b, off, len);
	}

	@Override
	public byte[] digest() {
		return md.digest();
	}

	@Override
	public void reset() {
		md.reset();
	}

	/**
	 * 功能描述: 直接获取文件 file 的 {@link MD5} 摘要的 16 进制字符串, 若文件类型是目录, 则获取的是目录的名字的摘要. 若既不是文件也不是目录, 返回空数字符串
	 * @createTime: 2016年8月20日 下午7:16:02
	 * @author: <a href="mailto:676096658@qq.com">xiaochangyu</a>
	 * @param file 不能为 null
	 * @return String
	 * @throws IOException
	 */
	public static String digestFileToHex(File file) throws IOException {
		return digestFileToHex(file, 1024 * 10);
	}

	/**
	 * 功能描述: 直接获取文件 file 的 {@link MD5} 摘要的 16 进制字符串, 若文件类型是目录, 则获取的是目录的名字的摘要. 若既不是文件也不是目录, 返回空数字符串
	 * @createTime: 2016年8月20日 下午7:16:02
	 * @author: <a href="mailto:676096658@qq.com">xiaochangyu</a>
	 * @param file 不能为 null
	 * @param bufSize 读取文件的缓冲区大小
	 * @return String
	 * @throws IOException
	 */
	public static String digestFileToHex(File file, int bufSize) throws IOException {
		return MessageDigestInterface.convertDigestToHex(digestFile(file, bufSize));
	}

	/**
	 * 功能描述: 直接获取文件 file 的 {@link MD5} 摘要, 若文件类型是目录, 则获取的是目录的名字的摘要. 若既不是文件也不是目录, 返回空数组
	 * @createTime: 2016年8月20日 下午7:16:02
	 * @author: <a href="mailto:676096658@qq.com">xiaochangyu</a>
	 * @param file 不能为 null
	 * @return byte[]
	 * @throws IOException
	 */
	public static byte[] digestFile(File file) throws IOException {
		return digestFile(file, 1024 * 10);
	}

	/**
	 * 功能描述: 直接获取文件 file 的 {@link MD5} 摘要, 若文件类型是目录, 则获取的是目录的名字的摘要. 若既不是文件也不是目录, 返回空数组
	 * @createTime: 2016年8月20日 下午7:16:02
	 * @author: <a href="mailto:676096658@qq.com">xiaochangyu</a>
	 * @param file 不能为 null
	 * @param bufSize 读取文件的缓冲区大小
	 * @return byte[]
	 * @throws IOException
	 */
	public static byte[] digestFile(File file, int bufSize) throws IOException {
		if (file.isDirectory()) {
			return digestString(file.getName());
		} else if (file.isFile()) {
			BufferedInputStream bis = null;
			try {
				bis = new BufferedInputStream(new FileInputStream(file), bufSize);
				MD5 md = new MD5();
				byte[] bytes = new byte[bufSize];
				int len = 0;
				while ((len = bis.read(bytes)) != -1) {
					md.update(bytes, 0, len);
				}
				return md.digest();
			} catch (IOException e) {
				throw e;
			} finally {
				if (bis != null) bis.close();
			}
		}
		return new byte[0];
	}

	/**
	 * 功能描述: 直接获取字符串 s 的 {@link MD5} 摘要
	 * @createTime: 2016年8月20日 下午7:16:02
	 * @author: <a href="mailto:676096658@qq.com">xiaochangyu</a>
	 * @param s 不能为 null
	 * @return byte[]
	 */
	public static byte[] digestString(String s) {
		return digest(s.getBytes());
	}

	/**
	 * 功能描述: 直接获取字符串 s 的 {@link MD5} 摘要的 16 进制字符串
	 * @createTime: 2016年8月20日 下午7:15:26
	 * @author: <a href="mailto:676096658@qq.com">xiaochangyu</a>
	 * @param s 不能为 null
	 * @return String
	 */
	public static String digestStringToHex(String s) {
		return MessageDigestInterface.convertDigestToHex(digest(s.getBytes()));
	}

	/**
	 * 功能描述: 直接获取字节数组 b 的 {@link MD5} 摘要的 16 进制字符串
	 * @createTime: 2016年8月20日 下午7:10:40
	 * @author: <a href="mailto:676096658@qq.com">xiaochangyu</a>
	 * @param b
	 * @return String
	 */
	public static String digestToHex(byte[] b) {
		return MessageDigestInterface.convertDigestToHex(digest(b));
	}

	/**
	 * 功能描述: 直接获取字节数组 b 的 {@link MD5} 摘要
	 * @createTime: 2016年8月20日 下午7:07:10
	 * @author: <a href="mailto:676096658@qq.com">xiaochangyu</a>
	 * @param b
	 * @return byte[]
	 */
	public static byte[] digest(byte[] b) {
		return digest(b, 0, b.length);
	}

	/**
	 * 功能描述: 直接获取字节数组 b 从 off 位置开始 len 长度的字节数组 的 {@link MD5} 摘要
	 * @createTime: 2016年8月20日 下午7:10:10
	 * @author: <a href="mailto:676096658@qq.com">xiaochangyu</a>
	 * @param b
	 * @param off
	 * @param len
	 * @return byte[]
	 */
	public static byte[] digest(byte[] b, int off, int len) {
		MessageDigest md;
		try {
			md = MessageDigest.getInstance(_algorithm_);
			md.update(b, off, len);
			return md.digest();
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}
}
