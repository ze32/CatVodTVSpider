package com.github.catvod.demo;

import android.content.Context;
import com.github.catvod.spider.Caiji;
import org.checkerframework.checker.units.qual.C;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * 测试
 */
public class TestCaiji {
    Caiji caiji;

    @Before
    public void init() throws Exception {
        caiji = new Caiji();
        String extend = "{\"siteUrl\": \"https://api.1080zyku.com/inc/apijson.php\", \"rule\": {\"regex\": []}}";
        caiji.init(new Context(), extend);
    }

    @Test
    public void homeContent() throws Exception {
        System.out.println(caiji.homeContent(true));
    }

    @Test
    public void homeVideoContent() throws Exception {
    }

    @Test
    public void categoryContent() throws Exception {
        HashMap<String, String> extend = new HashMap<>();
        System.out.println(caiji.categoryContent("10", "1", true, null));
    }

    @Test
    public void detailContent() throws Exception {
        List<String> ids = Arrays.asList("55420");
        System.out.println(caiji.detailContent(ids));
    }

    @Test
    public void searchContent() throws Exception {
    }

    @Test
    public void playerContent() throws Exception {
        String id = "https://vip.play-cdn23.com/20240516/13370_389ca0dc/index.m3u8";
        System.out.println(caiji.playerContent("", id, null));
    }
}
