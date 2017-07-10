package microsoft.exchange.webservices.data;

import sun.misc.BASE64Encoder;

import java.net.URI;
import java.text.SimpleDateFormat;

/**
 * Test read email folders. We can print all of email folders. The code just for test.
 * If we can read email folders, we can read all email of the folder.
 * 测试邮件目录信息
 * Created by Koupu Alen on 2017/7/9.
 */
public class TestEmailFolders {

	private static final SimpleDateFormat Format_YYYY_MM_DD_HH_MM_SS = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
	private static final BASE64Encoder BASE_64_ENCODER = new BASE64Encoder();

	public static void main(String[] args) throws Exception {
		String username = "uncle.sam";
		String password = "america";
		ExchangeService service = new ExchangeService(ExchangeVersion.Exchange2007_SP1);
		ExchangeCredentials credentials = new WebCredentials(username, password, "USA");
		service.setCredentials(credentials);
		service.setUrl(new URI("https://mail.unclesam.us/ews/Exchange.asmx"));
		//绑定邮箱
		Folder inbox = Folder.bind(service, WellKnownFolderName.Inbox);
		System.out.println("Inbox.DisplayName: " + inbox.getDisplayName());
		System.out.println("Inbox.ChildFolderCount: " + inbox.getChildFolderCount());
		//获取邮箱文件数量
		int count = inbox.getTotalCount();
		int unreadCount = inbox.getUnreadCount();
		System.out.println("Inbox.Count: " + count);
		System.out.println("Inbox.UnreadCount: " + unreadCount);
		Folder rootFolder = Folder.bind(service, WellKnownFolderName.Root);
		System.out.println("ROOT.DisplayName: " + rootFolder.getDisplayName());
		System.out.println("ROOT.ChildFolderCount: " + rootFolder.getChildFolderCount());
		System.out.println("Folder.Count: " + rootFolder.getTotalCount());
		System.out.println("Folder.UnreadCount: " + rootFolder.getUnreadCount());
		printFolders(rootFolder, 0);
	}

	private static void printFolders(Folder parentFolder, int deep) throws Exception {
		deep++;
		FindFoldersResults folders = parentFolder.findFolders(new FolderView(100));
		for (Folder folder : folders.getFolders()) {
			System.out.println(multiStr("--------", deep));
			System.out.println(multiStr("\t", deep) + "Folder.DisplayName: " + folder.getDisplayName());
			System.out.println(multiStr("\t", deep) + "Folder.ChildFolderCount: " + folder.getChildFolderCount());
			System.out.println(multiStr("\t", deep) + "Folder.Count: " + folder.getTotalCount());
			try {System.out.println(multiStr("\t", deep) + "Folder.UnreadCount: " + folder.getUnreadCount());} catch (Throwable t) {t.printStackTrace();}
			if (folder.getChildFolderCount() > 0) {
				printFolders(folder, deep);
			}
		}
		deep--;
	}

	private static String multiStr(String str, int multiple) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < multiple; i++) {
			sb.append(str);
		}
		return sb.toString();
	}
}
