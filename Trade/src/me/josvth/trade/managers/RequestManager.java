package me.josvth.trade.managers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.josvth.trade.Trade;
import me.josvth.trade.exchanges.CurrencyExchange;
import me.josvth.trade.exchanges.ItemExchange;

import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

public class RequestManager {

	private final Trade plugin;
	private ConfigurationManager configurationManger;
	private ExtensionManager extensionManager;
	private LanguageManager languageManager;

	// key = requester , value = requested
	public Map<Player, Player> pendingRequests = new HashMap<Player, Player>();

	public Map<Player, ItemExchange> activeExchanges = new HashMap<Player, ItemExchange>();

	public RequestManager( Trade instance ) {
		plugin = instance;
	}

	public void initalize() {
		configurationManger = plugin.getConfigurationManager();
		extensionManager = plugin.getExtensionManager();
		languageManager = plugin.getLanguageManager();
	}

	public void makeRequest( final Player requester, final Player requested ){		
		
		if ( configurationManger.debugMode ) 
			plugin.getLogger().info( "(RM) New request made! " + requester.getName() + " - " + requested.getName());
		
		pendingRequests.put( requester, requested );
		
		languageManager.sendMessage( requester, "request.new.self", new String[][]{ {"%playername%", requested.getName()} });
		languageManager.sendMessage( requested, "request.new.other", new String[][]{ {"%playername%", requester.getName()} });

		plugin.getServer().getScheduler().scheduleAsyncDelayedTask(plugin, new Runnable() {

			@Override
			public void run() {
				if ( !isRequesting( requester ) ) return;
				languageManager.sendMessage( requester, "request.no-response", new String[][]{ {"%playername%", requested.getName()} });
				pendingRequests.remove( requester );
			}

		}, configurationManger.requestTimeout * 20 );
	}

	public RequestRestriction mayRequest( Player requester, Player requested, RequestMethod method ){

		if ( configurationManger.usePermissions && !requester.hasPermission( method.permission ) ) 
			return RequestRestriction.NO_PERMISSION;
		
		if ( !configurationManger.allowRequestRightClick && method.equals( RequestMethod.RIGHT_CLICK ) )
			return RequestRestriction.METHOD_NOT_ALLOWED;
		
		if ( !configurationManger.allowRequestShiftRightClick && method.equals( RequestMethod.SHIFT_RIGHT_CLICK ) )
			return RequestRestriction.METHOD_NOT_ALLOWED;
		
		if ( !configurationManger.allowRequestCrossWorld	&& requester.getWorld() != requested.getWorld() ) 
			return RequestRestriction.CROSS_WORLD;

		if ( !configurationManger.allowRequestCrossGamemode && !requester.getGameMode().equals( requested.getGameMode() ) )
			return RequestRestriction.CROSS_GAMEMODE;

		if ( inTrade( requested ) ) return RequestRestriction.IN_TRADE;

		if ( configurationManger.allowRequestIgnoring	&& isIgnoring( requested ) )
			return RequestRestriction.IGNORING;

		if ( configurationManger.disabledWorlds.contains( requester.getWorld() ) ) 
			return RequestRestriction.REQUESTER_WORLD;

		if ( configurationManger.disabledWorlds.contains( requested.getWorld() ) ) 
			return RequestRestriction.REQUESTED_WORLD;

		if ( !configurationManger.allowRequestOutOfSight	&& !requester.canSee( requested ) ) 
			return RequestRestriction.NO_SIGHT;
		
		if ( configurationManger.maximumDistance != -1 && requester.getLocation().distance( requested.getLocation() ) > configurationManger.maximumDistance )
			return RequestRestriction.OUT_OF_RANGE;
			
		if ( isRequesting( requester ) )
			return RequestRestriction.WAIT;

		// Extensions check
		if ( configurationManger.useExtensionMobArena ) {
			if ( extensionManager.inMobArena( requester ) ) 
				return RequestRestriction.REQUESTER_MOBARENA;
			if ( extensionManager.inMobArena( requested ) ) 
				return RequestRestriction.REQUESTED_MOBARENA;
		}

		if ( configurationManger.useExtensionCitizens && extensionManager.isCitizen( requested ) ) 
			return RequestRestriction.CITIZEN;		

		if ( configurationManger.useExtensionWorldGuard ) {
			if ( extensionManager.inRestrictedRegion( requester ) )
				return RequestRestriction.REQUESTER_REGION;
			if ( extensionManager.inRestrictedRegion( requested )) 
				return RequestRestriction.REQUESTED_REGION;
		}

		return RequestRestriction.ALLOW;

	}

