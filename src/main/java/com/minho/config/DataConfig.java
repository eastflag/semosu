package com.minho.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@Lazy
@EnableTransactionManagement
@ConfigurationProperties(prefix = "datasource")
public class DataConfig {
	@Autowired
	private ApplicationContext applicationContext;

	@Autowired
	private DatasourceProfile profile;

	@Bean(destroyMethod = "close")
	public DataSource dataSource() {
		org.apache.tomcat.jdbc.pool.DataSource dataSource = new org.apache.tomcat.jdbc.pool.DataSource();
		dataSource.setDriverClassName(profile.getDriverClassName());
		dataSource.setUrl(profile.getUrl());
		dataSource.setUsername(profile.getUserName());
		dataSource.setPassword(profile.getPassword());
		dataSource.setInitialSize(profile.getInitialSize());
		dataSource.setMaxActive(profile.getMaxActive());
		dataSource.setMaxIdle(profile.getMaxIdle());
		dataSource.setMinIdle(profile.getMinIdle());
		dataSource.setMaxWait(profile.getMaxWait());

		//validation connections
		dataSource.setValidationQuery(profile.getValidationQuery());
		dataSource.setValidationInterval(profile.getValidationInterval());
		dataSource.setTestOnBorrow(profile.getTestOnBorrow());
		return dataSource;
	}

	@Bean
	public SqlSessionFactory sqlSessionFactory() throws Exception {
		SqlSessionFactoryBean sessionFactoryBean = new SqlSessionFactoryBean();
		sessionFactoryBean.setDataSource(dataSource());
		//gradle build로 컴파일 후 실행시에 typeAliases 가 매핑되지 않아서 config를 사용함.
		//sessionFactoryBean.setTypeAliasesPackage("com.center.domain");
		sessionFactoryBean.setConfigLocation(applicationContext.getResource("classpath:mybatis/mybatis-config.xml"));
		//sessionFactoryBean.setMapperLocations(applicationContext.getResources("classpath:mybatis/mapper/**/*.xml"));
		return sessionFactoryBean.getObject();
	}


	@Bean
	public PlatformTransactionManager transactionManager() {
		DataSourceTransactionManager transactionManager = new DataSourceTransactionManager(dataSource());
		transactionManager.setGlobalRollbackOnParticipationFailure(false);
		return transactionManager;
	}
}
