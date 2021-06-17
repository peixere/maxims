package cn.gotom.commons.entities;

public enum UserTenantJoin {

	REFUSE("拒绝加入"), //
	JOINING("申请加入"), //
	JOINED("申请通过"),//
	;

	private String memo;

	private UserTenantJoin(String memo) {
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

	public static UserTenantJoin of(String name) {
		for (UserTenantJoin c : UserTenantJoin.values()) {
			if (c.name().equalsIgnoreCase(name)) {
				return c;
			}
		}
		return JOINED;
	}
}
