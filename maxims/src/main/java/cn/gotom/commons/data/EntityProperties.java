package cn.gotom.commons.data;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.Transient;
import javax.persistence.Version;

import com.fasterxml.jackson.annotation.JsonIgnore;

import cn.gotom.commons.model.Sorted;
import cn.gotom.commons.utils.TextUtils;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Setter(value = AccessLevel.PRIVATE)
@Getter
public class EntityProperties {
	public enum FieldType {
		BOOLEAN, NUMERIC, DATE, STRING, ENUM
	}

	@JsonIgnore
	private final PropertyDescriptor propertyDescriptor;
	@JsonIgnore
	private final Field field;
	private final String column;
	private final String name;
	private final boolean updatable;
	private final boolean id;
	private final boolean version;
	private final boolean temporal;
	private final boolean persistable;
//	private FieldType fieldType = FieldType.STRING;

	public EntityProperties(Field field, PropertyDescriptor propertyDescriptor) {
		this.propertyDescriptor = propertyDescriptor;
		this.field = field;
		this.name = field.getName();
		String column = name;
		boolean updatable = true;
		Column c = getAnnotation(Column.class);
		if (c != null) {
			if (!c.name().isEmpty()) {
				column = c.name();
			} else {
				column = TextUtils.camelToUnderline(column);
			}
			updatable = c.updatable();
		} else {
			column = TextUtils.camelToUnderline(column);
		}
		this.column = column;
		this.updatable = updatable;
		this.id = (hasAnnotation(Id.class) || hasAnnotation(EmbeddedId.class));
		this.version = (hasAnnotation(Version.class));
		this.temporal = (hasAnnotation(Temporal.class));
		this.persistable = (persistable());
//		setFieldType(fieldType());
	}

	protected FieldType fieldType() {
		if (field.getType().equals(java.lang.Integer.class)//
				|| field.getType().equals(java.lang.Byte.class)//
				|| field.getType().equals(java.lang.Long.class)//
				|| field.getType().equals(java.lang.Double.class)//
				|| field.getType().equals(java.lang.Float.class)//
				|| field.getType().equals(java.lang.Short.class)//
				|| field.getType().equals(byte[].class)//
				|| field.getType().equals(java.math.BigDecimal.class)//
				|| field.getType().equals(java.math.BigInteger.class)) {
			return FieldType.NUMERIC;
		}
		if (field.getType().equals(java.lang.Boolean.class)) {
			return FieldType.BOOLEAN;
		}
		if (field.getType().equals(java.util.Date.class)//
				|| field.getType().equals(java.sql.Date.class)//
				|| field.getType().equals(java.sql.Time.class)//
				|| field.getType().equals(java.sql.Timestamp.class)//
				|| field.getType().equals(java.sql.Date.class)) {
			return FieldType.DATE;
		}
		return FieldType.STRING;
	}

	private boolean persistable() {
		if (!isPersistable(field.getDeclaringClass())) {
			return false;
		}
		if (!isDbType(field.getType())) {
			return false;
		}
		int mod = field.getModifiers();
		if (hasAnnotation(Transient.class) || Modifier.isTransient(mod) || Modifier.isFinal(mod)
				|| Modifier.isStatic(mod)) {
			return false;
		}
		return true;
	}

	public <T extends Annotation> boolean hasAnnotation(Class<T> annotationClass) {
		return getAnnotation(annotationClass) != null;
	}

	public Sorted.Order getOrder() {
		OrderBy annot = null;
		if (annot == null && propertyDescriptor != null) {
			Method read = propertyDescriptor.getReadMethod();
			annot = read != null ? read.getAnnotation(OrderBy.class) : null;
		}
		if (annot == null && propertyDescriptor != null) {
			Method method = propertyDescriptor.getWriteMethod();
			annot = method != null ? method.getAnnotation(OrderBy.class) : null;
		}
		if (annot == null) {
			annot = field.getAnnotation(OrderBy.class);
		}
		if (annot == null) {
			return null;
		} else {
			return new Sorted.Order(annot.value(), column, annot.order());
		}
	}

	public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
		T annot = null;
		if (annot == null && propertyDescriptor != null) {
			Method read = propertyDescriptor.getReadMethod();
			annot = read != null ? read.getAnnotation(annotationClass) : null;
		}
		if (annot == null && propertyDescriptor != null) {
			Method method = propertyDescriptor.getWriteMethod();
			annot = method != null ? method.getAnnotation(annotationClass) : null;
		}
		if (annot == null) {
			annot = field.getAnnotation(annotationClass);
		}
		return annot;
	}

	private static boolean isPersistable(Class<?> klass) {
		return null != klass.getAnnotation(Table.class)//
				|| null != klass.getAnnotation(Entity.class)//
				|| null != klass.getAnnotation(org.springframework.data.relational.core.mapping.Table.class) //
				|| null != klass.getAnnotation(Embeddable.class) //
				|| null != klass.getAnnotation(MappedSuperclass.class);
	}

	private static boolean isDbType(Class<?> clazz) {
		if (clazz.isPrimitive()) {
			return true;
		}
		if (clazz.equals(java.lang.Integer.class)//
				|| clazz.equals(java.lang.Byte.class)//
				|| clazz.equals(java.lang.Long.class)//
				|| clazz.equals(java.lang.Double.class)//
				|| clazz.equals(java.lang.Float.class)//
				|| clazz.equals(java.lang.Short.class)//
				|| clazz.equals(java.lang.Boolean.class)//
				|| clazz.equals(java.lang.String.class)//
				|| clazz.equals(java.lang.Byte[].class)//
				|| clazz.equals(byte[].class)//
				|| clazz.equals(java.math.BigDecimal.class)//
				|| clazz.equals(java.math.BigInteger.class)//
				|| clazz.equals(java.util.Date.class)//
				|| clazz.equals(java.time.LocalDateTime.class)//
				|| clazz.equals(java.time.LocalDate.class)//
				|| clazz.equals(java.time.LocalTime.class)//
				|| clazz.equals(java.time.Duration.class)//
				|| clazz.equals(java.sql.Date.class)//
				|| clazz.equals(java.sql.Time.class)//
				|| clazz.equals(java.sql.Timestamp.class)//
				|| clazz.equals(java.sql.Date.class)) {
			return true;
		}
		return false;
	}
}