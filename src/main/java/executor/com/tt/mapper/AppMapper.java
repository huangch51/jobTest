package executor.com.tt.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;


@Mapper
public interface AppMapper {
    List<Map> queryTbOne();
}
