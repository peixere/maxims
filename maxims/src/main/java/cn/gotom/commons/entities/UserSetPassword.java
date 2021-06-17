package cn.gotom.commons.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.Transient;
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
@Table("base_user")
@javax.persistence.Table(name = "base_user")
@Forbid
@ApiModel("修改密码")
public class UserSetPassword extends SuperEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1154739028872976784L;

	@ApiModelProperty(value = "用 户 名")
	@Column(updatable = false)
	private String account;

	@ApiModelProperty(value = "用户密码")
	private String password;

	@ApiModelProperty(value = "随机盐")
	@Column(length = 16)
	private String salt;

	@ApiModelProperty(value = "新的密码")
	@Transient
	@NotBlank(message = "密码不可以为空")
	@Length(min = 4, max = 20, message = "密码长度需要在4-20个字符")
	private transient String newpassword;

	@ApiModelProperty(value = "确认新的密码")
	@Transient
	private transient String newpasswordconfirm;

	@ApiModelProperty(value = "验 证 码")
	@Transient
	private transient String captcha;
}
