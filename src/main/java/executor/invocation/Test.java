package executor.invocation;

import org.apache.commons.collections4.CollectionUtils;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Test {
    public static void main(String[] args) throws IOException, InterruptedException {
         User user=new Student();

        LocalDateTime format = LocalDateTime.now();
        System.out.println(format);
        System.out.println(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM).format(format));
        List list =null;
        System.out.println(CollectionUtils.isEmpty(new ArrayList<>()));//

        String[] arrayA = new String[] { "A", "B", "C", "D", "E", "F"};
        String[] arrayB = new String[] { "B", "D", "F", "G", "H", "K"};
        List<String> listA = Arrays.asList(arrayA);
        List<String> listB = Arrays.asList(arrayB);
        //2个数组取并集
        System.out.println(CollectionUtils.union(listA, listB));

        List<String> listC=new ArrayList<>();
        listC.add("s");
        System.out.println(listC);

    }
}







//        UserHandler userHandler=new UserHandler(user);
//
//        User proxy=(User) Proxy.newProxyInstance(userHandler.getClass().getClassLoader(),user.getClass().getInterfaces(),userHandler);
//
//        System.out.println(proxy.info("k"));
//        System.out.println(userHandler.getClass());
//Runtime.getRuntime().exec("Xshell.exe");