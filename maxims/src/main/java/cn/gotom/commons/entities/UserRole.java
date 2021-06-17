package cn.gotom.commons.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.UniqueConstraint;

import org.springframework.data.relational.core.mapping.Table;

import cn.gotom.commons.data.Forbid;
import cn.gotom.commons.data.ForeignKey;
import cn.gotom.commons.model.TenantEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table("base_user_role")
@javax.persistence.Table(//
		name = "base_user_role", //
		uniqueConstraints = {
				@UniqueConstraint(name = "UK_USER_ROLE_USERID_ROLEID", columnNames = { "userId", "roleId" })//
		}, //
		indexes = { //
				@Index(unique = false, columnList = "userId", name = "IDX_USER_ROLE_USERID"), //
				@Index(unique = false, columnList = "roleId", name = "IDX_USER_ROLE_ROLEID"), //
		})
@Forbid
@ApiModel("用户拥有角色")
public class UserRole extends TenantEntity {

	private static final long serialVersionUID = 1892273722398118670L;

	@ApiModelProperty(value = "角色ID")
	@Column(nullable = false, length = 32)
	@ForeignKey(Role.class)
	private String roleId;

	@ApiModelProperty(value = "管理员")
	@Column(nullable = false, length = 32)
	@ForeignKey(User.class)
	private String userId;

//	@ApiModelProperty(value = "组织架构ID", hidden = true)
//	@ReadOnlyProperty
//	private transient String structureId;

	/**
	 * {@inheritDoc}
	 */
	public int hashCode() {
		return (roleId + userId).hashCode();
	}
}
