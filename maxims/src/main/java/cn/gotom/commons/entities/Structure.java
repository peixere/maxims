package cn.gotom.commons.entities;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotBlank;

import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Table;

import cn.gotom.commons.data.Deleted;
import cn.gotom.commons.data.LinkDelete;
import cn.gotom.commons.data.LinkDeletes;
import cn.gotom.commons.data.UniqueIndex;
import cn.gotom.commons.model.TenantEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table("base_structure")
@javax.persistence.Table(//
		name = "base_structure", //
		uniqueConstraints = { //
				// @UniqueConstraint(name = "UK_STRUCTURE_LEVEL", columnNames = { "tenantId",
				// "level", "levelDecimal" }), //
				@UniqueConstraint(name = "UK_STRUCTURE_NO", columnNames = { "tenantId", "no" }), //
				@UniqueConstraint(name = "UK_STRUCTURE_NAME", columnNames = { "tenantId", "name" }),//
		}, //
		indexes = { //
				@Index(unique = false, columnList = "tenantId", name = "IDX_STRUCTURE_TENANTID"), //
//				@Index(unique = false, columnList = "created", name = "IDX_STRUCTURE_CREATED"), //
		}//
)
@ApiModel("组织架构")
@LinkDeletes({ //
		@LinkDelete(value = StructureUser.class, column = "structure_id"), //
		@LinkDelete(value = Role.class, column = "structure_id"), //
})
public class Structure extends TenantEntity {

	private static final long serialVersionUID = 1892273722398118670L;

	@ApiModelProperty(value = "顶级组织架构")
	public static final String TOP = "顶级组织架构";

	@ApiModelProperty(value = "组织名称")
	@Column(length = 128)
	@NotBlank(message = "组织名称不可以为空")
	@UniqueIndex({ "name", "tenantId" })
	private String name;

	@ApiModelProperty(value = "负 责 人")
	@Column(length = 64)
	private String leader;

	@ApiModelProperty(value = "详细地址")
	private String address;

	@ApiModelProperty(value = "所属上级")
	@Column(length = 32, updatable = false)
	private String parentId;

	@ApiModelProperty(value = "联系电话")
	@Column(length = 32)
	private String phoneNum;

	@ApiModelProperty(value = "排列顺序")
	private Integer sort;

	@ApiModelProperty(value = "备注信息")
	private String memo;

	@ApiModelProperty(value = "组织类型( 0-其他, 1-省, 2-市, 3-区, 4-企业 / 用户, 5-配电房)", notes = " 0-其他, 1-省, 2-市, 3-区, 4-企业 / 用户, 5-配电房")
	private Integer type;

	@ApiModelProperty(value = "坐 标 点", notes = "格式： 经度,纬度")
	@Column(length = 64)
	private String position;

	@ApiModelProperty(value = "组织编号")
	@UniqueIndex({ "no", "tenantId" })
	private Integer no;

	@ApiModelProperty(value = "软删除标志(false/0:未删除 true/1:已删除)")
	@Column()
	@Deleted
	private Boolean deleted;
	
	@ApiModelProperty(value = "停用标志(false/0:未停用 true/1:已停用)")
	private Boolean disabled;
	
	@ApiModelProperty(value = "子架构", hidden = true)
	@Transient
	private transient List<Structure> children;
}
