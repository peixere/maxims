package cn.gotom.commons.entities;

public enum RightTargetEnum {

	IFRAME("框架页面"), //
	COMPONENT("内部组件"), //
	OLD("内部组件(旧)"),//
	;

	private String memo;

	private RightTargetEnum(String memo) {
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

	public static RightTargetEnum of(String name) {
		for (RightTargetEnum c : RightTargetEnum.values()) {
			if (c.name().equalsIgnoreCase(name)) {
				return c;
			}
		}
		return COMPONENT;
	}
}
