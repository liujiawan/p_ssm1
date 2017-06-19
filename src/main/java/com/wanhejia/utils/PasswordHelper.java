package com.wanhejia.utils;
import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
import com.wanhejia.domain.User;
/**
 * 添加用户时生成的盐和密码
 * @author jack
 *
 */
public class PasswordHelper {
	private static RandomNumberGenerator randomNumberGenerator = new SecureRandomNumberGenerator();
	private static String algorithmName = "md5";
	private static int hashIterations = 2;
	public static void encryptPassword(User user) {
		String salt=randomNumberGenerator.nextBytes().toHex();
		user.setCredentialsSalt(salt);
		String newPassword = new SimpleHash(algorithmName, user.getPassword(), ByteSource.Util.bytes(user.getAccountname()+salt), hashIterations).toHex();
		user.setPassword(newPassword); 
	}
	public static void main(String[] args) {
		User user=new User();
		user.setPassword("123456");
		encryptPassword(user);
		System.out.println(user.getPassword());
	}
}
