package executor.com.tt.sql;

import executor.com.tt.base.MyBatisUtil;
import org.apache.ibatis.session.SqlSession;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author huangcaihuan
 * @Since 2020/9/11
 */
public class TestOne {

//    public static List<Map> temp(List<Map> list1,List<Map> list2) {
//
//        List<Map> different = new ArrayList<Map>();
//        List<Map> maxList = list1;
//        List<Map> minList = list2;
//        if (list2.size() > list1.size()) {
//            maxList = list2;
//            minList = list1;
//        }
//        Map<Map, Map> map = new HashMap<Map, Map>(maxList.size());
//        for (Map Map : maxList) {
//            map.put(Map, (java.util.Map) map.keySet());
//        }
//        for (Map Map : minList) {
//            if (null!=map.get(Map)) {
//                //当前小集合元素在大集合内存在
//                // 大集合元素对应在map中的key-value的值改为2（标明该元素是共有元素）
//                map.put(Map, (java.util.Map) map.keySet());
//                continue;
//            }
//            //当前小集合元素在大集合内不存在
//            //将该元素存入different
//            different.add(Map);
//        }
//        for (Map.Entry<Map, Map> entry : map.entrySet()) {
//            //判断大集合内非共有元素
//            //将元素存入different
//            if (entry.getValue() == 1) {
//                different.add(entry.getKey());
//            }
//
//        }
//        return different;
//    }

    public static  List getMumList(List list1,List list2){
        List<Map> different = new ArrayList<Map>();
        if (null!=list1 || null!=list2) {
            //遍历集合list1
            for (int i = 0; i <list1.size(); i++) {
                //遍历集合2，判断集合1中是否包含集合2中元素，若包含，则把这个共同元素加入新集合中
                for (int j = 0; j <list2.size(); j++) {
                    if (!list1.contains(list2.get(j))){
                        list1.add(list1.get(j));
                    }else {
                        different.add((Map) list1.get(j));
                    }
                    //continue;
                }

            }
        }
        return different;

    }

    private static List<Map> getDiffrent(List<Map> list1, List<Map> list2) {
        long st = System.nanoTime();
        List<Map> diff = new ArrayList<Map>();
        if(list1.size()!=list2.size()) {
            if(list1.size()>list2.size()){
                list1.retainAll(list2);
                System.out.println("add"+list1);
            }else if(list1.size()<list2.size()){
                list2.removeAll(list1);
                System.out.println("delete"+list2);
            }
        }if(list1.size() == list2.size() && !list1.containsAll(list2)){
            System.out.println("1.修改");
            for(Map str:list1)
            {
                if(!list2.contains(str))
                {
                    diff.add(str);
                }
            }
            System.out.println("修改"+diff);
            return diff;
        }
         return null;
    }

    private static List<Map> updateC(List<Map> list1, List<Map> list2) {
        long st = System.nanoTime();
        List<Map> diff = new ArrayList<Map>();
        if(list1.size() == list2.size() && !list1.containsAll(list2)){
            System.out.println("相同");
            for(Map str:list1)
            {
                if(!list2.contains(str))
                {
                    diff.add(str);
                }
            }
            System.out.println("修改"+diff);

        }
        return diff;
    }



    public static void main(String[] args){

        Map param=new HashMap();

        param.put("table_name","dim_room_type_map");
        SqlSession sqlSession= MyBatisUtil.getSqlSession();
        String statement = "executor.com.tt.mapper.UserMapper.queryTableColumn";
        List<Map> list=sqlSession.selectList(statement,param);
       // System.out.println(list);
        sqlSession.close();

//        Map param1=new HashMap();
//        param1.put("table_name","dim_room_type_map1");
//        SqlSession sqlSession1= MyBatisUtil.getSqlSession();
//        String statement1 = "executor.com.tt.mapper.UserMapper.queryTableColumn1";
//        List<Map> list3=sqlSession1.selectList(statement1,param1);
        List<Map> list3=lit("testk");

       // System.out.println(list3);

        System.out.println(getDiffrent(list3,list).toString());
    }

    public static List<Map> lit(String tableName){
        Map param1=new HashMap();
        param1.put("table_name",tableName);
        SqlSession sqlSession1= MyBatisUtil.getSqlSession();
        String statement1 = "executor.com.tt.mapper.UserMapper.queryTableColumn1";
        List<Map> list3=sqlSession1.selectList(statement1,param1);
        return list3;
     }
    }
