package me.josvth.trade.managers;

import me.josvth.trade.Trade;
import static me.josvth.trade.managers.LanguageManager._s;
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
					_s(sender, "command.no-permission");
					return true;
				}

				if(!(sender instanceof Player)){ 
					_s(sender, "command.only-player");
					return true;
				}

				Player player = (Player)sender;

				requestManager.toggleIgnoring( player );

				return true;
			}

			// /trade listen
			if(args[0].equalsIgnoreCase("listen")){

				if(!sender.hasPermission("trade.ignore")){
					_s(sender, "command.no-permission");
					return true;
				}

				if(!(sender instanceof Player)){ 
					_s(sender, "command.only-player");
					return true;
				}

				Player player = (Player)sender;

				requestManager.listenAll( player );
				
				return true;
			}
			
			// /trade reload
			if(args[0].equalsIgnoreCase("reload")){
				if(!sender.hasPermission("trade.reload")){
					_s(sender, "command.no-permission");
				}else{

					plugin.getLanguageManager().reload();
					plugin.getConfigurationManager().reload();
					plugin.getLanguageManager().reload();

					_s(sender, "global.reload");
				}
				return true;
			}

			// /trade <player> // accept or make new

			if(!(sender instanceof Player)){ 
				_s(sender, "command.only-player");
				return true;
			}

			Player player = (Player)sender;

			Player requested = plugin.getServer().getPlayer(args[0]);

			if(requested == null){
				_s( player, "request.player-not-found", new String[][]{ {"%playername%", args[0]} });
				return true;
			}

			boolean acceptation = requestManager.isRequested( requested, player );
			
			RequestRestriction restriction = requestManager.mayRequest( player , requested, RequestMethod.COMMAND );

			if ( !RequestRestriction.ALLOW.equals( restriction ) ) {
				sendMayRequestResponse( player, requested, restriction );
				return true;
			}

			if ( acceptation ) 
				requestManager.acceptRequest( player, requested);
			else
				requestManager.makeRequest( player, requested );

			return true;

		}

		if(args.length == 2){

			// /trade refuse <player>
			if (args[0].equalsIgnoreCase("refuse")){

				if(!(sender instanceof Player)){ 
					_s(sender, "command.only-player");
					return true;
				}


				Player player = (Player)sender;
				Player requester = plugin.getServer().getPlayer(args[1]);

				if(requester == null){
					_s( player, "request.player-not-found", new String[][]{ {"%playername%", args[0]} });
					return true;
				}
				
				RequestRestriction restriction = requestManager.mayRequest( player , requester, RequestMethod.COMMAND );

				if ( !RequestRestriction.ALLOW.equals( restriction ) ) {
					sendMayRequestResponse( player, requester, restriction );
					return true;
				}

				requestManager.refuseRequest( player, requester );

				return true;
				
			}
		}

		// /trade accept <player>
		if (args[0].equalsIgnoreCase("accept") ){

			if(!(sender instanceof Player)){ 
				_s(sender, "command.only-player");
				return true;
			}

			Player player = (Player)sender;
			Player requester = plugin.getServer().getPlayer(args[1]);

			if(requester == null){
				_s( player, "request.player-not-found", new String[][]{ {"%playername%", args[0]} });
				return true;
			}

			RequestRestriction restriction = requestManager.mayRequest( player , requester, RequestMethod.COMMAND );

			if ( !RequestRestriction.ALLOW.equals( restriction ) ) {
				sendMayRequestResponse( player, requester, restriction );
				return true;
			}

			requestManager.acceptRequest( player, requester );
			
		}

		return false;

	}
	
	private void sendMayRequestResponse( Player player, Player requester, RequestRestriction restriction ) {
		
		switch ( restriction ) {
		case NO_PERMISSION:
			_s( player, "command.no-permission");
			break;
		case CROSS_WORLD:
			_s( player, "request.no-cross-world");
			break;	
		case CROSS_GAMEMODE:
			_s( player, "request.no-cross-gamemode");			
			break;
		case IN_TRADE:
			_s( player, "request.in-trade");			
			break;
		case IGNORING:
			_s( player, "request.ignoring.is-ignoring", new String[][]{ {"%playername%", requester.getName()} } );			
			break;
		case REQUESTER_WORLD:
			_s( player, "request.disabled-world.player");			
			break;
		case REQUESTED_WORLD:
			_s( player, "request.disabled-world.requested", new String[][]{ {"%playername%", requester.getName()} });			
			break;
		case NO_SIGHT:
			_s( player, "request.must-see");			
			break;
		case WAIT:
			_s( player, "request.please-wait");			
			break;
		case REQUESTER_MOBARENA:
			_s( player, "request.mobarena.player");			
			break;
		case REQUESTED_MOBARENA:
			_s( player, "request.mobarena.requested", new String[][]{ {"%playername%", requester.getName()} });			
			break;
		case CITIZEN:
			_s( player, "request.citizen");			
			break;
		case REQUESTER_REGION:
			_s( player, "request.region.player");			
			break;
		case REQUESTED_REGION:
			_s( player, "request.region.requested", new String[][]{ {"%playername%", requester.getName()} });			
			break;
		default:
			break;
		}
		
	}
}
