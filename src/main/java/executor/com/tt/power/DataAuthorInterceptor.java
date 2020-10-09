package executor.com.tt.power;


import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.Statements;
import net.sf.jsqlparser.statement.select.*;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

@Intercepts(@Signature(type = Executor.class, method = "query", args = { MappedStatement.class, Object.class,
        RowBounds.class, ResultHandler.class }))
public class DataAuthorInterceptor implements Interceptor {

    private Properties properties;

    //拦截并实现代理注入
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        final Object[] args = invocation.getArgs();
        MappedStatement ms = (MappedStatement)args[0];
        SqlSource sqlSource = ms.getSqlSource();
        SqlSource w = SqlSourceInterceptor.wrap(sqlSource, SqlSource.class);
        Field field = ms.getClass().getDeclaredField("sqlSource");
        field.setAccessible(true);
        field.set(ms, w);

        return invocation.proceed();
    }

    //boundsql修改
    public static BoundSql modifyBoundSql(BoundSql boundSql) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
        //获取初始sql
        String originSql = boundSql.getSql();
        //修改sql
        String changeSql = parseSql(originSql);
        Field field = boundSql.getClass().getDeclaredField("sql");
        field.setAccessible(true);
        field.set(boundSql, changeSql);
        return boundSql;
    }

    //解析SQL，根据数据权限，去除没有权限的字段
    private static String parseSql(String sql) {
        List<String> blockList = getBlockCols();
        StringBuffer newSql = new StringBuffer();
        try {
            //解析sql 获得结构化statement
            Statements s = CCJSqlParserUtil.parseStatements(sql);
            //对每一个statement
            for(Statement st : s.getStatements()) {
                if(null != st) {
                    //查询sql处理
                    if(st instanceof Select) {
                        SelectBody selectBody = ((Select) st).getSelectBody();
                        if(selectBody instanceof PlainSelect) {
                            Iterator<SelectItem> it = ((PlainSelect) selectBody).getSelectItems().iterator();
                            //遍历查询字段
                            while(it.hasNext()) {
                                SelectItem si = it.next();
                                if(si instanceof SelectExpressionItem) {
                                    for(String str : blockList) {
                                        //查询字段同 权限隐藏字段匹配 则从查询中移除
                                        if(si.toString().contains(str)) {
                                            it.remove();
                                        }
                                    }
                                }
                            }
                        }

                        //return ((Select) st).getSelectBody().toString();
                    }
                    newSql.append(st + ";");
                }
            }

        } catch (JSQLParserException e) {
            e.printStackTrace();
        }

        return newSql.toString();
    }


    //解析sql结构体
    private static List<String> getBlockCols(){
        List<String> l = new ArrayList<String>();
        l.add("a");
        l.add("b1");
        return l;
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
        this.properties = properties;
    }


    public static void main(String[] args) {
        String sql = "select a, b, c from table1";
        String sql2 = "select a, b, c from (select e as a, f as b, g as c from table1)";
        String sql3 = "select a as a1, b b1, c from table3 as t3, (select e as a, f as b from table1) , (select g as c from table2)";
        String sql4 = "select c.vin8, (select auto_brand from vin_base b where b.vin8 = c.vin8)\n" +
                "from vin_base c  where c.vin8 = '72753413' ";
        String sql5 = "select a, b from table1 union select c, d from table2;";
        String changeSql = new DataAuthorInterceptor().parseSql(sql4);
        System.out.println("changeSql>>>>>>>>" + changeSql);


    }

}