	public void acceptRequest( Player requested, Player requester ){
		
		if ( !isRequesting( requester ) ) return;
		
		if ( configurationManger.debugMode ) 
			plugin.getLogger().info( "(RM) Request accepted! " + requester.getName() + " - " + requested.getName());
		
		ItemExchange exchange = createExchange( requester, requested );
		
		exchange.start();
		if ( configurationManger.debugMode ) 
			plugin.getLogger().info( "(RM) Exchange started! " + requester.getName() + " - " + requested.getName());

		pendingRequests.remove( requester );
		activeExchanges.put( requested, exchange );
		activeExchanges.put( requester, exchange );
		
	}

	public void refuseRequest( Player requested, Player requester ){
		if ( pendingRequests.get( requester ) != requested ) return;
		languageManager.sendMessage( requester, "request.refused", new String[][]{ {"%playername%", requested.getName()} });
		pendingRequests.remove( requester );
	}

	private ItemExchange createExchange(Player requester, Player requested) {
		ItemExchange exchange;
		if ( configurationManger.useExtensionEconomy )
			exchange = new CurrencyExchange( plugin, requester, requested );
		else 
			exchange = new ItemExchange( plugin, requester, requested );

		exchange.initialize();
		if ( configurationManger.debugMode ) 
			plugin.getLogger().info( "(RM) Exchange initialized! " + requester.getName() + " - " + requested.getName());
		
		return exchange;
	}

	private boolean isRequesting( Player requester ) {
		return pendingRequests.containsKey( requester );
	}

	private boolean isIgnoring( Player player ) {
		return player.hasMetadata( "trade.ignore" );
	}

	public void toggleIgnoring(Player player) {
		if (  isIgnoring(player) ) {
			player.removeMetadata( "trade.ignore", plugin );
			languageManager.sendMessage(player, "request.ignoring.disabled");
		} else  {
			player.setMetadata( "trade.ignore", new FixedMetadataValue( plugin, true) );
			languageManager.sendMessage(player, "request.ignoring.enabled");
		}
	}
	
	private boolean inTrade( Player player ) {
		return player.hasMetadata( "trade.trading" );
	}

	public Map<Player, ItemExchange> getActiveExchanges() {
		return activeExchanges;
	}

	public enum RequestRestriction {
		NO_PERMISSION,
		METHOD_NOT_ALLOWED,
		NO_SIGHT,
		CROSS_GAMEMODE,
		CROSS_WORLD,
		REQUESTER_WORLD,
		REQUESTED_WORLD,
		IN_TRADE,
		IGNORING,
		WAIT,
		ALLOW,
		REQUESTER_MOBARENA,
		REQUESTER_REGION,
		REQUESTED_MOBARENA,
		REQUESTED_REGION,
		CITIZEN, 
		OUT_OF_RANGE;
	}

	public enum RequestMethod {
		RIGHT_CLICK ("trade.request.right-click"),
		SHIFT_RIGHT_CLICK ("trade.request.shift-right-click"),
		COMMAND ("trade.request.command");

		public String permission;

		private RequestMethod(String permission) {
			this.permission = permission;
		}

	}

	// Checks if the requester is already requested by the requested
	public boolean isRequested( Player requester, Player requested ) {
		return pendingRequests.get( requested ) == requester;
	}

	public void stopAll() {
		pendingRequests.clear();
		List<ItemExchange> exchanges = new ArrayList<ItemExchange>( activeExchanges.values() );
		for ( ItemExchange exchange : exchanges )
			exchange.stop();
	}

}