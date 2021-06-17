package cn.gotom.commons.model;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
public abstract class TenantEntity extends SuperEntity {

	private static final long serialVersionUID = 2245439601561837916L;
	
	public static final String TENANTID = "tenantId";
	public static final String TENANTID_COLUMN = "tenant_id";
	public static final String USERID = "userId";
	
	@Column(length = 32)
	@ApiModelProperty(value = "租户ID", hidden = true)
	private String tenantId;

}
