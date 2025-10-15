package nccc.btp.config;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * Database(EMS) 連線設置
 * ------------------------------------------------------
 * 建立人員: ChauYung
 * 建立日期: 2025-10-08
 */
@Configuration
@EnableJpaRepositories( // 不同的 DB 之 Repository 全部放在一起的情況; 使用 includeFilters 限定之
	    basePackages = "nccc.btp.repository",  
	    includeFilters = @ComponentScan.Filter(
	        type = FilterType.ASSIGNABLE_TYPE,
	        classes = { 
            		nccc.btp.repository.EmsMeqItemRepository.class,
            		nccc.btp.repository.EmsMeqPurRepository.class,
            		nccc.btp.repository.EmsMtermModelRepository.class,
            		nccc.btp.repository.EmsMlookupRepository.class }
	    ),
	    entityManagerFactoryRef = "emsEntityManager",
	    transactionManagerRef = "emsTxManager"
	)
public class DbEmsConfig {
	
	/**
	 * (EMS)資料表對應管理員
	 * @param builder - (EMS)資料表對接工廠
	 * @param ds - Database(EMS) 連線設置
	 * @return - (EMS)資料表對應管理員
	 */
    @Bean(name = "emsEntityManager")
    LocalContainerEntityManagerFactoryBean logEntityManager(
            EntityManagerFactoryBuilder builder,
            @Qualifier("dataSourceEMS") DataSource ds) {
        return builder
                .dataSource(ds)
                .packages("nccc.btp.entity") // 雖然 Entity 混在一起沒關係
                .persistenceUnit("emsPU")
                .build();
    }

    /**
     * (EMS)資料交易管理員
     * @param emf - (EMS)資料表管理工廠
     * @return - (EMS)資料交易管理員
     */
    @Bean(name = "emsTxManager")
    PlatformTransactionManager logTxManager(
            @Qualifier("emsEntityManager") EntityManagerFactory emf) {
        return new JpaTransactionManager(emf);
    }
}
