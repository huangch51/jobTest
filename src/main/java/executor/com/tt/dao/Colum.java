package executor.com.tt.dao;

import lombok.Data;
import org.springframework.stereotype.Repository;

/**
 * @author huangcaihuan
 * @Since 2020/9/1
 */
@Data
@Repository
public class Colum {

    private String columnName;

    private String columnComment;
}
