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
@Table("base_structure_user")
@javax.persistence.Table(//
		name = "base_structure_user", //
		uniqueConstraints = { //
				@UniqueConstraint(name = "UK_STRUCTUREUSER", columnNames = { "userId", "structureId" }),//
		}, //
		indexes = { //
				@Index(unique = false, columnList = "userId", name = "IDX_STRUCTURE_USERID"), //
				@Index(unique = false, columnList = "structureId", name = "IDX_STRUCTURE_STRUCTUREID"), //
		}//
)
@Forbid
@ApiModel("组织架构关联用户")
public class StructureUser extends TenantEntity {

	private static final long serialVersionUID = 5682262122367885624L;

	@ApiModelProperty(value = "用户ID")
	@Column(nullable = false, length = 32)
	@ForeignKey(User.class)
	private String userId;

	@ApiModelProperty(value = "组织架构ID")
	@Column(nullable = false, length = 32)
	@ForeignKey(Structure.class)
	private String structureId;

	/**
	 * {@inheritDoc}
	 */
	public int hashCode() {
		return (structureId + userId).hashCode();
	}
}
