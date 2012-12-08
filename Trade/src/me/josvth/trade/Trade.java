package me.josvth.trade;

import java.io.File;
import java.util.logging.Logger;

import me.josvth.trade.listeners.TradeListener;
import me.josvth.trade.managers.CommandManager;
import me.josvth.trade.managers.ConfigurationManager;
import me.josvth.trade.managers.ExtensionManager;
import me.josvth.trade.managers.LanguageManager;
import me.josvth.trade.managers.LayoutManager;
import me.josvth.trade.managers.RequestManager;

import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class Trade extends JavaPlugin{

	Logger logger;
		
	ConfigurationManager 	configurationManager;
	CommandManager			commandManager;
	LanguageManager			languageManager;
	RequestManager			requestManager;
	LayoutManager			layoutManager;
	ExtensionManager 		extensionManager;
	
	TradeListener tradeListener;

	@Override
	public void onEnable() {
		
		logger = getLogger();
		
		configurationManager 	= new ConfigurationManager( this );
		layoutManager			= new LayoutManager( this );
		languageManager			= LanguageManager.getInstance();
		requestManager			= new RequestManager( this );
		extensionManager		= new ExtensionManager( this );
		commandManager			= new CommandManager( this );
		
		configurationManager.initalize();
		
		requestManager.initalize();
		
		layoutManager.initalize();
		
		languageManager.setLanguageFile( new File( getDataFolder(), "language.yml" ) );
		languageManager.setDefaults( getResource( "language.yml") );
		
		extensionManager.initialize();
						
		tradeListener = new TradeListener(this);
		
	}
	
	@Override
	public void onDisable() {
		requestManager.stopAll();
	}
	
    public ConfigurationManager getConfigurationManager() {
		return configurationManager;
	}

	public RequestManager getRequestManager() {
		return requestManager;
	}

	public LayoutManager getLayoutManager() {
		return layoutManager;
	}

	public LanguageManager getLanguageManager() {
		return languageManager;
	}

	public ExtensionManager getExtensionManager() {
		return extensionManager;
	}
		
	public boolean hasPermission(CommandSender sender, String permission){
		
		if( configurationManager.usePermissions ) return sender.hasPermission(permission);
		
		return !(permission.equalsIgnoreCase("trade.reload") && !sender.isOp());
		
	}

	public static int[] stringToIntArray( String string ) {
		
		if ( string.equals( "" ) || string == null ) return new int[]{};
		
		String[] split = string.replaceAll(" ", "").split(",");
		int[] slots = new int[ split.length ];
		int index = 0;
		for ( String number : split ){
			slots[index] = Integer.parseInt( number );
			index++;
		}
		return slots;
	}
	
	public static String intArrayToString ( int[] array ) {
		
		String string = "";

		for ( int i = 0; i < array.length; i++ )
			if ( i == array.length -1 )
				string += String.valueOf( array[i] );
			else
				string += String.valueOf( array[i] ) + ",";
		
		return string;
		
	}
}