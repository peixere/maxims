package cn.gotom.gateway.v.form;

import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserRoleForm {

	@ApiModelProperty(value = "用户ID")
	private String userId;
	@ApiModelProperty(value = "角色ID")
	private List<String> roleIdList;
}
