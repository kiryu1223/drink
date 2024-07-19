package io.github.kiryu1223.plugin;

import io.github.kiryu1223.drink.Drink;
import io.github.kiryu1223.drink.api.client.DrinkClient;
import io.github.kiryu1223.drink.api.crud.transaction.TransactionManager;
import io.github.kiryu1223.drink.core.dataSource.DataSourceManager;
import io.github.kiryu1223.drink.core.session.DefaultSqlSessionFactory;
import io.github.kiryu1223.drink.core.session.SqlSessionFactory;
import io.github.kiryu1223.plugin.configuration.DrinkProperties;
import io.github.kiryu1223.plugin.datasource.SolonDataSourceManagerWrap;
import io.github.kiryu1223.plugin.datasource.SolonDynamicDataSourceManager;
import io.github.kiryu1223.plugin.datasource.SolonSingleDataSourceManager;
import io.github.kiryu1223.plugin.transaction.SolonTransactionManager;
import org.noear.solon.core.AppContext;
import org.noear.solon.core.BeanWrap;
import org.noear.solon.core.Plugin;
import org.noear.solon.core.Props;
import org.noear.solon.data.dynamicds.DynamicDataSource;

import javax.sql.DataSource;
import java.util.Map;

public class XPluginImpl implements Plugin
{
    @Override
    public void start(AppContext context) throws Throwable
    {
        Map<String, Props> drink = context.cfg().getGroupedProp("drink");
        for (Map.Entry<String, Props> entry : drink.entrySet())
        {
            Props props = entry.getValue();
            DrinkProperties properties = props.getBean(DrinkProperties.class);
            if (properties.getDatasource().isEmpty()) continue;
            DataSourceManager dataSourceManager = new SolonDataSourceManagerWrap(properties.getDatasource());
            TransactionManager transactionManager = new SolonTransactionManager(dataSourceManager);
            SqlSessionFactory sqlSessionFactory = new DefaultSqlSessionFactory(dataSourceManager, transactionManager);
            DrinkClient client = Drink.bootStrap()
                    .setDataSourceManager(dataSourceManager)
                    .setTransactionManager(transactionManager)
                    .setSqlSessionFactory(sqlSessionFactory)
                    .setOption(properties.bulidOption())
                    .build();
            BeanWrap wrap = context.wrap(entry.getKey(), client);
            context.beanRegister(wrap, entry.getKey(), false);
        }
        context.subWrapsOfType(DataSource.class, beanWrap -> register(context, beanWrap));
    }


    private void register(AppContext context, BeanWrap beanWrap)
    {
        String name = beanWrap.name();
        for (DrinkClient client : context.getBeansOfType(DrinkClient.class))
        {
            SolonDataSourceManagerWrap dataSourceManagerWrap = (SolonDataSourceManagerWrap) client.getConfig().getDataSourceManager();
            if (!dataSourceManagerWrap.getDsName().equals(name)) continue;
            DataSource dataSource = beanWrap.get();
            if (dataSource instanceof DynamicDataSource)
            {
                DynamicDataSource dynamicDataSource = (DynamicDataSource) dataSource;
                dataSourceManagerWrap.setDataSourceManager(new SolonDynamicDataSourceManager(dynamicDataSource));
            }
            else
            {
                dataSourceManagerWrap.setDataSourceManager(new SolonSingleDataSourceManager(dataSource));
            }
            break;
        }
    }
}
