package cn.gotom.commons.entities;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;

import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import cn.gotom.commons.data.Deleted;
import cn.gotom.commons.data.ForeignKey;
import cn.gotom.commons.model.SuperEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table("base_tenant")
@javax.persistence.Table(//
		name = "base_tenant", //
		indexes = { //
				// @Index(unique = true, columnList = "name", name = "UK_TENANT_NAME"), //
				@Index(unique = false, columnList = "userId", name = "IDX_TENANT_USERID"),//
		}//
)
@ApiModel("租户信息")
public class Tenant extends SuperEntity {

	private static final long serialVersionUID = 1892273722398118670L;
	private static final Tenant SUPERADMIN = of();

	private static Tenant of() {
		Tenant e = new Tenant();
		e.setSuperAdmin(true);
		return e;
	}

	public static Tenant admin() {
		return SUPERADMIN;
	}

	@ApiModelProperty(value = "租户名称")
	@Column(length = 100)
	private String name;

	@ApiModelProperty(value = "排列顺序")
	private Integer sort;

	@ApiModelProperty(value = "系统标题")
	private String title;

	@ApiModelProperty(value = "系统图标")
	private String logoId;

	@ApiModelProperty(value = "坐 标 点", notes = "格式： 经度,纬度")
	@Column(length = 64)
	private String position;

	@ApiModelProperty(value = "主页信息", notes = "格式 type（0-页面 1-图形）;内容（页面地址或者图形id）")
	private String main;

	@ApiModelProperty(value = "看板路由")
	private String board;

	@ApiModelProperty(value = "租户地址")
	private String address;

	@ApiModelProperty(value = "公司名称")
	@Column()
	private String company;

	@ApiModelProperty(value = "公司手机")
	@Column(length = 50)
	private String mobile;

	@ApiModelProperty(value = "管理员")
	@Column(nullable = false, length = 32)
	@ForeignKey(User.class)
	private String userId;

	@ApiModelProperty(value = "超级管理员")
	@Column(updatable = false, nullable = false)
	@JsonIgnore
	private Boolean superAdmin;

	@ApiModelProperty(value = "软删除标志(false/0:未删除 true/1:已删除)")
	@Column()
	@Deleted
	private Boolean deleted;
	
	@ApiModelProperty(value = "停用标志(false/0:未停用 true/1:已停用)")
	private Boolean disabled;
	
	private void setSuperAdmin(Boolean superAdmin) {
		this.superAdmin = superAdmin;
	}

	@ApiModelProperty(value = "拥有功能", notes = "租户配置时使用")
	@Transient
	private transient List<String> rightIdList;
}
