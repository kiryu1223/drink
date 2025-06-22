package io.github.kiryu1223.drink.starter.configuration;

import io.github.kiryu1223.drink.base.dataSource.DataSourceManager;
import io.github.kiryu1223.drink.base.toBean.handler.ITypeHandler;
import io.github.kiryu1223.drink.base.toBean.handler.JsonTypeHandler;
import io.github.kiryu1223.drink.base.toBean.handler.TypeHandlerManager;
import io.github.kiryu1223.drink.base.transaction.TransactionManager;
import io.github.kiryu1223.drink.core.SqlBuilder;
import io.github.kiryu1223.drink.core.SqlClient;
import io.github.kiryu1223.drink.starter.annotation.GlobalTypeHandler;
import io.github.kiryu1223.drink.starter.dataSource.SpringDataSourceManager;
import io.github.kiryu1223.drink.starter.transaction.SpringTransactionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.annotation.AnnotationUtils;

import javax.sql.DataSource;
import java.util.Map;

import static io.github.kiryu1223.drink.base.util.DrinkUtil.cast;

@Configuration
@EnableConfigurationProperties(DrinkProperties.class)
@ConditionalOnBean(DataSource.class)
@AutoConfigureAfter(DataSourceAutoConfiguration.class)
@ConditionalOnProperty(
        prefix = "drink",
        name = "enable",
        havingValue = "true"
)
public class DrinkAutoConfiguration implements ApplicationListener<ContextRefreshedEvent>
{
    private static final Logger log = LoggerFactory.getLogger(DrinkAutoConfiguration.class);

    private SqlClient init(DataSource dataSource, DrinkProperties properties) {
        DataSourceManager dataSourceManager = new SpringDataSourceManager(dataSource);
        TransactionManager transactionManager = new SpringTransactionManager(dataSourceManager);
        return SqlBuilder.bootStrap()
                .setDataSourceManager(dataSourceManager)
                .setTransactionManager(transactionManager)
                .setDbType(properties.getDatabase())
                .setOption(properties.bulidOption())
                .setNameConverter(properties.getNameConversion().getNameConverter())
                .build();
    }

    @Bean
    @ConditionalOnBean(DataSource.class)
    @ConditionalOnMissingBean(SqlClient.class)
    public SqlClient build(DataSource dataSource, DrinkProperties properties) {
        return init(dataSource, properties);
    }

    // bean初始化完成时，找到所有的被注解为组件的typeHandler然后保存起来
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event)
    {
        ApplicationContext context = event.getApplicationContext();

        Map<String, ITypeHandler<?>> typeHandlerMap = cast(context.getBeansOfType(ITypeHandler.class));
        for (ITypeHandler<?> value : typeHandlerMap.values())
        {
            // Json类型单独处理
            if(value instanceof JsonTypeHandler<?>)
            {
                TypeHandlerManager.setJsonTypeHandler((JsonTypeHandler<?>) value);
            }
            // 标记为全局的类型处理器注册到按type缓存
            else if (AnnotationUtils.isAnnotationDeclaredLocally(GlobalTypeHandler.class, value.getClass()))
            {
                TypeHandlerManager.set(value);
            }
            // 注册到按typeHandler缓存
            TypeHandlerManager.setHandler(value);
        }
    }
}
