package cn.gotom.commons.entities;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.validation.constraints.NotNull;

import org.springframework.data.relational.core.mapping.Table;

import cn.gotom.commons.data.Forbid;
import cn.gotom.commons.model.SuperEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table("wechat_user")
@javax.persistence.Table(//
		name = "wechat_user", //
		indexes = { //
				@Index(unique = true, columnList = "code", name = "UK_CODE"),
				@Index(unique = false, columnList = "userId", name = "IDX_USER_ID"), //
		})
@Forbid
@ApiModel("微信用户信息")
public class WechatUser extends SuperEntity {

	private static final long serialVersionUID = -4552035754344621301L;

	@NotNull(message = "code不能为空")
	@ApiModelProperty(value = "微信code")
	private String code;

	private String openid;

	@ApiModelProperty(value = "用户主键")
	private String userId;

	@ApiModelProperty(value = "主动绑定", notes = "用户主动绑定过帐号")
	private Boolean bindUser;

	@ApiModelProperty(value = "用户非敏感字段")
	private String rawData;

	@ApiModelProperty(value = "签名")
	private String signature;

	@ApiModelProperty(value = "用户敏感字段")
	private String encryptedData;

	private String nickName;

	private String avatarUrl;

	private Integer gender;

	private String city;

	private String country;

	private String province;

	private String sessionKey;

}
