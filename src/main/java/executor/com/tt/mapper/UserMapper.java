package executor.com.tt.mapper;


import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface UserMapper {
   void selectByPrimaryKey(int id);
   void selectList();
}
