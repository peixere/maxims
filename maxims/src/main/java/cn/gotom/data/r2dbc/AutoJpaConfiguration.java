package cn.gotom.data.r2dbc;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

import javax.persistence.EntityManager;
import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.dialect.MySQL8Dialect;
import org.hibernate.engine.spi.SessionImplementor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateProperties;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateSettings;
import org.springframework.boot.autoconfigure.orm.jpa.JpaBaseConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.AbstractJpaVendorAdapter;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.jta.JtaTransactionManager;

import com.mysql.cj.jdbc.Driver;
import com.zaxxer.hikari.util.DriverDataSource;

import cn.gotom.commons.json.JSON;
import lombok.extern.slf4j.Slf4j;

@Configuration(proxyBeanMethods = false)
@ConditionalOnClass({ LocalContainerEntityManagerFactoryBean.class, EntityManager.class, SessionImplementor.class })
@EnableConfigurationProperties({ JpaProperties.class, HibernateProperties.class, DataSourceProperties.class })
@Slf4j
class AutoJpaConfiguration extends JpaBaseConfiguration {

	private final HibernateProperties hibernateProperties;

	protected AutoJpaConfiguration(//
			RoutingConnectionFactory factory, //
			DataSourceProperties dataSourceProperties, //
			JpaProperties properties, //
			HibernateProperties hibernateProperties, //
			ObjectProvider<JtaTransactionManager> jtaTransactionManager) {
		super(createDataSource(factory, dataSourceProperties), properties, jtaTransactionManager);
		this.hibernateProperties = hibernateProperties;
	}

	@Override
	protected AbstractJpaVendorAdapter createJpaVendorAdapter() {
		getProperties().setShowSql(true);
		getProperties().setGenerateDdl(true);
		HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
		adapter.setDatabase(Database.MYSQL);
		adapter.setDatabasePlatform(MySQL8Dialect.class.getName());
		return adapter;
	}

	@Override
	protected Map<String, Object> getVendorProperties() {
		String ddlAuto = hibernateProperties.getDdlAuto();
		if (StringUtils.isBlank(ddlAuto) || "none".equals(ddlAuto)) {
			hibernateProperties.setDdlAuto("update");
		}
		log.info(JSON.format(getPackagesToScan()));
		log.info(JSON.format(getProperties()));
		return new LinkedHashMap<>(this.hibernateProperties
				.determineHibernateProperties(getProperties().getProperties(), new HibernateSettings()));
	}

	private static DataSource createDataSource(RoutingConnectionFactory factory, DataSourceProperties dsProperties) {
		if (factory != null && factory.getProperties().length > 0) {
			RoutingProperties prop = factory.getProperties()[0];
			if (StringUtils.isNotBlank(prop.getUsername()) && StringUtils.isNotEmpty(prop.getPassword())) {
				dsProperties.setUsername(prop.getUsername());
				dsProperties.setPassword(prop.getPassword());
			}
			if (StringUtils.isNotBlank(prop.getUrl())) {
				String url = prop.getUrl();
				url = "jdbc" + url.substring(url.indexOf(":"), url.length());
				dsProperties.setUrl(url);
			}
		}
		if (StringUtils.isBlank(dsProperties.getDriverClassName())) {
			dsProperties.setDriverClassName(Driver.class.getName());
		}
		// spring.datasource.type: com.mysql.cj.jdbc.MysqlDataSource
		// spring.datasource.driverClassName: com.mysql.cj.jdbc.Driver
		DataSource dataSource = new DriverDataSource(dsProperties.getUrl(), //
				dsProperties.getDriverClassName(), //
				new Properties(), dsProperties.getUsername(), //
				dsProperties.getPassword());
		return dataSource;
	}

}
