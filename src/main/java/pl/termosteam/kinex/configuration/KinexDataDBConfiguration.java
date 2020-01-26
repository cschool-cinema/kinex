//package pl.termosteam.kinex.configuration;
//
//import com.zaxxer.hikari.HikariDataSource;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Primary;
//import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
//import org.springframework.orm.jpa.JpaTransactionManager;
//import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
//import org.springframework.transaction.PlatformTransactionManager;
//import org.springframework.transaction.annotation.EnableTransactionManagement;
//
//import javax.persistence.EntityManagerFactory;
//import javax.sql.DataSource;
//
//@Configuration
//@EnableTransactionManagement
//@EnableJpaRepositories(
//        transactionManagerRef = "kinexTransactionManager",
//        entityManagerFactoryRef = "kinexEntityManagerFactory",
//        basePackages = {
//                "pl.termosteam.kinex.repository",
//                "pl.termosteam.kinex.configuration"
//        })
//
//public class KinexDataDBConfiguration {
//    @Primary
//    @Bean(name = "kinexDataSourceProperties")
//    @ConfigurationProperties("kinex.datasource")
//    public DataSourceProperties dataSourceProperties() {
//        return new DataSourceProperties();
//    }
//
//    @Primary
//    @Bean(name = "kinexDataSource")
//    @ConfigurationProperties("kinex.datasource.configuration")
//    public DataSource dataSource(@Qualifier("kinexDataSourceProperties") DataSourceProperties
//    securityDataSourceProperties) {
//        return securityDataSourceProperties.initializeDataSourceBuilder().type(HikariDataSource.class)
//                .build();
//    }
//
//    @Primary
//    @Bean(name = "kinexEntityManagerFactory")
//    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
//            EntityManagerFactoryBuilder builder,
//            @Qualifier("kinexDataSource") DataSource kinexDataSource
//    ) {
//        return builder
//                .dataSource(kinexDataSource)
//                .packages("pl.termosteam.kinex.domain")
//                .persistenceUnit("auditorium")
//                .persistenceUnit("movie")
//                .persistenceUnit("screening")
//                .persistenceUnit("seat")
//                .persistenceUnit("ticket")
//                .build();
//    }
//
//    @Primary
//    @Bean(name = "kinexTransactionManager")
//    public PlatformTransactionManager transactionManager(
//            @Qualifier("kinexEntityManagerFactory") EntityManagerFactory kinexEntityManagerFactory) {
//        return new JpaTransactionManager(kinexEntityManagerFactory);
//    }
//}
