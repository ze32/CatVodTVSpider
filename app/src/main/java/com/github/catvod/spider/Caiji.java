package com.github.catvod.spider;

import android.content.Context;

import com.github.catvod.crawler.Spider;
import com.github.catvod.crawler.SpiderDebug;
import com.github.catvod.utils.SpUtil;
import com.github.catvod.utils.m3u8.AdFilter;

import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;

public class Caiji extends Spider {
    private String siteUrl;

    @Override
    public void init(Context context, String extend) throws Exception {
        super.init(context, extend);
        JSONObject obj = new JSONObject(SpUtil.decode(extend));
        siteUrl = obj.getString("siteUrl");
        // 这里要进行 rules 规则初始化
        if (obj.has("rulesUrl")) {
            String rulesUrl = obj.getString("rulesUrl");
            AdFilter.setRules(rulesUrl);
            SpiderDebug.log("rulesUrl链接是：" + rulesUrl);
        } else {
            JSONObject rule = obj.getJSONObject("rule");
            System.out.println(rule);
            AdFilter.setRuleMap(siteUrl, rule);
        }
        SpiderDebug.log("采集类初始化完成。。。");
    }

    @Override
    public String homeContent(boolean filter) throws Exception {
        String content = SpUtil.req(siteUrl, SpUtil.getHeader());
        // JSONObject obj = new JSONObject(content);
        return content;
    }

    @Override
    public String categoryContent(String tid, String pg, boolean filter, HashMap<String, String> extend) throws Exception {
        String link = String.format("%s?ac=detail&t=%s&pg=%s", siteUrl, tid, pg);
        String content = SpUtil.req(link, SpUtil.getHeader());
        return content;
    }

    @Override
    public String detailContent(List<String> ids) throws Exception {
        String link = String.format("%s?ac=detail&ids=%s", siteUrl, ids.get(0));
        String content = SpUtil.req(link, SpUtil.getHeader());
        return content;
    }

    @Override
    public String playerContent(String flag, String id, List<String> vipFlags) throws Exception {
        return SpUtil.result(0, SpUtil.getHeader(), "", AdFilter.proxyM3U8(id, siteUrl));
    }

    @Override
    public String searchContent(String key, boolean quick) throws Exception {
        return searchContent(key, quick, "1");
    }

    @Override
    public String searchContent(String key, boolean quick, String pg) throws Exception {
        String link = String.format("%s?ac=detail&wd=%s&pg=%s", siteUrl, URLEncoder.encode(key), pg);
        String content = SpUtil.req(link, SpUtil.getHeader());
        return content;
    }
}
