package cn.gotom.commons.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import org.springframework.data.domain.Sort.Direction;

import com.fasterxml.jackson.annotation.JsonIgnore;

import cn.gotom.commons.Note;
import cn.gotom.commons.data.OrderBy;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 
 * 实体表基类
 * 
 * @author xxx
 * 
 * @version 2012-12-03
 * 
 * @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")。控制入参
 * 
 * @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")。
 * 
 */
@Getter
@Setter
@MappedSuperclass
public abstract class SuperEntity extends IDEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3374620984663200989L;
	public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
	public static final String DATE_FORMAT = "yyyy-MM-dd";
	public static final String TIME_FORMAT = "HH:mm:ss";
	public static final String TIMEZONE = "GMT+8";
	@Note("通配符")
	public static final String WILDCARD = "*";
	@Note("创建时间字段（所有表共有）")
	public static final String CREATED_COLUMN = "create_time";
	public static final String DELETED_COLUMN = "deleted";

	@ApiModelProperty(value = "创建时间", hidden = true)
	@Column(updatable = false)
	@JsonIgnore
	@OrderBy(value = Direction.DESC, order = -1)
	private LocalDateTime createTime;

	@ApiModelProperty(value = "创建人", hidden = true)
	@Column(length = 50, updatable = false)
	private String creater;

	@JsonIgnore
	@ApiModelProperty(value = "更新时间", hidden = true)
	@Column(nullable = false)
	private LocalDateTime editTime;

	@ApiModelProperty(value = "更新人", hidden = true)
	@Column(length = 50, nullable = false)
	private String updater;

	@ApiModelProperty(value = "软删除标志(false/0:未删除 true/1:已删除)")
	@Column(name = DELETED_COLUMN, updatable = false)
	private Boolean deleted;

	@ApiModelProperty(value = "停用标志(false/0:未停用 true/1:已停用)")
	@Column(updatable = false)
	private Boolean disabled;

//	@ApiModelProperty(value = "版本号(乐观锁)", notes = "乐观锁")
//	@Column(updatable = false)
//	private Long versionNum;

	@Deprecated
	public LocalDateTime getCreateTime() {
		return createTime;
	}

	@Deprecated
	public void setCreateTime(LocalDateTime createTime) {
		this.createTime = createTime;
	}

	@Deprecated
	public LocalDateTime getEditTime() {
		return editTime;
	}

	@Deprecated
	public void setEditTime(LocalDateTime editTime) {
		this.editTime = editTime;
	}

	public LocalDateTime getUpdated() {
		return editTime;
	}

	public void setUpdated(LocalDateTime updated) {
		editTime = updated;
	}

	public LocalDateTime getCreated() {
		return createTime;
	}

	public void setCreated(LocalDateTime created) {
		createTime = created;
	}

}
