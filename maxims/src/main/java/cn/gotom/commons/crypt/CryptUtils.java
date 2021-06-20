package cn.gotom.commons.crypt;

import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.RandomUtils;

import cn.gotom.commons.utils.TextUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CryptUtils {

	public static String nextSalt() {
		return encodeHexString(RandomUtils.nextBytes(3));
	}

	public static String encodeBase64(byte[] data) {
		return new String(Base64.encodeBase64(data));
	}

	public static byte[] decodeBase64(String data) {
		return Base64.decodeBase64(data);
	}

	public static String encodeHexString(byte[] data) {
		return Hex.encodeHexString(data);
	}

	public static String encrypt(String username, String password, String salt) {
		return md5Hash(username, password, salt);
	}

	public static String md5Hash(String username, String password, String salt) {
		return md5Hash(username + password + salt);
	}

	public static String md5Hash(String str) {
		if (str == null || str.length() == 0) {
			throw new IllegalArgumentException("String to encript cannot be null or zero length");
		}
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(str.getBytes());
			return encodeHexString(md.digest());
		} catch (NoSuchAlgorithmException ex) {
			log.error(TextUtils.toStringWith(ex));
		}
		return str;
	}

	/**
	 * HmacSHA256加密
	 * 
	 * @param message
	 * @param secret
	 * @return
	 */
	public static String hmacSHA256(String message, String secret) {
		String hash = "";
		try {
			Mac mac = Mac.getInstance("HmacSHA256");
			SecretKeySpec secret_key = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
			mac.init(secret_key);
			byte[] bytes = mac.doFinal(message.getBytes());
			hash = encodeHexString(bytes);
		} catch (Exception ex) {
			log.error(TextUtils.toStringWith(ex));
		}
		return hash;
	}

	private static final String SIGNATURE_ALGORITHM = "SHA256withRSA";

	/**
	 *
	 * 用私钥对信息进行数字签名
	 *
	 * @param data       加密数据
	 *
	 * @param privateKey 私钥-base64加密的
	 *
	 * @return
	 *
	 * @throws Exception
	 *
	 */
	public static String sha256withRSA(String data, String privateKey) {
		return rsaSign(data, privateKey, SIGNATURE_ALGORITHM);
	}

	public static boolean sha256withRSAVerify(String data, String publicKey, String sign) {
		return rsaSignVerify(data, publicKey, sign, SIGNATURE_ALGORITHM);
	}

	/**
	 *
	 * 用私钥对信息进行数字签名
	 *
	 * @param data       加密数据
	 *
	 * @param privateKey 私钥-base64加密的
	 *
	 * @return
	 *
	 * @throws Exception
	 *
	 */
	public static String rsaSign(String data, String privateKey, String signatureAlgorithm) {
		try {
			byte[] keyBytes = decodeBase64(privateKey);
			PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
			KeyFactory factory = KeyFactory.getInstance("RSA");
			PrivateKey priKey = factory.generatePrivate(keySpec);// 生成私钥
			// 用私钥对信息进行数字签名
			Signature signature = Signature.getInstance(signatureAlgorithm);
			signature.initSign(priKey);
			signature.update(data.getBytes(StandardCharsets.UTF_8));
			return encodeBase64(signature.sign());
		} catch (Throwable ex) {
			log.error(TextUtils.toStringWith(ex));
			return null;
		}
	}

	public static boolean rsaSignVerify(String data, String publicKey, String sign, String signatureAlgorithm) {
		try {
			byte[] keyBytes = decodeBase64(publicKey);
			X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			PublicKey pubKey = keyFactory.generatePublic(keySpec);
			Signature signature = Signature.getInstance(signatureAlgorithm);
			signature.initVerify(pubKey);
			signature.update(data.getBytes(StandardCharsets.UTF_8));
			return signature.verify(decodeBase64(sign)); // 验证签名
		} catch (Throwable ex) {
			log.error(TextUtils.toStringWith(ex));
			return false;
		}
	}
}
