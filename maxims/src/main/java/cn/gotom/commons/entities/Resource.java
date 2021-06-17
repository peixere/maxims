package cn.gotom.commons.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;

import org.springframework.data.relational.core.mapping.Table;

import cn.gotom.commons.model.TenantEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table("base_resource")
@javax.persistence.Table(//
		name = "base_resource", //
		indexes = { //
				@Index(unique = false, columnList = "tenantId", name = "IDX_RESOURCE_TENANTID"), //
//				@Index(unique = false, columnList = "created", name = "IDX_RESOURCE_CREATED"), //
		}//
)
@ApiModel(value = "文件资源", description = "上传文件资源")
public class Resource extends TenantEntity {
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "文件名称")
	private String name;

	@ApiModelProperty(value = "排列顺序")
	private Integer sort;

	@ApiModelProperty(value = "文件路径")
	@Column(length = 512)
	private String path;

	@ApiModelProperty(value = "文件大小")
	private Long size;

	@ApiModelProperty(value = "文件类型(0.图片 1.音频 2.视频 3.其它)")
	private Integer type;

	@ApiModelProperty(value = "文件后缀")
	@Column(length = 16)
	private String suffixes;

	@ApiModelProperty(value = "缩略图路径")
	@Column(length = 512)
	private String thumb;

}
