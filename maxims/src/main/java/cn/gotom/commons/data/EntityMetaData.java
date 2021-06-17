package cn.gotom.commons.data;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;

import org.springframework.beans.BeanUtils;
import org.springframework.data.relational.core.mapping.Table;

import cn.gotom.commons.Note;
import cn.gotom.commons.json.JSON;
import cn.gotom.commons.model.Sorted;
import cn.gotom.commons.model.Sorted.Order;
import cn.gotom.commons.utils.ObjectUtils;
import cn.gotom.commons.utils.TextUtils;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Setter(AccessLevel.PROTECTED)
@Getter
@Slf4j
public class EntityMetaData {

	public final static Map<Class<?>, EntityMetaData> cache = new HashMap<>();

	private Class<?> clazz;
	private boolean entity = true;
	private String name;
	private String table;
	private boolean sqlDeleted;
	private String deleteColumn;
	private EntityProperties id;
	private EntityProperties version;
	@Note("所有持久化字段 Map<变量名, 属性>")
	private Map<String, EntityProperties> props;
	private org.springframework.data.domain.Sort sort;

	private LinkDelete[] linkDeletes;

	EntityMetaData(Class<?> clazz, boolean entityFlag) {
		this.setClazz(clazz);
		this.setName(clazz.getSimpleName());
		this.setEntity(entityFlag);
		this.setDeleteColumn(sqlDelete(clazz));
		if (isEntity()) {
			initEntity();
		}
		LinkDeletes linkDeletes = clazz.getAnnotation(LinkDeletes.class);
		if (linkDeletes != null) {
			this.setLinkDeletes(linkDeletes.value());
		}
		initProps();
	}

	private void initEntity() {
		Entity entity = getEntity(clazz);
		javax.persistence.Table table = getTable(clazz);
		if (table != null && !table.name().isEmpty()) {
			setTable(table.name());
		} else if (!entity.name().isEmpty()) {
			setTable(entity.name());
		} else {
			Table dataTable = getDataTable(clazz);
			if (dataTable != null && !dataTable.value().isEmpty()) {
				setTable(dataTable.value());
			} else {
				setTable(clazz.getSimpleName());
			}
		}
	}

	private void initProps() {
		Map<String, EntityProperties> props = new LinkedHashMap<>();
		Field[] fields = ObjectUtils.getNonAccessModifierFields(getClazz());
		List<Sorted.Order> orders = new ArrayList<>();
		for (Field field : fields) {
			PropertyDescriptor pd = BeanUtils.getPropertyDescriptor(getClazz(), field.getName());
			EntityProperties prop = new EntityProperties(field, pd);
			if (log.isDebugEnabled()) {
				log.debug(JSON.format(prop));
			}
			if (prop.isPersistable()) {
				if (!props.containsKey(prop.getName())) {
					props.put(prop.getName(), prop);
					if (prop.isId()) {
						setId(prop);
					}
					if (prop.isVersion()) {
						setVersion(prop);
					}
				} else {
					EntityProperties propHas = props.get(prop.getName());
					log.warn("Duplicate fields : {}.{} {}.{}", propHas.getField().getDeclaringClass().getSimpleName(),
							prop.getName(), field.getDeclaringClass().getSimpleName(), prop.getName());
				}
				Sorted.Order order = prop.getOrder();
				if (order != null) {
					orders.add(order);
				}
			}
		}
		orders.sort(Comparator.comparing(Sorted.Order::getOrder));
		sort(orders);
		if (getId() == null) {
			throw new RuntimeException(String.format("%s not found id cloumn", getName()));
		}
		setProps(props);
	}

	private void sort(List<Sorted.Order> orders) {
		if (orders != null && orders.size() > 0) {
			List<org.springframework.data.domain.Sort.Order> these = new ArrayList<>();
			for (Order order : orders) {
				these.add(new org.springframework.data.domain.Sort.Order(order.getDirection(), order.getProperty()));
			}
			setSort(org.springframework.data.domain.Sort.by(these));
		} else {
			setSort(org.springframework.data.domain.Sort.unsorted());
		}
	}

	private void setSort(org.springframework.data.domain.Sort sort) {
		this.sort = sort;
	}

	public String[] getFields() {
		String[] fieldNames = new String[props.size()];
		props.keySet().toArray(fieldNames);
		return fieldNames;
	}

