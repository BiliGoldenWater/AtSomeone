package io.github.biligoldenwater.atsomeone.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.github.biligoldenwater.atsomeone.AtSomeone;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class I18nManager {
    private final Map<String, Map<String, String>> languages = new HashMap<>();
    private boolean initialized = false;
    private String defaultLanguage = "en_us";


    public I18nManager(File dataPath, String langFolder, String defaultLanguage) {
        this(new File(dataPath.getPath(), langFolder), defaultLanguage);
    }

    public I18nManager(File path) {
        this(path, "en_us");
    }

    public I18nManager(File path, String defaultLanguage) {
        if (initialize(path, defaultLanguage.toLowerCase()) == StatusCode.success) initialized = true;
    }

    public int initialize(File languageFilePath) {
        return initialize(languageFilePath, "en_us");
    }

    public int initialize(File dataPath, String langFolder, String defaultLanguage) {
        return initialize(new File(dataPath.getPath(), langFolder), defaultLanguage);
    }

    public int initialize(File languageFilePath, String defaultLanguage) {
        this.defaultLanguage = defaultLanguage;

        if (!languageFilePath.exists()) {
            if (!languageFilePath.mkdirs()) { // 如果目录不存在则创建目录
                return StatusCode.failToCreateFolder; // 如果创建失败则返回错误
            }
        }
        if (!languageFilePath.isDirectory()) return StatusCode.pathError; // 如果不是一个目录则返回错误

        Gson gson = new Gson();

        File[] files = languageFilePath.listFiles(file -> { // 获取目录下所有json文件
            String fileName = file.getName();
            return fileName.endsWith(".json");
        });

        if (files == null) {
            return StatusCode.success;
        }

        for (File file : files) {
            if (!file.canRead()) return StatusCode.failToRead;

            try {
                Path path = Paths.get(file.getPath());
                byte[] data = Files.readAllBytes(path);
                String jsonStr = new String(data, StandardCharsets.UTF_8); // 读取内容

                Map<String, String> lang = gson.fromJson(jsonStr, new TypeToken<Map<String, String>>() {
                }.getType());

                languages.put(file.getName().replaceAll(".json", "").toLowerCase(), lang);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return StatusCode.success;
    }

    public String getL10n(String lang, String key) {
        if (!initialized) return "";
        Map<String, String> langData = languages.get(lang);

        if (langData == null || langData.get(key) == null) {
            Map<String, String> defaultLangData = languages.get(defaultLanguage);
            if (defaultLangData == null || defaultLangData.get(key) == null) {
                return key;
            } else {
                return languages.get(defaultLanguage).get(key);
            }
        }

        return langData.get(key);
    }

    public void releaseDefaultLangFile(JavaPlugin plugin, String folderInJar, String langListFileName) {
        InputStream inputStream = plugin.getResource(folderInJar + "/" + langListFileName);
        
    }

    public static class StatusCode {
        public static final int success = 0;
        public static final int pathError = 1;
        public static final int failToCreateFolder = 2;
        public static final int failToRead = 3;
    }
}
