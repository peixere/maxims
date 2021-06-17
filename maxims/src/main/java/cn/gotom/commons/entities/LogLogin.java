package cn.gotom.commons.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Index;

import org.springframework.data.relational.core.mapping.Table;

import cn.gotom.commons.data.ForeignKey;
import cn.gotom.commons.model.PlatformEnum;
import cn.gotom.commons.model.TenantEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table("base_login_log")
@javax.persistence.Table(//
		name = "base_login_log", //
		indexes = { //
				@Index(unique = false, columnList = "userId", name = "IDX_LOGIN_LOG_USERID"), //
				@Index(unique = false, columnList = "tenantId", name = "IDX_LOGIN_LOG_TENANTID"), //
//				@Index(unique = false, columnList = "created", name = "IDX_LOGIN_LOG_CREATED"), //
		}//
)
@ApiModel("登录日志")
public class LogLogin extends TenantEntity {

	private static final long serialVersionUID = -6056684693511899575L;

	@ApiModelProperty(value = "登录用户")
	@Column(nullable = false, length = 32)
	@ForeignKey(User.class)
	private String userId;

	@ApiModelProperty(value = "是否成功")
	private Boolean success;

	@ApiModelProperty(value = "登录备注")
	private String memo;

	@ApiModelProperty(value = "请求地址")
	@Column(length = 128)
	private String addressIp;

	@ApiModelProperty(value = "请求路由")
	private String forwardedIp;

	@ApiModelProperty(value = "IP对应区域地址")
	@Column(length = 128)
	private String address;

	@ApiModelProperty(value = "系统名称")
	private String osName;

	@ApiModelProperty(value = "是否手机")
	private Boolean mobile;

	@ApiModelProperty(value = "浏 览 器")
	private String browserName;

	@ApiModelProperty(value = "是否手机")
	private String deviceInfo;

	@ApiModelProperty(value = "对应平台(WEB,APP,APPLET,PUBLIC)")
	@Column(length = 10)
	@Enumerated(EnumType.STRING)
	private PlatformEnum platform;

}
