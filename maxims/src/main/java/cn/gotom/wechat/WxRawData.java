package cn.gotom.wechat;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@ApiModel("用户非敏感信息")
@Getter
@Setter
public class RawData {
	private String nickName;
	private String avatarUrl;
	private Integer gender;
	private String city;
	private String country;
	private String province;
}
