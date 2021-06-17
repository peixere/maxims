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
@Table("base_right_tenant")
@javax.persistence.Table(//
		name = "base_right_tenant", //
		uniqueConstraints = { @UniqueConstraint(name = "UK_RIGHTID_TENANTID", columnNames = { "rightId", "tenantId" })//
		}, //
		indexes = { //
				@Index(unique = false, columnList = "rightId", name = "IDX_RIGHT_TENANT_RIGHTID"), //
				@Index(unique = false, columnList = "tenantId", name = "IDX_RIGHT_TENANT_TENANTID"), //
		})
@Forbid
@ApiModel("用户所属租户")
public class RightTenant extends TenantEntity {

	private static final long serialVersionUID = 1892273722398118670L;

	@ApiModelProperty(value = "功能菜单ID")
	@Column(nullable = false, length = 32)
	@ForeignKey(Right.class)
	private String rightId;

	/**
	 * {@inheritDoc}
	 */
	public int hashCode() {
		return (this.getTenantId() + rightId).hashCode();
	}
}
