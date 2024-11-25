package demo.example1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class MapTest {
    private static Logger logger = LoggerFactory.getLogger(MapTest.class);

    public static void main(String[] args) throws IOException {
        List<String> strings = new ArrayList<>();
        strings.add("202201");
        strings.add("202301");
        strings.add("202308");
        strings.add("202208");

        Collections.sort(strings);
        logger.info("开始打印：");
        System.out.println(strings);

        File[] files = new File[10];

        InputStream is = null;
        BufferedInputStream bis = null;
        ZipOutputStream zos = null;

        byte[] buf = new byte[1024];
        int length = 0;

        try {
            for (File file : files) {
                String basePath = "D:/a";
                is = new FileInputStream(file);
                bis = new BufferedInputStream(is);
                zos.putNextEntry(new ZipEntry(basePath));
                while ((length = bis.read(buf)) > 0) {
                    zos.write(buf, 0, length);
                }

            }
        } finally {
            if (null != zos) {
                zos.close();
            }
            if (null != bis) {
                bis.close();
            }
            if (null != is) {
                is.close();
            }
        }

    }
}
