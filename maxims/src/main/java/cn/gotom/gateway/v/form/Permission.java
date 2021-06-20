package cn.gotom.gateway.v.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ApiModel("权限列表")
@ToString
public class Permission {

	@ApiModelProperty(value = "权限编码")
	private String code;

	@ApiModelProperty(value = "允许访问", hidden = true)
	private Boolean allow;
}
