package microsoft.exchange.webservices.data;

import microsoft.exchange.webservices.data.tools.MD5;
import sun.misc.BASE64Encoder;

import java.io.*;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Test receive email, and save email to file. Just test code.
 * 测试接收邮件
 * Created by Koupu Alen on 2017/7/9.
 */
public class TestReceiveEmail {

	private static final SimpleDateFormat Format_YYYY_MM_DD_HH_MM_SS = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
	private static final BASE64Encoder BASE_64_ENCODER = new BASE64Encoder();

	public static void main(String[] args) throws Exception {
		String username = "uncle.sam";//Use your own email name. remenber, no suffix like @xxxx.com.
		String password = "america";//Use your own email password.
		//If you want save all email to directory. 如果你想储存所有邮件到目录中的话.
		File dir = new File("savemail");
		ExchangeService service = new ExchangeService(ExchangeVersion.Exchange2007_SP1);
		ExchangeCredentials credentials = new WebCredentials(username, password, "USA");//Use your own email domain.
		service.setCredentials(credentials);
		/*
		I think you need try to access the url like https://mail.xxx.com/ews/Exchange.asmx,
		maybe you need input your username and password,
		then you can see a page like content of xml file after a moment.
		 */
		service.setUrl(new URI("https://mail.unclesam.us/ews/Exchange.asmx"));
		//绑定邮箱
		Folder inbox = Folder.bind(service, WellKnownFolderName.Inbox);
		System.out.println("Inbox.DisplayName: " + inbox.getDisplayName());
		//获取邮箱文件数量
		int count = inbox.getTotalCount();
		System.out.println("Count: " + count);
		//循环获取邮箱邮件
		int pageSize = 50;
		int pageCount = count / pageSize + (count % pageSize == 0 ? 0 : 1);
		for (int offset = 0; offset < pageCount; offset++) {
			System.out.println("==================================================================  " + offset);
			ItemView view = new ItemView(pageSize, offset * pageSize);
			FindItemsResults<Item> findResults = service.findItems(inbox.getId(), view);
			for (Item item : findResults.getItems()) {
				System.out.println("======================");
				EmailMessage message = EmailMessage.bind(service, item.getId());
				// 邮件消息非内容部分
				System.out.println("Id: " + message.getId());
				System.out.println("InternetMessageId: " + message.getInternetMessageId());
				System.out.println("Sender: " + message.getSender());
				System.out.println("From: " + message.getFrom());
				System.out.println("To: " + message.getToRecipients().getItems());
				System.out.println("CC: " + message.getCcRecipients().getItems());
				System.out.println("BCC: " + message.getBccRecipients().getItems());
				System.out.println("DisplayTo: " + message.getDisplayTo());
				System.out.println("DisplayCC: " + message.getDisplayCc());
				System.out.println("Subject: " + item.getSubject());
				System.out.println("DateTimeCreated: " + message.getDateTimeCreated());
				System.out.println("DateTimeSent: " + message.getDateTimeSent());
				System.out.println("DateTimeReceived: " + message.getDateTimeReceived());
				System.out.println("Size: " + message.getSize());
				System.out.println("IsRead: " + message.getIsRead());
				System.out.println("IsDraft: " + message.getIsDraft());
				System.out.println("IsNew: " + message.getIsNew());
				System.out.println("IsResend: " + message.getIsResend());
				System.out.println("XmlElementName: " + message.getXmlElementName());
				// 消息内容
				MessageBody messageBody = message.getBody();
				BodyType bodyType = messageBody.getBodyType();
				System.out.println("BodyType: " + bodyType);
				XmlNamespace namespace = messageBody.getNamespace();
				System.out.println("Namespace: " + namespace);
				// Print email content 打印消息内容
//				System.out.println("MessageBody: " + messageBody);

				// 将消息内容写入输出流, 并且用 xml 标签包起来
				ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
				messageBody.writeToXml(new EwsServiceXmlWriter(service, outputStream), message.getId().toString());
//				System.out.println(new String(outputStream.toByteArray()));
				// 将邮件信息写入文件
				saveToFile(dir, message, service);
			}
		}
		if (BW != null) {
			BW.flush();
			BW.close();
		}
	}

	private static BufferedWriter BW = null;

	/**
	 * simple method to save email message to file.
	 * @param dir save to a directory
	 * @param message email message object
	 * @param service exchange service object
	 * @throws Exception maybe, throw some Exception
	 */
	private static void saveToFile(File dir, EmailMessage message, ExchangeService service) throws Exception {
		// 保证目录存在并可用
		while (!dir.exists()) {
			boolean mkdirs = dir.mkdirs();
			if (!mkdirs) {
				System.err.println("CreateDirs False to sleep: " + dir.getAbsolutePath());
				Thread.sleep(500);
			}
		}

		// 将邮件所有信息写入输出流, xml 格式
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		EwsServiceXmlWriter esxw = new EwsServiceXmlWriter(service, baos);
		message.writeToXml(esxw);
		esxw.flush();

		// 构造文件名等信息
		byte[] byteArray = baos.toByteArray();
		String md5Hex = MD5.digestToHex(byteArray);
		String dirPath = dir.getAbsolutePath();
		Date received = message.getDateTimeReceived();
		String uniqueId = message.getId().getUniqueId();
		String base64UniqueId = BASE_64_ENCODER.encode(uniqueId.getBytes());
		base64UniqueId = base64UniqueId.replaceAll("\n", "").replaceAll("\r", "");
		String fileName = Format_YYYY_MM_DD_HH_MM_SS.format(received) + "_" + md5Hex + ".xml";

		{
			// 将文件名和 UniqueId 的 BASE64 字符串映射写入特定文件
			if (BW == null) {
				synchronized (service) {
					if (BW == null) {
						BW = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(dirPath + "/filename_uniqueId_mapping.properties")));
					}
				}
			}
			BW.write(fileName + "=" + base64UniqueId);
			BW.write("\n");
			BW.flush();
		}

		{
			// We just want to test save email data to file, don't worry about performance.将邮件数据写入文件
			File file = new File(dirPath + "/" + fileName);
			try {
				// 保证文件能够生成并可用
				while (!file.exists()) {
					boolean newFile = file.createNewFile();
					if (!newFile) {
						System.err.println("CreateNewFile False to sleep: " + file.getAbsolutePath());
						Thread.sleep(200);
					}
				}
			} catch (Throwable t) {
				t.printStackTrace();
				System.err.println("CreateNewFile False to throw: " + file.getAbsolutePath());
				throw new Exception(t);
			}
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(byteArray);
			fos.flush();
			fos.close();
		}
	}
}
