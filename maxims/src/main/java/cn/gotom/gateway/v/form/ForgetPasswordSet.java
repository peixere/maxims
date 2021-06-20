package cn.gotom.gateway.v.form;

import javax.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.Length;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@ApiModel("取回密码重置")
public class ForgetPasswordSet {

	@ApiModelProperty(value = "流水号", hidden = true)
	private String id;

	@ApiModelProperty(value = "用户密码")
	@NotBlank(message = "密码不可以为空")
	@Length(min = 4, max = 20, message = "密码长度需要在4-20个字符")
	private String password;

	@ApiModelProperty(value = "验 证 码")
	private String captcha;

}
