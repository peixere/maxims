package cn.gotom.commons.entities;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Index;
import javax.validation.constraints.NotBlank;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import cn.gotom.commons.Note;
import cn.gotom.commons.data.LinkDelete;
import cn.gotom.commons.data.LinkDeletes;
import cn.gotom.commons.model.PlatformEnum;
import cn.gotom.commons.model.SuperEntity;
import cn.gotom.commons.utils.TextUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table("base_right")
@javax.persistence.Table(//
		name = "base_right", //
		indexes = { //
				@Index(unique = true, columnList = "code", name = "IDX_RIGHT_CODE"), //
//				@Index(unique = false, columnList = "created", name = "IDX_RIGHT_CREATED"), //
		}//
)
@ApiModel("功能菜单")
@LinkDeletes({ //
		@LinkDelete(value = RoleRight.class, column = "right_id"), //
		@LinkDelete(value = RightTenant.class, column = "right_id") }//
)
public class Right extends SuperEntity {

	private static final long serialVersionUID = 2273939744862567921L;

	@ApiModelProperty(value = "功能名称")
	@Column(length = 128)
	@NotBlank(message = "功能名称不可以为空")
	private String name;

	@ApiModelProperty(value = "功能编码")
	@Column(length = 128, unique = false)
	private String code;

	@ApiModelProperty(value = "平台标识(WEB,APP,APPLET,PUBLIC)")
	@Column(length = 10)
	@Enumerated(EnumType.STRING)
	private PlatformEnum type;

	@ApiModelProperty(value = "功能类别(DIR.菜单目录, URL.连接地址, BTN.功能按钮)")
	@Column(length = 10)
	@Enumerated(EnumType.STRING)
	private RightCategoryEnum category;

	@ApiModelProperty(value = "排列顺序")
	private Integer sort;

	@ApiModelProperty(value = "页面类型", notes = "webPage属性")
	@Column(length = 10)
	@Enumerated(EnumType.STRING)
	private RightTargetEnum target;

	@ApiModelProperty(value = "WEB页面地址")
	private String webPage;

	@ApiModelProperty(value = "权限数据(URL正则表达式，“;”区分开)", notes = "URL正则表达式，“;”区分开")
	@Column(length = 512)
	private String urlPatterns;

	@ApiModelProperty(value = "图标")
	@Column(length = 64)
	private String iconFile;

	@ApiModelProperty(value = "上级菜单")
	@Column(length = 32)
	private String parentId;

	@ApiModelProperty(value = "超级功能")
	@Column(updatable = false)
	private Boolean superAdmin;

	@JsonIgnore
	@ApiModelProperty(value = "初始化名称", notes = "菜单初始化", hidden = true)
	@Column(updatable = false)
	private String initName;

	@ApiModelProperty(value = "子功能", hidden = true)
	@Transient
	private transient List<Right> children;
	
	@ApiModelProperty(value = "停用标志(false/0:未停用 true/1:已停用)")
	private Boolean disabled;	

	@Note("权限数据")
	public List<String> authorities() {
		List<String> authorities = TextUtils.split(urlPatterns);
		if (StringUtils.isNotBlank(webPage)) {
			authorities.add(webPage);
		}
		return authorities;
	}

}
