package cn.gotom.data.r2dbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class AutoJpaClosing implements CommandLineRunner {

	@Autowired
	private LocalContainerEntityManagerFactoryBean factory;

	public AutoJpaClosing() {
	}

	@Override
	public void run(String... args) throws Exception {
		if (log.isInfoEnabled()) {
			log.info("Closing JPA EntityManagerFactory for persistence unit '" + factory + "'");
		}
		factory.destroy();
	}

}
