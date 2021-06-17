package cn.gotom.commons.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
@Table("base_user_tenant")
@javax.persistence.Table(//
		name = "base_user_tenant", //
		uniqueConstraints = {
				@UniqueConstraint(name = "UK_USER_TENANT_USERID_TENANTID", columnNames = { "userId", "tenantId" })//
		}, //
		indexes = { //
				@Index(unique = false, columnList = "userId", name = "IDX_USER_TENANT_USERID"), //
				@Index(unique = false, columnList = "tenantId", name = "IDX_USER_TENANT_TENANTID"), //
		})
@Forbid
@ApiModel("用户所属租户")
public class UserTenant extends TenantEntity {

	private static final long serialVersionUID = 1892273722398118670L;

	@ApiModelProperty(value = "用户ID")
	@Column(nullable = false, length = 32)
	@ForeignKey(User.class)
	private String userId;

	@ApiModelProperty(value = "申请加入")
	@Enumerated(EnumType.STRING)
	@Column(length = 20)
	private UserTenantJoin joined;

	/**
	 * {@inheritDoc}
	 */
	public int hashCode() {
		return (getTenantId() + userId).hashCode();
	}
}
