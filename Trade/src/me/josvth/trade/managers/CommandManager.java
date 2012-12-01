package me.josvth.trade.managers;

import me.josvth.trade.Trade;
import me.josvth.trade.managers.LanguageManager.MessageArgument;
import me.josvth.trade.managers.RequestManager.RequestMethod;
import me.josvth.trade.managers.RequestManager.RequestRestriction;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandManager implements CommandExecutor {

	Trade plugin;

	LanguageManager languageManager;
	RequestManager requestManager;

	public CommandManager(Trade instance){
		plugin = instance;

		languageManager = plugin.getLanguageManager();
		requestManager = plugin.getRequestManager();

		plugin.getCommand("trade").setExecutor(this);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String label, String[] args) {

		if(args.length == 0) return false;

		if(args.length == 1){

			// /trade ignore
			if(args[0].equalsIgnoreCase("ignore")){

				if(!sender.hasPermission("trade.ignore")){
					languageManager.sendMessage(sender, "command.no-permission");
					return true;
				}

				if(!(sender instanceof Player)){ 
					languageManager.sendMessage(sender, "command.only-player");
					return true;
				}

				Player player = (Player)sender;

				requestManager.toggleIgnoring( player );


				return true;
			}

			// /trade reload
			if(args[0].equalsIgnoreCase("reload")){
				if(!sender.hasPermission("trade.reload")){
					languageManager.sendMessage(sender, "command.no-permission");
				}else{

					plugin.getLanguageManager().reload();
					plugin.getConfigurationManager().reload();
					plugin.getLanguageManager().reload();

					languageManager.sendMessage(sender, "global.reload");
				}
				return true;
			}

			// /trade <player>
			if(!(sender instanceof Player)){ 
				languageManager.sendMessage(sender, "command.only-player");
				return true;
			}

			Player player = (Player)sender;

			Player requested = plugin.getServer().getPlayer(args[0]);
			if(requested == null){
				languageManager.sendMessage( player, "request.player-not-found", new MessageArgument("%playername%", args[0]));
				return true;
			}

			RequestRestriction restriction = requestManager.mayRequest( player , requested, RequestMethod.COMMAND );

			switch ( restriction ) {
			case NO_PERMISSION:
				languageManager.sendMessage( player, "command.no-permission");
				break;
			case CROSS_WORLD:
				languageManager.sendMessage( player, "request.no-cross-world");
				break;	
			case CROSS_GAMEMODE:
				languageManager.sendMessage( player, "request.no-cross-gamemode");			
				break;
			case IN_TRADE:
				languageManager.sendMessage( player, "request.in-trade");			
				break;
			case IGNORING:
				languageManager.sendMessage( player, "request.ignoring.is-ignoring", new String[][]{ {"%playername%", requested.getName()} } );			
				break;
			case REQUESTER_WORLD:
				languageManager.sendMessage( player, "request.disabled-world.player");			
				break;
			case REQUESTED_WORLD:
				languageManager.sendMessage( player, "request.disabled-world.requested", new String[][]{ {"%playername%", requested.getName()} });			
				break;
			case OUT_OF_RANGE:
				languageManager.sendMessage( player, "request.out-of-range", new String[][]{ {"%playername%", requested.getName()} });			
				break;
			case NO_SIGHT:
				languageManager.sendMessage( player, "request.must-see");			
				break;
			case WAIT:
				languageManager.sendMessage( player, "request.please-wait");			
				break;
			case REQUESTER_MOBARENA:
				languageManager.sendMessage( player, "request.mobarena.player");			
				break;
			case REQUESTED_MOBARENA:
				languageManager.sendMessage( player, "request.mobarena.requested", new String[][]{ {"%playername%", requested.getName()} });			
				break;
			case CITIZEN:
				languageManager.sendMessage( player, "request.citizen");			
				break;
			case REQUESTER_REGION:
				languageManager.sendMessage( player, "request.region.player");			
				break;
			case REQUESTED_REGION:
				languageManager.sendMessage( player, "request.region.requested", new String[][]{ {"%playername%", requested.getName()} });			
				break;
			default:
				requestManager.makeRequest( player, requested );
				break;
			}

			return true;
		}

		if(args.length == 2){

			// /trade accept <player>
			if (args[0].equalsIgnoreCase("accept")){

				if(!(sender instanceof Player)){ 
					languageManager.sendMessage(sender, "command.only-player");
					return true;
				}

				
				Player player = (Player)sender;
				Player requester = plugin.getServer().getPlayer(args[1]);
				
				if(requester == null){
					languageManager.sendMessage( player, "request.player-not-found", new MessageArgument("%playername%", args[0]));
					return true;
				}
				
				RequestRestriction restriction = requestManager.mayRequest( player , requester, RequestMethod.COMMAND );

				switch ( restriction ) {
				case NO_PERMISSION:
					languageManager.sendMessage( player, "command.no-permission");
					break;
				case CROSS_WORLD:
					languageManager.sendMessage( player, "request.no-cross-world");
					break;	
				case CROSS_GAMEMODE:
					languageManager.sendMessage( player, "request.no-cross-gamemode");			
					break;
				case IN_TRADE:
					languageManager.sendMessage( player, "request.in-trade");			
					break;
				case IGNORING:
					languageManager.sendMessage( player, "request.ignoring.is-ignoring", new String[][]{ {"%playername%", requester.getName()} } );			
					break;
				case REQUESTER_WORLD:
					languageManager.sendMessage( player, "request.disabled-world.player");			
					break;
				case REQUESTED_WORLD:
					languageManager.sendMessage( player, "request.disabled-world.requested", new String[][]{ {"%playername%", requester.getName()} });			
					break;
				case NO_SIGHT:
					languageManager.sendMessage( player, "request.must-see");			
					break;
				case WAIT:
					languageManager.sendMessage( player, "request.please-wait");			
					break;
				case REQUESTER_MOBARENA:
					languageManager.sendMessage( player, "request.mobarena.player");			
					break;
				case REQUESTED_MOBARENA:
					languageManager.sendMessage( player, "request.mobarena.requested", new String[][]{ {"%playername%", requester.getName()} });			
					break;
				case CITIZEN:
					languageManager.sendMessage( player, "request.citizen");			
					break;
				case REQUESTER_REGION:
					languageManager.sendMessage( player, "request.region.player");			
					break;
				case REQUESTED_REGION:
					languageManager.sendMessage( player, "request.region.requested", new String[][]{ {"%playername%", requester.getName()} });			
					break;
				default:
					requestManager.acceptRequest( player, requester );
					break;
				}

				return true;
				
			}

			// /trade refuse <player>
			if (args[0].equalsIgnoreCase("refuse")){

				if(!(sender instanceof Player)){ 
					languageManager.sendMessage(sender, "command.only-player");
					return true;
				}

				
				Player player = (Player)sender;
				Player requester = plugin.getServer().getPlayer(args[1]);
				
				if(requester == null){
					languageManager.sendMessage( player, "request.player-not-found", new MessageArgument("%playername%", args[0]));
					return true;
				}
				
				RequestRestriction restriction = requestManager.mayRequest( player , requester, RequestMethod.COMMAND );
				switch ( restriction ) {
				case NO_PERMISSION:
					languageManager.sendMessage( player, "command.no-permission");
					break;
				case CROSS_WORLD:
					languageManager.sendMessage( player, "request.no-cross-world");
					break;	
				case CROSS_GAMEMODE:
					languageManager.sendMessage( player, "request.no-cross-gamemode");			
					break;
				case IN_TRADE:
					languageManager.sendMessage( player, "request.in-trade");			
					break;
				case IGNORING:
					languageManager.sendMessage( player, "request.ignoring.is-ignoring", new String[][]{ {"%playername%", requester.getName()} } );			
					break;
				case REQUESTER_WORLD:
					languageManager.sendMessage( player, "request.disabled-world.player");			
					break;
				case REQUESTED_WORLD:
					languageManager.sendMessage( player, "request.disabled-world.requested", new String[][]{ {"%playername%", requester.getName()} });			
					break;
				case NO_SIGHT:
					languageManager.sendMessage( player, "request.must-see");			
					break;
				case WAIT:
					languageManager.sendMessage( player, "request.please-wait");			
					break;
				case REQUESTER_MOBARENA:
					languageManager.sendMessage( player, "request.mobarena.player");			
					break;
				case REQUESTED_MOBARENA:
					languageManager.sendMessage( player, "request.mobarena.requested", new String[][]{ {"%playername%", requester.getName()} });			
					break;
				case CITIZEN:
					languageManager.sendMessage( player, "request.citizen");			
					break;
				case REQUESTER_REGION:
					languageManager.sendMessage( player, "request.region.player");			
					break;
				case REQUESTED_REGION:
					languageManager.sendMessage( player, "request.region.requested", new String[][]{ {"%playername%", requester.getName()} });			
					break;
				default:
					requestManager.refuseRequest( player, requester );
					break;
				}
				
				return true;
			}
		}

		return false;
		
	}
}
