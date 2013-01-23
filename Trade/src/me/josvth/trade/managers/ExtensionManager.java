package me.josvth.trade.managers;

import java.util.Iterator;
import java.util.List;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.UnknownDependencyException;

import com.garbagemule.MobArena.MobArena;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

import me.josvth.trade.Trade;

public class ExtensionManager {

	private final Trade plugin;
	private final ConfigurationManager configManager;
		
	// Hooks
	WorldGuardPlugin worldGuard;
	MobArena mobArena;
	public static Economy economy;
	
	public ExtensionManager( Trade instance ){
		plugin = instance;
		configManager = plugin.getConfigurationManager();
	}

	public void initialize() throws UnknownDependencyException {

		if ( configManager.useExtensionWorldGuard ) {
			
			Plugin wplugin = plugin.getServer().getPluginManager().getPlugin("WorldGuard");

			// WorldGuard may not be loaded
			if ( wplugin == null || !(wplugin instanceof WorldGuardPlugin) ) {
				configManager.useExtensionWorldGuard = false;
				configManager.save();
				throw new UnknownDependencyException("WorldGuard support is enabled but WorldGuard is not installed!");
			} else
				worldGuard = (WorldGuardPlugin) wplugin;
			
		}

		if ( configManager.useExtensionMobArena ) {
			
			Plugin mplugin = plugin.getServer().getPluginManager().getPlugin("MobArena");

			// MobArena may not be loaded
			if ( mplugin == null || !(mplugin instanceof MobArena) ) {
				configManager.useExtensionMobArena = false;
				configManager.save();
				throw new UnknownDependencyException("MobArena support is enabled but MobArena is not installed!");
			} else
				mobArena = (MobArena) mplugin;
			
		}

		if ( configManager.useExtensionCitizens ) {
			// No hooking necessary
		}

		if ( configManager.useExtensionEconomy ) {
			
			RegisteredServiceProvider<Economy> economyProvider = plugin.getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
	        
			if (economyProvider == null) {
				configManager.useExtensionEconomy = false;
				configManager.save();
				throw new UnknownDependencyException("Economy support is enabled but Vault is not installed!");
			} else
				economy = economyProvider.getProvider();
			
		}

	}
	
	public void reload() {
		worldGuard = null;
		mobArena = null;
		economy = null;
		initialize();
	}

	public WorldGuardPlugin getWorldGuard() {
		return worldGuard;
	}

	public MobArena getMobArena() {
		return mobArena;
	}

	public boolean inMobArena( Player player ) {
		if ( !configManager.useExtensionMobArena ) return false;	
		return mobArena.getArenaMaster().getArenaWithPlayer( player ) != null;
	}
	
	public boolean inRestrictedRegion( Player player ) {
		
		if ( !configManager.useExtensionWorldGuard ) return false;
		
		List<String> disabledRegions = configManager.disabledRegions;
			
		ApplicableRegionSet regions = worldGuard.getRegionManager( player.getWorld() ).getApplicableRegions( player.getLocation() );
		
		if ( regions == null ) return false;
		
		Iterator<ProtectedRegion> iterator = regions.iterator();
		
		while( iterator.hasNext() ) if ( disabledRegions.contains( iterator.next().getId() ) ) return true;
		
		return false;
			
	}
	
	public boolean isCitizen( Entity entity ) {
		return entity.hasMetadata("NPC");
	}
	
	public Economy getEconomy() {
		if ( !configManager.useExtensionEconomy ) return null;
		return economy;
	}
}
