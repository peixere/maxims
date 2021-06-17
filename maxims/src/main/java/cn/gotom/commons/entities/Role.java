package cn.gotom.commons.entities;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotBlank;

import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Table;

import cn.gotom.commons.Note;
import cn.gotom.commons.data.LinkDelete;
import cn.gotom.commons.data.LinkDeletes;
import cn.gotom.commons.data.UniqueIndex;
import cn.gotom.commons.model.TenantEntity;
import cn.gotom.commons.utils.TextUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table("base_role")
@javax.persistence.Table(//
		name = "base_role", //
		uniqueConstraints = { //
				@UniqueConstraint(name = "UK_ROLE_NAME_TENANTID", columnNames = { "name", "tenantId" })//
		}, //
		indexes = { //
				@Index(unique = false, columnList = "name", name = "IDX_ROLE_NAME"), //
				@Index(unique = false, columnList = "tenantId", name = "IDX_ROLE_TENANTID"), //
//				@Index(unique = false, columnList = "created", name = "IDX_ROLE_CREATED"), //
		}//
)
@ApiModel("角色信息")
@LinkDeletes({ //
		@LinkDelete(value = UserRole.class, column = "role_id"), //
		@LinkDelete(value = RoleRight.class, column = "role_id") }//
)
public class Role extends TenantEntity {

	private static final long serialVersionUID = 1892273722398118670L;
//	private static final String ADMIN = "超级管理员";
//	private static final Role SUPERADMIN = of();
//
//	private static Role of() {
//		Role role = new Role();
//		role.setSuperAdmin(true);
//		role.setName(Role.ADMIN);
//		role.setAuthorities("/**");
//		return role;
//	}
//
//	public static Role admin() {
//		return SUPERADMIN;
//	}

	@ApiModelProperty(value = "角色名称")
	@Column(length = 100)
	@NotBlank(message = "组织名称不可以为空")
	@UniqueIndex({ "name", "tenantId" })
	private String name;

	@ApiModelProperty(value = "排列顺序")
	private Integer sort;

	@ApiModelProperty(value = "备注信息")
	private String memo;

	@ApiModelProperty(value = "组织架构ID")
	@Column(nullable = false)
	private String structureId;

	@ApiModelProperty(value = "组织名称")
	@ReadOnlyProperty
	private transient String structure;
	
	@ApiModelProperty(value = "角色权限")
	@Column(updatable = false)
	private String authorities;


	@Note("角色权限")
	public List<String> authorities() {
		return TextUtils.split(authorities);
	}

	@ApiModelProperty(value = "拥有功能ID", notes = "角色配置时使用")
	@Transient
	private transient List<String> rightIdList;
}
