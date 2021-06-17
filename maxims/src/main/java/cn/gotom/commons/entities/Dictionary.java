package cn.gotom.commons.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotBlank;

import org.springframework.data.relational.core.mapping.Table;

import cn.gotom.commons.data.ForeignKey;
import cn.gotom.commons.data.UniqueIndex;
import cn.gotom.commons.model.TenantEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table("base_dictionary")
@javax.persistence.Table(//
		name = "base_dictionary", //
		uniqueConstraints = {
				@UniqueConstraint(name = "UK_DICTIONARY_LABEL_CATEGORYID", columnNames = { "label", "categoryId" })//
		}, //
		indexes = { //
//				@Index(unique = false, columnList = "created", name = "IDX_DICTIONARY_CREATED"), //
		}//
)
@ApiModel(value = "数据字典", description = "数据字典")
public class Dictionary extends TenantEntity {

	private static final long serialVersionUID = -506237846404236775L;

	@ApiModelProperty(value = "字典标签")
	@NotBlank(message = "字典标签不可以为空")
	@UniqueIndex({ "label", "categoryId" })
	private String label;

	@ApiModelProperty(value = "字典键值")
	private String value;

	@ApiModelProperty(value = "排列顺序")
	private Integer sort;

	@ApiModelProperty(value = "默 认 值")
	private Boolean defaults;

	@ApiModelProperty(value = "字典样式")
	@Column(length = 64)
	private String style;

	@ApiModelProperty(value = "所属类别")
	@Column(nullable = false, length = 32)
	@ForeignKey(DictionaryCategory.class)
	private String categoryId;
}
