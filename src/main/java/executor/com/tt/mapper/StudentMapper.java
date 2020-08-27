package executor.com.tt.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @author huangcaihuan
 * @Since 2020/8/18 14:08
 */
@Mapper
@Component
public interface StudentMapper {

    List<Map> queryStudent(Map params);
    void insertkkl();
}
