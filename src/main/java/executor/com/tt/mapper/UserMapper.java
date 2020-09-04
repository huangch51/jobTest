package executor.com.tt.mapper;

import java.util.List;
import java.util.Map;

public interface UserMapper {
   void selectByPrimaryKey(int id);
   void selectList();

   List<String> queryColumn(Map params);


   List<String>queryColumnName(Map params);

   List<Map> queryColumnList(Map params);

   void insertTable(Map params);

   void insertByMap(Map params);

}
