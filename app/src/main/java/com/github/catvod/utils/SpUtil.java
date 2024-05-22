package com.github.catvod.utils;

import com.github.catvod.net.OkHttp;

import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SpUtil {
    public static final String FIREFOX = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.14; rv:102.0) Gecko/20100101 Firefox/102.0";
    public static final String CHROME = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/112.0.0.0 Safari/537.36";
    public static final String IPHONE = "Mozilla/5.0 (iPhone; CPU iPhone OS 14_6 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/14.1.1 Mobile/15E148 Safari/604.1";


    public static Map<String, String> getHeader() {
        Map<String, String> header = new HashMap<>();
        header.put("User-Agent", FIREFOX);
        return header;
    }

    public static Map<String, String> getMobileHeader() {
        Map<String, String> header = new HashMap<>();
        header.put("User-Agent", IPHONE);
        return header;
    }

    public static Map<String, String> getHeader(String referer) {
        Map<String, String> header = new HashMap<>();
        header.put("User-Agent", FIREFOX);
        header.put("Referer", referer);
        return header;
    }

    public static OkHttpClient getOkHttpClient() {
        return OkHttp.client();
    }

    public static Response newCall(Request request) throws IOException {
        return getOkHttpClient().newCall(request).execute();
    }

    public static Response newCall(String url) throws IOException {
        return getOkHttpClient().newCall(new Request.Builder().url(url).build()).execute();
    }

    public static Response newCall(String url, Map<String, String> header) throws IOException {
        return getOkHttpClient().newCall(new Request.Builder().url(url).headers(Headers.of(header)).build()).execute();
    }

    public static String req(String url) throws Exception {
        return req(url, getHeader());
    }

    public static String req(String url, Map<String, String> header) throws Exception {
        return req(newCall(url, header), null);
    }

    public static String req(Response response, String encoding) throws Exception {
        if (!response.isSuccessful()) return "";
        if (response.body() == null) {
            response.close();
            return "";
        }
        byte[] bytes = response.body().bytes();
        response.close();
        return new String(bytes, encoding == null ? "UTF-8" : encoding);
    }

    public static String find(Pattern pattern, String html) {
        Matcher m = pattern.matcher(html);
        return m.find() ? m.group(1) : "";
    }

    public static String find(String regexStr, String htmlStr) {
        Pattern pattern = Pattern.compile(regexStr, Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(htmlStr);
        if (matcher.find()) return matcher.group(1);
        return "";
    }

    public static String removeHtmlTag(String str) {
        return str.replaceAll("</?[^>]+>", "");
    }


    public static String substring(String text) {
        return substring(text, 1);
    }

    public static String substring(String text, int num) {
        if (text != null && text.length() > num) {
            return text.substring(0, text.length() - num);
        } else {
            return text;
        }
    }

    public static JSONObject parse(String json) {
        try {
            return new JSONObject(json);
        } catch (JSONException e) {
            return new JSONObject();
        }
    }

    public static JSONObject safeObject(String json) {
        try {
            JSONObject obj = parse(json);
            return obj;
        } catch (JSONException e) {
            return new JSONObject();
        }
    }


    // ######################### 常用方法
    public static JSONArray classes(List<String> typeIds, List<String> typeNames) throws Exception {
        JSONArray classes = new JSONArray();
        for (int i = 0; i < typeIds.size(); i++) classes.put(classObj(typeIds.get(i), typeNames.get(i)));
        return classes;
    }

    public static JSONObject classObj(String typeId, String typeName) throws Exception {
        JSONObject classObject = new JSONObject();
        classObject.put("type_id", typeId);
        classObject.put("type_name", typeName);
        return classObject;
    }

    public static JSONObject vod(String vodId, String vodName, String vodPic, String vodRemarks) throws Exception {
        JSONObject vod = new JSONObject();
        vod.put("vod_id", vodId);
        vod.put("vod_name", vodName);
        vod.put("vod_pic", vodPic);
        vod.put("vod_remarks", vodRemarks);
        return vod;
    }

    public static String result(JSONArray classes, String filterStr, JSONArray videos) throws Exception {
        JSONObject result = new JSONObject();
        result.put("class", classes);
        result.put("filters", filterStr == null ? new JSONObject() : new JSONObject(filterStr));
        result.put("list", videos == null ? new JSONArray() : videos);
        return result.toString();
    }

    public static String result(String pg, JSONArray videos) throws Exception {
        int page = Integer.parseInt(pg), count = Integer.MAX_VALUE, limit = videos.length(), total = Integer.MAX_VALUE;
        JSONObject result = new JSONObject();
        result.put("page", page);
        result.put("pagecount", count);
        result.put("limit", limit);
        result.put("total", total);
        result.put("list", videos);
        return result.toString();
    }

    public static String result(JSONObject vod) {
        JSONArray list = new JSONArray();
        list.put(vod);
        JSONObject result = new JSONObject();
        result.put("list", list);
        return result.toString();
    }

    public static String result(JSONArray videos) {
        JSONObject result = new JSONObject();
        result.put("list", videos);
        return result.toString();
    }

    public static String result(int parse, Map<String, String> headers, String playUrl, String url) {
        JSONObject result = new JSONObject();
        result.put("parse", parse);
        result.put("header", headers == null ? "" : headers.toString());
        result.put("playUrl", playUrl);
        result.put("url", url);
        return result.toString();
    }
}
