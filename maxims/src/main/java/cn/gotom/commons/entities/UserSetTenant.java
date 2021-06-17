package cn.gotom.commons.entities;

import javax.persistence.Entity;

import org.springframework.data.relational.core.mapping.Table;

import cn.gotom.commons.data.Forbid;
import cn.gotom.commons.model.TenantEntity;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table("base_user")
@javax.persistence.Table(name = "base_user")
@Forbid
@ApiModel("切换租户")
public class UserSetTenant extends TenantEntity {
	private static final long serialVersionUID = -8380173386890928740L;

}
