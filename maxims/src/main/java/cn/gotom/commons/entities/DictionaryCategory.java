package cn.gotom.commons.entities;

import javax.persistence.Entity;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotBlank;

import org.springframework.data.relational.core.mapping.Table;

import cn.gotom.commons.data.UniqueIndex;
import cn.gotom.commons.model.TenantEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table("base_dictionary_category")
@javax.persistence.Table(//
		name = "base_dictionary_category", //
		uniqueConstraints = {
				@UniqueConstraint(name = "UK_DICTIONARY_CATEGORY_NAME", columnNames = { "code", "tenantId" })//
		}, //
		indexes = { //
//				@Index(unique = false, columnList = "created", name = "IDX_DICTIONARY_CATEGORY_CREATED"), //
		}//
)
@ApiModel(value = "字典类别", description = "字典类别")
public class DictionaryCategory extends TenantEntity {

	private static final long serialVersionUID = -506237846404236775L;

	@ApiModelProperty(value = "类别名称")
	private String name;

	@ApiModelProperty(value = "类别编码")
	@NotBlank(message = "类别编码不可以为空")
	@UniqueIndex({ "code", "tenantId" })
	private String code;

	@ApiModelProperty(value = "排列顺序")
	private Integer sort;

}
