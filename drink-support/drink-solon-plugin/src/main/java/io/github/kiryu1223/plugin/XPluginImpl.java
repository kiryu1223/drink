package io.github.kiryu1223.plugin;

import io.github.kiryu1223.drink.Drink;
import io.github.kiryu1223.drink.api.client.DrinkClient;
import io.github.kiryu1223.drink.api.transaction.TransactionManager;
import io.github.kiryu1223.drink.core.dataSource.DataSourceManager;
import io.github.kiryu1223.drink.core.session.DefaultSqlSessionFactory;
import io.github.kiryu1223.drink.core.session.SqlSessionFactory;
import io.github.kiryu1223.plugin.aot.DrinkRuntimeNativeRegistrar;
import io.github.kiryu1223.plugin.builder.AotBeanCreatorFactory;
import io.github.kiryu1223.plugin.configuration.DrinkProperties;
import io.github.kiryu1223.plugin.datasource.SolonDataSourceManager;
import io.github.kiryu1223.plugin.datasource.SolonDataSourceManagerWrap;
import io.github.kiryu1223.plugin.transaction.SolonTransactionManager;
import org.noear.solon.aot.RuntimeNativeRegistrar;
import org.noear.solon.core.AppContext;
import org.noear.solon.core.BeanWrap;
import org.noear.solon.core.Plugin;
import org.noear.solon.core.Props;
import org.noear.solon.core.runtime.NativeDetector;
import org.noear.solon.core.util.ClassUtil;
import org.noear.solon.data.datasource.DsUtils;

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
            DrinkProperties properties = props.toBean(DrinkProperties.class);
            String dsName = properties.getDsName();
            if (dsName.isEmpty()) continue;
            DataSourceManager dataSourceManager = new SolonDataSourceManagerWrap();
            TransactionManager transactionManager = new SolonTransactionManager(dataSourceManager);
            SqlSessionFactory sqlSessionFactory = new DefaultSqlSessionFactory(dataSourceManager, transactionManager);
            AotBeanCreatorFactory aotFastCreatorFactory = new AotBeanCreatorFactory();
            DrinkClient client = Drink.bootStrap()
                    .setDataSourceManager(dataSourceManager)
                    .setTransactionManager(transactionManager)
                    .setSqlSessionFactory(sqlSessionFactory)
                    .setFastCreatorFactory(aotFastCreatorFactory)
                    .setOption(properties.bulidOption())
                    .build();

            BeanWrap wrap = context.wrap(entry.getKey(), client);
            context.beanRegister(wrap, entry.getKey(), true);

            DsUtils.observeDs(context, dsName, beanWrap ->
            {
                SolonDataSourceManagerWrap sourceManagerWrap = (SolonDataSourceManagerWrap) client.getConfig().getDataSourceManager();
                sourceManagerWrap.setDataSourceManager(new SolonDataSourceManager(beanWrap.get()));
            });
        }

        //context.subWrapsOfType(DataSource.class, beanWrap -> registerDrink(context, beanWrap));
        registerAot(context);
    }

//    private void registerDrink(AppContext context, BeanWrap beanWrap)
//    {
//        String name = beanWrap.name();
//        for (DrinkClient client : context.getBeansOfType(DrinkClient.class))
//        {
//            SolonDataSourceManagerWrap dataSourceManagerWrap = (SolonDataSourceManagerWrap) client.getConfig().getDataSourceManager();
//            if (!dataSourceManagerWrap.getDsName().equals(name)) continue;
//            DataSource dataSource = beanWrap.get();
//            if (dataSource instanceof DynamicDataSource)
//            {
//                DynamicDataSource dynamicDataSource = (DynamicDataSource) dataSource;
//                dataSourceManagerWrap.setDataSourceManager(new SolonDynamicDataSourceManager(dynamicDataSource));
//            }
//            else
//            {
//                dataSourceManagerWrap.setDataSourceManager(new SolonSingleDataSourceManager(dataSource));
//            }
//            break;
//        }
//    }

    private void registerAot(AppContext context)
    {
        if (NativeDetector.isAotRuntime() && ClassUtil.hasClass(() -> RuntimeNativeRegistrar.class))
        {
            context.wrapAndPut(DrinkRuntimeNativeRegistrar.class, new DrinkRuntimeNativeRegistrar());
        }
    }
}
