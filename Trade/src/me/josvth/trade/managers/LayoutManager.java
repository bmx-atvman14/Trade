package me.josvth.trade.managers;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import me.josvth.trade.Trade;
import me.josvth.trade.layouts.CurrencyLayout;
import me.josvth.trade.layouts.ItemLayout;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

public class LayoutManager {
	
	public enum LayoutType {
		ITEM,
		CURRENCY;
	}
	
	private final Trade	plugin;
	private final ConfigurationManager configurationManager;
	
	private File layoutFile;	
	private FileConfiguration layouts;

	private Map<String, ItemLayout> customLayouts = new HashMap<String, ItemLayout>();
	
	public LayoutManager( Trade instance ) {
		plugin = instance;
		configurationManager = plugin.getConfigurationManager();
	}
	
	public void initalize() {
		setup();
		load();
	}

	public void setup() {
		layoutFile = new File(plugin.getDataFolder(), "layouts.yml");
		if (!layoutFile.exists()) plugin.saveResource("layouts.yml", false);
	}
	
	public void reload() {
		initalize();
	}

	public void load() {
		
		layouts = new YamlConfiguration();
		
		try {
			layouts.load( layoutFile );
		} catch ( Exception e ) {
			if ( configurationManager.debugMode )
				plugin.getLogger().log(Level.WARNING, "Could not load config file!", e);
			else
				plugin.getLogger().warning("Could not load config file!");
			return;
		}
		
		if ( !plugin.getDescription().getVersion().equals( layouts.getString("version") ) ) {
			if ( configurationManager.debugMode ) plugin.getLogger().info("Loading defaults in layouts.yml");
			loadDefaults();
			layouts.set("version", plugin.getDescription().getVersion());
			save();
		}
				
	}

	public void loadDefaults() {
		layouts.setDefaults(YamlConfiguration.loadConfiguration(plugin.getResource("layouts.yml")));
		layouts.options().copyDefaults(true);
	}
	
	public void save() {
		try {
			layouts.save( layoutFile );
		} catch (IOException e) {
			if ( configurationManager.debugMode )
				plugin.getLogger().log(Level.WARNING, "Could not save config file!", e);
			else
				plugin.getLogger().warning("Could not save config file!");
		}
	}
	
