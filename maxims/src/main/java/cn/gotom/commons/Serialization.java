package cn.gotom.commons;

public interface Serialization {

	String getAlgorithm();

	String encode(Message msg, String key);

	Message decode(String text, String key);

}