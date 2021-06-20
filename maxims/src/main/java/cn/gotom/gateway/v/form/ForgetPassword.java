package cn.gotom.gateway.v.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@ApiModel("取回密码")
public class ForgetPassword {

	@ApiModelProperty(value = "流水号", hidden = true)
	private String id;

	@ApiModelProperty(value = "邮箱或手机")
	private String emailOrMobile;

	@ApiModelProperty(value = "验 证 码")
	private String captcha;

}
