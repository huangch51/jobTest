package executor.com.tt.base;



import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.InputStream;

public class MyBatisUtil {

    public static SqlSessionFactory getSqlSessionFactory() {
        String resource = "mybatis-config.xml";
        // 使用类加载器加载mybatis的配置文件（它也加载关联的映射文件）
        InputStream is = MyBatisUtil.class.getClassLoader().getResourceAsStream(resource);
        // 构建sqlSession的工厂
        SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder().build(is);
        return sessionFactory;
    }

    public static SqlSession getSqlSession() {
        return getSqlSessionFactory().openSession();
    }

    public static SqlSession getSqlSession(boolean isAutoCommit) {
        return getSqlSessionFactory().openSession(isAutoCommit);
    }
}
