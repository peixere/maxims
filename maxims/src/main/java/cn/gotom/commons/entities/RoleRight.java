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
@Table("base_role_right")
@javax.persistence.Table(//
		name = "base_role_right", //
		uniqueConstraints = { @UniqueConstraint(name = "UK_ROLE_RIGHT", columnNames = { "roleId", "rightId" }) }, //
		indexes = { //
				@Index(unique = false, columnList = "rightId", name = "IDX_ROLE_RIGHT_RIGHTID"), //
				@Index(unique = false, columnList = "roleId", name = "IDX_ROLE_RIGHT_ROLEID"), //
		})
@Forbid
@ApiModel("角色拥有权限")
public class RoleRight extends TenantEntity {

	private static final long serialVersionUID = 1892273722398118670L;

	@ApiModelProperty(value = "角色ID")
	@Column(nullable = false, length = 32)
	@ForeignKey(Role.class)
	private String roleId;

	@ApiModelProperty(value = "权限ID")
	@Column(nullable = false, length = 32)
	@ForeignKey(Right.class)
	private String rightId;

	/**
	 * {@inheritDoc}
	 */
	public int hashCode() {
		return (roleId + rightId).hashCode();
	}
}
