package executor.com.tt.dao;
import lombok.Data;
import org.springframework.stereotype.Repository;

@Data
@Repository
public class User {
    private int id;
    private String name;
}
