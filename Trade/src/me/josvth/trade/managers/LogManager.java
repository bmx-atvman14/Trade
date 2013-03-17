package me.josvth.trade.managers;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Level;

import me.josvth.trade.Trade;
import me.josvth.trade.exchanges.CurrencyExchange;
import me.josvth.trade.exchanges.ItemExchange;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class LogManager {

	private final Trade	plugin;
	private final ConfigurationManager configurationManager;

	private File logFile;	
	private FileConfiguration logs;

	public LogManager( Trade instance ) {
		plugin = instance;
		configurationManager = plugin.getConfigurationManager();
	}

	public void initalize() {
		setup();
		load();
	}

	public void setup() {
		logFile = new File(plugin.getDataFolder(), "logs.yml");
		if (!logFile.exists()) plugin.saveResource("logs.yml", false);
	}

	public void reload() {
		initalize();
	}

	public void load() {

		logs = new YamlConfiguration();

		try {
			logs.load( logFile );
		} catch ( Exception e ) {
			if ( configurationManager.debugMode )
				plugin.getLogger().log(Level.WARNING, "Could not load log file!", e);
			else
				plugin.getLogger().warning("Could not load log file!");
			return;
		}

		if ( !plugin.getDescription().getVersion().equals( logs.getString("version") ) ) {
			if ( configurationManager.debugMode ) plugin.getLogger().info("Loading defaults in log.yml");
			loadDefaults();
			logs.set("version", plugin.getDescription().getVersion());
			save();
		}

	}

	public void loadDefaults() {
		logs.setDefaults(YamlConfiguration.loadConfiguration(plugin.getResource("logs.yml")));
		logs.options().copyDefaults(true);
	}

	public void save() {
		try {
			logs.save( logFile );
		} catch (IOException e) {
			if ( configurationManager.debugMode )
				plugin.getLogger().log(Level.WARNING, "Could not save config file!", e);
			else
				plugin.getLogger().warning("Could not save config file!");
		}
	}

	public ExchangeLog log( ItemExchange exchange ) {

		ExchangeLog log = new ExchangeLog();

		Player p1 = exchange.getP1();
		Player p2 = exchange.getP2();

		log.p1 = p1.getName();
		log.p2 = p2.getName();

		log.p1acc = exchange.hasAccepted(p1);
		log.p2acc = exchange.hasAccepted(p2);

		log.p1ref = exchange.hasRefused(p1);
		log.p2ref = exchange.hasRefused(p2);

		log.p1off = exchange.getOffers(p1);
		log.p2off = exchange.getOffers(p2);

		if ( exchange instanceof CurrencyExchange ) {
			CurrencyExchange cexchange = (CurrencyExchange) exchange;
			log.p1cur = cexchange.getCurrencyOffers(p1);
			log.p2cur = cexchange.getCurrencyOffers(p2);
		}

		saveLog(log);

		return log;
	}

	private void saveLog(ExchangeLog log) {
		ConfigurationSection section = logs.createSection(String.valueOf( createId() ));
		section.set("p1", log.p1);
		section.set("p2", log.p2);
		section.set("accepted.p1", log.p1acc);
		section.set("accepted.p2", log.p2acc);
		section.set("refused.p1", log.p1ref);
		section.set("refused.p2", log.p2ref);
		section.set("offers.p1", Arrays.asList(log.p1off));
		section.set("offers.p2", Arrays.asList(log.p2off));
		section.set("currency.p1", Arrays.asList(log.p1cur));
		section.set("currency.p2", Arrays.asList(log.p2cur));
		save();
	}

	public int createId() {

		int highest = 0;

		for( String key : logs.getKeys(false) ){
			try {
				int id = Integer.valueOf(key);
				if ( id > highest ) highest = id;
			} catch (NumberFormatException e) {}
		}

		return highest;

	}

	public ExchangeLog getLog( int id ) {
		return null;
	}

	public class ExchangeLog {

		public String p1;
		public String p2;

		public boolean p1acc;
		public boolean p2acc;

		public boolean p1ref;
		public boolean p2ref;

		public int p1cur;
		public int p2cur;

		public ItemStack[] p1off;
		public ItemStack[] p2off;

	}

}
