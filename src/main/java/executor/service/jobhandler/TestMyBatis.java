package executor.service.jobhandler;

import executor.com.tt.base.MyBatisUtil;
import executor.com.tt.dao.User;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TestMyBatis {

    public static void main(String[] args) {
        SqlSession session = MyBatisUtil.getSqlSession();
        /**
         * 映射sql的标识字符串，
         * com.tiantian.mybatis.mapper.userMapper是userMapper.xml文件中mapper标签的namespace属性的值，
         * selectByPrimaryKey是select标签的id属性值，通过select标签的id属性值就可以找到要执行的SQL
         */
        String statement = "executor.com.tt.mapper.UserMapper.selectByPrimaryKey";//映射sql的标识字符串

        String statement1 = "executor.com.tt.mapper.UserMapper.selectList";//映射sql的标识字符串
        //执行查询返回一个唯一user对象的sql
        User user = session.selectOne(statement,1);
        System.out.println(user);
        //执行查询返回一个唯一user对象的sql
        List<User> userList = session.selectList(statement1);
        System.out.println(userList);
    }
}