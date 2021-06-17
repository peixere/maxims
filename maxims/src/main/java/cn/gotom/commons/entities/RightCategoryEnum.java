package cn.gotom.commons.entities;

public enum RightCategoryEnum {

	DIR("菜单目录"), // 菜单目录
	URL("功能连接"), // 功能连接
	BTN("功能按钮"),// 功能按钮
	;

	private String memo;

	private RightCategoryEnum(String memo) {
		this.memo = memo;
	}

	public String memo() {
		return memo;
	}

	public String value() {
		return name();
	}

	@Override
	public String toString() {
		return name();
	}

	public static RightCategoryEnum of(String name) {
		for (RightCategoryEnum c : RightCategoryEnum.values()) {
			if (c.name().equalsIgnoreCase(name)) {
				return c;
			}
		}
		return DIR;
	}
}
