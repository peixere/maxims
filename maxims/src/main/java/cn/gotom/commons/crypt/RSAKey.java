package cn.gotom.commons.crypt;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.RSAPublicKeySpec;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RSAKey {

	private final PublicKey publicKey;
	private final PrivateKey privateKey;
	private String publicKeyModulus;
	private String publicKeyExponent;

	public RSAKey() {
		KeyPair kp = RSAUtils.generateKeyPair();
		this.publicKey = kp.getPublic();
		this.privateKey = kp.getPrivate();
		init();
	}

	public RSAKey(PublicKey publicKey, PrivateKey privateKey) {
		this.publicKey = publicKey;
		this.privateKey = privateKey;
		init();
	}

	public void init() {
		try {
			KeyFactory keyFactory = KeyFactory.getInstance(RSAUtils.ALGORITHM);
			RSAPublicKeySpec publicSpec = (RSAPublicKeySpec) keyFactory.getKeySpec(publicKey, RSAPublicKeySpec.class);
			String publicKeyModulus = publicSpec.getModulus().toString(16);
			String publicKeyExponent = publicSpec.getPublicExponent().toString(16);
			this.publicKeyModulus = publicKeyModulus;
			this.publicKeyExponent = publicKeyExponent;
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	/**
	 * 私钥解密
	 * 
	 * @param data
	 * @return
	 */
	public String decryptByPrivateKey(String hex) {
		return RSAUtils.decrypt4Hex(hex, privateKey);
	}

	/**
	 * 公钥加密
	 * 
	 * @param plainText
	 * @return
	 */
	public String encryptByPublicKey(String plainText) {
		return RSAUtils.encrypt2Hex(plainText, publicKey);
	}

}
