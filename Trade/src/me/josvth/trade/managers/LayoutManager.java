package me.josvth.trade.managers;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import me.josvth.trade.Trade;
import me.josvth.trade.layouts.CurrencyLayout;
import me.josvth.trade.layouts.ItemLayout;
import me.josvth.trade.layouts.ItemStackBase;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class LayoutManager {

	public enum LayoutType {
		ITEM,
		CURRENCY;
	}

	private final Trade	plugin;
	private final ConfigurationManager configurationManager;

	private File layoutFile;	
	private FileConfiguration layouts;

	private Map<String, ItemLayout> customLayouts;

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
		customLayouts = new HashMap<String, ItemLayout>();
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
			layouts.set("default", null);	// Cleanup for previous versions
			layouts.options().header("The default and default-currency layouts are just templates! Feel free to change them.\n Don't forget to change the custom-layout in the config.yml as well!");
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

	public ItemLayout getCustomLayout( String id, int rows, LayoutType type ){

		ItemLayout layout = customLayouts.get( id );
		
		if ( layout != null ) {
			if ( configurationManager.debugMode ) 
				plugin.getLogger().info( "(LM) Returning previously stored layout called: " + id );
			return layout;
		}

		if ( !layouts.contains( id ) ) {
			if ( configurationManager.debugMode ) 
				plugin.getLogger().info( "(LM) Layout with id: " + id + " was not found.");
			return null;	// Layout not found in layouts file
		}

		ConfigurationSection layoutSection = layouts.getConfigurationSection( id );

		boolean currency 	= type.equals( LayoutType.CURRENCY );

		if ( currency )
			layout = CurrencyLayout.createDefaultLayout( rows );
		else
			layout = ItemLayout.createDefaultLayout( rows );

		if ( configurationManager.debugMode ) 
			plugin.getLogger().info( "(LM) Created default " + type.name() + " layout.");
		
		System.out.println( rows );
		
		if ( layoutSection.contains( "slots." + rows ) ) {
			
			if ( configurationManager.debugMode ) 
				plugin.getLogger().info( "(LM) Setting custom slots.");
			
			// Accept slots
			layout.setAcceptSlots( getSlots( layoutSection, rows, "action.accept", layout.getAcceptSlots() ) );
			
			// Refuse slots
			layout.setRefuseSlots( getSlots( layoutSection, rows, "action.refuse", layout.getRefuseSlots() ) );
			
			// Status slots
			layout.setStatusSlots( getSlots( layoutSection, rows, "action.status", layout.getStatusSlots() ) );

			// Trade left slots
			layout.setLeftSlots( getSlots( layoutSection, rows, "action.status", layout.getLeftSlots() ) );

			// Trade right slots
			layout.setRightSlots( getSlots( layoutSection, rows, "action.status", layout.getRightSlots() ) );

			// Seperation slots
			layout.setSeperatorSlots( getSlots( layoutSection, rows, "action.status", layout.getSeperatorSlots() ) );

			if ( currency ) {
				
				if ( configurationManager.debugMode ) 
					plugin.getLogger().info( "(LM) Setting custom currency slots.");
				
				// Change slots
				( (CurrencyLayout) layout ).setChangeSmallSlots( getSlots( layoutSection, rows, "currency.change.small", ( (CurrencyLayout) layout ).getChangeSmallSlots() ) );
				( (CurrencyLayout) layout ).setChangeMediumSlots( getSlots( layoutSection, rows, "currency.change.medium", ( (CurrencyLayout) layout ).getChangeMediumSlots() ) );
				( (CurrencyLayout) layout ).setChangeLargeSlots( getSlots( layoutSection, rows, "currency.change.large", ( (CurrencyLayout) layout ).getChangeLargeSlots() ) );

				// Left Display slots
				( (CurrencyLayout) layout ).setLeftDisplaySmallSlots( getSlots( layoutSection, rows, "currency.display.left.small", ( (CurrencyLayout) layout ).getLeftDisplaySmallSlots() ) );
				( (CurrencyLayout) layout ).setLeftDisplayMediumSlots( getSlots( layoutSection, rows, "currency.display.left.medium", ( (CurrencyLayout) layout ).getLeftDisplayMediumSlots() ) );
				( (CurrencyLayout) layout ).setLeftDisplayLargeSlots( getSlots( layoutSection, rows, "currency.display.left.large", ( (CurrencyLayout) layout ).getLeftDisplayLargeSlots() ) );
				
				// Right Display slots
				( (CurrencyLayout) layout ).setRightDisplaySmallSlots( getSlots( layoutSection, rows, "currency.display.right.small", ( (CurrencyLayout) layout ).getRightDisplaySmallSlots() ) );
				( (CurrencyLayout) layout ).setRightDisplayMediumSlots( getSlots( layoutSection, rows, "currency.display.right.medium", ( (CurrencyLayout) layout ).getRightDisplayMediumSlots() ) );
				( (CurrencyLayout) layout ).setRightDisplayLargeSlots( getSlots( layoutSection, rows, "currency.display.right.large", ( (CurrencyLayout) layout ).getRightDisplayLargeSlots() ) );

			}
		}
		
		if ( layoutSection.contains( "items" ) ) {
			
			if ( configurationManager.debugMode ) 
				plugin.getLogger().info( "(LM) Setting custom items.");
			
			// Accept item
			layout.setAcceptItem( getItemBase( layoutSection, "action.accept", layout.getAcceptItem() ) );	

			// Accepted item
			layout.setAcceptedItem( getItemBase( layoutSection, "action.accepted", layout.getAcceptedItem() ) );

			// Refuse item
			layout.setRefuseItem( getItemBase( layoutSection, "action.refuse", layout.getRefuseItem() ) );

			// Pending item
			layout.setPendingItem( getItemBase( layoutSection, "action.pending", layout.getPendingItem() ) );

			// Separator item
			layout.setSeperatorItem( getItemBase( layoutSection, "action.seperator", layout.getSeperatorItem() ) );

			if ( currency ) {
				
				if ( configurationManager.debugMode ) 
					plugin.getLogger().info( "(LM) Setting custom currency items.");
								
				// Currency change buttons
				( (CurrencyLayout) layout ).setSmallChangeItem( getItemBase( layoutSection, "currency.change.small", ( (CurrencyLayout) layout ).getSmallChangeItem() ) );
				( (CurrencyLayout) layout ).setMediumChangeItem( getItemBase( layoutSection, "currency.change.medium", ( (CurrencyLayout) layout ).getMediumChangeItem() ) );
				( (CurrencyLayout) layout ).setLargeChangeItem( getItemBase( layoutSection, "currency.change.large", ( (CurrencyLayout) layout ).getLargeChangeItem() ) );

				// Currency display buttons
				( (CurrencyLayout) layout ).setSmallDisplayItem( getItemBase( layoutSection, "currency.display.small", ( (CurrencyLayout) layout ).getLargeDisplayItem() ) );
				( (CurrencyLayout) layout ).setMediumDisplayItem( getItemBase( layoutSection, "currency.display.medium", ( (CurrencyLayout) layout ).getLargeDisplayItem() ) );
				( (CurrencyLayout) layout ).setLargeDisplayItem( getItemBase( layoutSection, "currency.display.large", ( (CurrencyLayout) layout ).getLargeDisplayItem() ) );

			}

			if ( configurationManager.debugMode ) 
				plugin.getLogger().info( "(LM) Storing layout.");
			
		}
		
		if ( currency ) {
			
			if ( configurationManager.debugMode ) 
				plugin.getLogger().info( "(LM) Setting custom currency values.");
			
			// Currency values
			( (CurrencyLayout) layout ).setSmallCurrency( layoutSection.getInt( "currency.small", 1 ) );
			( (CurrencyLayout) layout ).setMediumCurrency( layoutSection.getInt( "currency.medium", 10 ) );
			( (CurrencyLayout) layout ).setLargeCurrency( layoutSection.getInt( "currency.large", 50 ) );
		}
		
		customLayouts.put( id , layout );

		return layout;
	}

	public ItemLayout getItemLayout( int rows ) {
		if ( configurationManager.layout.equals( "default" ) )
			return ItemLayout.createDefaultLayout(rows);
		else
			return getCustomLayout( configurationManager.layout, rows, LayoutType.ITEM );
	}

	public CurrencyLayout getCurrencyLayout( int rows ) {
		if ( configurationManager.layout.equals( "default" ) )
			return CurrencyLayout.createDefaultLayout( rows );
		else
			return (CurrencyLayout) getCustomLayout( configurationManager.layout, rows, LayoutType.CURRENCY );
	}
	
	private int[] getSlots( ConfigurationSection layoutsection, int rows, String path, int[] def ) {
				
		String slotPath = "slots." + rows + "." + path;
				
		String stringSlots = layoutsection.getString( slotPath );
				
		if ( stringSlots == null )
			return def;		
					
		if ( configurationManager.debugMode ) 
			plugin.getLogger().info( "(LM) Custom slots at path: " + layoutsection.getCurrentPath() + "." + slotPath + " found." );
		
		return Trade.stringToIntArray( stringSlots );
		
	}
	
	private ItemStackBase getItemBase( ConfigurationSection layoutsection, String path, ItemStackBase def ) {
		
		String itemPath = "items." + path;
				
		ConfigurationSection itemSection = layoutsection.getConfigurationSection( itemPath );
		
		if ( itemSection == null )	
			return def;
		
		if ( configurationManager.debugMode ) 
			plugin.getLogger().info( "(LM) Custom item at path: " + layoutsection.getCurrentPath() + "." + itemPath + " found." );
		
		int typeID 			=	itemSection.getInt( ".id", def.getType() );		
		int amount 			= 	itemSection.getInt(".amount", def.getAmount() );	
		short data 			= 	(short) itemSection.getInt( ".data", def.getData() );
		String label		=	itemSection.getString( ".label", def.getDisplayName() );
		List<String> lore	=	itemSection.getStringList( ".lore" );
		
		if(lore == null) lore = def.getLore();
		
		return new ItemStackBase(typeID, amount, data, label, lore);
		
	}
	

}
