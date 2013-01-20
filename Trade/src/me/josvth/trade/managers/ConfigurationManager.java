package me.josvth.trade.managers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import me.josvth.trade.Trade;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class ConfigurationManager {

	private final Trade	plugin;
	
	private File configFile;	
	private FileConfiguration config;

	public boolean 	usePermissions = true;
	public boolean 	debugMode = false;
	
	// Exchange
	public boolean 	useBiggestInterface = true;
	public int		defaultRows = 4;
	public String	layout = "default";
	public int[]	blacklistedItems = {};
	
	// Requesting
	public boolean allowRequestCrossGamemode = false;
	public boolean allowRequestCrossWorld = false;
	public boolean allowRequestOutOfSight = true;
	public boolean allowRequestIgnoring = true;
	public boolean allowRequestRightClick = true;
	public boolean allowRequestShiftRightClick = true;
	public boolean allowRequestCommand = true;
	
	public int maximumDistance = -1;
	public int requestTimeout = 10;
	public List<String> disabledWorlds;

	// Extensions
	public boolean useExtensionWorldGuard;
	public boolean useExtensionMobArena;
	public boolean useExtensionCitizens;
	public boolean useExtensionEconomy;
	
	public List<String> disabledRegions;

	public int smallCurrency = 1;
	public int mediumCurrency = 10;
	public int largeCurrency = 50;
	
	public ConfigurationManager( Trade instance ) {
		this.plugin = instance;
	}

	public void initalize() {
		setup();
		load();
	}

	public void setup() {
		configFile = new File(plugin.getDataFolder(), "config.yml");
		if (!(configFile.exists())) plugin.saveResource("config.yml", false);
	}
	
	public void reload() {
		initalize();
	}

	public void load() {
		
		config = new YamlConfiguration();
		
		try {
			config.load( configFile );
		} catch ( Exception e ) {
			if ( debugMode )
				plugin.getLogger().log(Level.WARNING, "Could not load config file!", e);
			else
				plugin.getLogger().warning("Could not load config file!");
			return;
		}
		
		if ( !plugin.getDescription().getVersion().equals(config.getString("version") ) ) {
			if ( debugMode ) plugin.getLogger().info("Loading defaults in config.yml");
			loadDefaults();
			config.set("version", plugin.getDescription().getVersion());
			save();
		}
		
		usePermissions 					= 	config.getBoolean("use-permissions", true);
		debugMode 						= 	config.getBoolean( "debug-mode", false );
		
		// Exchange
		useBiggestInterface				=	config.getBoolean("exchange.use-biggest-interface", true);
		defaultRows						=	config.getInt("exchange.default-rows", 4);
		layout							=	config.getString("exchange.custom-layout", "default");
		blacklistedItems				=	Trade.stringToIntArray( config.getString("exchange.blacklisted-items", "") );
		
		// Request
		requestTimeout 					= 	config.getInt("request.timeout",10);
		maximumDistance					= 	config.getInt("request.restrictions.max-distance", -1);
		allowRequestCrossGamemode 		= 	config.getBoolean("request.restrictions.cross-gamemode", false);
		allowRequestCrossWorld 			= 	config.getBoolean("request.restrictions.cross-world", false);
		allowRequestOutOfSight 			= 	config.getBoolean("request.restrictions.must-see", true);
		allowRequestIgnoring 			= 	config.getBoolean("request.restrictions.ignoring", true);
		allowRequestRightClick 			= 	config.getBoolean("request.restrictions.right-click", true);
		allowRequestShiftRightClick 	= 	config.getBoolean("request.restrictions.shift-right-click", true);
		allowRequestCommand 			= 	config.getBoolean("request.restrictions.command", true);
		
		disabledWorlds 					= 	config.getStringList("request.restrictions.disabled-worlds");
		if ( disabledWorlds == null )	disabledWorlds  = new ArrayList<String>();
		
		// Extensions
		useExtensionWorldGuard			= 	config.getBoolean("extensions.worldguard.enable", false);
		useExtensionMobArena	  		= 	config.getBoolean("extensions.mobarena.enable", false);
		useExtensionCitizens   			= 	config.getBoolean("extensions.citizens.enable", false);
		useExtensionEconomy 			= 	config.getBoolean("extensions.economy.enable", false);
		
		smallCurrency					=   config.getInt("extensions.economy.currency.small", 1);
		mediumCurrency					=   config.getInt("extensions.economy.currency.medium", 10);
		largeCurrency					=   config.getInt("extensions.economy.currency.large", 50);
		
		disabledRegions					= 	config.getStringList("extensions.worldguard.disabled-regions");
		if ( disabledRegions == null )	disabledRegions  = new ArrayList<String>();
		
	}

	public void loadDefaults() {
		config.setDefaults(YamlConfiguration.loadConfiguration(plugin.getResource("config.yml")));
		config.options().copyDefaults(true);
	}
	
	public void save() {
		try {
			config.save( configFile );
		} catch (IOException e) {
			if ( debugMode )
				plugin.getLogger().log(Level.WARNING, "Could not save config file!", e);
			else
				plugin.getLogger().warning("Could not save config file!");
		}
	}
	
}
