package me.josvth.trade.managers;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class LanguageManager {

	private static LanguageManager instance = new LanguageManager();

	Logger logger;
	
	File file;
	FileConfiguration yaml = new YamlConfiguration();

	private LanguageManager() {
		logger = Logger.getLogger("(LM)", "Trade");
	}
	
	public static LanguageManager getInstance() {
		return instance;
	}
	
	public void setLanguageFile( File file ) {
		this.file = file;
	}
	
	public void setDefaults( InputStream defaults ) {
		yaml.setDefaults( YamlConfiguration.loadConfiguration( defaults ) );
	}
	
	public void reload() {
		if ( !file.exists() )
			saveYaml();
		else
			loadYaml();
	}
	
	private void saveYaml() {
		try {
			file.getParentFile().mkdirs();
			file.createNewFile();
			yaml.save( file );
		} catch ( IOException e ) {
			logger.log(Level.WARNING, "Could not save language file!", e);
		}
	}

	private void loadYaml() {
		try{
			yaml.load(file);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public String getMessage(String path) {
		return yaml.getString( path );
	}

	public static String _( String path ) {
		String message = getInstance().getMessage( path );
		if ( message == null ) return null;
		message = ChatColor.translateAlternateColorCodes('&', message);
		return message;
	}
	
	public static String _( String path, String[][] arguments ) {
		String message = getInstance().getMessage( path );
		if ( message == null ) return null;
		message = ChatColor.translateAlternateColorCodes('&', message);
		for( int i = 0; i < arguments.length; i++ ) message = message.replaceAll(arguments[i][0], arguments[i][1]);
		return message;
	}
	
	public static void _s( CommandSender reciever, String path ) {
		reciever.sendMessage( _( path ) );
	}
	
	public static void _s( CommandSender reciever, String path, String[][] arguments ) {
		reciever.sendMessage( _( path, arguments ) );
	}
	
	@Deprecated
	public void sendMessage(CommandSender reciever, String path) {
		String message = yaml.getString(path);
		if(message == null) return;
		message = ChatColor.translateAlternateColorCodes('&', message);
		reciever.sendMessage(message);
	}
	
	@Deprecated
	public void sendMessage( CommandSender reciever, String path, String[][] arguments ){
		String message = yaml.getString( path );
		if(message == null) return;
		message = ChatColor.translateAlternateColorCodes('&', message);
		for( int i = 0; i < arguments.length; i++ ) message = message.replaceAll(arguments[i][0], arguments[i][1]);
		reciever.sendMessage( message );
	}
	
}