	public Collection<EntityProperties> getProperties() {
		return props.values();
	}

	public Set<String> getFieldSet() {
		return props.keySet();
	}

	public EntityProperties getProperty(String name) {
		EntityProperties prop = props.get(name);
		if (prop == null) {
			prop = props.get(TextUtils.underlineToCamel(name));
		}
		return prop;
	}

	public Field getField(String name) {
		EntityProperties prop = props.get(name);
		return prop != null ? prop.getField() : null;
	}

	private String sqlDelete(Class<?> klass) {
		Class<?> clazz = klass;
		while (clazz != null) {
			SQLDelete logicDelete = clazz.getAnnotation(SQLDelete.class);
			if (null != logicDelete) {
				sqlDeleted = true;
				return logicDelete.field();
			}
			clazz = clazz.getSuperclass();
		}
		return null;
	}

	public static EntityMetaData get(Class<?> klass, boolean isEntity) {
		final EntityMetaData metadata;
		if (cache.containsKey(klass)) {
			metadata = cache.get(klass);
			return metadata;
		} else {
			if (isEntity && !isEntity(klass)) {
				throw new RuntimeException(
						String.format("%s not found Annotation javax.persistence.Entity", klass.getName()));
			}
			metadata = new EntityMetaData(klass, isEntity);
		}
		cache.put(klass, metadata);
		return metadata;
	}

	public static EntityMetaData get(Class<?> klass) {
		return get(klass, true);
	}

	public static Object getValue(Object orig, EntityProperties prop) {
		try {
			Field field = prop.getField();
			if (field != null) {
				return ObjectUtils.readField(orig, field);
			}
		} catch (Throwable e) {
			log.warn(e.getMessage(), e.toString());
		}
		return null;
	}

	public static void setDefaultValue(Object orig) {
		Field[] fields = ObjectUtils.getNonAccessModifierFields(orig.getClass());
		for (Field field : fields) {
			if (ObjectUtils.readField(orig, field) == null) {
				ObjectUtils.setDefaultValue(orig, field);
			}
		}
	}

	public static boolean updatable(Object orig, String fieldName) {
		boolean updatable = true;
		Field field = ObjectUtils.getField(orig.getClass(), fieldName);
		if (field == null) {
			field = ObjectUtils.getField(orig.getClass(), TextUtils.underlineToCamel(fieldName));
		}
		if (field != null) {
			Column c = field.getAnnotation(Column.class);
			if (c != null) {
				updatable = c.updatable();
			}
		}
		return updatable;
	}

	public static boolean updatable(Field field) {
		boolean updatable = true;
		Column c = field.getAnnotation(Column.class);
		if (c != null) {
			updatable = c.updatable();
		}
		return updatable;
	}

	public static void setIdValue(Object orig, Object value) {
		try {
			EntityMetaData metadata = get(orig.getClass());
			Field field = metadata.getId().getField();
			if (field != null) {
				ObjectUtils.writeField(orig, field, value);
			}
		} catch (Throwable e) {
			log.warn(e.getMessage(), e.toString());
		}
	}

	public static javax.persistence.Table getTable(Class<?> klass) {
		Class<?> clazz = klass;
		while (clazz != null) {
			javax.persistence.Table entity = clazz.getAnnotation(javax.persistence.Table.class);
			if (null != entity) {
				return entity;
			}
			clazz = clazz.getSuperclass();
		}
		return null;
	}

	public static Table getDataTable(Class<?> klass) {
		Class<?> clazz = klass;
		while (clazz != null) {
			Table entity = clazz.getAnnotation(Table.class);
			if (null != entity) {
				return entity;
			}
			clazz = clazz.getSuperclass();
		}
		return null;
	}

	public static Entity getEntity(Class<?> klass) {
		Class<?> clazz = klass;
		while (clazz != null) {
			Entity entity = clazz.getAnnotation(Entity.class);
			if (null != entity) {
				return entity;
			}
			clazz = clazz.getSuperclass();
		}
		return null;
	}

	public static boolean isEntity(Class<?> klass) {
		Entity entity = getEntity(klass);
		if (entity != null) {
			return true;
		}
		javax.persistence.Table table = getTable(klass);
		if (table != null) {
			return true;
		}
		return getDataTable(klass) != null;
	}
}
