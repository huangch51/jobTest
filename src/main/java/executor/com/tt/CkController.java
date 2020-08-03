package executor.com.tt;

import executor.com.tt.mapper.AppMapper;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RestController
@Configuration
public class CkController {
    @Resource
   private AppMapper appMapper;

    @RequestMapping("/selectList")
    public List<Map> selectList () {
        System.out.println("dddddd");
        return appMapper.queryTbOne();

    }
}
