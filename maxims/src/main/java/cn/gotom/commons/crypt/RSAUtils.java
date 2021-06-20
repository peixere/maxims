package cn.gotom.commons.crypt;

import java.io.ByteArrayOutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import javax.crypto.Cipher;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import cn.gotom.CustomException;
import cn.gotom.commons.Note;
//import io.netty.buffer.ByteBufUtil;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Note("RSA编码解码器")
public class RSAUtils {

	public static final String ALGORITHM = "RSA";

	public static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

	/**
	 * RSA最大加密明文大小
	 */
	private static final int MAX_ENCRYPT_BLOCK = 117;

	/**
	 * RSA最大解密密文大小
	 */
	private static final int MAX_DECRYPT_BLOCK = 128;

	public static KeyPair generateKeyPair() {
		try {
			KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(ALGORITHM);
			keyPairGen.initialize(1024);
			KeyPair keyPair = keyPairGen.generateKeyPair();
			return keyPair;
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	@Note("不分段加密解密(加密最长117，解决最长128个字节)")
	private static byte[] cryptBytes(int mode, byte[] data, Key key) {
		if (Cipher.ENCRYPT_MODE == mode && data.length > MAX_ENCRYPT_BLOCK) {
			return cryptBlockBytes(mode, data, key);
		} else if (Cipher.DECRYPT_MODE == mode && data.length > MAX_DECRYPT_BLOCK) {
			return cryptBlockBytes(mode, data, key);
		} else {
			try {
				Cipher cipher = Cipher.getInstance(ALGORITHM);
				cipher.init(mode, key);
				byte[] decryptedBytes = cipher.doFinal(data);
				return decryptedBytes;
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}

	private static byte[] cryptBlockBytes(int mode, byte[] data, Key key) {
		try {
			int maxCryptBlock = Cipher.ENCRYPT_MODE == mode ? MAX_ENCRYPT_BLOCK : MAX_DECRYPT_BLOCK;
			Cipher cipher = Cipher.getInstance(ALGORITHM);
			cipher.init(mode, key);
			int inputLen = data.length;
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			int offSet = 0;
			byte[] cache;
			int i = 0;
			while (inputLen - offSet > 0) {
				if (inputLen - offSet > maxCryptBlock) {
					cache = cipher.doFinal(data, offSet, maxCryptBlock);
				} else {
					cache = cipher.doFinal(data, offSet, inputLen - offSet);
				}
				out.write(cache, 0, cache.length);
				i++;
				offSet = i * maxCryptBlock;
			}
			byte[] outText = out.toByteArray();
			out.close();
			return outText;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Note("不分段加密")
	public static byte[] encrypt(byte[] data, Key key) {
		return cryptBytes(Cipher.ENCRYPT_MODE, data, key);
	}

	@Note("不分段加密")
	public static String encrypt2Hex(String data, Key key) {
		return Hex.encodeHexString(encrypt(data.getBytes(DEFAULT_CHARSET), key));
		// return ByteBufUtil.hexDump(encrypt(data.getBytes(DEFAULT_CHARSET), key));
	}

	@Note("不分段加密")
	public static String encrypt2Base64(String data, Key key) {
		return base64Encode(encrypt(data.getBytes(DEFAULT_CHARSET), key));
	}

	@Note("不分段解密")
	public static byte[] decrypt(byte[] data, Key key) {
		return cryptBytes(Cipher.DECRYPT_MODE, data, key);
	}

	@Note("不分段解密")
	public static String decrypt4Hex(String hex, Key key) {
		// return new String(decrypt(Hex.decodeHex(hex), key), DEFAULT_CHARSET);
		try {
			return new String(decrypt(Hex.decodeHex(hex), key), DEFAULT_CHARSET);
		} catch (DecoderException e) {
			throw CustomException.er(e);
		}

	}

	@Note("不分段解密")
	public static String decrypt4Base64(String base64Str, Key key) {
		return new String(decrypt(base64Decode(base64Str), key), DEFAULT_CHARSET);
	}

	public static String encodeBase64(Key key) throws RuntimeException {
		if (key == null) {
			return null;
		}
		return base64Encode(key.getEncoded());
	}

	public static PrivateKey privateKeyDe(String base64Key) {
		try {
			byte[] keyBytes = base64Decode(base64Key);
			PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
			KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
			PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
			return privateKey;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static PublicKey publicKeyDe(String base64Key) {
		try {
			byte[] keyBytes = base64Decode(base64Key);
			X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
			KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
			return keyFactory.generatePublic(keySpec);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static byte[] base64Decode(String base64Str) {
		try {
			return Base64.getDecoder().decode(base64Str);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static String base64Encode(byte[] key) {
		try {
			return Base64.getEncoder().encodeToString(key);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
