package nccc.btp.config;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * Database(BTP) 連線設置
 * ------------------------------------------------------
 * 建立人員: ChauYung
 * 建立日期: 2025-10-08
 */
@Configuration
@EnableJpaRepositories( // 不同的 DB 之 Repository 全部放在一起的情況; 使用 excludeFilters 排除之
	    basePackages = "nccc.btp.repository",  
    	    excludeFilters = @ComponentScan.Filter(
    	            type = FilterType.ASSIGNABLE_TYPE,
    	            classes = { 
    	            		nccc.btp.repository.EmsMeqItemRepository.class,
    	            		nccc.btp.repository.EmsMeqPurRepository.class,
    	            		nccc.btp.repository.EmsMtermModelRepository.class,
    	            		nccc.btp.repository.EmsMlookupRepository.class }
    	        ),
	    entityManagerFactoryRef = "btpEntityManager",
	    transactionManagerRef = "btpTxManager"
	)
public class DbBtpConfig {
	
	/**
	 * (BTP)資料表對應管理員
	 * @param builder - (BTP)資料表對接工廠
	 * @param ds - Database(BTP) 連線設置
	 * @return - (BTP)資料表對應管理員
	 */
	@Primary
    @Bean(name = "btpEntityManager")
    LocalContainerEntityManagerFactoryBean logEntityManager(
            EntityManagerFactoryBuilder builder,
            @Qualifier("dataSourceBTP") DataSource ds) {
        return builder
                .dataSource(ds)
                .packages("nccc.btp.entity") // 雖然 Entity 混在一起沒關係
                .persistenceUnit("btpPU")
                .build();
    }

	/**
     * (BTP)資料交易管理員
     * @param emf - (BTP)資料表管理工廠
     * @return - (BTP)資料交易管理員
     */
	@Primary
	@Bean(name = "btpTxManager")
    PlatformTransactionManager logTxManager(
            @Qualifier("btpEntityManager") EntityManagerFactory emf) {
        return new JpaTransactionManager(emf);
    }
}
