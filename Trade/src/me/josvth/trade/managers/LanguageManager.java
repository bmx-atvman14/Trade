package me.josvth.trade.managers;

import java.io.File;


import me.josvth.trade.Trade;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;


public class LanguageManager {

	Trade plugin;

	File languageFile;
	FileConfiguration languageYaml;

	public LanguageManager(Trade instance) {
		plugin = instance;
	}

	public void initialize() {
		setupLanguageFile();
		loadLanguageYaml();
	}
	
	public void reload() {
		initialize();
	}
	
	private void setupLanguageFile() {
		languageFile = new File(plugin.getDataFolder(), "language.yml");
		if (!(languageFile.exists())) plugin.saveResource("language.yml", false);
	}

	private void loadLanguageYaml() {
		languageYaml = new YamlConfiguration();
		try{
			languageYaml.load(languageFile);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public void sendMessage(CommandSender reciever, String path) {
		String message = languageYaml.getString(path);
		if(message == null) return;
		message = ChatColor.translateAlternateColorCodes('&', message);
		reciever.sendMessage(message);
	}

	public void sendMessage(CommandSender reciever, Message message){
		sendMessage(reciever, message.path, message.args);
	}

	public void sendMessage( CommandSender reciever, String path, String[][] arguments ){
		String message = languageYaml.getString( path );
		if(message == null) return;
		message = ChatColor.translateAlternateColorCodes('&', message);
		for( int i = 0; i < arguments.length; i++ ) message = message.replaceAll(arguments[i][0], arguments[i][1]);
		reciever.sendMessage( message );
	}
	
	public void sendMessage(CommandSender reciever, String path, MessageArgument argument) {
		String message = languageYaml.getString(path);
		if(message == null) return;
		message = ChatColor.translateAlternateColorCodes('&', message);
		message = message.replaceAll(argument.variable, argument.value);
		reciever.sendMessage(message);
	}

	public void sendMessage(CommandSender reciever, String path, MessageArgument[] args) {
		String message = languageYaml.getString(path);
		if(message == null) return;
		message = ChatColor.translateAlternateColorCodes('&', message);
		for(MessageArgument argument : args){
			message = message.replaceAll(argument.variable, argument.value);
		}
		reciever.sendMessage(message);
	}

	public static class MessageArgument{
		public final String variable;
		public final String value;
		public MessageArgument(String variable, String value){
			this.variable = variable;
			this.value = value;
		}
	}

	public static class Message{
		public final String path;
		public final MessageArgument[] args;

		public Message(String path){
			this.path = path;
			this.args = new MessageArgument[0];
		}

		public Message(String path, MessageArgument[] args){
			this.path = path;
			this.args = args;
		}

		public Message(String path, MessageArgument arg) {
			this.path = path;
			this.args = new MessageArgument[]{arg};
		}
	}
}