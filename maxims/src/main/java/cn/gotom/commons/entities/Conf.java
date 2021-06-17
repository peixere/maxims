package cn.gotom.commons.entities;

import javax.persistence.Entity;
import javax.persistence.Index;

import org.springframework.data.relational.core.mapping.Table;

import cn.gotom.commons.data.UniqueIndex;
import cn.gotom.commons.model.SuperEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table("base_conf")
@javax.persistence.Table(//
		name = "base_conf", //
		indexes = { //
				@Index(unique = true, columnList = "code", name = "UK_CONF_CODE"),
//				@Index(unique = false, columnList = "created", name = "IDX_CONF_CREATED"), //
		}//
)
@ApiModel(value = "系统参数", description = "系统参数")
public class Conf extends SuperEntity {

	private static final long serialVersionUID = -506237846404236775L;

	@ApiModelProperty(value = "参数名称")
	@UniqueIndex
	private String name;

	@ApiModelProperty(value = "参数键名")
	@UniqueIndex
	private String code;

	@ApiModelProperty(value = "参数键值")
	private String value;

	@ApiModelProperty(value = "排列顺序")
	private Integer sort;
}