	public ItemLayout getCustomLayout( String id, LayoutType type ){
		
		ItemLayout layout = (ItemLayout) customLayouts.get( id );
		
		if ( layout != null ) return layout;
		
		if ( !layouts.contains( id ) ) return null;	// Layout not found in layouts file
				
		ConfigurationSection layoutSection = layouts.getConfigurationSection( id );
		
		boolean currency 	= type.equals( LayoutType.CURRENCY );

		if ( currency )
			layout = CurrencyLayout.createDefaultLayout( 6 );	// Fixed rows
		else
			layout = ItemLayout.createDefaultLayout( 6 );		// Fixed rows
		
		String buffer;
		
		buffer = layoutSection.getString( "slots.action.accept" );
		if ( buffer != null ) layout.setAcceptSlots( Trade.stringToIntArray( buffer ) );
		
		buffer = layoutSection.getString( "slots.action.refuse" );
		if ( buffer != null ) layout.setRefuseSlots( Trade.stringToIntArray( buffer ) );
		
		buffer = layoutSection.getString( "slots.action.status" );
		if ( buffer != null ) layout.setStatusSlots( Trade.stringToIntArray( buffer ) );
		
		buffer = layoutSection.getString( "slots.trade.left" );
		if ( buffer != null ) layout.setLeftSlots( Trade.stringToIntArray( buffer ) );
		
		buffer = layoutSection.getString( "slots.trade.right" );
		if ( buffer != null ) layout.setRightSlots( Trade.stringToIntArray( buffer ) );
		
		buffer = layoutSection.getString( "slots.seperation" );
		if ( buffer != null ) layout.setSeperatorSlots( Trade.stringToIntArray( buffer ) );
		
		int idBuffer = -1;
		int amountBuffer = 1;
		short dataBuffer = 0;
		
		idBuffer 		= 	layoutSection.getInt( "items.action.accept.type", -1 );
		amountBuffer 	= 	layoutSection.getInt( "items.action.accept.amount", 1 );
		dataBuffer 		= 	(short) layoutSection.getInt( "items.action.accept.data", 0 );
		
		if ( idBuffer > -1 ) layout.setAcceptItem( new ItemStack( idBuffer, amountBuffer, dataBuffer ) );
		
		idBuffer 		= 	layoutSection.getInt( "items.action.refuse.type", -1 );
		amountBuffer 	= 	layoutSection.getInt( "items.action.refuse.amount", 1 );
		dataBuffer 		= 	(short) layoutSection.getInt( "items.action.refuse.data", 0 );
		
		if ( idBuffer > -1 ) layout.setRefuseItem( new ItemStack( idBuffer, amountBuffer, dataBuffer ) );

		idBuffer 		= 	layoutSection.getInt( "items.action.pending.type", -1 );
		amountBuffer 	= 	layoutSection.getInt( "items.action.pending.amount", 1 );
		dataBuffer 		= 	(short) layoutSection.getInt( "items.action.pending.data", 0 );
		
		if ( idBuffer > -1 ) layout.setPendingItem( new ItemStack( idBuffer, amountBuffer, dataBuffer ) );
		
		idBuffer 		= 	layoutSection.getInt( "items.action.sperator.type", -1 );
		amountBuffer 	= 	layoutSection.getInt( "items.action.sperator.amount", 1 );
		dataBuffer 		= 	(short) layoutSection.getInt( "items.action.sperator.data", 0 );
		
		if ( idBuffer > -1 ) layout.setSeperatorItem( new ItemStack( idBuffer, amountBuffer, dataBuffer ) );
		
		if ( currency ) {
			
			buffer = layoutSection.getString( "slots.currency.add.small" );
			if ( buffer != null ) ( (CurrencyLayout) layout ).setAcceptSlots( Trade.stringToIntArray( buffer ) );
			
			buffer = layoutSection.getString( "slots.currency.add.medium" );
			if ( buffer != null ) ( (CurrencyLayout) layout ).setRefuseSlots( Trade.stringToIntArray( buffer ) );
			
			buffer = layoutSection.getString( "slots.currency.add.large" );
			if ( buffer != null ) ( (CurrencyLayout) layout ).setStatusSlots( Trade.stringToIntArray( buffer ) );
			
			buffer = layoutSection.getString( "slots.currency.display.left.small" );
			if ( buffer != null ) ( (CurrencyLayout) layout ).setLeftDisplaySmallSlots( Trade.stringToIntArray( buffer ) );
			
			buffer = layoutSection.getString( "slots.currency.display.left.medium" );
			if ( buffer != null ) ( (CurrencyLayout) layout ).setLeftDisplayMediumSlots( Trade.stringToIntArray( buffer ) );
			
			buffer = layoutSection.getString( "slots.currency.display.left.large" );
			if ( buffer != null ) ( (CurrencyLayout) layout ).setLeftDisplayLargeSlots( Trade.stringToIntArray( buffer ) );
			
			buffer = layoutSection.getString( "slots.currency.display.right.small" );
			if ( buffer != null ) ( (CurrencyLayout) layout ).setRightDisplaySmallSlots( Trade.stringToIntArray( buffer ) );
			
			buffer = layoutSection.getString( "slots.currency.display.right.medium" );
			if ( buffer != null ) ( (CurrencyLayout) layout ).setRightDisplayMediumSlots( Trade.stringToIntArray( buffer ) );
			
			buffer = layoutSection.getString( "slots.currency.display.right.large" );
			if ( buffer != null ) ( (CurrencyLayout) layout ).setRightDisplayLargeSlots( Trade.stringToIntArray( buffer ) );
			
			
			idBuffer 		= 	layoutSection.getInt( "items.currency.small.type", -1 );
			amountBuffer 	= 	layoutSection.getInt( "items.currency.small.amount", 1 );
			dataBuffer 		= 	(short) layoutSection.getInt( "items.currency.small.data", 0 );
			
			if ( idBuffer > -1 ) ( (CurrencyLayout) layout ).setSmallItem( new ItemStack( idBuffer, amountBuffer, dataBuffer ) );

			idBuffer 		= 	layoutSection.getInt( "items.currency.medium.type", -1 );
			amountBuffer 	= 	layoutSection.getInt( "items.currency.medium.amount", 1 );
			dataBuffer 		= 	(short) layoutSection.getInt( "items.currency.medium.data", 0 );
			
			if ( idBuffer > -1 ) ( (CurrencyLayout) layout ).setMediumItem( new ItemStack( idBuffer, amountBuffer, dataBuffer ) );
			
			idBuffer 		= 	layoutSection.getInt( "items.currency.large.type", -1 );
			amountBuffer 	= 	layoutSection.getInt( "items.currency.large.amount", 1 );
			dataBuffer 		= 	(short) layoutSection.getInt( "items.currency.large.data", 0 );
			
			if ( idBuffer > -1 ) ( (CurrencyLayout) layout ).setLargeItem( new ItemStack( idBuffer, amountBuffer, dataBuffer ) );
			
		}
		
		customLayouts.put( id , layout );
		
		return layout;
	}

	public ItemLayout getItemLayout( int rows ) {
		if ( configurationManager.layout.equals( "default" ) || rows != 6 )
			return ItemLayout.createDefaultLayout(rows);
		else
			return getCustomLayout( configurationManager.layout, LayoutType.ITEM );
	}

	public CurrencyLayout getCurrencyLayout( int rows ) {
		if ( configurationManager.layout.equals( "default" ) || rows != 6 )
			return CurrencyLayout.createDefaultLayout(rows);
		else
			return (CurrencyLayout) getCustomLayout( configurationManager.layout, LayoutType.CURRENCY );
	}
}
