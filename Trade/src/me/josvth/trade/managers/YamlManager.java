package me.josvth.trade.managers;

import java.io.File;
import java.io.IOException;


import me.josvth.trade.Trade;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;


public class YamlManager {
	
	Trade plugin;
	
	File configFile;
	public FileConfiguration configYaml;
	
	File languageFile;
	public FileConfiguration languageYaml;
	
	File layoutsFile;
	public FileConfiguration layoutsYaml;
	
	public YamlManager(Trade instance){
		plugin = instance;
	}
	
	public void initialize() {
		
		setupConfigYaml();
		loadConfig();		
		
		setupLanguageYaml();
		loadLanguage();
		
		setupLayoutsYaml();
		loadLayoutsYaml();
		
	}
	
	// Config YAML
	private void setupConfigYaml() {
		configFile = new File(plugin.getDataFolder(), "config.yml");
		if (!(configFile.exists())) plugin.saveResource("config.yml", false);
	}
		
	public void loadConfig() {
		
		//configYaml = new YamlConfiguration();
		
		try {
			configYaml.load(configFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(!plugin.getDescription().getVersion().equals(configYaml.getString("version"))) {
			loadDefaultConfig();
			configYaml.set("version", plugin.getDescription().getVersion());
			saveConfig();
		}
	}
	
	private void loadDefaultConfig() {
		configYaml.setDefaults(YamlConfiguration.loadConfiguration(plugin.getResource("config.yml")));
		configYaml.options().copyDefaults(true);
		saveConfig();
	}
		
	public void saveConfig() {
		try {
			configYaml.save(configFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public FileConfiguration getConfigYaml(){
		return configYaml;
	}
	
	// Language YAML
	private void setupLanguageYaml() {
		languageFile = new File(plugin.getDataFolder(), "language.yml");
		if (!(languageFile.exists())) plugin.saveResource("language.yml", false);
	}
	
	public void loadYamls(){
		loadConfig();
		loadLanguage();
	}
	
	public void saveYamls(){
		saveConfig();
		saveLanguage();
	}
	
	public void loadLanguage() {
		
		//languageYaml = new YamlConfiguration();
		
		try {
			languageYaml.load(languageFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(!plugin.getDescription().getVersion().equals(languageYaml.getString("version"))) {
			loadDefaultLanguage();
			languageYaml.set("version", plugin.getDescription().getVersion());
			saveLanguage();
		}
		
	}
	
	private void loadDefaultLanguage() {
		languageYaml.setDefaults(YamlConfiguration.loadConfiguration(plugin.getResource("language.yml")));
		languageYaml.options().copyDefaults(true);
		saveLanguage();
	}

	public void saveLanguage() {
		try {
			languageYaml.save(languageFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}	
	
	// Layouts yaml
	private void setupLayoutsYaml() {
		layoutsFile = new File(plugin.getDataFolder(), "layouts.yml");
		if (!(layoutsFile.exists())) plugin.saveResource("layouts.yml", false);
	}
		
	public void loadLayoutsYaml() {
		
		layoutsYaml = new YamlConfiguration();
		
		try {
			layoutsYaml.load(layoutsFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(!plugin.getDescription().getVersion().equals(layoutsYaml.getString("version"))) {
			loadDefaultLayoutsYaml();
			layoutsYaml.set("version", plugin.getDescription().getVersion());
			saveLayoutsYaml();
		}
	}
	
	private void loadDefaultLayoutsYaml() {
		layoutsYaml.setDefaults(YamlConfiguration.loadConfiguration(plugin.getResource("layouts.yml")));
		layoutsYaml.options().copyDefaults(true);
		saveLanguage();
	}

	public void saveLayoutsYaml() {
		try {
			layoutsYaml.save(layoutsFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public FileConfiguration getLayoutsYaml(){
		return layoutsYaml;
	}
}
